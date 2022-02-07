package com.lautert.yt_channel.api.youtube;

import java.io.IOException;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;

import org.springframework.beans.factory.annotation.Value;

public class YoutubeChannelAPI
{

    private YouTube youTubeAPI;

    @Value("${ytchannel.search.max-results}")
    private long maxResults;

    public YoutubeChannelAPI (
        YouTube youTubeAPI
    ) {
        this.youTubeAPI = youTubeAPI;
    }

    public ChannelListResponse getChannelDetailsByUserName (
        String userName, String pageToken
    ) {
        try
        {
            YouTube.Channels.List search = youTubeAPI
                .channels()
                .list("id,snippet,brandingSettings,contentDetails,statistics,topicDetails");
            search.setForUsername(userName);
            if(pageToken != null)
            {
                search.setPageToken(pageToken);
            }

            return search.execute();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Channel getChannelDetailsByChannelId (
        String channelId
    ) {
        try
        {
            YouTube.Channels.List search = youTubeAPI
                .channels()
                .list("id,snippet,brandingSettings,contentDetails,statistics,topicDetails");
            search.setId(channelId);

            return search.execute().getItems().get(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
