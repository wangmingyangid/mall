package org.wmy.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wmy.mall.dao.ProductMapper;
import org.wmy.mall.enums.ProductStatusEnum;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.pojo.Product;
import org.wmy.mall.service.ICategoryService;
import org.wmy.mall.service.IProductService;
import org.wmy.mall.vo.ProductDetailVo;
import org.wmy.mall.vo.ProductVo;
import org.wmy.mall.vo.ResponseVo;

import javax.xml.ws.Response;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wmy
 * @create 2020-11-09 20:13
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {
    @Autowired
    ICategoryService categoryService;
    @Autowired
    ProductMapper productMapper;
    
    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {
        HashSet<Integer> categoryIdSet = new HashSet<>();
        if(categoryId != null){
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);

        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());

        //此处必须这样写，否则在PageInfo中，list instanceof Page 是不会成立的；没办法进行赋值
        //等到所有分页相关的属性，赋值完毕后，可以采用替换的方式把productList数据替换掉
        //所以开启分页后，肯定是对List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        //做了处理，把productList转成page类型
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);

        //只对确定性条件进行判断
        if(product.getStatus().equals(ProductStatusEnum.OFF_SALE.getCode())||
                product.getStatus().equals(ProductStatusEnum.DELETE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product,productDetailVo);
        //敏感数据处理（比如，库存）
        productDetailVo.setStock(productDetailVo.getStock() > 100 ? 100:productDetailVo.getStock());
        return ResponseVo.success(productDetailVo);
    }
}
