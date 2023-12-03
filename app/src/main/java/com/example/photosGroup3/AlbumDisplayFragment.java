package com.example.photosGroup3;

import static androidx.core.view.KeyEventDispatcher.dispatchKeyEvent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// TO DO: add new toolbar for this fragment
public class AlbumDisplayFragment extends Fragment implements ImageDisplay.LongClickCallback {
    Context context;
    ImageButton back_button, resize_button;
    TextView album_name, album_images_count;
    Album album;
    FloatingActionButton add_images;
    TableLayout header;
    ArrayList<String> addedPaths = new ArrayList<>();
    ImageDisplay instance;

    public AlbumDisplayFragment() {
        // Required empty public constructor
    }


    public static AlbumDisplayFragment newInstance(Album album) {
        AlbumDisplayFragment fragment = new AlbumDisplayFragment();
        fragment.album = album;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get args
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CoordinatorLayout layout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_album_display, container, false);

        header = layout.findViewById(R.id.album_header);
        back_button = layout.findViewById(R.id.album_display_back);
        album_name = layout.findViewById(R.id.album_display_name);
        album_images_count = layout.findViewById(R.id.album_images_count3);
        resize_button = layout.findViewById(R.id.resizeBtn);

        back_button.setOnClickListener(view -> {
            getFragmentManager().popBackStack();
        });
        resize_button.setOnClickListener(view -> {
            instance.numCol = instance.numCol % 5 + 1;
            if (instance.numCol == 1) {
//                    numCol=2;
                instance.recyclerView.setAdapter(instance.listAdapter);

            } else if (instance.numCol == 2) {
//                ImageDisplay.getInstance().gridView.setAdapter(ImageDisplay.getInstance().customAdapter);
            }
//            ImageDisplay.getInstance().gridView.setNumColumns(ImageDisplay.getInstance().numCol);
        });
        add_images = layout.findViewById(R.id.add_image);
        add_images.setOnClickListener(view -> {
            ImageChoosingDialog dialog = new ImageChoosingDialog(context);
            dialog.show();
        });
        album_name.setText(album.name);
        album_images_count.setText(String.format(context.getString(R.string.album_image_count), album.imagePaths.size()));


        instance = new ImageDisplay();
        instance.setImagesData(album.imagePaths);
        instance.setLongClickCallBack(this);

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.album_display_list, instance, null)
                .commit();

        return layout;
    }

    @Override
    public void onDestroyView() {
//        ImageDisplay.restoreINSTANCE();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        instance.getToolbar().setVisibility(View.GONE);
        instance.fab_expand.setVisibility(View.GONE);
    }

    @Override
    public void onLongClick() {
        ViewGroup.LayoutParams params = header.getLayoutParams();
        params.height = (int) (60 * getResources().getDisplayMetrics().density);
        header.setLayoutParams(params);
    }

    @Override
    public void afterLongClick() {
        ViewGroup.LayoutParams params = header.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        header.setLayoutParams(params);
        album_images_count.setText(String.format(context.getString(R.string.album_image_count), album.imagePaths.size()));
    }


    private class ImageChoosingDialog extends Dialog {
        public ImageChoosingDialog(@NonNull Context context) {
            super(context);
            addedPaths.clear();
            @SuppressLint("InflateParams") RelativeLayout layout =
                    (RelativeLayout) getLayoutInflater().inflate(R.layout.image_choosing_bar, null);

            GridView imageList = layout.findViewById(R.id.image_choosing_imageList);
            imageList.setAdapter(new ImageChoosingAdapter(((MainActivity) context).getFileinDir()));


            ImageButton add_btn = layout.findViewById(R.id.image_choosing_add);
            add_btn.setOnClickListener(view -> {
                dismiss();
                MoveOrCopyForDialog.MoveOrCopyCallBack callBack = new MoveOrCopyForDialog.MoveOrCopyCallBack() {
                    @Override
                    public void dismissCallback(String method) {
                        album_images_count.setText(String.format(context.getString(R.string.album_image_count), album.imagePaths.size()));
                    }

                    @Override
                    public void copiedCallback(String newImagePath) {
                        instance.addNewImage(newImagePath, 1);
                    }

                    @Override
                    public void removedCallback(String oldImagePath, String newImagePath) {
                        ((MainActivity) context).FileInPaths.remove(oldImagePath);
                        instance.addNewImage(newImagePath, 1);
                    }
                };
                MoveOrCopyForDialog dialog = new MoveOrCopyForDialog(context, callBack, album, addedPaths);
                dialog.show();
            });

            ImageButton cancel_btn = layout.findViewById(R.id.image_choosing_cancel);
            cancel_btn.setOnClickListener(view -> dismiss());

            setContentView(layout);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(Objects.requireNonNull(getWindow()).getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(layoutParams);
        }

        private class ImageChoosingAdapter extends BaseAdapter {
            ArrayList<String> allImagePaths;
            ArrayList<Boolean> checkBoxValues;

            public ImageChoosingAdapter(ArrayList<String> allImagePaths) {
                this.allImagePaths = allImagePaths;

                checkBoxValues = new ArrayList<>();
                for (int i = 0; i < allImagePaths.size(); i++) {
                    checkBoxValues.add(false);
                }
            }

            @Override
            public int getCount() {
                return allImagePaths.size();
            }

            @Override
            public Object getItem(int i) {
                return allImagePaths.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder viewHolder;
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.row_item_with_choose, viewGroup, false);
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = view.findViewById(R.id.image_to_choose);
                    viewHolder.checkBox = view.findViewById(R.id.image_check_box);
                    viewHolder.checkBox.setChecked(checkBoxValues.get(i));
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                view.setOnClickListener(view1 -> {
                    ViewHolder viewHolder1 = (ViewHolder) view1.getTag();
                    if (viewHolder1.checkBox.isChecked()) {
                        checkBoxValues.remove(i);
                        checkBoxValues.add(i, false);
                        viewHolder1.checkBox.setChecked(checkBoxValues.get(i));
                        addedPaths.remove(allImagePaths.get(i));
                    } else {
                        checkBoxValues.remove(i);
                        checkBoxValues.add(i, true);
                        viewHolder1.checkBox.setChecked(checkBoxValues.get(i));
                        addedPaths.add(allImagePaths.get(i));
                    }
                });

                viewHolder.checkBox.setChecked(checkBoxValues.get(i));
                File imgFile = new File(allImagePaths.get(i));
//                ImageLoader.getInstance().displayImage(String.valueOf(
//                        Uri.parse("file://" + imgFile.getAbsolutePath())), viewHolder.imageView);
                Glide.with(context)
                        .load(Uri.parse("file://" + imgFile.getAbsolutePath()))
                        .into(viewHolder.imageView);
                return view;
            }

            private class ViewHolder {
                ImageView imageView;
                CheckBox checkBox;
            }
        }
    }

}