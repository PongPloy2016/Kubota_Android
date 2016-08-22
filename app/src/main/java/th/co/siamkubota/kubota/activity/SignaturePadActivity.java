package th.co.siamkubota.kubota.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.Date;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.utils.function.Converter;
import th.co.siamkubota.kubota.utils.function.ImageFile;

public class SignaturePadActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_TITLE = "TITLE";
    public static final String KEY_SIGNATURE_IMAGE_PATH = "SIGNATURE_IMAGE_PATH";

    private AppController app;

    private SignaturePad mSignaturePad;
    private Button clearButton;
    private Button saveButton;
    private TextView titleTextView;
    private ProgressBar progress;

    private String title;
    private String signatureImagePath;
    private Bitmap signatureBitmap;
    private boolean signed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_pad);

        if(savedInstanceState == null){

            Bundle bundle = getIntent().getExtras();
            title = bundle.getString(KEY_TITLE, getString(R.string.service_hint_signature));
           /* signatureImagePath = bundle.getString(KEY_SIGNATURE_IMAGE_PATH, null);

            if(signatureImagePath != null){
                signatureBitmap = ImageFile.bitmapFromFilePath(signatureImagePath);
            }*/


            clearButton = (Button) findViewById(R.id.clearButton);
            saveButton = (Button) findViewById(R.id.saveButton);
            mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
            titleTextView = (TextView) findViewById(R.id.titleTextView);
            progress = (ProgressBar) findViewById(R.id.progress);

            titleTextView.setText(title);

           /* if(signatureBitmap != null){
                mSignaturePad.setSignatureBitmap(signatureBitmap);
            }*/

            mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onSigned() {
                    //Event triggered when the pad is signed
                    signed = true;
                }

                @Override
                public void onClear() {
                    //Event triggered when the pad is cleared
                    signed = false;
                }
            });



            clearButton.setOnClickListener(this);
            saveButton.setOnClickListener(this);

        }else{

            if(savedInstanceState != null && savedInstanceState.containsKey("KEY_BITMAP")){
                Bitmap bitmap = (Bitmap) savedInstanceState.getParcelable("KEY_BITMAP");
                if(bitmap != null){
                    //mSignaturePad.setSignatureBitmap(bitmap);
                    signatureBitmap = bitmap;
                }
            }


            if(signatureBitmap != null && mSignaturePad != null){
                mSignaturePad.setSignatureBitmap(signatureBitmap);
            }
        }

    }


    @Override
    public void onClick(View v) {
        if(v == clearButton){

            mSignaturePad.clear();

        }else if(v == saveButton){

            if(signed){

                progress.setVisibility(View.VISIBLE);
                //getActivity().getSupportFragmentManager().popBackStack();
                signatureBitmap = mSignaturePad.getTransparentSignatureBitmap(true);
                //signatureBitmap = mSignaturePad.getSignatureBitmap();

                // signatureBitmap =  ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                replaceColor(signatureBitmap);


                finishWithResult(signatureBitmap);
            }else{

                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_custom_layout,
                        (ViewGroup) findViewById(R.id.toast_layout_root));
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 300);
                toast.setDuration(Toast.LENGTH_LONG);

                TextView  textView = (TextView) layout.findViewById(R.id.textView1);
                textView.setText(getString(R.string.sign_pad_not_complete));

                toast.setView(layout);
                toast.show();
            }

        }
    }


    private void finishWithResult(Bitmap bitmap)
    {
        String dateString = Converter.DateToString(new Date(), "yyyyMMddHHmmss");
        if(dateString == null || dateString.isEmpty()){
            return;
        }

        String imagePath = ImageFile.saveBitmapToPath(SignaturePadActivity.this, bitmap, getString(R.string.image_path), "signature_" + dateString);

        if(imagePath != null && !imagePath.isEmpty()){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("imagePath", imagePath);
            bundle.putString("takenDate", Converter.DateToString(new Date(), "dd/MM/yyyy"));

            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public void replaceColor(Bitmap myBitmap){


    int [] allpixels = new int [myBitmap.getHeight()*myBitmap.getWidth()];

    myBitmap.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());

    for(int i = 0; i < allpixels.length; i++)
    {
        if(allpixels[i] == Color.TRANSPARENT)
        {
            allpixels[i] = Color.WHITE;
        }
    }

    myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
    }

/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mSignaturePad != null && mSignaturePad.getSignatureBitmap() != null){
            outState.putParcelable("KEY_BITMAP", mSignaturePad.getSignatureBitmap());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.containsKey("KEY_BITMAP")){
            Bitmap bitmap = (Bitmap) savedInstanceState.getParcelable("KEY_BITMAP");
            if(bitmap != null){
                //mSignaturePad.setSignatureBitmap(bitmap);
                signatureBitmap = bitmap;
            }
        }

        //mSignaturePad.setSignatureBitmap((Bitmap) savedInstanceState.getParcelable("KEY_BITMAP"));

    }*/

}
