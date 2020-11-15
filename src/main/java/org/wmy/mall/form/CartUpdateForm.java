package org.wmy.mall.form;

import lombok.Data;

/**
 * @author wmy
 * @create 2020-11-14 10:21
 *
 * 这两项都是非必填项
 */

@Data
public class CartUpdateForm {

    //代表最终的结果，而不是对商品进行加减的数量
    private Integer quantity;

    private Boolean selected;
}
