package th.co.siamkubota.kubota.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


import java.io.File;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.utils.function.ImageFile;


public class ImageViewActivity extends BaseActivity {

    public static final String KEY_IMAGE_TITLE = "imageTitle";
    public static final String KEY_IMAGE_PATH = "imagePath";
    public static final String KEY_IMAGE_ID = "imageId";

    private TextView textTitle;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        textTitle = (TextView) findViewById(R.id.textTitle);
        imageView = (ImageView) findViewById(R.id.imageView);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        Bundle bundle = getIntent().getExtras();

        String title = null;
        String imagePath = null ;
        String imageId = null;

        if(bundle.containsKey(KEY_IMAGE_TITLE)){
            title = bundle.getString(KEY_IMAGE_TITLE);
        }

        if(bundle.containsKey(KEY_IMAGE_PATH)){
            imagePath = bundle.getString(KEY_IMAGE_PATH);
        }else if(bundle.containsKey(KEY_IMAGE_ID)){
            imageId = bundle.getString(KEY_IMAGE_ID);
        }


        if(title != null && !title.isEmpty()){
            textTitle.setText(title);
        }else{
            textTitle.setVisibility(View.GONE);
        }

        if(imagePath != null && !imagePath.isEmpty()){
            imageView.setImageURI(Uri.fromFile(new File(imagePath)));
        }

      /*  Bitmap bitmap  = ImageFile.bitmapFromFilePath(imagePath);

        if(bitmap != null){
            imageView.setMaxWidth(bitmap.getWidth());
            imageView.setMaxHeight(bitmap.getHeight());
        }*/


    }

    public boolean onTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            matrix.setScale(scaleFactor, scaleFactor);
            imageView.setImageMatrix(matrix);
            return true;
        }
    }
}
