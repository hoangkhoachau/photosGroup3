package com.example.photosGroup3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.photosGroup3.Callback.EditImageCallbacks;
import com.example.photosGroup3.Utils.ImageDelete;
import com.example.photosGroup3.Utils.ImageUtility;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class EditImage extends AppCompatActivity implements EditImageCallbacks{

    ImageButton edit_cancel;
    ImageButton edit_confirm;
    ImageButton edit_reset;
    LinearLayout transform_btn, filter_btn, blur_btn;
    ImageView edit_img;
    FragmentTransaction ft;
    EditTransformFragment transformFragment;
    BlurEditorFragment blurFragment;
    EditFilterFragment filterFragment;
    String imgName = null;
    Bitmap editedImage = null;
    Boolean VerticalFlip = false;
    Boolean HorizontalFlip = false;
    LinearLayout linearView;
    FrameLayout fragmentLayoutDisplay;
    String[] listName = {"No Effect", "Forest", "Cozy", "Evergreen", "Grayscale", "Vintage"};
    RelativeLayout edit_nav;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        initView();
        edit_confirm.setOnClickListener(view -> {
//            String[] temp = new String[1];
//            temp[0] = ImageDelete.saveImage(editedImage, imgName);
            ImageDelete.overwriteImage(editedImage, imgName);
//            Intent intent = new Intent(getApplicationContext(), SelectedPicture.class);
//            intent.putExtra("imgPath", temp[0]);
//            setResult(RESULT_OK, intent);
            finish();
        });
        edit_reset.setOnClickListener(view -> {
            File imgFile = new File(imgName);
            editedImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            edit_img.setImageBitmap(editedImage);
        });
        imgName = getIntent().getStringExtra("imgPath");
        assert imgName != null;
        File imgFile = new File(imgName);
        editedImage = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        edit_img.setImageBitmap(editedImage);
        edit_cancel.setOnClickListener(view -> finish());
        transform_btn.setOnClickListener(view -> {
            File tempDestination = createTempFile("cropped1");
            File tempFile = createTempFile("input");
            OutputStream os = null;
            try {
                os = new FileOutputStream(tempFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            editedImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
            try {
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AspectRatio[] aspectRatios = {
                    new AspectRatio("Free", 0, 0),      // Free aspect ratio
                    new AspectRatio("1:1", 1, 1),       // Square
                    new AspectRatio("16:9", 16, 9),     // Widescreen
                    new AspectRatio("4:3", 4, 3),       // Standard
                    new AspectRatio("3:2", 3, 2),       // 35mm film
                    new AspectRatio("5:4", 5, 4),       // Large format
                    new AspectRatio("7:5", 7, 5),       // Traditional photo print
                    new AspectRatio("9:16", 9, 16)      // Portrait video
            };

            UCrop.Options options = new UCrop.Options();
            options.setAspectRatioOptions(0, aspectRatios[0], aspectRatios[1], aspectRatios[2], aspectRatios[3], aspectRatios[4], aspectRatios[5], aspectRatios[6], aspectRatios[7]);
            options.setCompressionQuality(100);

            UCrop.of(Uri.fromFile(tempFile), Uri.fromFile(tempDestination))
                    /**/.withOptions(options)
                    .withMaxResultSize(1000, 1000)
                    .start(this); // Use requireContext() to get the fragment's context


//            linearView.setVisibility(View.INVISIBLE);
//            fragmentLayoutDisplay.setVisibility(View.VISIBLE);
//            edit_nav.setVisibility(View.GONE);
//            ft = getSupportFragmentManager().beginTransaction();
//            transformFragment = EditTransformFragment.newInstance();
//            ft.replace(R.id.fragment_function_btns, transformFragment);
//            ft.commit();
        });

        blur_btn.setOnClickListener(view -> {
            linearView.setVisibility(View.INVISIBLE);
            fragmentLayoutDisplay.setVisibility(View.VISIBLE);
            edit_nav.setVisibility(View.GONE);
            ft = getSupportFragmentManager().beginTransaction();
            blurFragment = BlurEditorFragment.newInstance();
            ft.replace(R.id.fragment_function_btns, blurFragment);
            ft.commit();
        });

        filter_btn.setOnClickListener(view -> {
            linearView.setVisibility(View.INVISIBLE);
            edit_nav.setVisibility(View.GONE);
            ArrayList<Bitmap> listImage = new ArrayList<>();
            for (String s : listName) {
                listImage.add(ImageUtility.setFilter(editedImage, s));
            }

            fragmentLayoutDisplay.setVisibility(View.VISIBLE);
            ft = getSupportFragmentManager().beginTransaction();
            filterFragment = EditFilterFragment.newInstance(listName, listImage);
            ft.replace(R.id.fragment_function_btns, filterFragment);
            ft.commit();
        });
    }


    @Override
    public void TransformVertical() {
        Matrix matrixMirror = new Matrix();
        VerticalFlip = !VerticalFlip;
        matrixMirror.preScale(-1.0f, 1.0f);
        editedImage = Bitmap.createBitmap(
                editedImage,
                0,
                0,
                editedImage.getWidth(),
                editedImage.getHeight(),
                matrixMirror,
                false);

        edit_img.setImageBitmap(editedImage);
        Toast.makeText(this, "Vertical", Toast.LENGTH_SHORT).show();
    }

    void initView() {
        fragmentLayoutDisplay = findViewById(R.id.fragment_function_btns);
        linearView = findViewById(R.id.edit_central_btn);
        edit_cancel = findViewById(R.id.edit_cancel_btn);
        edit_confirm = findViewById(R.id.edit_confirm_btn);
        edit_nav = findViewById(R.id.edit_nav);
        edit_reset = findViewById(R.id.edit_reset_btn);
        filter_btn = findViewById(R.id.edit_filter_btn);
        transform_btn = findViewById(R.id.edit_transform_btn);
        blur_btn = findViewById(R.id.blur_btn);
        edit_img = findViewById(R.id.edit_image_object);
    }

    @Override
    public void TransformHorizontal() {

        Matrix matrixMirror = new Matrix();
        HorizontalFlip = !HorizontalFlip;

        matrixMirror.preScale(1.0f, -1.0f);

        editedImage = Bitmap.createBitmap(
                editedImage,
                0,
                0,
                editedImage.getWidth(),
                editedImage.getHeight(),
                matrixMirror,
                false);

        edit_img.setImageBitmap(editedImage);

        Toast.makeText(this, "Horizontal", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void BackFragment() {
        linearView.setVisibility(View.VISIBLE);
        fragmentLayoutDisplay.setVisibility(View.GONE);
        edit_nav.setVisibility(View.VISIBLE);
        edit_img.setImageBitmap(editedImage);
    }

    @Override
    public Bitmap blurFast(int radius) {
        Bitmap bmp = editedImage;
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pix = new int[w * h];
        bmp.getPixels(pix, 0, w, 0, 0, w, h);

        for (int r = radius; r >= 1; r /= 2) {
            for (int i = r; i < h - r; i++) {
                for (int j = r; j < w - r; j++) {
                    int tl = pix[(i - r) * w + j - r];
                    int tr = pix[(i - r) * w + j + r];
                    int tc = pix[(i - r) * w + j];
                    int bl = pix[(i + r) * w + j - r];
                    int br = pix[(i + r) * w + j + r];
                    int bc = pix[(i + r) * w + j];
                    int cl = pix[i * w + j - r];
                    int cr = pix[i * w + j + r];

                    pix[(i * w) + j] = 0xFF000000 |
                            (((tl & 0xFF) + (tr & 0xFF) + (tc & 0xFF) + (bl & 0xFF) +
                                    (br & 0xFF) + (bc & 0xFF) + (cl & 0xFF) + (cr & 0xFF)) >> 3) & 0xFF |
                            (((tl & 0xFF00) + (tr & 0xFF00) + (tc & 0xFF00) + (bl & 0xFF00)
                                    + (br & 0xFF00) + (bc & 0xFF00) + (cl & 0xFF00) + (cr & 0xFF00)) >> 3) & 0xFF00 |
                            (((tl & 0xFF0000) + (tr & 0xFF0000) + (tc & 0xFF0000) +
                                    (bl & 0xFF0000) + (br & 0xFF0000) + (bc & 0xFF0000) + (cl & 0xFF0000) +
                                    (cr & 0xFF0000)) >> 3) & 0xFF0000;
                }
            }
        }
        Bitmap blurred = Bitmap.createBitmap(w, h, bmp.getConfig());
        blurred.setPixels(pix, 0, w, 0, 0, w, h);

        edit_img.setImageBitmap(blurred);
        return blurred;
    }

    @Override
    public void ConfirmBlur(Bitmap input) {
        editedImage = input;
    }

    @Override
    public void BitmapFilterChoose(Bitmap input, String name) {
        edit_img.setImageBitmap(input);

    }

    @Override
    public void recreateOnDarkMode() {
        recreate();
    }

    // Helper method to create a temporary file
    private File createTempFile(String prefix) {
        try {
            return File.createTempFile(prefix, null, this.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // Get the cropped image URI
            final Uri resultUri = UCrop.getOutput(data);

            // Load the cropped image into your ImageView or do further processing
            if (resultUri != null) {
                editedImage = BitmapFactory.decodeFile(resultUri.getPath());
                edit_img.setImageBitmap(editedImage);
                // Now you have the cropped Bitmap, do something with it
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle any errors that occurred during cropping
            final Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                cropError.printStackTrace();
            }
        }
    }

}
