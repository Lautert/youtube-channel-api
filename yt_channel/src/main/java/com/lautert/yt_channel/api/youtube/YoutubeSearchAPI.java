package com.lautert.yt_channel.api.youtube;

import java.io.IOException;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

public class YoutubeSearchAPI
{

    private YouTube youTubeAPI;

    public YoutubeSearchAPI (
        YouTube youTubeAPI
    ) {
        this.youTubeAPI = youTubeAPI;
    }

    public SearchListResponse searchVideosByChannel (
        String channelId,
        String pageToken,
        long maxResults
    ) {
        try
        {
            YouTube.Search.List search = youTubeAPI.search().list("id, snippet");

            search.setChannelId(channelId);
            search.setOrder("date");
            search.setMaxResults(maxResults);

            if (pageToken != null)
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
}
