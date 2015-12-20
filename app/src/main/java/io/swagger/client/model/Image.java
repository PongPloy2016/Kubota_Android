package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;
import java.util.Date;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Image   {
  
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
}


