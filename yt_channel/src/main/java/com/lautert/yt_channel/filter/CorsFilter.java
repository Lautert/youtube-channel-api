package com.lautert.yt_channel.filter;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.lautert.yt_channel.configuration.ChannelProperties;

/**
 * Filtro para suporte a requests "Cross-Origin"
 *
 * @author rafaelboekel
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("dev")
public class CorsFilter implements Filter
{

    private static final Logger LOGGER = LoggerFactory
        .getLogger(CorsFilter.class);

    @PostConstruct
    public void logInit () {
        LOGGER.info("CorsFilter started for 'dev' profile.");
    }

    @Autowired
    private ChannelProperties properties;

    @Override
    public void doFilter (
        ServletRequest req,
        ServletResponse resp,
        FilterChain chain
    )
        throws IOException,
        ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (!response.containsHeader("Access-Control-Allow-Origin"))
        {
            response
                .setHeader(
                    "Access-Control-Allow-Origin",
                    this.properties.getCors().getAllowedOrigin()
                );
        }

        if (!response.containsHeader("Access-Control-Allow-Credentials"))
        {
            response
                .setHeader(
                    "Access-Control-Allow-Credentials",
                    "true"
                );
        }

        if (
            "OPTIONS".equals(request.getMethod()) &&
                this.properties
                    .getCors()
                    .getAllowedOrigin()
                    .equals(request.getHeader("Origin"))
        )
        {

            // adiciona headers extras que devem ser permitidos
            StringBuilder sbAllowedHeaders = new StringBuilder();
            sbAllowedHeaders.append("Authorization, Content-Type, Accept");

            if (
                ArrayUtils
                    .isNotEmpty(this.properties.getCors().getAllowedHeaders())
            )
            {

                for (
                    int i = 0; i < this.properties
                        .getCors()
                        .getAllowedHeaders().length; i++
                )
                {
                    sbAllowedHeaders.append(", ");
                    sbAllowedHeaders
                        .append(
                            this.properties.getCors().getAllowedHeaders()[i]
                        );
                }
            }

            response
                .setHeader(
                    "Access-Control-Allow-Methods",
                    "POST, GET, DELETE, PUT, OPTIONS"
                );
            response
                .setHeader(
                    "Access-Control-Allow-Headers",
                    sbAllowedHeaders.toString()
                );
            response
                .setHeader(
                    "Access-Control-Max-Age",
                    "3600"
                );

            response.setStatus(HttpServletResponse.SC_OK);

        } else
        {
            chain
                .doFilter(
                    req,
                    resp
                );
        }
    }

    @Override
    public void destroy () {}

    @Override
    public void init (
        FilterConfig arg0
    )
        throws ServletException {}

}
