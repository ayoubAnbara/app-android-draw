package anbara.ayoub.drawing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import anbara.ayoub.drawing.Utils.BitmapUtils;
import anbara.ayoub.drawing.fragments.AddTextFragment;
import anbara.ayoub.drawing.fragments.BackgroundFragment;
import anbara.ayoub.drawing.fragments.BrushFragment;
import anbara.ayoub.drawing.fragments.FrameFragment;
import anbara.ayoub.drawing.fragments.NumberFragment;
import anbara.ayoub.drawing.interfaces.AddFrameListener;
import anbara.ayoub.drawing.interfaces.AddNumberListener;
import anbara.ayoub.drawing.interfaces.AddTextFragmentListener;
import anbara.ayoub.drawing.interfaces.BackgroundFragmentListener;
import anbara.ayoub.drawing.interfaces.BrushFragmentListener;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;


import java.io.ByteArrayOutputStream;

public class DrawingActivity extends AppCompatActivity implements AddNumberListener, AddFrameListener, BrushFragmentListener, AddTextFragmentListener, BackgroundFragmentListener {
    public static final String pictureName = "flash.jpeg"; // to resolve problem size of background use image in format jpeg and use .jpeg not .jpg
    // public static final String pictureName = "foret.png"; // to resolve problem size of background use image in format jpeg and use .jpeg not .jpg
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    Bitmap original_bitmap;

    CardView btn_refresh, btn_background, btn_add_letter, btn_add_number, btn_brush, btn_add_text, btn_add_image;
    private HorizontalScrollView horizontalScrollView;
    private boolean isClose = true;
    private InterstitialAd interstitialAd;
    private SharedPreferences sharedPreferences;
    private int  nombreClickOnBtnRefresh;
    private ProgressBar progressBar;
    private ImageButton  imageBtn_pin;
    private BrushFragment brushFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_drawing);
        showMyInterstitialAd();
        initAllView();
        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        // initialize nombreClickOnBtnRefresh
        nombreClickOnBtnRefresh = sharedPreferences.getInt("nombreClickOnBtnRefresh", 0);


        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .build();


        photoEditor.setBrushDrawingMode(true);
        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enable brush mode
                photoEditor.setBrushDrawingMode(true);
                // BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment = BrushFragment.getInstance();

                brushFragment.setListener(DrawingActivity.this);
                if (!brushFragment.isAdded())
                    brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());


            }
        });

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setListener(DrawingActivity.this);
                if (!addTextFragment.isAdded())
                    addTextFragment.show(getSupportFragmentManager(), addTextFragment.getTag());

            }
        });
        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageToPicture();
            }
        });

        btn_add_letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FrameFragment frameFragment = FrameFragment.getInstance();
                frameFragment.setListener(DrawingActivity.this);

                if (!frameFragment.isAdded())
                    frameFragment.show(getSupportFragmentManager(), frameFragment.getTag());

            }
        });
        btn_add_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NumberFragment numberFragment = NumberFragment.getInstance();
                numberFragment.setListener(DrawingActivity.this);

                if (!numberFragment.isAdded()) {
                    numberFragment.show(getSupportFragmentManager(), numberFragment.getTag());
                    //  DrawingActivity.hideStatusBar();
                }
            }
        });

        btn_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundFragment backgroundFragment = BackgroundFragment.getInstance();
                backgroundFragment.setListener(DrawingActivity.this);

                if (!backgroundFragment.isAdded())
                    backgroundFragment.show(getSupportFragmentManager(), backgroundFragment.getTag());

            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("nombreClickOnBtnRefresh", ++nombreClickOnBtnRefresh);
                editor.apply()  ;
                if (sharedPreferences.getInt("nombreClickOnBtnRefresh", 0) >= 3) {
                  //   Toast.makeText(DrawingActivity.this, ""+sharedPreferences.getInt("nombreClickOnBtnRefresh", 0), Toast.LENGTH_SHORT).show();
                    if (interstitialAd.isLoaded()) {
                        editor.putInt("nombreClickOnBtnRefresh", 0);
                        interstitialAd.show();

                    }
                }
                editor.apply();
                recreate();
            }
        });

        loadImage();
        horizontalScrollView.setVisibility(View.INVISIBLE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean b = sharedPreferences.getBoolean("isRecreate", false);
        if (b) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            editor.putBoolean("isRecreate", false);
            editor.apply();
        }


    }

    private void addImageToPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PERMISSION_INSERT_IMAGE);
    }

    private void loadImage() {
        //if (getIntent().getBooleanExtra(Constants.KEY_NEW_PAGE,false)) {
        original_bitmap = BitmapUtils.getBitmapFromAssets(this
                , pictureName, 300, 300);
        photoEditorView.getSource().setImageBitmap(original_bitmap);

        //}else {
//            String path = getIntent().getStringExtra(IMAGE_PATH);
//            photoEditorView.getSource().setImageURI(Uri.parse(path));
//            //photoEditorView.getSource().setImageResource(R.drawable.a);
//        }
    }

    //private void saveImageToGallery() {
