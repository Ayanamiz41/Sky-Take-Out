package com.sky.service;


import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /**
     * 提交订单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 订单分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id获取菜品信息字符串
     * @param id
     * @return
     */
    String getOrderDishesStr(Long id);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO getStatistics();

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO getDetails(Long id);

    /**
     * 取消订单
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO)throws  Exception;

    /**
     * 拒绝订单
     * @param ordersRejectionDTO
     * @return
     */
    void reject(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 接单
     * @param ordersConfirmDTO
     * @return
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 派送
     * @param id
     * @return
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id
     * @return
     */
    void complete(Long id);

    /**
     * 根据id查询地址簿，并返回地址串
     * @param id
     * @return
     */
    String getAddressStr(Long id);

    /**
     * 查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult getHistoryOrders(String page, String pageSize, String status);

    /**
     * 根据订单获取订单VO
     * @param ordersList
     * @return
     */
    List<OrderVO> getOrderVOList(List<Orders> ordersList);

    /**
     * 再来一单
     * @param id
     * @return
     */
    void repetition(Long id);

    /**
     * 根据id取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 客户催单
     * @param id
     * @return
     */
    void reminder(Long id);
}
