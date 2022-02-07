package com.lautert.yt_channel.api.youtube;

import java.io.IOException;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class YoutubeAPI
{
    private static YouTube youtube;

    private String apiKey;
    private String applicationName;

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Autowired
    public YoutubeAPI (
        @Value("${ytchannel.google.youtube.credentials}") String apiKey,
        @Value("${ytchannel.application.name}") String applicationName
    ) {
        this.apiKey = apiKey;
        this.applicationName = applicationName;
    }

    // Create a singleton YouTube object
    public YouTube getYouTubeAPI () {
        if (youtube == null) {
            startYouTube();
        }
        return youtube;
    }

    private void startYouTube () {
        youtube = new YouTube.Builder(
            HTTP_TRANSPORT,
            JSON_FACTORY,
            new HttpRequestInitializer() {
                public void initialize (
                    HttpRequest request
                )
                    throws IOException {}
            }
        )
            .setApplicationName(applicationName)
            .setYouTubeRequestInitializer(
                new YouTubeRequestInitializer(
                    apiKey
                )
            )
            .build();
    }
}
