package th.co.siamkubota.kubota.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicReference;

import io.swagger.client.model.LoginData;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.fragment.ContinueQuestionFragment;
import th.co.siamkubota.kubota.fragment.FinishDialogFragment;
import th.co.siamkubota.kubota.fragment.QuestionnairFragment;
import th.co.siamkubota.kubota.fragment.ServiceFragment;
import th.co.siamkubota.kubota.fragment.UnfinishTaskFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class QuestionnairActivity extends BaseActivity implements
ContinueQuestionFragment.OnFragmentInteractionListener{

    private AppController app;
    private LoginData loginData;
    AtomicReference<TextView> mTitle;
    private String shopName;

    private  AlertDialog alert;
    private boolean leave = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = new AtomicReference<>((TextView) toolbar.findViewById(R.id.toolbar_title));

        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(ResultActivity.KEY_LOGIN_DATA)){
            loginData = bundle.getParcelable(ResultActivity.KEY_LOGIN_DATA);
        }

        if(loginData != null){
            mTitle.get().setText(loginData.getShopName());
        }

      /*  if(bundle.containsKey("shopName")){
            shopName = bundle.getString("shopName");
            mTitle.get().setText(shopName);
        }
*/

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ContinueQuestionFragment newFragment = ContinueQuestionFragment.newInstance();
        newFragment.setmListener(this);
        ft.replace(R.id.content, newFragment, "continueQuestionFragment");
        ft.commit();

    }

    ///////////////////////////////////////////////////////////////////// implement method


    @Override
    public void onConfirmContinue(boolean result) {

        if(result){

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft = getSupportFragmentManager().beginTransaction();
            //ft.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            QuestionnairFragment fragment = QuestionnairFragment.newInstance();
            ft.replace(R.id.content, fragment, "questionnairFragment");
            //ft.addToBackStack(null);
            // Start the animated transition.
            ft.commit();

        }else{
            // alert finish dialog

            FinishDialogFragment alert = new FinishDialogFragment();
            alert.setmListener(new FinishDialogFragment.onActionListener() {
                @Override
                public void onFinishDialog() {
                    finish();
                }
            });

            alert.show(getSupportFragmentManager(), "finish");
        }
    }

    private  void buildAlertConfirmLeave(final int keyCode)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(QuestionnairActivity.this);
        builder.setMessage(getString(R.string.service_leave_confirm_message))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.main_button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leave = true;

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
}
