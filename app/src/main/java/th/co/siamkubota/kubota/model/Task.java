package th.co.siamkubota.kubota.model;

/**
 * Created by sipangka on 15/11/2558.
 */
public class Task {
    private String name;
    private Boolean complete;

    public Task(String name, Boolean complete) {
        this.name = name;
        this.complete = complete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
