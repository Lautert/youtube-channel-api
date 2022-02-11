package com.lautert.yt_channel.dto;

import java.math.BigInteger;
import java.util.Date;

public class YoutubeChannelTrackInfoDTO
{
    private BigInteger cdYoutubeChannel;
    private String channelId;
    private String youtubeChannelName;
    private Integer totalVideos;
    private Date dateRegistry;
    private Date dateLastUpdate;
    private Boolean trackFinished;

    public YoutubeChannelTrackInfoDTO (
        BigInteger cdYoutubeChannel,
        String channelId,
        String youtubeChannelName,
        Date dateRegistry,
        Date dateLastUpdate,
        Boolean trackFinished,
        Integer totalVideos
    ) {
        this.cdYoutubeChannel = cdYoutubeChannel;
        this.channelId = channelId;
        this.youtubeChannelName = youtubeChannelName;
        this.totalVideos = totalVideos;
        this.dateRegistry = dateRegistry;
        this.dateLastUpdate = dateLastUpdate;
        this.trackFinished = trackFinished;
    }

    public BigInteger getCdYoutubeChannel() {
        return this.cdYoutubeChannel;
    }

    public void setCdYoutubeChannel(BigInteger cdYoutubeChannel) {
        this.cdYoutubeChannel = cdYoutubeChannel;
    }

    public String getChannelId() {
        return this.channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getYoutubeChannelName() {
        return this.youtubeChannelName;
    }

    public void setYoutubeChannelName(String youtubeChannelName) {
        this.youtubeChannelName = youtubeChannelName;
    }

    public Integer getTotalVideos() {
        return this.totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public Date getDateRegistry() {
        return this.dateRegistry;
    }

    public void setDateRegistry(Date dateRegistry) {
        this.dateRegistry = dateRegistry;
    }

    public Date getDateLastUpdate() {
        return this.dateLastUpdate;
    }

    public void setDateLastUpdate(Date dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
    }

    public Boolean isTrackFinished() {
        return this.trackFinished;
    }

    public Boolean getTrackFinished() {
        return this.trackFinished;
    }

    public void setTrackFinished(Boolean trackFinished) {
        this.trackFinished = trackFinished;
    }
}
