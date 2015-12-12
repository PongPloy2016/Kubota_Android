package th.co.siamkubota.kubota.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.List;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.fragment.ServiceFragment;
import th.co.siamkubota.kubota.fragment.SignaturePadFragment;
import th.co.siamkubota.kubota.fragment.UnfinishTaskFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServiceActivity extends BaseActivity implements ServiceFragment.OnFragmentInteractionListener{

    private AppController app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ServiceFragment newFragment = ServiceFragment.newInstance();
        newFragment.setmListener(this);
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


    }

    @Override
    public void onRelayInvokeSignPad() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        SignaturePadFragment fragment = SignaturePadFragment.newInstance();
        ft.replace(R.id.content, fragment, "signaturePadFragment");
        ft.addToBackStack(null);
        // Start the animated transition.
        ft.commit();
    }

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
}
