package th.co.siamkubota.kubota.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.swagger.client.model.LoginData;
import io.swagger.client.model.Task;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.fragment.ServiceFragment;
import th.co.siamkubota.kubota.fragment.SignaturePadFragment;
import th.co.siamkubota.kubota.fragment.UnfinishTaskFragment;
import th.co.siamkubota.kubota.interfaces.OnHomePressedListener;
import th.co.siamkubota.kubota.service.Constants;
import th.co.siamkubota.kubota.service.GeocodeAddressIntentService;
import th.co.siamkubota.kubota.utils.ui.HomeWatcher;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServiceActivity extends BaseActivity {

    private AppController app;

    AtomicReference<TextView> mTitle;

    private  AlertDialog alert;
    private boolean leave = false;

    private LoginData loginData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = new AtomicReference<>((TextView) toolbar.findViewById(R.id.toolbar_title));


        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(LoginActivity.KEY_LOGIN_DATA)){
            loginData = bundle.getParcelable(LoginActivity.KEY_LOGIN_DATA);
        }


        if(loginData != null){
            mTitle.get().setText(loginData.getShopName());
        }




        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ServiceFragment newFragment = ServiceFragment.newInstance(loginData);
        //newFragment.setmListener(this);
        ft.replace(R.id.content, newFragment, "serviceFragment");
        ft.commit();


        ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        UnfinishTaskFragment unfinishTaskFragment = UnfinishTaskFragment.newInstance();
        ft.replace(R.id.content, unfinishTaskFragment, "unfinishTaskFragment");
        ft.addToBackStack(null);
        // Start the animated transition.
        ft.commit();



/*
        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                // do something here...

            }

            @Override
            public void onHomeLongPressed() {
                Toast.makeText(ServiceActivity.this, "Long press", Toast.LENGTH_SHORT).show();
            }
        });
        mHomeWatcher.startWatch();
        */


    }

    @Override
    protected void onResume() {
        super.onResume();

        leave = false;
    }

    /* @Override
    public void onRelayInvokeSignPad() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        SignaturePadFragment fragment = SignaturePadFragment.newInstance();
        ft.replace(R.id.content, fragment, "signaturePadFragment");
        ft.addToBackStack(null);
        // Start the animated transition.
        ft.commit();
    }*/

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if(fragment != null){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    private  void buildAlertConfirmLeave(final int keyCode)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ServiceActivity.this);
        builder.setMessage(getString(R.string.service_leave_confirm_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.main_button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leave = true;
                        /*if (keyCode == KeyEvent.KEYCODE_HOME) {
                            onUserLeaveHint();
                        } else {
                            onBackPressed();
                        }*/

                        onBackPressed();

                    }
                })
                .setNegativeButton(getString(R.string.main_button_no), null);

        alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {


        if(!leave){
            buildAlertConfirmLeave(KeyEvent.KEYCODE_BACK);
            return;
        }

        super.onBackPressed();
    }

/*
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK)
        {
            //The Code Want to Perform.
            if(!leave){
                buildAlertConfirmLeave();
                return false;
            }

        }

        return true;
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK)
        {
            //The Code Want to Perform.
            if(!leave){
                buildAlertConfirmLeave(keyCode);
                return false;
            }

        }

        return super.onKeyDown(keyCode, event);

    }

    /*
    @Override
    protected void onUserLeaveHint() {

        if(!leave){
            buildAlertConfirmLeave(KeyEvent.KEYCODE_HOME);
            return ;
        }
        super.onUserLeaveHint();
    }*/
}