//        Dexter.withActivity(this)
//                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
//                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
//                                @Override
//                                public void onBitmapReady(Bitmap saveBitmap) {
//                                    try {
//
//                                        photoEditorView.getSource().setImageBitmap(saveBitmap);
//
//
//                                        final String path = BitmapUtils.inserImage(getContentResolver()
//                                                , saveBitmap,
//                                                System.currentTimeMillis() + "_drawing.jpg"
//                                                , null
//                                        );
//
//                                        progressBar.setVisibility(View.VISIBLE);
//                                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//                                        Runnable progressRunnable = new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                progressBar.setVisibility(View.GONE);
//                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                                                if (!TextUtils.isEmpty(path)) {
//
//                                                    // Toast.makeText(DrawingActivity.this, "image_saved_to_gallery", Toast.LENGTH_SHORT).show();
//                                                    Toasty.success(DrawingActivity.this, R.string.image_saved_to_gallery, Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toasty.error(DrawingActivity.this, R.string.unable_to_save_image, Toast.LENGTH_SHORT).show();
//
//                                                }
//                                            }
//                                        };
//                                        Handler pdCanceller = new Handler();
//                                        pdCanceller.postDelayed(progressRunnable, 2500);
//
//
//
//                                    } catch(IOException e)
//
//                                    {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure (Exception e){
//
//                                }
//                            });
//
//
//                        } else
//
//                        {
//                            // Toast.makeText(MainActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
//                            Toasty.warning(DrawingActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT, true).show();
//
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown
//                            (List < PermissionRequest > permissions, PermissionToken token){
//
//                        token.continuePermissionRequest();
//                    }
//                })
//                .check();
////        if(mInterstitialAd.isLoaded())
////
////        {
////            mInterstitialAd.show();
////        }

//           }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == PERMISSION_INSERT_IMAGE && data != null) {

                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(),
                        250, 250);
                photoEditor.addImage(bitmap);
                imageBtn_pin.setBackgroundColor(Color.RED);
            }
        }
    }


    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);

    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);

    }

    @Override
    public void onBrushClickListener() {
        photoEditor.setBrushDrawingMode(true);
        //if (brushFragment.isAdded())
        brushFragment.dismiss();
    }

    @Override
    public void onEraserClickListener() {
        photoEditor.brushEraser();
        brushFragment.dismiss();


    }


    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface, text, color);
        AddTextFragment.getInstance().dismiss();
        imageBtn_pin.setBackgroundColor(Color.RED);
    }

    public void onClickUndo(View view) {
        photoEditor.undo();

    }

    public void onClickRedo(View view) {
        photoEditor.redo();
    }

    public void onClearAll(View view) {
        photoEditor.clearAllViews();
    }

    public void onClearHelperBox(View view) {
        photoEditor.clearHelperBox();
        imageBtn_pin.setBackgroundColor(ContextCompat.getColor(DrawingActivity.this, R.color.card_background_color));
    }

    private void initAllView() {
        photoEditorView = findViewById(R.id.photoEditorView);
        btn_brush = findViewById(R.id.floatingBtn_brush);
        btn_add_text = findViewById(R.id.btn_add_text);
        btn_add_image = findViewById(R.id.btn_add_image);
        btn_add_letter = findViewById(R.id.btn_add_letter);
        btn_add_number = findViewById(R.id.btn_add_number);
        btn_background = findViewById(R.id.btn_background);
        btn_refresh = findViewById(R.id.btn_refresh);
        progressBar = findViewById(R.id.progress_bar);
        horizontalScrollView = findViewById(R.id.horizontalScrollView2);
        imageBtn_pin = findViewById(R.id.imageBtn_pin);

    }


    public void onOpenMenu(View view) {
        if (isClose) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            isClose = false;
        } else {
            horizontalScrollView.setVisibility(View.GONE);
            isClose = true;
        }
    }

    @Override
    public void onAddFrame(int frame) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), frame);
        photoEditor.addImage(bitmap);
        FrameFragment.getInstance().dismiss();
        imageBtn_pin.setBackgroundColor(Color.RED);

    }

    @Override
    public void onAddNumber(int number) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), number);
        photoEditor.addImage(bitmap);
        NumberFragment.getInstance().dismiss();
        imageBtn_pin.setBackgroundColor(Color.RED);
    }

    @Override
    public void onBackgroundColorChangedListener(int color) {
        ImageView imageView = photoEditorView.getSource();
        imageView.setBackgroundColor(color);
        imageView.buildDrawingCache();
        Bitmap bmap = imageView.getDrawingCache();
        photoEditorView.getSource().setImageBitmap(bmap);


    }


    public void onClickSave(View view) {
        //saveImageToGallery();
        myFunctionSave();
    }

    void myFunctionSave() {

        photoEditor.saveAsBitmap(new OnSaveBitmap() {
            @Override
            public void onBitmapReady(Bitmap saveBitmap) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    // val bitmap = draw_view.getBitmap()
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byte[] byteArray = bStream.toByteArray();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("bitmap", byteArray);
                    setResult(Activity.RESULT_OK, returnIntent);
                    DrawingActivity.this.finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //
            @Override
            public void onFailure(Exception e) {

            }
        });


    }

    void showMyInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        interstitialAd.loadAd(new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAd.loadAd(new AdRequest.Builder().tagForChildDirectedTreatment(true).build());
            }

        });
    }
}
