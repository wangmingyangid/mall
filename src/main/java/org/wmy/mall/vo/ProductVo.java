package org.wmy.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wmy
 * @create 2020-11-09 20:08
 */

@Data
public class ProductVo {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;
}
