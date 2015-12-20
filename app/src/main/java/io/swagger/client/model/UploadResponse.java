package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;
import java.util.*;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class UploadResponse   {
  
  @SerializedName("result")
  private String result = null;
  
  @SerializedName("message")
  private String message = null;
  
  @SerializedName("parameter")
  private List<String> parameter = new ArrayList<String>();
  

  
  /**
   * success|error
   **/
  @ApiModelProperty(value = "success|error")
  public String getResult() {
    return result;
  }
  public void setResult(String result) {
    this.result = result;
  }

  
  /**
   * Success or error message.
   **/
  @ApiModelProperty(value = "Success or error message.")
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  
  /**
   * images path.
   **/
  @ApiModelProperty(value = "images path.")
  public List<String> getParameter() {
    return parameter;
  }
  public void setParameter(List<String> parameter) {
    this.parameter = parameter;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadResponse uploadResponse = (UploadResponse) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(result, uploadResponse.result) &&
              Objects.equals(message, uploadResponse.message) &&
              Objects.equals(parameter, uploadResponse.parameter);
    }else{
      return Objects.equals(result, uploadResponse.result) &&
              Objects.equals(message, uploadResponse.message) &&
              Objects.equals(parameter, uploadResponse.parameter);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(result, message, parameter);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class UploadResponse {\n");
    
    sb.append("    result: ").append(StringUtil.toIndentedString(result)).append("\n");
    sb.append("    message: ").append(StringUtil.toIndentedString(message)).append("\n");
    sb.append("    parameter: ").append(StringUtil.toIndentedString(parameter)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}


