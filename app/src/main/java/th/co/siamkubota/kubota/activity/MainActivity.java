package th.co.siamkubota.kubota.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.swagger.client.model.LoginData;
import io.swagger.client.model.Task;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.app.AppController;
import th.co.siamkubota.kubota.fragment.ServiceFragment;
import th.co.siamkubota.kubota.sqlite.DatabaseInfo;
import th.co.siamkubota.kubota.sqlite.TaskDataSource;
import th.co.siamkubota.kubota.utils.function.Converter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends BaseActivity  implements
        View.OnClickListener{

    private Button enterBtn;
    private TextView versionName;

    private String tVersionNameApp;
    private int versionCode;
    private TaskDataSource dataSource;


    private AppController app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if(checkIncompleteTask()){

            if(dataSource == null){
                dataSource = new TaskDataSource(MainActivity.this);
            }

            HashMap<Task,LoginData> mapTask = dataSource.getIncompleteTask();

            if(mapTask.size() > 0){

                List<Task> tasks = new ArrayList(mapTask.keySet());
                Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ServiceFragment.KEY_TASK, tasks.get(0));
                bundle.putParcelable(LoginActivity.KEY_LOGIN_DATA, mapTask.get(tasks.get(0)));
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
            }

        }else{

            setContentView(R.layout.activity_main);

            getWindow().setBackgroundDrawableResource(R.drawable.login_bg);

            enterBtn = (Button) findViewById(R.id.enterBtn);
            enterBtn.setOnClickListener(this);

            try {
                PackageInfo pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                tVersionNameApp = pinfo.versionName; // 1.0
                versionCode = pinfo.versionCode;

                versionName = (TextView) findViewById(R.id.versionName);
                versionName.setText("v " + tVersionNameApp);
                versionName.setVisibility(View.VISIBLE);

            } catch (Exception e) {

            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(dataSource != null){
            dataSource.close();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == enterBtn){

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean checkIncompleteTask(){

        if(dataSource == null){
            dataSource = new TaskDataSource(MainActivity.this);
        }

        dataSource.open();

        HashMap<String, String>  map = dataSource.getCheckTempTask();

        /*
        List<String> keys = new ArrayList(map.keySet());

        for (String key : keys){

            String dateString = map.get(key);
            Date date = Converter.StringToDate(dateString, "dd/MM/yyyy HH:mm:ss");
            Date currentDate = new Date();

            //int numberOfDays=(int)((finalDay.getTime()-today.getTime())/(3600*24*1000));// day
            //int numberOfSeconds=(int)((finalDay.getTime()-today.getTime())/(1000)); // seconds
            int numberOfHours = (int)((currentDate.getTime()-date.getTime())/(3600*1000));

            if(numberOfHours > 6){
                dataSource.deleteTask(key);
                map.remove(key);
            }
        }
*/

        if(map.size() > 0){
            return true;
        }

        return false;
    }
}
