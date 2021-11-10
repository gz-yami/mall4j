/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.common.serializer.springfox;

import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import springfox.documentation.spring.web.json.JacksonModuleRegistrar;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;

import java.util.List;

/**
 * 自定义Swagger 的序列化，去除分页参数中的records值
 * @author LGH
 */
public class SpringfoxJsonSerializer extends JsonSerializer {

    public SpringfoxJsonSerializer(List<JacksonModuleRegistrar> modules) {
        super(modules);
    }

    @Override
    public Json toJson(Object toSerialize) {
        if (!(toSerialize instanceof Swagger)) {
            return super.toJson(toSerialize);
        }
        Swagger swagger = (Swagger)toSerialize;

        swagger.getPaths().forEach((key, path) ->{
            Operation get = path.getGet();
            if (get != null) {

                List<Parameter> parameters = get.getParameters();
                if (parameters != null) {
                    parameters.removeIf(parameter -> parameter.getName().startsWith("records[0]."));
                }
            }
        });

        return super.toJson(swagger);
//        return super.toJson(toSerialize);
    }
}
