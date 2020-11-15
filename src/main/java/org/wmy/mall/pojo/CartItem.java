package org.wmy.mall.pojo;

import lombok.Data;

/**
 * @author wmy
 * @create 2020-11-13 19:35
 * 该类是往redis中存储的
 */

@Data
public class CartItem {
    private Integer productId;

    private Integer quantity;

    private Boolean productSelected;

    public CartItem(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }

    public CartItem() {
    }
}
