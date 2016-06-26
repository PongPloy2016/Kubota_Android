package th.co.siamkubota.kubota.model;

import io.swagger.client.model.LoginData;
import io.swagger.client.model.Task;

/**
 * Created by sipangka on 26/06/2559.
 */
public class OfflineTask {
    private LoginData loginData;
    private Task task;

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
