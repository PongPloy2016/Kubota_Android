package th.co.siamkubota.kubota.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


import java.io.File;
import java.util.ArrayList;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.ImageViewPagerAdapter;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.model.Photo;
import th.co.siamkubota.kubota.utils.function.ImageFile;


public class ImageViewActivity extends BaseActivity {

    public static final String KEY_IMAGE_LIST = "imageList";
    public static final String KEY_SELECT_ITEM = "selectItem";
    public static final String KEY_IMAGE_TITLE = "imageTitle";
    public static final String KEY_IMAGE_PATH = "imagePath";
    public static final String KEY_IMAGE_ID = "imageId";

    private ViewPager pager;
    private TextView textTitle;
    private ImageView imageView;
    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = new Matrix();

    private ArrayList<Photo> images;
    private ImageViewPagerAdapter adapter;
    private int item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        pager = (ViewPager) findViewById(R.id.pager);


        Bundle bundle = getIntent().getExtras();

        if(bundle.containsKey(KEY_IMAGE_LIST)){
            images = bundle.getParcelableArrayList(KEY_IMAGE_LIST);
        }

        if(bundle.containsKey(KEY_SELECT_ITEM)){
            item = bundle.getInt(KEY_SELECT_ITEM, 0);
        }

        if(images != null){
            adapter = new ImageViewPagerAdapter(this, getSupportFragmentManager(), images, null);
            pager.setAdapter(adapter);
            pager.setCurrentItem(item);
        }


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

   /* *//**
     * Get the current view position from the ViewPager by
     * extending SimpleOnPageChangeListener class and adding your method
     *//*
    public class CustomOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        private int currentPage;

        @Override
        public void onPageSelected(int position) {

            currentPage = position;

            switch (position){
                case 0 :
                    //button1.requestFocus();
                    button1.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    SpannableString content = new SpannableString(button1.getText().toString());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    button1.setText(content);
                    break;
                case 1 :
                    //button2.requestFocus();
                    button2.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    break;
                case 2 :
                    //button3.requestFocus();
                    button3.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    break;
                case 3 :
                    //button4.requestFocus();
                    button4.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_gray_stage));
                    break;
            }


        }

        public final int getCurrentPage() {
            return currentPage;
        }
    }*/
}
