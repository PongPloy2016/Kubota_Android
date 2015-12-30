package th.co.siamkubota.kubota.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ServiceGenerator;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Image;
import io.swagger.client.model.LoginData;
import io.swagger.client.model.LoginResponse;
import io.swagger.client.model.Signature;
import io.swagger.client.model.Task;
import io.swagger.client.model.UploadResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.fragment.LoadingDialogFragment;
import th.co.siamkubota.kubota.sqlite.TaskDataSource;
import th.co.siamkubota.kubota.utils.function.Ui;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_TASK = "TASK";
    public static final String KEY_LOGIN_DATA = "LOGIN_DATA";

    private AppController app;
    private RelativeLayout rootLayout;
    private ImageView imageView;
    private TextView titleText;
    private TextView messageText;
    private Button okButton;
    private Task task;
    private LoginData loginData;

    private Call<UploadResponse> callUpload;
    private Call<io.swagger.client.model.Response> call;

    private LoadingDialogFragment alertLoading;
    private String shopName;

    private TaskDataSource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        imageView = (ImageView)  findViewById(R.id.imageView);
        titleText = (TextView)  findViewById(R.id.titleText);
        messageText = (TextView)  findViewById(R.id.messageText);
        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(this);

        //Ui.setupUI(ResultActivity.this, rootLayout);

        Bundle bundle = getIntent().getExtras();
        if(bundle.containsKey(ResultActivity.KEY_TASK)){
            task = bundle.getParcelable(ResultActivity.KEY_TASK);
        }


        if(bundle.containsKey(ResultActivity.KEY_LOGIN_DATA)){
            loginData = bundle.getParcelable(ResultActivity.KEY_LOGIN_DATA);
        }

        if(bundle.containsKey("shopName")){
            shopName = bundle.getString("shopName");
        }

        if(task != null){
            //alertLoading = new LoadingDialogFragment(); // loginData.getShopName()
            alertLoading = LoadingDialogFragment.newInstance(loginData.getShopName());
            alertLoading.setmListener(new LoadingDialogFragment.onActionListener() {
                @Override
                public void onFinishDialog() {
                    //finish();
                }
            });

            //alert.show(getSupportFragmentManager(), "loading");

            uploadImage();
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
        if(v == okButton){
            Intent intent = new Intent(ResultActivity.this, QuestionnairActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putParcelable(ResultActivity.KEY_LOGIN_DATA, loginData);
            //bundle.putString("shopName", shopName);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        }
    }

    private void uploadImage(){

        //spinner.setVisibility(View.VISIBLE);
        alertLoading.show(getSupportFragmentManager(), "loading");

        RequestBody image1body = null;
        RequestBody image2body = null;
        RequestBody image3body = null;
        RequestBody image4body = null;
        RequestBody signature1body = null;
        RequestBody signature2body = null;

        List<Image> images = task.getTaskImages();

        if( images.get(0).getImagePath() != null && !images.get(0).getImagePath().isEmpty() ){
            File image1 = new File(task.getTaskImages().get(0).getImagePath() );
            image1body = RequestBody.create(MediaType.parse("image/jpeg"), image1);
        }
        if( images.get(1).getImagePath() != null && !images.get(1).getImagePath().isEmpty() ){
            File image2 = new File(task.getTaskImages().get(1).getImagePath());
            image2body = RequestBody.create(MediaType.parse("image/jpeg"), image2);
        }
        if( images.get(2).getImagePath() != null && !images.get(2).getImagePath().isEmpty() ){
            File image3 = new File(task.getTaskImages().get(2).getImagePath());
            image3body = RequestBody.create(MediaType.parse("image/jpeg"), image3);
        }
        if( images.get(3).getImagePath() != null && !images.get(3).getImagePath().isEmpty() ){
            File image4 = new File(task.getTaskImages().get(3).getImagePath());
            image4body = RequestBody.create(MediaType.parse("image/jpeg"), image4);
        }

        //File file = new File(media.getPath());
       /* image1 = new File(task.getTaskImages().get(0).getImagePath() );
        RequestBody image1body = RequestBody.create(MediaType.parse("image/jpeg"), image1);
        image2 = new File(task.getTaskImages().get(1).getImagePath());
        RequestBody image2body = RequestBody.create(MediaType.parse("image/jpeg"), image2);
        image3 = new File(task.getTaskImages().get(2).getImagePath());
        RequestBody image3body = RequestBody.create(MediaType.parse("image/jpeg"), image3);
        image4 = new File(task.getTaskImages().get(3).getImagePath());
        RequestBody image4body = RequestBody.create(MediaType.parse("image/jpeg"), image4);
         signature1 = new File(task.getSignature().getCustomerSignatureImage().getImagePath());
        RequestBody signature1body = RequestBody.create(MediaType.parse("image/jpeg"), signature1);

        signature2 = new File(task.getSignature().getEngineerSignatureImage().getImagePath());
        RequestBody signature2body = RequestBody.create(MediaType.parse("image/jpeg"), signature2);
        */


        Signature signature = task.getSignature();

        if( signature.getCustomerSignatureImage().getImagePath() != null &&  !signature.getCustomerSignatureImage().getImagePath().isEmpty() ){
            File signature1 = new File(task.getSignature().getCustomerSignatureImage().getImagePath());
            signature1body = RequestBody.create(MediaType.parse("image/jpeg"), signature1);
        }

        if( signature.getEngineerSignatureImage().getImagePath() != null &&  !signature.getEngineerSignatureImage().getImagePath().isEmpty() ){
            File signature2 = new File(task.getSignature().getEngineerSignatureImage().getImagePath());
            signature2body = RequestBody.create(MediaType.parse("image/jpeg"), signature2);
        }




        DefaultApi service = ServiceGenerator.createService(DefaultApi.class);

        callUpload = service.uploadMultiple(image1body, image2body, image3body, image4body, signature1body, signature2body);
        callUpload.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Response<UploadResponse> response, Retrofit retrofit) {
                // Get result Repo from response.body()

                UploadResponse uploadData = response.body();

                if(uploadData != null && uploadData.getResult().equals("success")){

                    List<Image> images = task.getTaskImages();
                    if(uploadData.getParameter().getImage1() != null){
                        images.get(0).setImage(uploadData.getParameter().getImage1());
                    }

                    if(uploadData.getParameter().getImage2() != null){
                        images.get(1).setImage(uploadData.getParameter().getImage2());
                    }

                    if(uploadData.getParameter().getImage3() != null){
                        images.get(2).setImage(uploadData.getParameter().getImage3());
                    }

                    if(uploadData.getParameter().getImage4() != null){
                        images.get(3).setImage(uploadData.getParameter().getImage4());
                    }


                    Signature signature = task.getSignature();

                    if(uploadData.getParameter().getSignature1() != null){
                        signature.setCustomerSignature(uploadData.getParameter().getSignature1());
                    }

                    if(uploadData.getParameter().getSignature2() != null){
                        signature.setEngineerSignature(uploadData.getParameter().getSignature2());
                    }


                 /*   images.get(0).setImage(uploadData.getParameter().getImage1());
                    images.get(1).setImage(uploadData.getParameter().getImage2());
                    images.get(2).setImage(uploadData.getParameter().getImage3());
                    images.get(3).setImage(uploadData.getParameter().getImage4());
                    signature.setCustomerSignature(uploadData.getParameter().getSignature1());
                    signature.setEngineerSignature(uploadData.getParameter().getSignature2());
                    */


                    images.get(0).setImagePath(null);
                    images.get(1).setImagePath(null);
                    images.get(2).setImagePath(null);
                    images.get(3).setImagePath(null);
                    signature.setCustomerSignatureImage(null);
                    signature.setEngineerSignatureImage(null);


                    List<Task> taskList = new ArrayList<Task>();
                    taskList.add(task);

                    Gson gson = new Gson();

                    String json = gson.toJson(taskList);

                    submitTask(taskList);
                    //saveTask(taskList.get(0));

/*                    Intent intent = new Intent(ResultActivity.this, QuestionnairActivity.class);
                    //intent.putExtra(LoginActivity.KEY_ARGS, bundle);
                    // Create Bundle & Add user
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(LoginActivity.KEY_LOGIN_DATA,loginData.getParameter());

                    //intent.putExtras(bundle);
                    startActivity(intent);
                    finish();*/


                }else{

                    String message;
                    if(uploadData != null){
                        message = uploadData.getMessage();
                    }else{
                        message = response.raw().message();
                    }

                    //Ui.showAlertError(ResultActivity.this, message);
                    showResultFail();
                    alertLoading.dismiss();
                }
                //spinner.setVisibility(View.GONE);

            }


            @Override
            public void onFailure(Throwable t) {
                //spinner.setVisibility(View.GONE);
                alertLoading.dismiss();
                //Ui.showAlertError(ResultActivity.this, t.getMessage());
                showResultFail();
            }
        });
    }

    private void submitTask(final List<Task> taskList){

        //spinner.setVisibility(View.VISIBLE);
        //alertLoading.show(getSupportFragmentManager(), "loading");

        DefaultApi service = ServiceGenerator.createService(DefaultApi.class);

        call = service.submitTask(taskList);
        call.enqueue(new Callback<io.swagger.client.model.Response>() {
            @Override
            public void onResponse(Response<io.swagger.client.model.Response> response, Retrofit retrofit) {
                // Get result Repo from response.body()

                io.swagger.client.model.Response submitData = response.body();

                if(submitData != null && submitData.getResult().equals("success")){

                    //deleteTask(task.getTaskInfo().getTaskCode());

                    saveTask(taskList.get(0));


                }else{

                    String message;
                    if(submitData != null){
                        message = submitData.getMessage();
                    }else{
                        message = response.raw().message();
                    }

                    showResultFail();
                    saveTask(taskList.get(0));

                }
                //spinner.setVisibility(View.GONE);
                alertLoading.dismiss();
            }


            @Override
            public void onFailure(Throwable t) {
                //spinner.setVisibility(View.GONE);
                alertLoading.dismiss();
                //Ui.showAlertError(ResultActivity.this, t.getMessage());
                showResultFail();
            }
        });
    }

    private void showResultFail(){
        imageView.setImageResource(R.drawable.failed_icon);
        titleText.setText(getString(R.string.service_popup_fail_title));
        messageText.setText(getString(R.string.service_popup_fail_message));
    }

    private void saveTask(Task task){

        dataSource = new TaskDataSource(ResultActivity.this);
        dataSource.open();
        dataSource.addTask(task);

    }

    private void deleteTask(String taskCode){

        dataSource = new TaskDataSource(ResultActivity.this);
        dataSource.open();

        dataSource.deleteTask(taskCode);

    }
}
