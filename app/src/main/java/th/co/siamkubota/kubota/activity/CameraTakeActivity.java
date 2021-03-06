package th.co.siamkubota.kubota.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.nostra13.universalimageloader.utils.L;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.logger.Logger;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.ImageFile;


public class CameraTakeActivity extends BaseActivity implements
        View.OnClickListener
{

    private String TAG = CameraTakeActivity.class.getSimpleName();
    protected AppController app;

    public static final String KEY_COMMAND = "COMMAND";
    public static final String KEY_REQUEST_CODE = "REQUEST_CODE";
    public static final String KEY_GROUP_CODE = "GROUP_CODE";
    public static final String KEY_CHECK_PRESENT = "CHECK_PRESENT";
    public static final String KEY_RENDER_DATE = "RENDER_DATE";
    public static final String KEY_RENDER_TEXT = "RENDER_TEXT";

    public static final int COMMAND_REQUEST_CAMERA = 0;
    public static final int COMMAND_SELECT_FILE = 1;

    //private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private int command;
    private int GROUP_CODE = -1;
    private boolean checkPresentImage = false;
    private boolean renderDate = false;
    private String renderText;

    private ImageView imageView;
//    private Button confirmButton;
//    private Button retakeButton;

    private Bitmap image;
    private Date takenDate;
    private Uri mImageUri;

    public static final int PERMISSION_ALL = 20;
    String[] PERMISSIONS = { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_confirm);

        app = (AppController)getApplication();

        imageView = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        command  = bundle.getInt(CameraTakeActivity.KEY_COMMAND, 0);  //รับ KEY_COMMAND
        Logger.Log("command", String.valueOf(command));
        GROUP_CODE = bundle.getInt(CameraTakeActivity.KEY_GROUP_CODE, -1);
        checkPresentImage = bundle.getBoolean(CameraTakeActivity.KEY_CHECK_PRESENT, false);
        //renderDate = bundle.getBoolean(CameraTakeActivity.KEY_RENDER_DATE,false);
        renderText = bundle.getString(CameraTakeActivity.KEY_RENDER_TEXT, null); //รับ KEY_RENDER_TEXT
        Logger.Log("renderText", String.valueOf(renderText));

       /* switch (command){
            case 0:
                requestCamera();
                break;
            case 1:
                selectFile();
                break;
            default:
                break;
        }*/

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else{

           /* switch (command){
                case 0:
                    requestCamera();
                    break;
                case 1:
                    selectFile();
                    break;
                default:
                    break;
            }*/

            chooser(command);
        }


    }

    private void chooser(int command){
        switch (command){
            case 0:

                requestCamera();
                Logger.Log("requestCamera_requestCamera", String.valueOf(command));
                break;
            case 1:
                selectFile();
                Logger.Log("requestCamera_selectFile", String.valueOf(command));
                break;
            default:
                break;
        }
    }


