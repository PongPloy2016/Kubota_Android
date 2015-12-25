package th.co.siamkubota.kubota.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import th.co.siamkubota.kubota.R;


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
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (getArguments() != null) {
            title = getArguments().getString("shopName");
        }

        dialog.setContentView(R.layout.alert_dialog_loading);

        mTitleView = (TextView) dialog.findViewById(R.id.titleText);
        mTitleView.setText(title);


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
}
