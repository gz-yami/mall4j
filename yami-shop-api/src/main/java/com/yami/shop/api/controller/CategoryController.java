/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */

package com.yami.shop.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.common.response.ServerResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yami.shop.bean.app.dto.CategoryDto;
import com.yami.shop.bean.model.Category;
import com.yami.shop.service.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import ma.glasnost.orika.MapperFacade;

/**
 * @author lanhai
 */
@RestController
@RequestMapping("/category")
@Tag(name = "分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MapperFacade mapperFacade;

    /**
     * 分类信息列表接口
     */
    @GetMapping("/categoryInfo")
    @Operation(summary = "分类信息列表" , description = "获取所有的产品分类信息，顶级分类的parentId为0,默认为顶级分类")
    @Parameter(name = "parentId", description = "分类ID", required = false)
    public ServerResponseEntity<List<CategoryDto>> categoryInfo(@RequestParam(value = "parentId", defaultValue = "0") Long parentId) {
        List<Category> categories = categoryService.listByParentId(parentId);
        List<CategoryDto> categoryDtos = mapperFacade.mapAsList(categories, CategoryDto.class);
        return ServerResponseEntity.success(categoryDtos);
    }


}
