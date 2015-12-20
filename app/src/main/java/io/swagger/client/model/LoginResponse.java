package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;
import io.swagger.client.model.LoginData;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class LoginResponse   {
  
  @SerializedName("result")
  private String result = null;
  
  @SerializedName("message")
  private String message = null;
  
  @SerializedName("parameter")
  private LoginData parameter = null;
  

  
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
   * shop and form data.
   **/
  @ApiModelProperty(value = "shop and form data.")
  public LoginData getParameter() {
    return parameter;
  }
  public void setParameter(LoginData parameter) {
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
    LoginResponse loginResponse = (LoginResponse) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(result, loginResponse.result) &&
              Objects.equals(message, loginResponse.message) &&
              Objects.equals(parameter, loginResponse.parameter);
    }else{
      return Objects.equals(result, loginResponse.result) &&
              Objects.equals(message, loginResponse.message) &&
              Objects.equals(parameter, loginResponse.parameter);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(result, message, parameter);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResponse {\n");
    
    sb.append("    result: ").append(StringUtil.toIndentedString(result)).append("\n");
    sb.append("    message: ").append(StringUtil.toIndentedString(message)).append("\n");
    sb.append("    parameter: ").append(StringUtil.toIndentedString(parameter)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}


