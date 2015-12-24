package io.swagger.client.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import io.swagger.client.StringUtil;
import io.swagger.client.model.Image;
import java.util.Date;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class Signature implements Parcelable {

  @SerializedName("customerName")
  private String customerName = null;
  
  @SerializedName("customerSignature")
  private String customerSignature = null;
  
  @SerializedName("customerSignedDate")
  private Date customerSignedDate = null;

  @SerializedName("engineerName")
  private String engineerName = null;
  
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
  public String getCustomerNamr() {
    return customerName;
  }
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  
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
  public String getEngineerName() {
    return engineerName;
  }
  public void setEngineerName(String engineerName) {
    this.engineerName = engineerName;
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
      return  Objects.equals(customerName, signature.customerName) &&
              Objects.equals(customerSignature, signature.customerSignature) &&
              Objects.equals(customerSignedDate, signature.customerSignedDate) &&
              Objects.equals(engineerName, signature.engineerName) &&
              Objects.equals(engineerSignature, signature.engineerSignature) &&
              Objects.equals(engineerSignedDate, signature.engineerSignedDate) &&
              Objects.equals(customerSignatureImage, signature.customerSignatureImage) &&
              Objects.equals(engineerSignatureImage, signature.engineerSignatureImage);
    }else{
      return  Objects.equals(customerName, signature.customerName) &&
              Objects.equals(customerSignature, signature.customerSignature) &&
              Objects.equals(customerSignedDate, signature.customerSignedDate) &&
              Objects.equals(engineerName, signature.engineerName) &&
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

    sb.append("    customerName: ").append(StringUtil.toIndentedString(customerName)).append("\n");
    sb.append("    customerSignature: ").append(StringUtil.toIndentedString(customerSignature)).append("\n");
    sb.append("    customerSignedDate: ").append(StringUtil.toIndentedString(customerSignedDate)).append("\n");
    sb.append("    engineerName: ").append(StringUtil.toIndentedString(engineerName)).append("\n");
    sb.append("    engineerSignature: ").append(StringUtil.toIndentedString(engineerSignature)).append("\n");
    sb.append("    engineerSignedDate: ").append(StringUtil.toIndentedString(engineerSignedDate)).append("\n");
    sb.append("    customerSignatureImage: ").append(StringUtil.toIndentedString(customerSignatureImage)).append("\n");
    sb.append("    engineerSignatureImage: ").append(StringUtil.toIndentedString(engineerSignatureImage)).append("\n");
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
    dest.writeString(this.customerName);
    dest.writeString(this.customerSignature);
    dest.writeLong(customerSignedDate != null ? customerSignedDate.getTime() : -1);
    dest.writeString(this.engineerName);
    dest.writeString(this.engineerSignature);
    dest.writeLong(engineerSignedDate != null ? engineerSignedDate.getTime() : -1);
    dest.writeParcelable(this.customerSignatureImage, 0);
    dest.writeParcelable(this.engineerSignatureImage, 0);
  }

  public Signature() {
  }

  protected Signature(Parcel in) {
    this.customerName = in.readString();
    this.customerSignature = in.readString();
    long tmpCustomerSignedDate = in.readLong();
    this.customerSignedDate = tmpCustomerSignedDate == -1 ? null : new Date(tmpCustomerSignedDate);
    this.engineerName = in.readString();
    this.engineerSignature = in.readString();
    long tmpEngineerSignedDate = in.readLong();
    this.engineerSignedDate = tmpEngineerSignedDate == -1 ? null : new Date(tmpEngineerSignedDate);
    this.customerSignatureImage = in.readParcelable(Image.class.getClassLoader());
    this.engineerSignatureImage = in.readParcelable(Image.class.getClassLoader());
  }

  public static final Creator<Signature> CREATOR = new Creator<Signature>() {
    public Signature createFromParcel(Parcel source) {
      return new Signature(source);
    }

    public Signature[] newArray(int size) {
      return new Signature[size];
    }
  };
}


