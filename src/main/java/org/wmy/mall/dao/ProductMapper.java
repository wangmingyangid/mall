package org.wmy.mall.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wmy.mall.pojo.Product;

import java.util.List;
import java.util.Set;


public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);

}