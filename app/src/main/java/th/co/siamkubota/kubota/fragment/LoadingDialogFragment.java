package th.co.siamkubota.kubota.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.logger.Logger;


public class LoadingDialogFragment extends DialogFragment implements View.OnClickListener{
    Button mButton;

    //public onSubmitListener mListener;
    private onActionListener mListener;
    public int icon;
    public String title = "";
    public String text = "";
    public String actions[];
    public boolean cancelable = false;

    public View focusView;


    ImageView mIcon;
    TextView mTitleView;
    TextView mTextView;
    LinearLayout mActionLayout;
    Button okButton;
    ProgressBar progressBar;
    int progress;

    private Handler handler;
    private Runnable runnable;
    private final long limitTime = 2000L;
    private long delay_time;
    private long time = limitTime;
    //private long time = 2000L;


    public static LoadingDialogFragment newInstance(String shopName) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        args.putString("shopName", shopName);
        fragment.setArguments(args);
        return fragment;
    }


    public interface onActionListener {
        void onFinishDialog();
    }

    public void setmListener(onActionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity(),R.style.CustomDialogTheme);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        if (getArguments() != null) {
            title = getArguments().getString("shopName");
        }

        dialog.setContentView(R.layout.alert_dialog_loading);

        mTitleView = (TextView) dialog.findViewById(R.id.titleText);
        progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);

        mTitleView.setText(title);
        progressBar.setProgress(this.progress);


//        okButton = (Button) dialog.findViewById(R.id.okButton);
//        okButton.setOnClickListener(this);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        setCancelable(cancelable);
        dialog.show();

        //mButton = (Button) dialog.findViewById(R.id.buttonOk);


        return dialog;

        // return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        mListener.onFinishDialog();
    }

    public void updateProgress(int progress){
        this.progress = progress;
        if(progressBar != null){
            progressBar.setProgress(this.progress);
        }
    }

    public void increaseProgress(int increment){
        this.progress += increment;
        if(progressBar != null){
            progressBar.setProgress(this.progress);
        }
    }

    public int getProgress(){
        if(progressBar != null){
            this.progress = progressBar.getProgress();
        }

        return this.progress;
    }

    @Override
    public void dismiss() {

        time = limitTime;

        if(handler == null){
            handler = new Handler();
        }

        runnable = new Runnable() {
            public void run() {
                delayDismiss();

                Logger.Log("delayDismiss","delayDismiss");
            }
        };

        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    private void delayDismiss(){

        super.dismiss();


    }
}
