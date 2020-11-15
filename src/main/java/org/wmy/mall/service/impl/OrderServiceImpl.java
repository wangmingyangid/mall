package org.wmy.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.wmy.mall.dao.OrderItemMapper;
import org.wmy.mall.dao.OrderMapper;
import org.wmy.mall.dao.ProductMapper;
import org.wmy.mall.dao.ShippingMapper;
import org.wmy.mall.enums.OrderStatusEnum;
import org.wmy.mall.enums.PaymentTypeEnum;
import org.wmy.mall.enums.ProductStatusEnum;
import org.wmy.mall.enums.ResponseEnum;
import org.wmy.mall.pojo.*;
import org.wmy.mall.service.ICartService;
import org.wmy.mall.service.IOrderService;
import org.wmy.mall.vo.OrderItemVo;
import org.wmy.mall.vo.OrderVo;
import org.wmy.mall.vo.ResponseVo;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wmy
 * @create 2020-11-14 18:00
 */

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //1.收货地址校验
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if(shipping == null){
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //2.获取购物车，校验是否存在、库存

        //2.1 获得购物车中被选中的商品项
        List<CartItem> cartItemList = cartService.listForCartItem(uid).stream()
                .filter(CartItem::getProductSelected)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cartItemList)){
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }
        //2.2 获得cartItemList中所有商品的id
        Set<Integer> productIdSet = cartItemList.stream()
                .map(CartItem::getProductId)
                .collect(Collectors.toSet());

        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);

        //此出进行转换时为了方便下面的代码
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();

        for(CartItem cartItem : cartItemList){
            //判断数据库中是否存在该商品
            Product product = map.get(cartItem.getProductId());
            if(product == null){
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        ResponseEnum.PRODUCT_NOT_EXIST.getDesc()+",productId:"+cartItem.getProductId());
            }
            //判断商品的上下架状态
            if(!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())){
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,
                        ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE+":"+product.getName());
            }

            //判读库存是否充足
            if(product.getStock() < cartItem.getQuantity()){
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,
                        ResponseEnum.PRODUCT_STOCK_ERROR.getDesc()+":"+product.getName());
            }

            OrderItem orderItem = buildOrderItem(uid, orderNo, cartItem.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock()-cartItem.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if(row <= 0){
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }

        //3.计算总价，只计算被选中的
        //4.生成订单，入库：order 和 orderItem （事务控制）
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if(rowForOrder <=0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if(rowForOrderItem <=0){
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //5.减库存，放到上面做了
        //6.更新购物车（选中的商品）
        for(CartItem cartItem : cartItemList){
            cartService.delete(uid,cartItem.getProductId());
        }

        //7.构造 OrderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //可利用buildOrderVo方法进行操作

        //获得用户的所有订单
        List<Order> orderList = orderMapper.selectByUid(uid);
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());

        //根据订单号获得用户的所有订单项
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        Set<Integer> shippingSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        //根据订单里的收获地址集合，获得用户的所有收获地址
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingSet);
        //根据orderItemList，获得map集合，集合的key是订单号，集合的值是订单项列表
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order, orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }

        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        HashSet<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //规定，只有订单未支付时才可以取消订单
        if(!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if(row <=0 ){
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc()+"订单id"+orderNo);
        }
        //规定，只有订单未支付时才可以变成已支付
        if(!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc()+"订单id"+orderNo);
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        //最好是从payInfo中取出，但是表设计有问题
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if(row <=0 ){
            throw new RuntimeException("将订单更新为已支付状态失败，订单Id:"+orderNo);
        }

    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream()
                .map(e -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    BeanUtils.copyProperties(e, orderItemVo);
                    return orderItemVo;
                })
                .collect(Collectors.toList());

        orderVo.setOrderItemVoList(orderItemVoList);
        //如果地址被删除了，就查不到了；所以是订单表设计有问题，不应该只保存shippingId，应该保留地址的详细
        if(shipping != null){
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    private Order buildOrder(Integer uid,Long orderNo,Integer shippingId,List<OrderItem> orderItemList) {

        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        //设置支付类型，比如在线支付/货到付款
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        //设置运费
        order.setPostage(0);
        //设置订单状态
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    /**
     *
     *  此出只是为了模拟
     *  企业级的生成方案：分布式唯一id/主键
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis()+new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid,Long orderNo,Integer quantity,Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(uid);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        return orderItem;
    }
}
