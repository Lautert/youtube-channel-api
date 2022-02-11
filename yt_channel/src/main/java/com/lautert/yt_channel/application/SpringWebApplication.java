package com.lautert.yt_channel.application;

import com.lautert.yt_channel.configuration.ChannelProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages =
{
    "com.lautert.yt_channel"
})
@EnableConfigurationProperties(ChannelProperties.class)
@EnableScheduling
public class SpringWebApplication extends SpringBootServletInitializer
{

    public static void main (
        String[] args
    ) {
        SpringApplication
            .run(
                SpringWebApplication.class,
                args
            );
    }

    @Override
    protected SpringApplicationBuilder configure (
        SpringApplicationBuilder application
    ) {
        return application.sources(SpringWebApplication.class);
    }
}
