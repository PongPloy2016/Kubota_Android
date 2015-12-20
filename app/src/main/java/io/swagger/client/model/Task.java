package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;
import io.swagger.client.model.TaskInfo;
import io.swagger.client.model.Signature;
import java.util.*;
import io.swagger.client.model.Image;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Task   {
  
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
    return complete;
  }
  public void setComplete(Boolean complete) {
    this.complete = complete;
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
              Objects.equals(complete, task.complete);
    }else{
      return Objects.equals(taskInfo, task.taskInfo) &&
              Objects.equals(taskImages, task.taskImages) &&
              Objects.equals(signature, task.signature) &&
              Objects.equals(answers, task.answers) &&
              Objects.equals(complete, task.complete);
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
    sb.append("}");
    return sb.toString();
  }
}


