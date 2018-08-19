package com.imagine.bd.hayvenapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imagine.bd.hayvenapp.R;
import com.imagine.bd.hayvenapp.utils.CircleTransform;
import com.imagine.bd.hayvenapp.utils.RealPathUtil;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class NewGroupChatActivity extends AppCompatActivity {
    TextView tvNext;
    ImageView imageView, imgCapture;
    private Context con;
    private EditText etGroupName;
    private Dialog dialog;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2 = 99;
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA = 100;
    Bitmap bitmap;
    Uri uri;
    Intent picIntent = null;
    private File file;
    // get real path
    private String filePath = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group_chat);
        con = this;
        initUI();
    }

    public void initUI() {
        etGroupName = (EditText) findViewById(R.id.etGroupName);
        imgCapture = (ImageView) findViewById(R.id.imgCapture);
        tvNext = (TextView) findViewById(R.id.tvNext);
        imageView = (ImageView) findViewById(R.id.notificationChat);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = etGroupName.getText().toString();

                Intent i = new Intent(NewGroupChatActivity.this, SocialCommittee.class);
                i.putExtra("groupName", groupName);
                startActivity(i);
                finish();
            }
        });


        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureDialog();
            }
        });
    }


    public void captureDialog() {
        // TODO Auto-generated method stub
        dialog = new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chang_photo_dialogue);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout tvUseCam = (LinearLayout) dialog
                .findViewById(R.id.tvUseCam);
        LinearLayout tvRoll = (LinearLayout) dialog
                .findViewById(R.id.tvRoll);
        LinearLayout tvCance = (LinearLayout) dialog
                .findViewById(R.id.tvCance);

        // Typeface tf = Typeface.createFromAsset(context.getAssets(),
        // "fonts/OpenSans-Bold.ttf");
        // Typeface tf2 = Typeface.createFromAsset(context.getAssets(),
        // "fonts/OpenSans-Light.ttf");
        //
        // ok_about.setTypeface(tf);
        // version.setTypeface(tf2);

        tvRoll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              /*  // TODO Auto-generated method stub
                picIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                picIntent.setType("image*//*");
                picIntent.putExtra("return_data", true);
                startActivityForResult(picIntent, GALLERY_REQUEST);
                dialog.dismiss();*/
                checkPermissionExternal();
            }
        });

        tvUseCam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* // TODO Auto-generated method stub
                picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(picIntent, CAMERA_REQUEST);
                dialog.dismiss();*/
                checkPermissionExternalCamera();
            }
        });

        tvCance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                        BitmapFactory.decodeStream(con.getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        Bitmap image = BitmapFactory.decodeStream(con.getContentResolver().openInputStream(uri), null, options);
                        //  take_picture.setVisibility(View.GONE);
                        //   tvUpload.setVisibility(View.VISIBLE);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

// get real path
                    String realPath;
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(con, data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(con, data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API19(con, data.getData());

                    Log.e("galary path", realPath);
                    filePath = realPath;

                    file = new File(realPath);
                    Picasso.with(con).load(file).transform(new CircleTransform()).into(imgCapture);

                } else {
                    Toast.makeText(con.getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(con.getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("data")) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    uri = getImageUri(NewGroupChatActivity.this, bitmap);
                    file = new File(getRealPathFromUri(uri));
                    Log.e("camera path", getRealPathFromUri(uri));
                    filePath = getRealPathFromUri(uri);

                    Picasso.with(con).load(file).transform(new CircleTransform()).into(imgCapture);
                    //   take_picture.setVisibility(View.GONE);
                    //   tvUpload.setVisibility(View.VISIBLE);
                } else if (data.getExtras() == null) {

                    Toast.makeText(con.getApplicationContext(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());
                    Picasso.with(con).load(file).transform(new CircleTransform()).into(imgCapture);

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(con.getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = con.getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private Uri getImageUri(Activity youractivity, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(con.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    // check runtime gallery read extarnal storage
    public void checkPermissionExternal() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA);

            }
        } else {

            //  display_galary_image_dialogue();

            picIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
            picIntent.setType("image*//*");
            picIntent.putExtra("return_data", true);
            startActivityForResult(picIntent, GALLERY_REQUEST);
            dialog.dismiss();
        }
    }

    // check runtime permission read external and camera
    public void checkPermissionExternalCamera() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA);
            }
        } else {

            picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(picIntent, CAMERA_REQUEST);
            dialog.dismiss();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    //   display_galary_image_dialogue();
                    picIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    picIntent.setType("image*//*");
                    picIntent.putExtra("return_data", true);
                    startActivityForResult(picIntent, GALLERY_REQUEST);
                    dialog.dismiss();

                    Toast.makeText(con, "A Permission garanted ", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(con, "A Permission Denied ", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }


            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                   /* startActivity(new Intent(con, Custom_CameraActivity.class));
                    Toast.makeText(con, "A Permission garanted ", Toast.LENGTH_SHORT).show();*/

                    picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(picIntent, CAMERA_REQUEST);
                    dialog.dismiss();

                } else {
                    Toast.makeText(con, "A Permission Denied ", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request

        }
    }
}
