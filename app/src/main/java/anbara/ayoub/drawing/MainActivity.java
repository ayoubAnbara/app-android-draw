package anbara.ayoub.drawing;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import anbara.ayoub.drawing.adapter.DrawAdapter;

import es.dmoral.toasty.Toasty;
import guy4444.smartrate.SmartRate;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DrawAdapter drawAdapter;
    private final int REQUEST_CODE_DRAW = 101;

    static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));
        MobileAds.initialize(this, (initializationStatus) ->{
            }
        );


        // private InterstitialAd interstitialAd;
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .tagForChildDirectedTreatment(true)
                .build();
        mAdView.loadAd(adRequest);


        mainActivity = this;
        recyclerView = findViewById(R.id.recycler_view);

        /**
         * TODO change two parameter of LinearLayoutManager() to adapt portrait layout
         * */
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        methodRequiresTwoPermission();

       /*
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            drawAdapter = new DrawAdapter(MainActivity.this, getFilesPath(), MainActivity.this);
                            recyclerView.setAdapter(drawAdapter);
                            drawAdapter.notifyDataSetChanged();


                        } else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(getString(R.string.permission_denied))
                                    .setMessage(getString(R.string.msgToUser_when_permission_denied))
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            MainActivity.this.recreate();
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            MainActivity.this.finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown
                            (List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                })
                .check();

*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final String appPackageName = getPackageName();

        switch (id) {
            //final String appPackageName = "ayoub.anbara.yoga";
            case R.id.rate:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;

            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String txt = getString(R.string.text_to_share) + " " + "https://play.google.com/store/apps/details?id=" + appPackageName;
                shareIntent.putExtra(Intent.EXTRA_TEXT, txt);
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app_intent)));
                return true;

            case R.id.moreApp:
                moreApp(MainActivity.this);
                return true;

            case R.id.privacy_policy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.getString(R.string.url_pravacy)));
                startActivity(browserIntent);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_DRAW) {
                byte[] result = data.getByteArrayExtra("bitmap");
                Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
                String fileName = UUID.randomUUID().toString();
                String imageDir = "${Environment.DIRECTORY_PICTURES}/Android Draw/";
                File path = Environment.getExternalStoragePublicDirectory(imageDir);
                // Log.e("path", path.toString())
                File file = new File(path, fileName + ".png");
                path.mkdirs();
                try {
                    file.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    drawAdapter.addItem(Uri.fromFile(file));

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void moreApp(Context c) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=pub:" + "developer 4you"));
        if (!MyStartActivity(intent, c)) {
            intent.setData(Uri.parse("http://play.google.com/store/search?q=pub:" + "developer 4you"));
            if (!MyStartActivity(intent, c)) {
                Toasty.warning(MainActivity.this, getString(R.string.dowloand_play_store), Toast.LENGTH_LONG, true).show();
            }
        }
    }

    public static boolean MyStartActivity(Intent aIntent, Context c) {
        try {
            c.startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private ArrayList<String> getFilesPath() {
        ArrayList<String> resultList = new ArrayList<>();
        String imageDir = "${Environment.DIRECTORY_PICTURES}/Android Draw/";
        File path = Environment.getExternalStoragePublicDirectory(imageDir);
        path.mkdirs();
        File[] imageList = path.listFiles();
        for (File imagePath : imageList) {
            resultList.add(imagePath.getAbsolutePath());
        }
        return resultList;
    }



    @Override
    public void onBackPressed() {
        showRateUsDialog();
            super.onBackPressed();

    }

public final  void showRateUsDialog(){
    // For continual calls -
    SmartRate.Rate(MainActivity.this
            , "Rate Us"
            , "Tell others what you think about this app"
            , "Continue"
            , "Please take a moment and rate us on Google Play"
            , "click here"
            , "Ask me later"
            , "Never ask again"
            , "Cancel"
            , "Thanks for the feedback"
            , Color.parseColor("#2196F3")
            , 4
            , 48
            , 60
    );
}

    public void onClickFabNew(View view)
    {
        Intent intent = new Intent(MainActivity.this, DrawingActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DRAW);
    }
    public static final int READ_AND_WRITE = 777;
    @AfterPermissionGranted(READ_AND_WRITE)
    private void methodRequiresTwoPermission() {

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            drawAdapter = new DrawAdapter(MainActivity.this, getFilesPath(), MainActivity.this);
            recyclerView.setAdapter(drawAdapter);
            drawAdapter.notifyDataSetChanged();


        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.msgToUser_when_permission_denied),
                    READ_AND_WRITE, perms);
        }
    }
}

