package org.wmy.mall.service;

import org.wmy.mall.vo.CategoryVo;
import org.wmy.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

/**
 * @author wmy
 * @create 2020-11-08 9:28
 */
public interface ICategoryService {

    /**
     * 查询所有种类
     */
    ResponseVo<List<CategoryVo>> selectAll();
    void findSubCategoryId(Integer id, Set<Integer> resultSet);
}
