package th.co.siamkubota.kubota.utils.function;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by atthapok on 03/10/2558.
 */
public class ImageFile {

    public static Date getImageTakenDate(String path){

       /* String pathToFile = "Your file path";
        File file = new File(path);

        if(file.exists()) {
            long date = file.lastModified();
        }*/

        ExifInterface intf = null;
        try {
            intf = new ExifInterface(path);
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(intf == null) {
            /* File doesn't exist or isn't an image */
            return null;
        }

        String dateString = intf.getAttribute(ExifInterface.TAG_DATETIME);
        /* Do your date/time stuff here */
        Log.d("Dated : ", dateString); //Dispaly dateString. You can do/use it your own way
//2015:09:21 16:58:04
        Date date = Converter.StringToDate(dateString, "yyyy:MM:dd HH:mm:ss");

        return date;
    }

    public static void CopyExifData(String oldImagePath, String imagePath){

        ExifInterface oldExif = null;
        try {
            oldExif = new ExifInterface(oldImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String exifOrientation = oldExif.getAttribute(ExifInterface.TAG_ORIENTATION);

        if (exifOrientation != null) {
            ExifInterface newExif = null;
            try {
                newExif = new ExifInterface(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newExif.setAttribute(ExifInterface.TAG_ORIENTATION, exifOrientation);
            try {
                newExif.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap ImageResize(Bitmap bitmap, int maxWidth, int orientation){

        Point imgSize = new Point(bitmap.getWidth(), bitmap.getHeight());
        Point boundary = new Point(maxWidth, maxWidth);

        Point newSize = getScaledDimension(imgSize, boundary);

        Bitmap resized = Bitmap.createScaledBitmap(bitmap, newSize.x, newSize.y, true);

        //resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);

        Bitmap rotated = rotateBitmap(resized, orientation);

        return rotated;
    }

    public static Bitmap ImageResizeFixWidth(Bitmap bitmap, int maxWidth, int orientation){

        // original measurements
        int origWidth = bitmap.getWidth();
        int origHeight = bitmap.getHeight();

        final int destWidth = maxWidth;//or the width you need

        if(origWidth > destWidth){
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight/( origWidth / destWidth ) ;
            // we create an scaled bitmap so it reduces the image, not just trim it
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            // compress to the format you want, JPEG, PNG...
            // 70 is the 0-100 quality percentage
            resized.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            // we save the file, at least until we have made use of it
            Bitmap rotated = rotateBitmap(resized, orientation);

            return rotated;
        }


        return bitmap;
    }

    public static Point getScaledDimension(Point imgSize, Point boundary) {

        int original_width = imgSize.x;
        int original_height = imgSize.y;
        int bound_width = boundary.x;
        int bound_height = boundary.y;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Point(new_width, new_height);
    }

    /** Create a file Uri for saving an image or video */
    public static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }

    public static File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdir();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    public static int getImageOrientation(String imagePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        return orientation;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void scaleImage(ImageView view, int boundBoxInDp)
    {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        //view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private int dpToPx(Context context, int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static String saveBitmapToPath(Context mContext,Bitmap bm, String name){

        try{
            String tempFilePath = Environment.getExternalStorageDirectory() + "/"
                    + mContext.getPackageName() + "/ImageTempPerDay/" + name + ".jpg";
            File tempFile = new File(tempFilePath);
            if (!tempFile.exists()) {
                if (!tempFile.getParentFile().exists()) {
                    tempFile.getParentFile().mkdirs();
                }
            }
            tempFile.delete();
            tempFile.createNewFile();

            int quality = 100;
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);

            bos.flush();
            bos.close();

            //Log.d("IMAGE ### ", bm.getByteCount()+" ####");

            bm.recycle();
            Log.d("TAG SAVE IMAGE", tempFilePath);

            return tempFilePath;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }
    public static void deleteFilesFromStorage(final Context mContext){

        new Thread(new Runnable() {
            @Override
            public void run() {

                String filePath = Environment.getExternalStorageDirectory() + "/"
                        + mContext.getPackageName() + "/ImageTempPerDay";

                File dir = new File(filePath);
                if (dir.isDirectory())
                {
                    String[] child = dir.list();
                    for (int i = 0; i < child.length; i++)
                    {
                        new File(dir, child[i]).delete();

                        Log.d("TAG DELETE", child[i]);
                    }
                }
            }
        }).start();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
