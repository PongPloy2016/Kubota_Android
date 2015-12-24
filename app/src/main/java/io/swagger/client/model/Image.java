package io.swagger.client.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import io.swagger.client.StringUtil;
import java.util.Date;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Image implements Parcelable {
  
  @SerializedName("image")
  private String image = null;
  
  @SerializedName("captured_at")
  private Date capturedAt = null;
  
  @SerializedName("imagePath")
  private String imagePath = null;
  

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getImage() {
    return image;
  }
  public void setImage(String image) {
    this.image = image;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCapturedAt() {
    return capturedAt;
  }
  public void setCapturedAt(Date capturedAt) {
    this.capturedAt = capturedAt;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getImagePath() {
    return imagePath;
  }
  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Image image = (Image) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(image, image.image) &&
              Objects.equals(capturedAt, image.capturedAt) &&
              Objects.equals(imagePath, image.imagePath);
    }else{

      return Objects.equals(image, image.image) &&
              Objects.equals(capturedAt, image.capturedAt) &&
              Objects.equals(imagePath, image.imagePath);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(image, capturedAt, imagePath);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Image {\n");
    
    sb.append("    image: ").append(StringUtil.toIndentedString(image)).append("\n");
    sb.append("    capturedAt: ").append(StringUtil.toIndentedString(capturedAt)).append("\n");
    sb.append("    imagePath: ").append(StringUtil.toIndentedString(imagePath)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /////////////////////////////////////////////////////////

  public Image(String imagePath, Date capturedAt) {
    this.imagePath = imagePath;
    this.capturedAt = capturedAt;
  }


  //////////////////////////////////////////////////////// implement parcelable


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.image);
    dest.writeLong(capturedAt != null ? capturedAt.getTime() : -1);
    dest.writeString(this.imagePath);
  }

  public Image() {
  }

  protected Image(Parcel in) {
    this.image = in.readString();
    long tmpCapturedAt = in.readLong();
    this.capturedAt = tmpCapturedAt == -1 ? null : new Date(tmpCapturedAt);
    this.imagePath = in.readString();
  }

  public static final Creator<Image> CREATOR = new Creator<Image>() {
    public Image createFromParcel(Parcel source) {
      return new Image(source);
    }

    public Image[] newArray(int size) {
      return new Image[size];
    }
  };
}


