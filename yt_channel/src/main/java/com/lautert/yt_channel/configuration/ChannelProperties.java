package com.lautert.yt_channel.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriedades da aplicação
 *
 * @author rafaelboekel
 *
 */
@ConfigurationProperties("channel")
public class ChannelProperties
{

    private final Cors cors = new Cors();
    private final Rest rest = new Rest();

    public Cors getCors () {
        return cors;
    }

    public Rest getRest () {
        return rest;
    }

    public static class Cors
    {

        private String allowedOrigin;
        private String[] allowedHeaders;

        public String getAllowedOrigin () {
            return allowedOrigin;
        }

        public void setAllowedOrigin (
            String allowedOrigin
        ) {
            this.allowedOrigin = allowedOrigin;
        }

        public String[] getAllowedHeaders () {
            return allowedHeaders;
        }

        public void setAllowedHeaders (
            String[] allowedHeaders
        ) {
            this.allowedHeaders = allowedHeaders;
        }
    }

    public static class Rest
    {

        private int readTimeoutInSeconds;
        private int connectionTimeoutInSeconds;

        public int getReadTimeoutInSeconds () {
            return readTimeoutInSeconds;
        }

        public void setReadTimeoutInSeconds (
            int readTimeoutInSeconds
        ) {
            this.readTimeoutInSeconds = readTimeoutInSeconds;
        }

        public int getConnectionTimeoutInSeconds () {
            return connectionTimeoutInSeconds;
        }

        public void setConnectionTimeoutInSeconds (
            int connectionTimeoutInSeconds
        ) {
            this.connectionTimeoutInSeconds = connectionTimeoutInSeconds;
        }
    }
}
