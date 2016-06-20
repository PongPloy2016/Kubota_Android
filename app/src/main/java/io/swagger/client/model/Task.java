package io.swagger.client.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import io.swagger.client.StringUtil;
import io.swagger.client.model.TaskInfo;
import io.swagger.client.model.Signature;
import java.util.*;
import io.swagger.client.model.Image;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;
import th.co.siamkubota.kubota.utils.function.Copier;


@ApiModel(description = "")
public class Task implements Parcelable, Cloneable {
  
  @SerializedName("taskInfo")
  private TaskInfo taskInfo = null;
  
  @SerializedName("taskImages")
  private List<Image> taskImages = new ArrayList<Image>();
  
  @SerializedName("signature")
  private Signature signature = null;
  
  @SerializedName("answers")
  private List<Boolean> answers = new ArrayList<Boolean>();
  
  @SerializedName("complete")
  private Boolean complete = null;

  @SerializedName("currentStep")
  private Integer currentStep = null;

  @SerializedName("taskId")
  private String taskId = null;

  @SerializedName("offline")
  private Boolean offline = null;
  

  
  /**
   **/
  @ApiModelProperty(value = "")
  public TaskInfo getTaskInfo() {
    return taskInfo;
  }
  public void setTaskInfo(TaskInfo taskInfo) {
    this.taskInfo = taskInfo;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public List<Image> getTaskImages() {
    return taskImages;
  }
  public void setTaskImages(List<Image> taskImages) {
    this.taskImages = taskImages;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Signature getSignature() {
    return signature;
  }
  public void setSignature(Signature signature) {
    this.signature = signature;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public List<Boolean> getAnswers() {
    return answers;
  }
  public void setAnswers(List<Boolean> answers) {
    this.answers = answers;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getComplete() {
    if(complete == null){
      return false;
    }
    return complete;
  }
  public void setComplete(Boolean complete) {
    this.complete = complete;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getCurrentStep() {
    if(currentStep == null){
      return 1;
    }
    return currentStep;
  }
  public void setCurrentStep(Integer currentStep) {
    this.currentStep = currentStep;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTaskId() {
     return taskId;
  }
  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(taskInfo, task.taskInfo) &&
              Objects.equals(taskImages, task.taskImages) &&
              Objects.equals(signature, task.signature) &&
              Objects.equals(answers, task.answers) &&
              Objects.equals(complete, task.complete) &&
              Objects.equals(currentStep, task.currentStep) &&
              Objects.equals(taskId, task.taskId);
    }else{
      return Objects.equals(taskInfo, task.taskInfo) &&
              Objects.equals(taskImages, task.taskImages) &&
              Objects.equals(signature, task.signature) &&
              Objects.equals(answers, task.answers) &&
              Objects.equals(complete, task.complete) &&
              Objects.equals(currentStep, task.currentStep) &&
              Objects.equals(taskId, task.taskId);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(taskInfo, taskImages, signature, answers, complete);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Task {\n");
    
    sb.append("    taskInfo: ").append(StringUtil.toIndentedString(taskInfo)).append("\n");
    sb.append("    taskImages: ").append(StringUtil.toIndentedString(taskImages)).append("\n");
    sb.append("    signature: ").append(StringUtil.toIndentedString(signature)).append("\n");
    sb.append("    answers: ").append(StringUtil.toIndentedString(answers)).append("\n");
    sb.append("    complete: ").append(StringUtil.toIndentedString(complete)).append("\n");
    sb.append("    currentStep: ").append(StringUtil.toIndentedString(currentStep)).append("\n");
    sb.append("    taskId: ").append(StringUtil.toIndentedString(taskId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  //////////////////////////////////////////////////////// implement parcelable


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.taskInfo, flags);
    dest.writeTypedList(taskImages);
    dest.writeParcelable(this.signature, flags);
    dest.writeList(this.answers);
    dest.writeValue(this.complete);
    dest.writeValue(this.currentStep);
    dest.writeString(this.taskId);
  }

  public Task() {
  }

  protected Task(Parcel in) {
    this.taskInfo = in.readParcelable(TaskInfo.class.getClassLoader());
    //this.taskImages = in.createTypedArrayList(Image.CREATOR);
    in.readTypedList(this.taskImages, Image.CREATOR);
    this.signature = in.readParcelable(Signature.class.getClassLoader());
    this.answers = new ArrayList<Boolean>();
    in.readList(this.answers, List.class.getClassLoader());
    this.complete = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.currentStep = (Integer) in.readValue(Integer.class.getClassLoader());
    this.taskId = in.readString();
  }

  public static final Creator<Task> CREATOR = new Creator<Task>() {
    public Task createFromParcel(Parcel source) {
      return new Task(source);
    }

    public Task[] newArray(int size) {
      return new Task[size];
    }
  };

  ////////////////////////////////////////////////////////////////////////// implement Cloneable

  @Override
  public Object clone() throws CloneNotSupportedException {
    Task cloned = (Task)super.clone();

    cloned.setComplete(cloned.getComplete());
    // the above is applicable in case of primitive member types,
    // however, in case of non primitive types
    // cloned.setNonPrimitiveType(cloned.getNonPrimitiveType().clone());

    cloned.setTaskInfo(cloned.getTaskInfo());
    cloned.setTaskImages(cloned.getTaskImages());
    cloned.setSignature(cloned.getSignature());
    cloned.setAnswers(cloned.getAnswers());
    return cloned;
  }

  public Task(String taskId) {
    this.taskId = taskId;
  }

  public Task(Task task) {

    setTaskInfo(new TaskInfo());
    setTaskImages(new ArrayList<Image>());
    setSignature(new Signature());
    setAnswers(new ArrayList<Boolean>());
    setComplete(task.getComplete());
    setCurrentStep(task.getCurrentStep());
    setTaskId(task.getTaskId());

    Copier.copy(task.taskInfo, this.taskInfo);
    Copier.copy(task.signature, this.signature);

    this.signature.setCustomerSignatureImage(new Image());
    this.signature.setEngineerSignatureImage(new Image());

    Copier.copy(task.signature.getCustomerSignatureImage(), this.signature.getCustomerSignatureImage());
    Copier.copy(task.signature.getEngineerSignatureImage(), this.signature.getEngineerSignatureImage());

    for (Image img : task.getTaskImages()){

      Image i = new Image();
      Copier.copy(img, i);
      taskImages.add(i);

    }

    for (Boolean b : task.getAnswers()){
      answers.add(b);
    }

  }
}


