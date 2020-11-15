package org.wmy.mall.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 收货地址实体类
 * 由于该实体类的字段跟要返回给前端的字段一致，所以不新建vo类了
 */

@Data
public class Shipping {
    private Integer id;

    private Integer userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

    private Date createTime;

    private Date updateTime;

}