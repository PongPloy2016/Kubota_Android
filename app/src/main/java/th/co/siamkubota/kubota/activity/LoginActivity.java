package th.co.siamkubota.kubota.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import io.swagger.client.ServiceGenerator;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.LoginResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.utils.function.Ui;
import th.co.siamkubota.kubota.utils.function.Validate;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private AppController app;
    private RelativeLayout rootLayout;
    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar spinner;

    private  AlertDialog alert;
    private String username;

    private Call<LoginResponse> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        spinner=(ProgressBar)findViewById(R.id.spinner);

        spinner.setVisibility(View.GONE);
        loginButton.setOnClickListener(this);

        Ui.setupUI(LoginActivity.this, rootLayout);


        // Get token of creator
        SharedPreferences appPref =
                PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext()
                );
        username = appPref.getString("username", null);

        if(username != null){
            usernameEditText.setText(username);
            passwordEditText.requestFocus();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
           /* Intent intent = new Intent(LoginActivity.this, ServiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();*/

            View view = Validate.inputValidate(rootLayout, "required");
            if(view == null){
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginRequest(username, password);

            }else{
                buildAlertInvalidInput(view);
            }

        }
    }

    private  void buildAlertInvalidInput(final View view)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(getString(R.string.login_invalid_username_password))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.service_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        view.requestFocus();

                    }
                });
                //.setNegativeButton(getString(R.string.main_button_no), null);

        alert = builder.create();
        alert.show();
    }

    private void loginRequest(final String username, String password)
    {

        spinner.setVisibility(View.VISIBLE);

        DefaultApi service = ServiceGenerator.createService(DefaultApi.class);

        call = service.login(username, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                // Get result Repo from response.body()

                LoginResponse loginData = response.body();

                if(loginData != null && loginData.getResult().equals("success")){


                    SharedPreferences appPref = PreferenceManager
                            .getDefaultSharedPreferences(
                                    getApplicationContext());
                    SharedPreferences.Editor appPrefEditor = appPref.edit();
                    //appPrefEditor.putString("token", loginData.getUser().getToken());
                    appPrefEditor.putString("username", username);
                    appPrefEditor.commit();




                    Intent intent = new Intent(LoginActivity.this, ServiceActivity.class);
                    //intent.putExtra(LoginActivity.KEY_ARGS, bundle);
                    // Create Bundle & Add user
                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(LoginActivity.KEY_USER,loginData.getUser());
//                    bundle.putInt(LoginActivity.KEY_UNREAD,loginData.getUnread());

                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();


                }else{

                    String message;
                    if(loginData != null){
                        message = loginData.getMessage();
                    }else{
                        message = response.raw().message();
                    }

                    passwordEditText.setText("");

                    Ui.showAlertError(LoginActivity.this, message);
                }
                spinner.setVisibility(View.GONE);
            }


            @Override
            public void onFailure(Throwable t) {
                spinner.setVisibility(View.GONE);
                Ui.showAlertError(LoginActivity.this, t.getMessage());
            }
        });

    }
}
