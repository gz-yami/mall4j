/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.enums;

/**
 * @author lanhai
 */
public enum SmsType {

	/**
	 *发送验证码
	 */
	VALID(0,"SMS_152288010","感谢您对xxx的支持。您的验证码是${code}，请勿把验证码泄漏给第三方。"),
	
	/**
	 * 商品库存不足通知
	 */
	STOCKS_ARM(1,"SMS_152288054","尊敬的${name}，感谢您对xxx的支持。您的${prodName}库存仅剩${num}，为免影响您的客户下单，请及时补货！"),
	
	/**
	 * 行业客户审核通过通知
	 */
	DISTRIBUTOR_PASS_AUDIT(2,"SMS_152283207","尊敬的${name}，感谢您对xxx的支持。您提交的资料已审核通过！祝您购物愉快!"),
	
	/**
	 * 分销商订单购买成功通知
	 */
	DISTRIBUTOR_BUY_SUCCESS(3,"SMS_152283148","尊敬的${name}，感谢您对xxx的支持。您的订单${orderNumber}已确认成功，我们会尽快发货！"),
	
	/**
	 * 用户发货通知
	 */
	NOTIFY_DVY(4,"SMS_152283152","尊敬的${name}，感谢您对xxx的支持。您的订单${orderNumber}已通过${dvyName}发货，快递单号是：${dvyFlowId}。请注意查收。"),
	
	/**
	 * 代理商审核通知
	 */
	AGENT_PASS_AUDIT(5,"SMS_152288028","尊敬的${name}，感谢您对xxx的支持。您提交的代理商申请已审核通过！请重新登陆获取新的会员信息。"),
	
	/**
	 * 代理商商品被购买通知
	 */
	NOTIFY_BUY(6,"SMS_152288372","尊敬的${name}，感谢您对xxx的支持。与您有关联的订单${orderNumber}已生成，客户${buyerName}，提货${prodName}，数量${buyNum}，剩余库存为${stockNum}。"),
	
	/**
	 * 代理商新增分销商通知
	 */
	ADD_DISTRIBUTOR(7,"SMS_152283192","尊敬的${name}，感谢您对xxx的支持。您有新增绑定客户资料生成：客户${userName}，联系方式${mobilePhone}，联系地址${addr}，合计共有客户${number}名。"),

	/**
	 * 代理商解除与分销商合作通知
	 */
	UNBINDING(8,"SMS_152283198","尊敬的${name}，感谢您对xxx的支持。您已成功解除与此客户的绑定关系：客户${userName}，联系方式${mobilePhone}，联系地址${addr}，现合计共有客户${number}名。"),
	
	/**
	 * 代理商补货订单模板
	 */
	AGENT_BUY_SUCCESS(9,"SMS_152288475","尊敬的${name}，感谢您对xxx的支持。您的补货订单${orderNumber}已完成入库，${prodName}剩余库存为${stockNum}。"),
	
	/**
	 * 普通用户下单成功通知
	 */
	USER_BUY_SUCCESS(10,"SMS_152288329","亲爱的客户，感谢您对xxx的支持。您的订单${orderNumber}已支付成功。我们会尽快发货!")
	
	;
	
	private Integer num;
	
	private String templateCode;
	
	private String content;
	public Integer value() {
		return num;
	}
	
	SmsType(Integer num,String templateCode,String content){
		this.num = num;
		this.templateCode = templateCode;
		this.content = content;
	}
	
	public static SmsType instance(Integer value) {
		SmsType[] enums = values();
		for (SmsType statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public String getTemplateCode() {
		return this.templateCode;
	}

	public String getContent() {
		return this.content;
	}
}
