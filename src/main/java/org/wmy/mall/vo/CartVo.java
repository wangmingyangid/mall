package org.wmy.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wmy
 * @create 2020-11-10 20:42
 */

@Data
public class CartVo {
    private List<CartProductVo> cartProductVoList;

    private Boolean selectedAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;
}
