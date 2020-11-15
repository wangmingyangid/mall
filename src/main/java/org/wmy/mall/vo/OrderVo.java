package org.wmy.mall.vo;

import lombok.Data;
import org.wmy.mall.pojo.Shipping;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author wmy
 * @create 2020-11-14 17:50
 */

@Data
public class OrderVo {
    private Long orderNo;

    private BigDecimal payment;

    private Integer paymentType;

    private Integer postage;

    private Integer status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;

    private Date createTime;

    private List<OrderItemVo> orderItemVoList;

    private Integer shippingId;

    private Shipping shippingVo;

}
