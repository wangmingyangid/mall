package org.wmy.mall.service.impl;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.wmy.mall.dao.ProductMapper;
import org.wmy.mall.enums.ProductStatusEnum;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.form.CartAddForm;
import org.wmy.mall.form.CartUpdateForm;
import org.wmy.mall.pojo.CartItem;
import org.wmy.mall.pojo.Product;
import org.wmy.mall.service.ICartService;
import org.wmy.mall.vo.CartProductVo;
import org.wmy.mall.vo.CartVo;
import org.wmy.mall.vo.ResponseVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author wmy
 * @create 2020-11-13 19:14
 */

@Service
public class CartServiceImpl implements ICartService {
    
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    private static final String CART_REDIS_KEY_TEMPLATE = "cart_%d";
    
    @Override
    public ResponseVo<CartVo> add(Integer uid,CartAddForm cartAddForm) {
        Integer quantity=1;

        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        //判断商品是否存在
        if(product == null){
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }
        //判断商品的状态
        if(!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //商品库存是否充足(商品添加每次添加一件)
        if(product.getStock() <= 0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        //写入redis，key：cart_uid
        //三个参数，分别是h:uid，hk:productId，hv:CartItem
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String productId = String.valueOf(product.getId());
        String value = opsForHash.get(redisKey, productId);

        CartItem cartItem;
        if(StringUtils.isEmpty(value)){
            //购物车没有该商品
            cartItem = new CartItem(product.getId(),quantity,cartAddForm.getSelected());

        } else {
            //购物车存在该商品，数量加1
            cartItem = gson.fromJson(value, CartItem.class);
            cartItem.setQuantity(quantity+cartItem.getQuantity());
        }

        opsForHash.put(redisKey, productId, gson.toJson(cartItem));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);

        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        Boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        //获得键对应的所有值
        Map<String, String> entries = opsForHash.entries(redisKey);
        for(Map.Entry<String,String> entry : entries.entrySet()){
            Integer productId = Integer.valueOf(entry.getKey());
            CartItem cartItem = gson.fromJson(entry.getValue(), CartItem.class);

            Product product = productMapper.selectByPrimaryKey(productId);
            if(product != null){
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cartItem.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())),
                        product.getStock(),
                        cartItem.getProductSelected());
                cartProductVoList.add(cartProductVo);
                if(!cartItem.getProductSelected()){
                    selectAll=false;
                }
                //计算总价（只计算选中的）
                if(cartProductVo.getProductSelected()){
                    //add方法必须要重新赋值，否则没有效果
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }

            }
            cartTotalQuantity+=cartItem.getQuantity();
        }
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = opsForHash.get(redisKey, String.valueOf(productId));

        if(StringUtils.isEmpty(value)){
            //购物车没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //购物车存在该商品，修改内容
        CartItem cartItem = gson.fromJson(value, CartItem.class);
        if(form.getQuantity() != null && form.getQuantity() >= 0){
            cartItem.setQuantity(form.getQuantity());
        }
        if(form.getSelected() != null){
            cartItem.setProductSelected(form.getSelected());
        }
        opsForHash.put(redisKey,String.valueOf(productId),gson.toJson(cartItem));

        return list(uid);

    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = opsForHash.get(redisKey, String.valueOf(productId));

        if(StringUtils.isEmpty(value)){
            //购物车没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //购物车存在该商品，删除
        opsForHash.delete(redisKey,String.valueOf(productId));
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        for(CartItem cartItem:listForCartItem(uid)){
            cartItem.setProductSelected(true);
            opsForHash.put(redisKey,
                    String.valueOf(cartItem.getProductId()),
                    gson.toJson(cartItem));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        for(CartItem cartItem:listForCartItem(uid)){
            cartItem.setProductSelected(false);
            opsForHash.put(redisKey,
                    String.valueOf(cartItem.getProductId()),
                    gson.toJson(cartItem));
        }
        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCartItem(uid).stream()
                .map(CartItem::getQuantity)
                .reduce(0, Integer::sum);
        return ResponseVo.success(sum);
    }

    public List<CartItem> listForCartItem(Integer uid){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        ArrayList<CartItem> cartItemList = new ArrayList<>();
        for(Map.Entry<String,String> entry : entries.entrySet()){
            cartItemList.add(gson.fromJson(entry.getValue(),CartItem.class));
        }
        return cartItemList;

    }

    /**
     *
     * 该方法是list的优化方法，避免了多次查询数据库
     */
    private ResponseVo<CartVo> list1(Integer uid){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        //获得所有商品，key:商品id   value:cartItem的json字符串
        Map<String, String> entries = opsForHash.entries(redisKey);
        HashSet<Integer> productIdSet = new HashSet<>();
        for(Map.Entry<String,String> entry : entries.entrySet()){
            String productId = entry.getKey();
            productIdSet.add(Integer.valueOf(productId));
        }
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        Boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        if (productIdSet.size() < 1){
            cartVo.setCartProductVoList(cartProductVoList);
            cartVo.setCartTotalQuantity(cartTotalQuantity);
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setSelectedAll(selectAll);
            return  ResponseVo.success(cartVo);
        }
        List<Product> products = productMapper.selectByProductIdSet(productIdSet);
        for(Product product:products) {
            String value = entries.get(String.valueOf(product.getId()));
            CartItem cartItem = gson.fromJson(value, CartItem.class);

            CartProductVo cartProductVo = new CartProductVo(product.getId(),
                    cartItem.getQuantity(),
                    product.getName(),
                    product.getSubtitle(),
                    product.getMainImage(),
                    product.getPrice(),
                    product.getStatus(),
                    product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())),
                    product.getStock(),
                    cartItem.getProductSelected());
            cartProductVoList.add(cartProductVo);
            if(!cartItem.getProductSelected()){
                selectAll=false;
            }
            //计算总价（只计算选中的）
            if(cartProductVo.getProductSelected()){
                //add方法必须要重新赋值，否则没有效果
                cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
            }
            cartTotalQuantity+=cartItem.getQuantity();
        }
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }
}
