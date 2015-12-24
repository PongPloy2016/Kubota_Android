package io.swagger.client.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.StringUtil;


@ApiModel(description = "")
public class UploadData  {

  @SerializedName("image1")
  private String image1 = null;

  @SerializedName("image2")
  private String image2 = null;

  @SerializedName("image3")
  private String image3 = null;

  @SerializedName("image4")
  private String image4 = null;

  @SerializedName("signature1")
  private String signature1 = null;

  @SerializedName("signature2")
  private String signature2 = null;



  @ApiModelProperty(value = "user id.")
  public String getImage1() {
    return image1;
  }

  public void setImage1(String image1) {
    this.image1 = image1;
  }


  @ApiModelProperty(value = "user id.")
  public String getImage2() {
    return image2;
  }

  public void setImage2(String image2) {
    this.image2 = image2;
  }

  @ApiModelProperty(value = "user id.")
  public String getImage3() {
    return image3;
  }

  public void setImage3(String image3) {
    this.image3 = image3;
  }

  @ApiModelProperty(value = "user id.")
  public String getImage4() {
    return image4;
  }

  public void setImage4(String image4) {
    this.image4 = image4;
  }

  @ApiModelProperty(value = "user id.")
  public String getSignature1() {
    return signature1;
  }

  public void setSignature1(String signature1) {
    this.signature1 = signature1;
  }

  @ApiModelProperty(value = "user id.")
  public String getSignature2() {
    return signature2;
  }

  public void setSignature2(String signature2) {
    this.signature2 = signature2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UploadData uploadData = (UploadData) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(image1, uploadData.image1) &&
              Objects.equals(image2, uploadData.image2) &&
              Objects.equals(image3, uploadData.image3) &&
              Objects.equals(image4, uploadData.image4) &&
              Objects.equals(signature1, uploadData.signature1) &&
              Objects.equals(signature2, uploadData.signature2);
    }else{
      return Objects.equals(image1, uploadData.image1) &&
              Objects.equals(image2, uploadData.image2) &&
              Objects.equals(image3, uploadData.image3) &&
              Objects.equals(image4, uploadData.image4) &&
              Objects.equals(signature1, uploadData.signature1) &&
              Objects.equals(signature2, uploadData.signature2);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(image1, image2, image3, image4, signature1, signature2);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginData {\n");

    sb.append("    image1: ").append(StringUtil.toIndentedString(image1)).append("\n");
    sb.append("    image2: ").append(StringUtil.toIndentedString(image2)).append("\n");
    sb.append("    image3: ").append(StringUtil.toIndentedString(image3)).append("\n");
    sb.append("    image4: ").append(StringUtil.toIndentedString(image4)).append("\n");
    sb.append("    signature1: ").append(StringUtil.toIndentedString(signature1)).append("\n");
    sb.append("    signature2: ").append(StringUtil.toIndentedString(signature2)).append("\n");
    sb.append("}");
    return sb.toString();
  }


}


