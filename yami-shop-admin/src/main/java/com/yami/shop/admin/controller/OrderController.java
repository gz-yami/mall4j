/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.admin.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Objects;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.UserAddrOrder;
import com.yami.shop.bean.param.DeliveryOrderParam;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.admin.util.SecurityUtils;
import com.yami.shop.service.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author lgh on 2018/09/15.
 */
@Slf4j
@RestController
@RequestMapping("/order/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserAddrOrderService userAddrOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SkuService skuService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('order:order:page')")
    public ServerResponseEntity<IPage<Order>> page(OrderParam orderParam,PageParam<Order> page) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        orderParam.setShopId(shopId);
        IPage<Order> orderPage = orderService.pageOrdersDetailByOrderParam(page, orderParam);
        return ServerResponseEntity.success(orderPage);


    }

    /**
     * 获取信息
     */
    @GetMapping("/orderInfo/{orderNumber}")
    @PreAuthorize("@pms.hasPermission('order:order:info')")
    public ServerResponseEntity<Order> info(@PathVariable("orderNumber") String orderNumber) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        if (!Objects.equal(shopId, order.getShopId())) {
            throw new YamiShopBindException("您没有权限获取该订单信息");
        }
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber);
        order.setOrderItems(orderItems);
        UserAddrOrder userAddrOrder = userAddrOrderService.getById(order.getAddrOrderId());
        order.setUserAddrOrder(userAddrOrder);
        return ServerResponseEntity.success(order);
    }

    /**
     * 发货
     */
    @PutMapping("/delivery")
    @PreAuthorize("@pms.hasPermission('order:order:delivery')")
    public ServerResponseEntity<Void> delivery(@RequestBody DeliveryOrderParam deliveryOrderParam) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        Order order = orderService.getOrderByOrderNumber(deliveryOrderParam.getOrderNumber());
        if (!Objects.equal(shopId, order.getShopId())) {
            throw new YamiShopBindException("您没有权限修改该订单信息");
        }

        Order orderParam = new Order();
        orderParam.setOrderId(order.getOrderId());
        orderParam.setDvyId(deliveryOrderParam.getDvyId());
        orderParam.setDvyFlowId(deliveryOrderParam.getDvyFlowId());
        orderParam.setDvyTime(new Date());
        orderParam.setStatus(OrderStatus.CONSIGNMENT.value());
        orderParam.setUserId(order.getUserId());

        orderService.delivery(orderParam);

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(deliveryOrderParam.getOrderNumber());
        for (OrderItem orderItem : orderItems) {
            productService.removeProductCacheByProdId(orderItem.getProdId());
            skuService.removeSkuCacheBySkuId(orderItem.getSkuId(),orderItem.getProdId());
        }
        return ServerResponseEntity.success();
    }

    /**
     * 打印待发货的订单表
     *
     * @param order
     * @param consignmentName   发件人姓名
     * @param consignmentMobile 发货人手机号
     * @param consignmentAddr   发货地址
     */
    @GetMapping("/waitingConsignmentExcel")
    @PreAuthorize("@pms.hasPermission('order:order:waitingConsignmentExcel')")
    public void waitingConsignmentExcel(Order order, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime, String consignmentName, String consignmentMobile,
                                        String consignmentAddr, HttpServletResponse response) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        order.setShopId(shopId);
        order.setStatus(OrderStatus.PADYED.value());
        List<Order> orders = orderService.listOrdersDetailByOrder(order, startTime, endTime);

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getBigWriter();
        Sheet sheet = writer.getSheet();
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 60 * 256);
        sheet.setColumnWidth(4, 60 * 256);
        sheet.setColumnWidth(7, 60 * 256);
        sheet.setColumnWidth(8, 60 * 256);
        sheet.setColumnWidth(9, 60 * 256);
        // 待发货
        String[] hearder = {"订单编号", "收件人", "手机", "收货地址", "商品名称", "数量", "发件人姓名", "发件人手机号", "发货地址", "备注"};
        writer.merge(hearder.length - 1, "发货信息整理");
        writer.writeRow(Arrays.asList(hearder));

        int row = 1;
        for (Order dbOrder : orders) {
            UserAddrOrder addr = dbOrder.getUserAddrOrder();
            String addrInfo = addr.getProvince() + addr.getCity() + addr.getArea() + addr.getAddr();
            List<OrderItem> orderItems = dbOrder.getOrderItems();
            row++;
            for (OrderItem orderItem : orderItems) {
                // 第0列开始
                int col = 0;
                writer.writeCellValue(col++, row, dbOrder.getOrderNumber());
                writer.writeCellValue(col++, row, addr.getReceiver());
                writer.writeCellValue(col++, row, addr.getMobile());
                writer.writeCellValue(col++, row, addrInfo);
                writer.writeCellValue(col++, row, orderItem.getProdName());
                writer.writeCellValue(col++, row, orderItem.getProdCount());
                writer.writeCellValue(col++, row, consignmentName);
                writer.writeCellValue(col++, row, consignmentMobile);
                writer.writeCellValue(col++, row, consignmentAddr);
                writer.writeCellValue(col++, row, dbOrder.getRemarks());
            }
        }
        writeExcel(response, writer);
    }

    /**
     * 已销售订单
     *
     * @param order
     */
    @GetMapping("/soldExcel")
    @PreAuthorize("@pms.hasPermission('order:order:soldExcel')")
    public void soldExcel(Order order, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime, HttpServletResponse response) {
        Long shopId = SecurityUtils.getSysUser().getShopId();
        order.setShopId(shopId);
        order.setIsPayed(1);
        List<Order> orders = orderService.listOrdersDetailByOrder(order, startTime, endTime);

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getBigWriter();
        // 待发货
        String[] hearder = {"订单编号", "下单时间", "收件人", "手机", "收货地址", "商品名称", "数量", "订单应付", "订单运费", "订单实付"};
        Sheet sheet = writer.getSheet();
        sheet.setColumnWidth(0, 20 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 60 * 256);
        sheet.setColumnWidth(5, 60 * 256);

        writer.merge(hearder.length - 1, "销售信息整理");
        writer.writeRow(Arrays.asList(hearder));

        int row = 1;
        for (Order dbOrder : orders) {
            UserAddrOrder addr = dbOrder.getUserAddrOrder();
            String addrInfo = addr.getProvince() + addr.getCity() + addr.getArea() + addr.getAddr();
            List<OrderItem> orderItems = dbOrder.getOrderItems();
            int firstRow = row + 1;
            int lastRow = row + orderItems.size();
            int col = -1;
            // 订单编号
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, dbOrder.getOrderNumber());
            // 下单时间
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, dbOrder.getCreateTime());
            // 收件人
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, addr.getReceiver());
            // "手机"
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, addr.getMobile());
            // "收货地址"
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, addrInfo);
            int prodNameCol = ++col;
            int prodCountCol = ++col;
            for (OrderItem orderItem : orderItems) {
                row++;
                // 商品名称
                writer.writeCellValue(prodNameCol, row, orderItem.getProdName());
                // 数量
                writer.writeCellValue(prodCountCol, row, orderItem.getProdCount());
            }
            // 订单应付
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, dbOrder.getTotal());
            // 订单运费
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, dbOrder.getFreightAmount());
            // 订单实付
            mergeIfNeed(writer, firstRow, lastRow, ++col, col, dbOrder.getActualTotal());

        }
        writeExcel(response, writer);
    }

    /**
     * 如果需要合并的话，就合并
     */
    private void mergeIfNeed(ExcelWriter writer, int firstRow, int lastRow, int firstColumn, int lastColumn, Object content) {
        if (content instanceof Date) {
            content = DateUtil.format((Date) content, DatePattern.NORM_DATETIME_PATTERN);
        }
        if (lastRow - firstRow > 0 || lastColumn - firstColumn > 0) {
            writer.merge(firstRow, lastRow, firstColumn, lastColumn, content, false);
        } else {
            writer.writeCellValue(firstColumn, firstRow, content);
        }

    }

    private void writeExcel(HttpServletResponse response, ExcelWriter writer) {
        //response为HttpServletResponse对象
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=1.xls");

        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            writer.flush(servletOutputStream);
            servletOutputStream.flush();
        } catch (IORuntimeException | IOException e) {
            log.error("写出Excel错误：", e);
        } finally {
            IoUtil.close(writer);
        }
    }
}
