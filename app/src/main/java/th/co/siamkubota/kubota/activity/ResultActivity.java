package th.co.siamkubota.kubota.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.swagger.client.ServiceGenerator;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Image;
import io.swagger.client.model.LoginData;
import io.swagger.client.model.LoginResponse;
import io.swagger.client.model.Signature;
import io.swagger.client.model.Task;
import io.swagger.client.model.TaskInfo;
import io.swagger.client.model.UploadResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.app.Config;
import th.co.siamkubota.kubota.fragment.FinishDialogFragment;
import th.co.siamkubota.kubota.fragment.LoadingDialogFragment;
import th.co.siamkubota.kubota.logger.Logger;
import th.co.siamkubota.kubota.model.OfflineTask;
import th.co.siamkubota.kubota.sqlite.TaskDataSource;
import th.co.siamkubota.kubota.utils.function.Copier;
import th.co.siamkubota.kubota.utils.function.ImageFile;
import th.co.siamkubota.kubota.utils.function.Network;
import th.co.siamkubota.kubota.utils.function.Ui;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ResultActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_TASK = "TASK";
    public static final String KEY_LOGIN_DATA = "LOGIN_DATA";
    public static final String KEY_FROM = "FROM";

    public static final int REQUEST_CODE_LOGIN = 118;

    private AppController app;
    private RelativeLayout rootLayout;
    private ImageView imageView;
    private TextView titleText;
    private TextView messageText;
    private Button okButton;
    private Task task;
    private Task taskSave;
    private LoginData loginData;

    private Call<UploadResponse> callUpload;
    private Call<io.swagger.client.model.Response> call;

    private  AlertDialog alert;
    private boolean leave = false;
    private boolean success = true;

    private LoadingDialogFragment alertLoading;
    private String shopName;

    private TaskDataSource dataSource;


    private Handler handler;
    private Runnable runnable;
    boolean mStopHandler = false;
    private final long limitTime = 500L;
    private long delay_time;
    private long time = limitTime;

    private String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Logger.Log("ResultActivity","ResultActivity");

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
            taskSave = new Task(task);
        }


        if(bundle.containsKey(ResultActivity.KEY_LOGIN_DATA)){
            loginData = bundle.getParcelable(ResultActivity.KEY_LOGIN_DATA);

            if(loginData != null){
                task.getTaskInfo().setshopID(loginData.getUserId());
                taskSave.getTaskInfo().setshopID(loginData.getUserId());
            }
        }

        if(bundle.containsKey("shopName")){
            shopName = bundle.getString("shopName");
        }

        if(bundle.containsKey(KEY_FROM)){
            from = bundle.getString(KEY_FROM);

            Logger.Log("from UnfinishTaskFragment ",from);
        }

        submitData();

    }


    private  void buildAlertConfirmLeave(final int keyCode)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
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

    private  void buildAlertNoInternet()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        builder.setMessage(getString(R.string.result_no_internet))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.service_button_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveTask(taskSave, null);
                        navigateToUnfinishTaskList();
                    }
                });

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



    @Override
    public void onClick(View v) {
        if(v == okButton){

            navigateToUnfinishTaskList();

        }
    }

    private void uploadImage(){

        alertLoading.show(getSupportFragmentManager(), "loading");

        runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if(alertLoading.getProgress() < 50){
                    alertLoading.increaseProgress(2);
                    handler.postDelayed(this, time);
                }
            }
        };
        handler.post(runnable);


        RequestBody image1body = null;
        RequestBody image2body = null;
        RequestBody image3body = null;
        RequestBody image4body = null;
        RequestBody signature1body = null;
        RequestBody signature2body = null;

        List<Image> images = task.getTaskImages();

        try{
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
        }catch (IndexOutOfBoundsException e){
            if(Config.showDefault){
                File image1 = new File(task.getTaskImages().get(0).getImagePath() );
                image1body = RequestBody.create(MediaType.parse("image/jpeg"), image1);
            }
        }


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

                handler.removeCallbacks(runnable);

                UploadResponse uploadData = response.body();

                if(uploadData != null && uploadData.getResult().equals("success")){

                    alertLoading.updateProgress(50);

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


                    images.get(0).setImagePath(null);
                    images.get(1).setImagePath(null);
                    images.get(2).setImagePath(null);
                    images.get(3).setImagePath(null);
                    signature.setCustomerSignatureImage(null);
                    signature.setEngineerSignatureImage(null);

                    submitTask(task);

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
                    saveTask(taskSave, null);
                    //showToastError(message);
                }

            }


            @Override
            public void onFailure(Throwable t) {
                handler.removeCallbacks(runnable);
                showResultFail();
                alertLoading.dismiss();
                saveTask(taskSave, null);
                showToastError(t.getMessage());
            }
        });
    }

    private void submitTask(Task task){


        final List<Task> taskList = new ArrayList<Task>();
        taskList.add(task);
       /* Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd' 'HH:mm:ss").create();
        String json = gson.toJson(taskList);*/


        runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if(alertLoading.getProgress() < 100){
                    alertLoading.increaseProgress(2);
                    handler.postDelayed(this, time);
                }
            }
        };
        handler.post(runnable);


        DefaultApi service = ServiceGenerator.createService(DefaultApi.class);

        call = service.submitTask(taskList);
        call.enqueue(new Callback<io.swagger.client.model.Response>() {
            @Override
            public void onResponse(Response<io.swagger.client.model.Response> response, Retrofit retrofit) {
                // Get result Repo from response.body()

                handler.removeCallbacks(runnable);
                alertLoading.updateProgress(100);

                io.swagger.client.model.Response submitData = response.body();

                if (submitData != null && submitData.getResult().equals("success")) {


                    List<Image> images = taskSave.getTaskImages();

                    for (Image image : images) {
                        ImageFile.deleteFile(image.getImagePath());
                    }

                    Signature signature = taskSave.getSignature();
                    ImageFile.deleteFile(signature.getCustomerSignatureImage().getImagePath());
                    ImageFile.deleteFile(signature.getEngineerSignatureImage().getImagePath());

                    deleteTask(taskSave.getTaskId());

                    //saveTask(taskSave);
                    showResultSuccess();

                } else {

                    String message;
                    if (submitData != null) {
                        message = submitData.getMessage();
                    } else {
                        message = response.raw().message();
                    }

                    if(message == null || message.isEmpty()){
                        message = getResources().getString(R.string.result_submit_failed);
                    }

                    showResultFail();
                    saveTask(taskSave, null);
                    showToastError(message);

                }
                alertLoading.dismiss();
            }


            @Override
            public void onFailure(Throwable t) {
                handler.removeCallbacks(runnable);
                alertLoading.dismiss();
                showResultFail();
                saveTask(taskSave, null);
                showToastError(t.getMessage());
            }
        });
    }

    private void showResultFail(){
        imageView.setImageResource(R.drawable.failed_icon);
        titleText.setText(getString(R.string.service_popup_fail_title));
        messageText.setText(getString(R.string.service_popup_fail_message));

        success = false;
    }

    private void showResultSuccess(){
        imageView.setImageResource(R.drawable.success_icon);
        titleText.setText(getString(R.string.service_popup_success_title));
        messageText.setText(getString(R.string.service_popup_success_message));

        success = true;
    }

    private void saveTask(Task task){

        dataSource = new TaskDataSource(ResultActivity.this);
        dataSource.open();
        dataSource.addTask(task);

    }

    private void saveTask(Task task, LoginData loginData){

        dataSource = new TaskDataSource(ResultActivity.this);
        dataSource.open();
        dataSource.addTask(task, loginData);

    }

    private void deleteTask(String taskId){

        dataSource = new TaskDataSource(ResultActivity.this);
        dataSource.open();

        dataSource.deleteTask(taskId);

    }


    private void showToastError(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_data_complete_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        Toast toast = new Toast(ResultActivity.this);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 250);
        toast.setDuration(Toast.LENGTH_SHORT);

        TextView  textView = (TextView) layout.findViewById(R.id.textView1);
        textView.setText(message);

        toast.setView(layout);
        toast.show();
    }


    private void submitData(){

        //check internet and login data

        //if( Network.isNetworkAvailable(this) && (loginData == null || (from != null && !from.isEmpty()))){
        if( Network.isNetworkAvailable(this) && (loginData == null )){
            //go to login page
            Logger.Log("LoginActivity ","LoginActivity");
            Intent intent = new Intent(ResultActivity.this, LoginActivity.class);
            Bundle loginbundle = new Bundle();
            loginbundle.putString(LoginActivity.KEY_REQUEST_LOGIN_FROM,"ResultActivity");

            if(loginData != null){
                loginbundle.putParcelable(LoginActivity.KEY_LOGIN_DATA, loginData);
            }

            intent.putExtras(loginbundle);
            intent.setFlags(0);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);

        }else if(loginData != null && Network.isNetworkAvailable(this)){

            if(task != null){

                Logger.Log("send submitData ","send submitData");
                //alertLoading = new LoadingDialogFragment(); // loginData.getShopName()
                alertLoading = LoadingDialogFragment.newInstance(loginData.getShopName());
                alertLoading.setmListener(new LoadingDialogFragment.onActionListener() {
                    @Override
                    public void onFinishDialog() {
                        //finish();
                        //alertLoading.updateProgress(100);
                    }
                });

                handler = new Handler();

                uploadImage();
            }

        }else{

            buildAlertNoInternet();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Logger.Log("startActivityForResult requestCode ture ", String.valueOf(requestCode));
        Logger.Log("startActivityForResult resultCode ture ", String.valueOf(resultCode));

        if(requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){

            Bundle bundle = data.getExtras();

            if(bundle.containsKey(ResultActivity.KEY_LOGIN_DATA)){
                loginData = bundle.getParcelable(ResultActivity.KEY_LOGIN_DATA);

                if(loginData != null){
                    task.getTaskInfo().setshopID(loginData.getUserId());
                }
            }

            if(bundle.containsKey("shopName")){
                shopName = bundle.getString("shopName");
            }

            from = null;

            submitData();

        }
    }

    private void getUnfinishTask(){

        dataSource = new TaskDataSource(ResultActivity.this);
        dataSource.open();
        ArrayList<OfflineTask> unfinishTasks;
        unfinishTasks = (ArrayList<OfflineTask>) dataSource.getOfflineTasks(getApplicationContext());

        navigateToUnfinishTaskList(unfinishTasks);

    }

    private void navigateToUnfinishTaskList(List<OfflineTask> unfinishList){

        if(unfinishList != null && unfinishList.size()>0){
            Intent intent = new Intent(ResultActivity.this, ServiceActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(LoginActivity.KEY_LOGIN_DATA,loginData);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }/*else{
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void navigateToUnfinishTaskList(){

        Intent intent = new Intent(ResultActivity.this, ServiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(LoginActivity.KEY_LOGIN_DATA,loginData);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
