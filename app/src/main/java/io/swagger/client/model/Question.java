package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;
import java.util.*;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Question   {
  
  @SerializedName("title")
  private String title = null;
  
  @SerializedName("choices")
  private List<String> choices = new ArrayList<String>();
  

  
  /**
   * question title.
   **/
  @ApiModelProperty(value = "question title.")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   * question choices.
   **/
  @ApiModelProperty(value = "question choices.")
  public List<String> getChoices() {
    return choices;
  }
  public void setChoices(List<String> choices) {
    this.choices = choices;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Question question = (Question) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(title, question.title) &&
              Objects.equals(choices, question.choices);

    }else{
      return Objects.equals(title, question.title) &&
              Objects.equals(choices, question.choices);

    }
  }



  @Override
  public int hashCode() {
    return Objects.hash(title, choices);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Question {\n");
    
    sb.append("    title: ").append(StringUtil.toIndentedString(title)).append("\n");
    sb.append("    choices: ").append(StringUtil.toIndentedString(choices)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}


