package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Response   {
  
  @SerializedName("result")
  private String result = null;
  
  @SerializedName("message")
  private String message = null;
  

  
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Response response = (Response) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(result, response.result) &&
              Objects.equals(message, response.message);
    }else{
      return Objects.equals(result, response.result) &&
              Objects.equals(message, response.message);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(result, message);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Response {\n");
    
    sb.append("    result: ").append(StringUtil.toIndentedString(result)).append("\n");
    sb.append("    message: ").append(StringUtil.toIndentedString(message)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}


