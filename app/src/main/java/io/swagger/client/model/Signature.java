package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;
import io.swagger.client.model.Image;
import java.util.Date;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Signature   {
  
  @SerializedName("customerSignature")
  private String customerSignature = null;
  
  @SerializedName("customerSignedDate")
  private Date customerSignedDate = null;
  
  @SerializedName("engineerSignature")
  private String engineerSignature = null;
  
  @SerializedName("engineerSignedDate")
  private Date engineerSignedDate = null;
  
  @SerializedName("customerSignatureImage")
  private Image customerSignatureImage = null;
  
  @SerializedName("engineerSignatureImage")
  private Image engineerSignatureImage = null;
  

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getCustomerSignature() {
    return customerSignature;
  }
  public void setCustomerSignature(String customerSignature) {
    this.customerSignature = customerSignature;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCustomerSignedDate() {
    return customerSignedDate;
  }
  public void setCustomerSignedDate(Date customerSignedDate) {
    this.customerSignedDate = customerSignedDate;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getEngineerSignature() {
    return engineerSignature;
  }
  public void setEngineerSignature(String engineerSignature) {
    this.engineerSignature = engineerSignature;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getEngineerSignedDate() {
    return engineerSignedDate;
  }
  public void setEngineerSignedDate(Date engineerSignedDate) {
    this.engineerSignedDate = engineerSignedDate;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Image getCustomerSignatureImage() {
    return customerSignatureImage;
  }
  public void setCustomerSignatureImage(Image customerSignatureImage) {
    this.customerSignatureImage = customerSignatureImage;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Image getEngineerSignatureImage() {
    return engineerSignatureImage;
  }
  public void setEngineerSignatureImage(Image engineerSignatureImage) {
    this.engineerSignatureImage = engineerSignatureImage;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Signature signature = (Signature) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(customerSignature, signature.customerSignature) &&
              Objects.equals(customerSignedDate, signature.customerSignedDate) &&
              Objects.equals(engineerSignature, signature.engineerSignature) &&
              Objects.equals(engineerSignedDate, signature.engineerSignedDate) &&
              Objects.equals(customerSignatureImage, signature.customerSignatureImage) &&
              Objects.equals(engineerSignatureImage, signature.engineerSignatureImage);
    }else{
      return Objects.equals(customerSignature, signature.customerSignature) &&
              Objects.equals(customerSignedDate, signature.customerSignedDate) &&
              Objects.equals(engineerSignature, signature.engineerSignature) &&
              Objects.equals(engineerSignedDate, signature.engineerSignedDate) &&
              Objects.equals(customerSignatureImage, signature.customerSignatureImage) &&
              Objects.equals(engineerSignatureImage, signature.engineerSignatureImage);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(customerSignature, customerSignedDate, engineerSignature, engineerSignedDate, customerSignatureImage, engineerSignatureImage);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Signature {\n");
    
    sb.append("    customerSignature: ").append(StringUtil.toIndentedString(customerSignature)).append("\n");
    sb.append("    customerSignedDate: ").append(StringUtil.toIndentedString(customerSignedDate)).append("\n");
    sb.append("    engineerSignature: ").append(StringUtil.toIndentedString(engineerSignature)).append("\n");
    sb.append("    engineerSignedDate: ").append(StringUtil.toIndentedString(engineerSignedDate)).append("\n");
    sb.append("    customerSignatureImage: ").append(StringUtil.toIndentedString(customerSignatureImage)).append("\n");
    sb.append("    engineerSignatureImage: ").append(StringUtil.toIndentedString(engineerSignatureImage)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}


