package com.lautert.yt_channel.application;

import java.util.Arrays;

import com.lautert.yt_channel.dto.ResponseBody;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ComponentScan
public class SwaggerConfig implements WebMvcConfigurer
{

    @Value("${build.version}")
    private String version;

    @Value("${swagger.enabled:false}")
    private boolean enableSwagger;

    @Bean
    public Docket api () {
        return new Docket(DocumentationType.SWAGGER_2)
            .enable(enableSwagger)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.lautert.yt_channel"))
            .paths(PathSelectors.any())
            .build()
            .pathMapping("/")
            .genericModelSubstitutes(ResponseBody.class)
            .useDefaultResponseMessages(false)
            .globalResponses(
                HttpMethod.GET,
                Arrays.asList(new Response[]
                {
                    new ResponseBuilder()
                        .code("500")
                        .description(
                            "The service cannot complete the request"
                        )
                        .build(),
                    new ResponseBuilder()
                        .code("401")
                        .description(
                            "You do not have permission to execute this action"
                        )
                        .build(),
                    new ResponseBuilder()
                        .code("412")
                        .description(
                            "Invalid parameters, the request cannot be completed."
                        )
                        .build()
                })
            );
    }

    private ApiInfo apiInfo () {
        return new ApiInfoBuilder()
            .title("Youtube Channel API")
            .description(
                "This API is a simple API for register a YouTube Channel and track its videos"
            )
            .version(version)
            .termsOfServiceUrl("http://example.com.br")
            .license("LICENSE")
            .licenseUrl("http://example.com.br")
            .build();
    }

    @Override
    public void addResourceHandlers (
        ResourceHandlerRegistry registry
    ) {

        registry
            .addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry
            .addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
