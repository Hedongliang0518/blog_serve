package com.hdl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.hdl.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("hdl", "xxxxxxxxxxx", "my@my.com");
        return new ApiInfoBuilder()
                .title("文档标题1")
                .description("文档描述2")
                .contact(contact)   // 联系方式
                .version("1.1.1")  // 版本
                .build();
    }
}