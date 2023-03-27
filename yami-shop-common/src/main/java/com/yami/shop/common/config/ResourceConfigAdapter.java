package com.yami.shop.common.config;

import com.yami.shop.common.util.ImgUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TRACK
 */
@Configuration
public class ResourceConfigAdapter implements WebMvcConfigurer {

    @Autowired
    private ImgUploadUtil imgUploadUtil;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/mall4j/img/**").addResourceLocations("file:" + imgUploadUtil.getUploadPath());
    }
}
