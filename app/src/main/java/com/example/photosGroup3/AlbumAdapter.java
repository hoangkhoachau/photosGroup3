package com.example.photosGroup3;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> albumList;
    ViewHolder choosingAlbumView = null;
    Album choosingAlbum = null;
    AlbumAdapter adapter = this;
    boolean isPasswordSet = false;
    String savedPass;
    String savedNumber;
    public AlbumAdapter(ArrayList<Album> albumList, Context context) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_album_square, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (position == 0) {
//            holder.itemView.setVisibility(View.GONE);
//            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
//        } else {

        View.OnClickListener displayAlbum = view -> {
            int pos = holder.getBindingAdapterPosition();
            Fragment previous = AlbumHostingFragment.getInstance().getChildFragmentManager().findFragmentById(R.id.album_hosting_fragment);
            AlbumHostingFragment.getInstance().getChildFragmentManager().beginTransaction()
                    .add(R.id.album_hosting_fragment, AlbumDisplayFragment.newInstance(albumList.get(pos)), null)
                    .hide(previous)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

        };
        View.OnClickListener privateAlbum=view1 -> {
            SharedPreferences sharePrf = MainActivity.mainActivity.getSharedPreferences("AppPreferences", MODE_PRIVATE);
            isPasswordSet = sharePrf.getBoolean("pass_set", false);
            if (isPasswordSet){
                showInputPasswordDialog(displayAlbum);
            } else {
                showSetPasswordDialog();
            }
        };
            holder.albumName.setText(albumList.get(position).name);
            if (albumList.get(position).name.equals(AlbumsFragment.privateAlbum)) {
                holder.imageView.setImageResource(R.drawable.ic_lock_lock);
                holder.albumImagesCount.setText("");
                holder.itemView.setOnClickListener(privateAlbum);
            }
            else{
            if (albumList.get(position).imagePaths.size() != 0) {
                File imgFile = new File(albumList.get(position).imagePaths.get(0));
                Glide.with(MainActivity.mainActivity)
                        .load(Uri.parse("file://" + imgFile.getAbsolutePath()))
                        .into(holder.imageView);
            }
                holder.albumImagesCount.setText(String.format(context.getString(R.string.album_image_count), albumList.get(position).imagePaths.size()));
                holder.itemView.setOnClickListener(displayAlbum);
            }

            setBackgroundColor(holder.itemView, null);

            // Nếu là album Favourite
//            if (albumList.get(position).name.equals(AlbumsFragment.favourite)) {
//                holder.imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
//                return;
//            } else {
//                holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
//            }

            colorChoosingState(holder.itemView, holder.isChoosing);
            holder.itemView.setOnLongClickListener(view -> {

                holder.isChoosing = !holder.isChoosing;
                colorChoosingState(holder.itemView, holder.isChoosing);
                choosingAlbumView = holder;
                choosingAlbum = albumList.get(holder.getAbsoluteAdapterPosition());
                AlbumOperationDialog dialog = new AlbumOperationDialog(context);
                dialog.show();
                return true;
            });
//        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView albumName;
        TextView albumImagesCount;
        LinearLayout albumInfo;
        boolean isChoosing = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album_image);
            imageView.setImageResource(R.drawable.ic_baseline_folder_24);
            albumName = itemView.findViewById(R.id.album_name);
            albumImagesCount = itemView.findViewById(R.id.album_images_count);
            albumInfo = itemView.findViewById(R.id.album_info);
        }
    }

    public void setBackgroundColor(View view, Integer colorID) {
        if (colorID == null) {
            view.setBackgroundTintList(null);
            return;
        }
        Drawable buttonDrawable = view.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, context.getResources().getColor(colorID));
        view.setBackground(buttonDrawable);
    }

    public void colorChoosingState(View view, boolean isChoosing) {


        int colorWhileChoosing = R.color.fullScreenBtn;
        if (isChoosing) {
            view.setBackgroundResource(R.drawable.custom_row_album);
            setBackgroundColor(view, colorWhileChoosing);
        } else {
            view.setBackgroundResource(android.R.color.transparent);
            setBackgroundColor(view, null);
        }
    }

    private class AlbumOperationDialog extends BottomSheetDialog {
        boolean clearChoosingState = true;

        @Override
        public void dismiss() {
            super.dismiss();
            choosingAlbumView.isChoosing = !choosingAlbumView.isChoosing;
            colorChoosingState(choosingAlbumView.itemView, choosingAlbumView.isChoosing);
            if (clearChoosingState) {
                clearChoosing();
            }

        }

        public AlbumOperationDialog(@NonNull Context context) {
            super(context);
            @SuppressLint("InflateParams") View layout =
                    getLayoutInflater().inflate(R.layout.album_editer, null);

            Button renameBtn = layout.findViewById(R.id.album_rename_option);
            renameBtn.setOnClickListener(view -> {
                RenameAlbumDialog dialog = new RenameAlbumDialog(context);
                dialog.show();
                clearChoosingState = false;
                dismiss();
            });


            Button deleteBtn = layout.findViewById(R.id.album_delete_option);
            deleteBtn.setOnClickListener(view -> {

                ConfirmDeleteAlbumDialog dialog = new ConfirmDeleteAlbumDialog(context, choosingAlbum, () -> {
                    int index = albumList.indexOf(choosingAlbum);
                    albumList.remove(index);
                    adapter.notifyItemRemoved(index);
                    File album = new File(choosingAlbum.path);
                    fileDelete(album);
                    dismiss();
                });
                dialog.show();
            });

            setContentView(layout);
        }
    }

    public class RenameAlbumDialog extends Dialog {
        @Override
        public void dismiss() {
            super.dismiss();
            clearChoosing();
        }

        public RenameAlbumDialog(@NonNull Context context) {
            super(context);
            @SuppressLint("InflateParams") LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.rename_album_dialog, null);
            ImageButton renameBtn = layout.findViewById(R.id.rename_alubum_button);
            ImageButton cancleBtn = layout.findViewById(R.id.rename_album_cancel);
            EditText oldName = layout.findViewById(R.id.old_album_name);
            oldName.setText(choosingAlbum.name);
            EditText newName = layout.findViewById(R.id.rename_album_name);
            renameBtn.setOnClickListener(view -> {
                int index = albumList.indexOf(choosingAlbum);

                String newAlbumName = newName.getText().toString();

                File oldAlbum = new File(choosingAlbum.path);
                String folderPath = Objects.requireNonNull(oldAlbum.getParentFile()).getAbsolutePath();
                File newAlbum = new File(folderPath + "/" + newAlbumName);

                if (newAlbumName.equals(choosingAlbum.name)) {
                    dismiss();
                    return;
                }
                if (newAlbum.isDirectory()) {
                    Toast.makeText(context, "Album's name already used", Toast.LENGTH_SHORT).show();
                    return;
                }
                oldAlbum.renameTo(newAlbum);
                newAlbum = new File(folderPath + "/" + newAlbumName);
                ArrayList<String> imagePath = new ArrayList<>();

                for (File file : Objects.requireNonNull(newAlbum.listFiles())) {
                    if (file.isDirectory()) {

                    } else {
                        for (String extension : MainActivity.ImageExtensions) {

                            if (file.getAbsolutePath().toLowerCase().endsWith(extension)) {
                                imagePath.add(file.getAbsolutePath());
                                break;
                            }

                        }
                    }
                }
                albumList.remove(index);
                albumList.add(index, new Album(newAlbum.getAbsolutePath(), newAlbum.getName(), imagePath));
                adapter.notifyItemChanged(index);
                dismiss();
            });
            cancleBtn.setOnClickListener(view -> dismiss());
            setContentView(layout);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(Objects.requireNonNull(getWindow()).getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(layoutParams);
        }
    }

    public static class ConfirmDeleteAlbumDialog extends Dialog {
        CallBack callBack;


        public ConfirmDeleteAlbumDialog(@NonNull Context context, Album album, @NonNull CallBack callBack) {
            super(context);
            this.callBack = callBack;

            this.setTitle("Delete confirm");
            this.setContentView(R.layout.delete_album_confirm_dialog);
            Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            String text = (String) ((TextView) this.findViewById(R.id.delete_album_notify)).getText();
            ((TextView) this.findViewById(R.id.delete_album_notify))
                    .setText(text.replace("album_name", album.name));

            this.findViewById(R.id.delete_album_cancel)
                    .setOnClickListener(view -> dismiss());

            this.findViewById(R.id.delete_album_confirm)
                    .setOnClickListener(view -> {
                        callBack.confirmClickedCallback();
                        dismiss();
                    });
        }

        public interface CallBack {
            void confirmClickedCallback();
        }
    }

    private void fileDelete(File file) {
        if (file.isDirectory()) {
            File[] fileInFolder = file.listFiles();
            assert fileInFolder != null;
            for (File fl : fileInFolder) {
                fileDelete(fl);
            }
        }
        if (!file.delete()) {
            Toast.makeText(context, "Delete file fail in AlbumAdapter", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearChoosing() {
        choosingAlbumView = null;
        choosingAlbum = null;
    }

    void showSetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainActivity);
        LayoutInflater inflater = LayoutInflater.from(MainActivity.mainActivity);
        View dialogView = inflater.inflate(R.layout.set_pass,null);
        Button cancelButton = dialogView.findViewById(R.id.set_pass_cancel_button);
        Button confirmButton = dialogView.findViewById(R.id.confirm_pass_button);
        EditText password = dialogView.findViewById(R.id.enter_set_pass);
        EditText confirmPassword = dialogView.findViewById(R.id.confirm_pass);
        EditText numberPhone = dialogView.findViewById(R.id.set_number_phone);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        confirmButton.setOnClickListener(v ->{
            String pass = password.getText().toString().trim();
            String cfpass = confirmPassword.getText().toString().trim();
            String num = numberPhone.getText().toString().trim();
            if (pass.length() < 5){
                Toast.makeText(MainActivity.mainActivity,"Password must have more than 4 characters", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(cfpass)){
                Toast.makeText(MainActivity.mainActivity,"Please enter the correct confirm-password", Toast.LENGTH_SHORT).show();
            } else if (num.length()!=10){
                Toast.makeText(MainActivity.mainActivity,"Please enter the correct your number phone", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharePrf = MainActivity.mainActivity.getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharePrf.edit();
                edit.putBoolean("pass_set", true);
                edit.putString("password", pass);
                edit.putString("number_phone", num);
                edit.apply();
                alertDialog.dismiss();
                Toast.makeText(MainActivity.mainActivity, "Set password success",Toast.LENGTH_SHORT).show();
            }
        });

    }


    void showInputPasswordDialog(View.OnClickListener onClickListener) {
        LayoutInflater inflater = LayoutInflater.from(MainActivity.mainActivity);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.mainActivity);
        View dialogView = inflater.inflate(R.layout.input_pass,null);
        Button okButton = dialogView.findViewById(R.id.ok_pass_button);
        EditText password = dialogView.findViewById(R.id.enter_input_pass);
        EditText numberPhone = dialogView.findViewById(R.id.input_number_phone);
        TextView forgotText = dialogView.findViewById(R.id.forgot_pass);

        forgotText.setOnClickListener(v -> {
            forgotText.setVisibility(View.INVISIBLE);
            dialogView.findViewById(R.id.forgot_layout).setVisibility(View.VISIBLE);
        });



        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(v->{
            String pass = password.getText().toString().trim();
            String num = numberPhone.getText().toString().trim();
            SharedPreferences sharePrf = MainActivity.mainActivity.getSharedPreferences("AppPreferences", MODE_PRIVATE);
            savedPass = sharePrf.getString("password","");
            savedNumber = sharePrf.getString("number_phone", "");
            if (pass.equals(savedPass)){
                onClickListener.onClick(v);
                alertDialog.dismiss();
            } else if (num.equals(savedNumber)){
                onClickListener.onClick(v);
                alertDialog.dismiss();
            } else {
                if (dialogView.findViewById(R.id.forgot_layout).getVisibility() == View.INVISIBLE && !pass.equals(savedPass)){
                    Toast.makeText(MainActivity.mainActivity, "Please enter the correct password",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.mainActivity, "Please enter the correct number phone",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
