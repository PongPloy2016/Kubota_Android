package io.swagger.client.model;

import android.os.Build;

import io.swagger.client.StringUtil;


import com.google.gson.annotations.SerializedName;


import java.util.Objects;

import io.swagger.annotations.*;




@ApiModel(description = "")
public class TaskInfo   {
  
  @SerializedName("engineerID")
  private String engineerID = null;
  
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
  
  @SerializedName("serviceAddress")
  private String serviceAddress = null;
  
  @SerializedName("customerAddress")
  private String customerAddress = null;
  

  
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
  public String getServiceAddress() {
    return serviceAddress;
  }
  public void setServiceAddress(String serviceAddress) {
    this.serviceAddress = serviceAddress;
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
              Objects.equals(serviceAddress, taskInfo.serviceAddress) &&
              Objects.equals(customerAddress, taskInfo.customerAddress);
    }else{
      return Objects.equals(engineerID, taskInfo.engineerID) &&
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
              Objects.equals(serviceAddress, taskInfo.serviceAddress) &&
              Objects.equals(customerAddress, taskInfo.customerAddress);
    }

  }

  @Override
  public int hashCode() {
    return Objects.hash(engineerID, taskType, product, carModel, carModelOther, taskCode, customerName, tel1, tel2, carNo, engineNo, usageHours, serviceAddress, customerAddress);
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TaskInfo {\n");
    
    sb.append("    engineerID: ").append(StringUtil.toIndentedString(engineerID)).append("\n");
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
    sb.append("    serviceAddress: ").append(StringUtil.toIndentedString(serviceAddress)).append("\n");
    sb.append("    customerAddress: ").append(StringUtil.toIndentedString(customerAddress)).append("\n");
    sb.append("}");
    return sb.toString();
  }
}


