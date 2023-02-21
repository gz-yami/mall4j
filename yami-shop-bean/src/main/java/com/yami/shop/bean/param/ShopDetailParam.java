/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.bean.param;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author lanhai
 */
public class ShopDetailParam {
	

	private Long shopId;
	
    /**
     * 店铺名称(数字、中文，英文(可混合，不可有特殊字符)，可修改)、不唯一
     */
	@NotBlank(message="店铺名称不能为空")
	@Size(max = 50,message = "商品名称长度应该小于{max}")
    private String shopName;

    /**
     * 店铺简介(可修改)
     */
	@Size(max = 200,message = "店铺简介长度应该小于{max}")
    private String intro;

    /**
     * 店铺公告(可修改)
     */
	@Size(max = 50,message = "店铺公告长度应该小于{max}")
    private String shopNotice;

    /**
     * 店铺联系电话
     */
	@NotBlank(message="店铺联系电话不能为空")
	@Size(max = 20,message = "店铺公告长度应该小于{max}")
    private String tel;

    /**
     * 店铺详细地址
     */
	@NotBlank(message="店铺详细地址不能为空")
	@Size(max = 100,message = "店铺公告长度应该小于{max}")
    private String shopAddress;

    /**
     * 店铺所在省份（描述）
     */
	@NotBlank(message="店铺所在省份不能为空")
	@Size(max = 10,message = "店铺所在省份长度应该小于{max}")
    private String province;

    /**
     * 店铺所在城市（描述）
     */
	@NotBlank(message="店铺所在城市不能为空")
	@Size(max = 10,message = "店铺所在城市长度应该小于{max}")
    private String city;

    /**
     * 店铺所在区域（描述）
     */
	@NotBlank(message="店铺所在区域不能为空")
	@Size(max = 10,message = "店铺所在省份区域长度应该小于{max}")
    private String area;
    
    /**
     * 店铺省市区代码，用于回显
     */
	@NotBlank(message="店铺省市区代码不能为空")
	@Size(max = 20,message = "店铺省市区代码长度应该小于{max}")
    private String pcaCode;
    
    /**
     * 店铺logo(可修改)
     */
	@NotBlank(message="店铺logo不能为空")
	@Size(max = 200,message = "店铺logo长度应该小于{max}")
    private String shopLogo;

    /**
     * 店铺相册
     */
	@Size(max = 1000,message = "店铺相册长度应该小于{max}")
    private String shopPhotos;

    /**
     * 每天营业时间段(可修改)
     */
	@NotBlank(message="每天营业时间段不能为空")
	@Size(max = 100,message = "每天营业时间段长度应该小于{max}")
    private String openTime;
	
	/**
     * 店铺状态(-1:未开通 0: 停业中 1:营业中)，可修改
     */
    private Integer shopStatus;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getShopNotice() {
		return shopNotice;
	}

	public void setShopNotice(String shopNotice) {
		this.shopNotice = shopNotice;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
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

	public String getPcaCode() {
		return pcaCode;
	}

	public void setPcaCode(String pcaCode) {
		this.pcaCode = pcaCode;
	}

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	public String getShopPhotos() {
		return shopPhotos;
	}

	public void setShopPhotos(String shopPhotos) {
		this.shopPhotos = shopPhotos;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public Integer getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

}
