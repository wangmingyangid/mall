package org.wmy.mall.service;

import com.github.pagehelper.PageInfo;
import org.wmy.mall.vo.ProductDetailVo;
import org.wmy.mall.vo.ProductVo;
import org.wmy.mall.vo.ResponseVo;

import javax.xml.ws.Response;
import java.util.List;

/**
 * @author wmy
 * @create 2020-11-09 20:11
 */
public interface IProductService {
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
