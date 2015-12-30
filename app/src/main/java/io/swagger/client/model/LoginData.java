package io.swagger.client.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import io.swagger.client.StringUtil;
import java.util.*;
import io.swagger.client.model.Question;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class LoginData  implements Parcelable {
  
  @SerializedName("user_id")
  private String userId = null;
  
  @SerializedName("shop_id")
  private String shopId = null;
  
  @SerializedName("shop_name")
  private String shopName = null;
  
  @SerializedName("questions")
  private List<Question> questions = new ArrayList<Question>();
  

  
  /**
   * user id.
   **/
  @ApiModelProperty(value = "user id.")
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }

  
  /**
   * shop id.
   **/
  @ApiModelProperty(value = "shop id.")
  public String getShopId() {
    return shopId;
  }
  public void setShopId(String shopId) {
    this.shopId = shopId;
  }

  
  /**
   * shop name.
   **/
  @ApiModelProperty(value = "shop name.")
  public String getShopName() {
    return shopName;
  }
  public void setShopName(String shopName) {
    this.shopName = shopName;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public List<Question> getQuestions() {
    return questions;
  }
  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginData loginData = (LoginData) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(userId, loginData.userId) &&
              Objects.equals(shopId, loginData.shopId) &&
              Objects.equals(shopName, loginData.shopName) &&
              Objects.equals(questions, loginData.questions);
    }else{
      return Objects.equals(userId, loginData.userId) &&
              Objects.equals(shopId, loginData.shopId) &&
              Objects.equals(shopName, loginData.shopName) &&
              Objects.equals(questions, loginData.questions);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, shopId, shopName, questions);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginData {\n");
    
    sb.append("    userId: ").append(StringUtil.toIndentedString(userId)).append("\n");
    sb.append("    shopId: ").append(StringUtil.toIndentedString(shopId)).append("\n");
    sb.append("    shopName: ").append(StringUtil.toIndentedString(shopName)).append("\n");
    sb.append("    questions: ").append(StringUtil.toIndentedString(questions)).append("\n");
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
    dest.writeString(this.userId);
    dest.writeString(this.shopId);
    dest.writeString(this.shopName);
    //dest.writeList(this.questions);
    dest.writeTypedList(this.questions);
  }

  public LoginData() {
  }

  protected LoginData(Parcel in) {
    this.userId = in.readString();
    this.shopId = in.readString();
    this.shopName = in.readString();
    //this.questions = new ArrayList<Question>();
    if(this.questions  == null){
      this.questions = new ArrayList<Question>();
    }
    in.readTypedList(this.questions, Question.CREATOR);
    //in.readList(this.questions, List.class.getClassLoader());
  }

  public static final Creator<LoginData> CREATOR = new Creator<LoginData>() {
    public LoginData createFromParcel(Parcel source) {
      return new LoginData(source);
    }

    public LoginData[] newArray(int size) {
      return new LoginData[size];
    }
  };
}


