/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.app.param;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

/**
 * @author lanhai
 */
@Schema(description = "地址参数")
public class AddrParam {

	@Schema(description = "地址ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private Long addrId;

	@NotNull(message = "收货人不能为空")
	@Schema(description = "收货人" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String receiver;

	@NotNull(message = "地址不能为空")
	@Schema(description = "地址" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String addr;

	@Schema(description = "邮编" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String postCode;

	@NotNull(message = "手机不能为空")
	@Schema(description = "手机" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String mobile;

	@NotNull(message = "省ID不能为空")
	@Schema(description = "省ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private Long provinceId;

	@NotNull(message = "城市ID不能为空")
	@Schema(description = "城市ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private Long cityId;

	@NotNull(message = "区ID不能为空")
	@Schema(description = "区ID" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private Long areaId;

	@NotNull(message = "省不能为空")
	@Schema(description = "省" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String province;

	@NotNull(message = "城市不能为空")
	@Schema(description = "城市" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String city;

	@NotNull(message = "区不能为空")
	@Schema(description = "区" ,requiredMode = Schema.RequiredMode.REQUIRED)
	private String area;


	public Long getAddrId() {
		return addrId;
	}

	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

}
