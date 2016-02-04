package io.swagger.client.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import io.swagger.client.StringUtil;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class TaskInfo implements Parcelable, Cloneable {
  

  @SerializedName("engineerID")
  private String engineerID = null;

  @SerializedName("shopID")
  private String shopID = null;
  
  @SerializedName("taskType")
  private String taskType = null;
  
  @SerializedName("product")
  private String product = null;
  
  @SerializedName("carModel")
  private String carModel = null;
  
  @SerializedName("carModelOther")
  private String carModelOther = null;
  
  @SerializedName("taskCode")
  private String taskCode = null;
  
  @SerializedName("customerName")
  private String customerName = null;
  
  @SerializedName("tel1")
  private String tel1 = null;
  
  @SerializedName("tel2")
  private String tel2 = null;
  
  @SerializedName("carNo")
  private String carNo = null;
  
  @SerializedName("engineNo")
  private String engineNo = null;
  
  @SerializedName("usageHours")
  private String usageHours = null;
  
  @SerializedName("address")
  private String address = null;
  
  @SerializedName("customerAddress")
  private String customerAddress = null;

  @SerializedName("isOwner")
  private Boolean isOwner = null;

  

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getEngineerID() {
    return engineerID;
  }
  public void setEngineerID(String engineerID) {
    this.engineerID = engineerID;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getShopID() {
    return shopID;
  }
  public void setshopID(String shopID) {
    this.shopID = shopID;
  }


  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getTaskType() {
    return taskType;
  }
  public void setTaskType(String taskType) {
    this.taskType = taskType;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getProduct() {
    return product;
  }
  public void setProduct(String product) {
    this.product = product;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getCarModel() {
    return carModel;
  }
  public void setCarModel(String carModel) {
    this.carModel = carModel;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getCarModelOther() {
    return carModelOther;
  }
  public void setCarModelOther(String carModelOther) {
    this.carModelOther = carModelOther;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getTaskCode() {
    return taskCode;
  }
  public void setTaskCode(String taskCode) {
    this.taskCode = taskCode;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getCustomerName() {
    return customerName;
  }
  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getTel1() {
    return tel1;
  }
  public void setTel1(String tel1) {
    this.tel1 = tel1;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getTel2() {
    return tel2;
  }
  public void setTel2(String tel2) {
    this.tel2 = tel2;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getCarNo() {
    return carNo;
  }
  public void setCarNo(String carNo) {
    this.carNo = carNo;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getEngineNo() {
    return engineNo;
  }
  public void setEngineNo(String engineNo) {
    this.engineNo = engineNo;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getUsageHours() {
    return usageHours;
  }
  public void setUsageHours(String usageHours) {
    this.usageHours = usageHours;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getCustomerAddress() {
    return customerAddress;
  }
  public void setCustomerAddress(String customerAddress) {
    this.customerAddress = customerAddress;
  }


  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getIsOwner() {
    if (isOwner == null )
      return false;
    return isOwner;
  }
  public void setIsOwner(Boolean isOwner) {
    this.isOwner = isOwner;
  }




  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TaskInfo taskInfo = (TaskInfo) o;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      return Objects.equals(engineerID, taskInfo.engineerID) &&
              Objects.equals(shopID, taskInfo.shopID) &&
              Objects.equals(taskType, taskInfo.taskType) &&
              Objects.equals(product, taskInfo.product) &&
              Objects.equals(carModel, taskInfo.carModel) &&
              Objects.equals(carModelOther, taskInfo.carModelOther) &&
              Objects.equals(taskCode, taskInfo.taskCode) &&
              Objects.equals(customerName, taskInfo.customerName) &&
              Objects.equals(tel1, taskInfo.tel1) &&
              Objects.equals(tel2, taskInfo.tel2) &&
              Objects.equals(carNo, taskInfo.carNo) &&
              Objects.equals(engineNo, taskInfo.engineNo) &&
              Objects.equals(usageHours, taskInfo.usageHours) &&
              Objects.equals(address, taskInfo.address) &&
              Objects.equals(customerAddress, taskInfo.customerAddress) &&
              Objects.equals(isOwner, taskInfo.isOwner);
    }else{
      return Objects.equals(engineerID, taskInfo.engineerID) &&
              Objects.equals(shopID, taskInfo.shopID) &&
              Objects.equals(taskType, taskInfo.taskType) &&
              Objects.equals(product, taskInfo.product) &&
              Objects.equals(carModel, taskInfo.carModel) &&
              Objects.equals(carModelOther, taskInfo.carModelOther) &&
              Objects.equals(taskCode, taskInfo.taskCode) &&
              Objects.equals(customerName, taskInfo.customerName) &&
              Objects.equals(tel1, taskInfo.tel1) &&
              Objects.equals(tel2, taskInfo.tel2) &&
              Objects.equals(carNo, taskInfo.carNo) &&
              Objects.equals(engineNo, taskInfo.engineNo) &&
              Objects.equals(usageHours, taskInfo.usageHours) &&
              Objects.equals(address, taskInfo.address) &&
              Objects.equals(customerAddress, taskInfo.customerAddress) &&
              Objects.equals(isOwner, taskInfo.isOwner);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(engineerID, shopID, taskType, product, carModel, carModelOther, taskCode, customerName, tel1, tel2, carNo, engineNo, usageHours, address, customerAddress, isOwner);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskInfo {\n");
    
    sb.append("    engineerID: ").append(StringUtil.toIndentedString(engineerID)).append("\n");
    sb.append("    shopID: ").append(StringUtil.toIndentedString(shopID)).append("\n");
    sb.append("    taskType: ").append(StringUtil.toIndentedString(taskType)).append("\n");
    sb.append("    product: ").append(StringUtil.toIndentedString(product)).append("\n");
    sb.append("    carModel: ").append(StringUtil.toIndentedString(carModel)).append("\n");
    sb.append("    carModelOther: ").append(StringUtil.toIndentedString(carModelOther)).append("\n");
    sb.append("    taskCode: ").append(StringUtil.toIndentedString(taskCode)).append("\n");
    sb.append("    customerName: ").append(StringUtil.toIndentedString(customerName)).append("\n");
    sb.append("    tel1: ").append(StringUtil.toIndentedString(tel1)).append("\n");
    sb.append("    tel2: ").append(StringUtil.toIndentedString(tel2)).append("\n");
    sb.append("    carNo: ").append(StringUtil.toIndentedString(carNo)).append("\n");
    sb.append("    engineNo: ").append(StringUtil.toIndentedString(engineNo)).append("\n");
    sb.append("    usageHours: ").append(StringUtil.toIndentedString(usageHours)).append("\n");
    sb.append("    address: ").append(StringUtil.toIndentedString(address)).append("\n");
    sb.append("    customerAddress: ").append(StringUtil.toIndentedString(customerAddress)).append("\n");
    sb.append("    isOwner: ").append(StringUtil.toIndentedString(isOwner)).append("\n");
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
    dest.writeString(this.engineerID);
    dest.writeString(this.shopID);
    dest.writeString(this.taskType);
    dest.writeString(this.product);
    dest.writeString(this.carModel);
    dest.writeString(this.carModelOther);
    dest.writeString(this.taskCode);
    dest.writeString(this.customerName);
    dest.writeString(this.tel1);
    dest.writeString(this.tel2);
    dest.writeString(this.carNo);
    dest.writeString(this.engineNo);
    dest.writeString(this.usageHours);
    dest.writeString(this.address);
    dest.writeString(this.customerAddress);
    dest.writeValue(this.isOwner);
  }

  public TaskInfo() {
  }

  protected TaskInfo(Parcel in) {
    this.engineerID = in.readString();
    this.shopID = in.readString();
    this.taskType = in.readString();
    this.product = in.readString();
    this.carModel = in.readString();
    this.carModelOther = in.readString();
    this.taskCode = in.readString();
    this.customerName = in.readString();
    this.tel1 = in.readString();
    this.tel2 = in.readString();
    this.carNo = in.readString();
    this.engineNo = in.readString();
    this.usageHours = in.readString();
    this.address = in.readString();
    this.customerAddress = in.readString();
    this.isOwner = (Boolean) in.readValue(Boolean.class.getClassLoader());

  }

  public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
    public TaskInfo createFromParcel(Parcel source) {
      return new TaskInfo(source);
    }

    public TaskInfo[] newArray(int size) {
      return new TaskInfo[size];
    }
  };


  ////////////////////////////////////////////////////////////////////////// implement Cloneable

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}