//    private void selectImage() {
//        final CharSequence[] items = { "Take Photo", "Choose from Library",
//                "Cancel" };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(CameraTakeActivity.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, COMMAND_REQUEST_CAMERA);
//                } else if (items[item].equals("Choose from Library")) {
//                    Intent intent = new Intent(
//                            Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select File"),
//                            COMMAND_SELECT_FILE);
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }


    @Override
    public void onClick(View v) {
        /*if(v == confirmButton){
            finishWithResult(image);
        }else{
            requestCamera();
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.Log("requestCodeCamere", String.valueOf(requestCode));

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == COMMAND_SELECT_FILE) {

                Logger.Log("requestCodeCamere COMMAND_SELECT_FILE =1  ", String.valueOf(requestCode));
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == COMMAND_REQUEST_CAMERA){
                Logger.Log("requestCodeCamere COMMAND_REQUEST_CAMERA = 0", String.valueOf(requestCode));
                //onCaptureImageResult(data);
                grabImage(imageView);

//                {
//                    Uri imageUri = data.getData();
//                    try {
//                        Bitmap bitmap = getThumbnail(imageUri);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

        }else{
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }




    public void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(mImageUri, null);  //Return a ContentResolver instance for your application's package.
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;

        int inWidth = 0;
        int inHeight = 0;

        try
        {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;



            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //inJustDecodeBounds = true <-- will not load the bitmap into memory
            bmOptions.inJustDecodeBounds = true;


            WindowManager wm = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            Point size = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                display.getSize(size);
            }
            int width = size.x;
            int height = size.y;

            Logger.Log("width", String.valueOf(width));
            Logger.Log("height", String.valueOf(height));



            //bitmap = MediaStore.Images.Media.getBitmap(cr, mImageUri);
            bitmap = ImageFile.decodeSampledBitmapFromUri(cr, mImageUri,width, height);
            Logger.Log("bitmap", String.valueOf(bitmap));
            //image = ImageFile.ImageResize(bitmap, 640, ImageFile.getImageOrientation(mImageUri.getPath()));
            image = ImageFile.ImageResizeFixWidth(bitmap, width, ImageFile.getImageOrientation(mImageUri.getPath()));  //set path
            Logger.Log("mImageUri.getPath()",mImageUri.getPath());
            Logger.Log("image camere ", String.valueOf(image));
            //imageView.setImageBitmap(image);
            takenDate = new Date();
            finishWithResult(image);

            bitmap.recycle();
        }
        catch (OutOfMemoryError err){
            Toast.makeText(this, "Out of memory error : " + err.getCause(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Failed to load", err.getCause());
            err.printStackTrace();
            finish();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Logger.Log("Failed to load",e.toString());
            Log.d(TAG, "Failed to load", e);
            finish();
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);

        image = thumbnail;
        takenDate = new Date();
    }



    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 640;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);


        //image = ImageFile.ImageResize(bm, 640, ImageFile.getImageOrientation(selectedImagePath));
        image = ImageFile.ImageResizeFixWidth(bm, 640, ImageFile.getImageOrientation(selectedImagePath));
        imageView.setImageBitmap(image);

        takenDate = ImageFile.getImageTakenDate(selectedImagePath);


        if(checkPresentImage && (takenDate == null || !Converter.DateToString(takenDate, "dd-MM-yyyy").equals(Converter.DateToString(new Date(), "dd-MM-yyyy")))){
            //takenDate = new Date();
            //app.showAlert(CameraTakeActivity.this, getString(R.string.alert_incorrect_selected_image), 0, this);
        }else{
            if(takenDate == null){
                takenDate = new Date();
            }

            finishWithResult(image);
        }

    }


    private void requestCamera(){
        //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo;
        try
        {
            // place where to store camera taken picture
            photo = ImageFile.createTemporaryFile("picture", ".jpg");
            photo.delete();
        }
        catch(Exception e)
        {
            Log.v(TAG, "Can't create file to take picture!");
            Toast.makeText(this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG);
            return ;
        }
        mImageUri = Uri.fromFile(photo);

        Logger.Log("mImageUri", String.valueOf(mImageUri));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //start camera intent
        startActivityForResult(intent, COMMAND_REQUEST_CAMERA);
    }

    private void selectFile(){
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                COMMAND_SELECT_FILE);
    }



    private void finishWithResult(Bitmap bitmap)
    {
        // Resize image
        //Bitmap resized = ImageFile.ImageResize(bitmap, 600);

        if(renderText != null && !renderText.isEmpty()){
            //String strDate = Converter.DateToString(takenDate, "dd/MM/yyyy");
            bitmap = ImageFile.drawTextToBitmap(CameraTakeActivity.this, bitmap, renderText );

            Logger.Log("bitmap finishWithResult", String.valueOf(bitmap));
        }

        String imagePath = ImageFile.saveBitmapToPath(CameraTakeActivity.this, bitmap, getString(R.string.image_path), Converter.DateToString(takenDate, "yyyyMMddHHmmss"));
        Logger.Log("imagePath finishWithResult",imagePath);

        //Convert to byte array
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        //bundle.putByteArray("image", byteArray);
        bundle.putString("takenDate", Converter.DateToString(takenDate, "yyyy-MM-dd HH:mm:ss"));
        bundle.putInt(CameraTakeActivity.KEY_GROUP_CODE, GROUP_CODE);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    chooser(command);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // dialog
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

///**********************************


//
//    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException{
//        InputStream input = this.getContentResolver().openInputStream(uri);
//
//        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
//        onlyBoundsOptions.inJustDecodeBounds = true;
//        onlyBoundsOptions.inDither=true;//optional
//        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
//        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
//        input.close();
//        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
//            return null;
//
//        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
//
//        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;
//
//        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
//        bitmapOptions.inDither=true;//optional
//        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
//        input = this.getContentResolver().openInputStream(uri);
//        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
//        input.close();
//        return bitmap;
//    }
//
//    private static int getPowerOfTwoForSampleRatio(double ratio){
//        int k = Integer.highestOneBit((int)Math.floor(ratio));
//        if(k==0) return 1;
//        else return k;
//    }



}
