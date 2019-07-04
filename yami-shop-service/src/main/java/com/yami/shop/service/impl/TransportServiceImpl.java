/*
 * Copyright (c) 2018-2999 广州亚米信息科技有限公司 All rights reserved.
 *
 * https://www.gz-yami.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yami.shop.bean.model.Area;
import com.yami.shop.bean.model.Transcity;
import com.yami.shop.bean.model.TranscityFree;
import com.yami.shop.bean.model.Transfee;
import com.yami.shop.bean.model.TransfeeFree;
import com.yami.shop.bean.model.Transport;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.dao.TranscityMapper;
import com.yami.shop.dao.TransfeeMapper;
import com.yami.shop.dao.TransportMapper;
import com.yami.shop.service.TransportService;

import cn.hutool.core.collection.CollectionUtil;

/**
 *
 * @author lgh on 2018/11/16.
 */
@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements TransportService {

    @Autowired
    private TransportMapper transportMapper;

    @Autowired
    private TransfeeMapper transfeeMapper;

    @Autowired
    private TranscityMapper transcityMapper;

    @Autowired
    private TransportService transportService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertTransportAndTransfee(Transport transport) {

		// 插入运费模板
		transportMapper.insert(transport);
		// 插入所有的运费项和城市
		insertTransfeeAndTranscity(transport);

		// 插入所有的指定包邮条件项和城市
		if (transport.getHasFreeCondition() == 1) {
			insertTransfeeFreeAndTranscityFree(transport);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = "TransportAndAllItems", key = "#transport.transportId")
	public void updateTransportAndTransfee(Transport transport) {
		Transport dbTransport = transportService.getTransportAndAllItems(transport.getTransportId());

		// 删除所有的运费项
		transfeeMapper.deleteByTransportId(transport.getTransportId());
		// 删除所有的指定包邮条件项
		transfeeMapper.deleteTransfeeFreesByTransportId(transport.getTransportId());

		List<Long> transfeeIds = dbTransport.getTransfees().stream().map(Transfee::getTransfeeId).collect(Collectors.toList());
		List<Long> transfeeFreeIds = dbTransport.getTransfeeFrees().stream().map(TransfeeFree::getTransfeeFreeId).collect(Collectors.toList());


		// 删除所有运费项包含的城市
		transcityMapper.deleteBatchByTransfeeIds(transfeeIds);
		if(CollectionUtil.isNotEmpty(transfeeFreeIds)) {
			// 删除所有指定包邮条件项包含的城市
			transcityMapper.deleteBatchByTransfeeFreeIds(transfeeFreeIds);
		}

		// 更新运费模板
		transportMapper.updateById(transport);

		// 插入所有的运费项和城市
		insertTransfeeAndTranscity(transport);
		// 插入所有的指定包邮条件项和城市
		if (transport.getHasFreeCondition() == 1) {
			insertTransfeeFreeAndTranscityFree(transport);
		}
	}


	private void insertTransfeeFreeAndTranscityFree(Transport transport) {
		Long transportId = transport.getTransportId();
		List<TransfeeFree> transfeeFrees = transport.getTransfeeFrees();
		for (TransfeeFree transfeeFree : transfeeFrees) {
			transfeeFree.setTransportId(transportId);
		}
		// 批量插入指定包邮条件项 并返回指定包邮条件项 id，供下面循环使用
		transfeeMapper.insertTransfeeFrees(transfeeFrees);

		List<TranscityFree> transcityFrees = new ArrayList<>();
		for (TransfeeFree transfeeFree : transfeeFrees) {
			List<Area> cityList = transfeeFree.getFreeCityList();
			if (CollectionUtil.isEmpty(cityList)) {
				throw new YamiShopBindException("请选择指定包邮城市");
			}
			// 当地址不为空时
			for (Area area : cityList) {
				TranscityFree transcityParam = new TranscityFree();
				transcityParam.setTransfeeFreeId(transfeeFree.getTransfeeFreeId());
				transcityParam.setFreeCityId(area.getAreaId());
				transcityFrees.add(transcityParam);
			}
		}

		// 批量插入指定包邮条件项中的城市
		if (CollectionUtil.isNotEmpty(transcityFrees)) {
			transcityMapper.insertTranscityFrees(transcityFrees);
		}
	}

	private void insertTransfeeAndTranscity(Transport transport) {
		Long transportId = transport.getTransportId();
		List<Transfee> transfees = transport.getTransfees();
		for (Transfee transfee : transfees) {
			transfee.setTransportId(transportId);
		}
		// 批量插入运费项 并返回运费项id，供下面循环使用
		transfeeMapper.insertTransfees(transfees);

		List<Transcity> transcities = new ArrayList<>();
		for (Transfee transfee : transfees) {
			List<Area> cityList = transfee.getCityList();
			if (CollectionUtil.isEmpty(cityList)) {
				continue;
			}
			// 当地址不为空时
			for (Area area : cityList) {
				Transcity transcityParam = new Transcity();
				transcityParam.setTransfeeId(transfee.getTransfeeId());
				transcityParam.setCityId(area.getAreaId());
				transcities.add(transcityParam);
			}
		}

		// 批量插入运费项中的城市
		if (CollectionUtil.isNotEmpty(transcities)) {
			transcityMapper.insertTranscities(transcities);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteTransportAndTransfeeAndTranscity(Long[] ids) {


		for (Long id : ids) {
			Transport dbTransport = transportService.getTransportAndAllItems(id);
			List<Long> transfeeIds = dbTransport.getTransfees().stream().map(Transfee::getTransfeeId).collect(Collectors.toList());
			// 删除所有运费项包含的城市
			transcityMapper.deleteBatchByTransfeeIds(transfeeIds);
			// 删除所有的运费项
			transfeeMapper.deleteByTransportId(id);
		}
		// 删除运费模板
		transportMapper.deleteTransports(ids);
	}


	@Override
	@Cacheable(cacheNames = "TransportAndAllItems", key = "#transportId")
	public Transport getTransportAndAllItems(Long transportId) {
		Transport transport = transportMapper.getTransportAndTransfeeAndTranscity(transportId);
		List<TransfeeFree> transfeeFrees = transportMapper.getTransfeeFreeAndTranscityFreeByTransportId(transportId);
		transport.setTransfeeFrees(transfeeFrees);
		return transport;
	}
}
