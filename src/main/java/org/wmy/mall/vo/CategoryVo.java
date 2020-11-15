package org.wmy.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wmy
 * @create 2020-11-08 9:25
 */

@Data
public class CategoryVo {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer sortOrder;
    private List<CategoryVo> subCategories;
}

