package org.wmy.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wmy
 * @create 2020-11-14 15:17
 */

@Data
public class ShippingForm {

    @NotBlank
    private String receiverName;

    @NotBlank
    private String receiverPhone;

    @NotBlank
    private String receiverMobile;

    @NotBlank
    private String receiverProvince;

    @NotBlank
    private String receiverCity;

    @NotBlank
    private String receiverDistrict;

    @NotBlank
    private String receiverAddress;

    @NotBlank
    private String receiverZip;
}
