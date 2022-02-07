package com.lautert.yt_channel.dto;

import java.util.Date;

public class YoutubeVideoInfoDTO
{
    private String videoId;
    private String videoTitle;
    private String videoDescription;
    private Date videoPublishedAt;
    private String videoUrl;

    public YoutubeVideoInfoDTO (
        String videoId,
        String videoTitle,
        String videoDescription,
        Date videoPublishedAt
    ) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoDescription = videoDescription;
        this.videoPublishedAt = videoPublishedAt;
        this.videoUrl = "https://www.youtube.com/watch?v=" + videoId;
    }

    public String getVideoId () {
        return this.videoId;
    }

    public void setVideoId (
        String videoId
    ) {
        this.videoId = videoId;
    }

    public String getVideoTitle () {
        return this.videoTitle;
    }

    public void setVideoTitle (
        String videoTitle
    ) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription () {
        return this.videoDescription;
    }

    public void setVideoDescription (
        String videoDescription
    ) {
        this.videoDescription = videoDescription;
    }

    public Date getVideoPublishedAt () {
        return this.videoPublishedAt;
    }

    public void setVideoPublishedAt (
        Date videoPublishedAt
    ) {
        this.videoPublishedAt = videoPublishedAt;
    }

    public String getVideoUrl () {
        return this.videoUrl;
    }

    public void setVideoUrl (
        String videoUrl
    ) {
        this.videoUrl = videoUrl;
    }
}
