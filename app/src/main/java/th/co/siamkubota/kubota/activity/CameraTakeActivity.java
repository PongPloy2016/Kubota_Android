package th.co.siamkubota.kubota.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_confirm);

        app = (AppController)getApplication();

        imageView = (ImageView) findViewById(R.id.imageView);

        Bundle bundle = getIntent().getExtras();
        command  = bundle.getInt(CameraTakeActivity.KEY_COMMAND, 0);
        GROUP_CODE = bundle.getInt(CameraTakeActivity.KEY_GROUP_CODE, -1);
        checkPresentImage = bundle.getBoolean(CameraTakeActivity.KEY_CHECK_PRESENT, false);
        //renderDate = bundle.getBoolean(CameraTakeActivity.KEY_RENDER_DATE,false);
        renderText = bundle.getString(CameraTakeActivity.KEY_RENDER_TEXT, null);


        switch (command){
            case 0:
                requestCamera();
                break;
            case 1:
                selectFile();
                break;
            default:
                break;
        }

    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CameraTakeActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, COMMAND_REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            COMMAND_SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


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

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == COMMAND_SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == COMMAND_REQUEST_CAMERA){
                //onCaptureImageResult(data);
                grabImage(imageView);
            }

        }else{
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    public void grabImage(ImageView imageView)
    {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(cr, mImageUri);
            //image = ImageFile.ImageResize(bitmap, 640, ImageFile.getImageOrientation(mImageUri.getPath()));
            image = ImageFile.ImageResizeFixWidth(bitmap, 640, ImageFile.getImageOrientation(mImageUri.getPath()));
            //imageView.setImageBitmap(image);
            takenDate = new Date();
            finishWithResult(image);
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Failed to load", e);
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
        }

        String imagePath = ImageFile.saveBitmapToPath(CameraTakeActivity.this, bitmap, Converter.DateToString(takenDate, "yyyyMMddHHmmss"));

        //Convert to byte array
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        //bundle.putByteArray("image", byteArray);
        bundle.putString("takenDate", Converter.DateToString(takenDate, "yyyy-MM-dd hh:mm:ss"));
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


}
