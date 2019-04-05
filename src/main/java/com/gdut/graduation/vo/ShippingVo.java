package com.gdut.graduation.vo;

import lombok.Data;

/**
 * @Description 收获地址视图对象
 * @Author Skye
 * @Date 2019/4/3 15:10
 * @Version 1.0
 **/
@Data
public class ShippingVo {

    private String receiverName;
    private String reveiverPhone;
    private String receiverMobile;
    private String receiverProvince;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverAddress;
    private String receiverZip;
}
