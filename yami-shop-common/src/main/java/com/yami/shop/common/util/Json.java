/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.util;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.core.json.JsonWriteFeature;
import tools.jackson.databind.*;
import tools.jackson.core.JacksonException;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author lanhai
 */
@Slf4j
public class Json {

	public static JsonMapper.Builder newBaseBuilder() {
		return JsonMapper.builder()
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				// 兼容 Jackson 2 的宽松行为，允许 null 映射到基础类型并使用其默认值。
				.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
				.disable(SerializationFeature.FAIL_ON_SELF_REFERENCES)
				.enable(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL)
				.disable(EnumFeature.FAIL_ON_NUMBERS_FOR_ENUMS)
				.changeDefaultVisibility(vc -> vc
						.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
						.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
						.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
						.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
						.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
		// 如果你想尽量贴近 Jackson 2 默认行为，可再补：
		// .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
	}

	/**
	 * 普通用途：深拷贝、parseObject、mapAsList
	 */
	private static final ObjectMapper objectMapper =
			newBaseBuilder().build();

	/**
	 * 对象转json
	 * @param object
	 * @return
	 */
	public static String toJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JacksonException e) {
			log.error("对象转json错误：", e);
		}
		return null;
	}

	/**
	 * json转换换成对象
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseObject(String json, Class<T> clazz) {
		T result = null;
		try {
			result = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("对象转json错误：", e);
		}
		return result;
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}


	/**
	 * 	 * https://stackoverflow.com/questions/6349421/how-to-use-jackson-to-deserialise-an-array-of-objects
	 * 	 * List<MyClass> myObjects = Arrays.asList(mapper.readValue(json, MyClass[].class))
	 * 	 * works up to 10 time faster than TypeRefence.
	 * @return
	 */
	public static <T> List<T> parseArray(String json, Class<T[]> clazz){
		T[] result = null;
		try {
			result = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			log.error("Json转换错误：", e);
		}
		if (result == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(result);
	}


	/**
	 * 转换成json节点，即map
	 * @param jsonStr
	 * @return
	 */
	public static JsonNode parseJson(String jsonStr) {
		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(jsonStr);
		} catch (Exception e) {
			log.error("Json转换错误：", e);
		}
		return jsonNode;
	}
}
