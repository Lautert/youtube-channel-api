package com.lautert.yt_channel.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.Gson;
import com.lautert.yt_channel.api.youtube.YoutubeAPI;
import com.lautert.yt_channel.api.youtube.YoutubeChannelAPI;
import com.lautert.yt_channel.api.youtube.YoutubeSearchAPI;
import com.lautert.yt_channel.dto.ResponseDataListYoutubeChannelTrackDTO;
import com.lautert.yt_channel.dto.ResponseDataListYoutubeVideoInfoDTO;
import com.lautert.yt_channel.dto.YoutubeChannelTrackInfoDTO;
import com.lautert.yt_channel.dto.YoutubeVideoInfoDTO;
import com.lautert.yt_channel.exception.MessageUserException;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelEntity;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelVideoEntity;
import com.lautert.yt_channel.repository.yt_channel.YoutubeChannelRepository;
import com.lautert.yt_channel.repository.yt_channel.YoutubeChannelVideoRepository;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

/**
 * Youtube Channel Service
 *
 * @author Guilherme Lautert
 *
 *         SRP: Manage the bussines rules to handle Youtube Channel information
 *         and provide methods for the consumer the information
 *         Objectives:
 *         - Get a List of Youtube Channel Ids by UserName
 *         - Add a ChannelId to trackScan
 *         - Start the scheduler trackScan to get information about the channel
 *         and videos
 */
@Service
public class YoutubeApiService
{

    private Logger logger = LoggerFactory.getLogger(YoutubeApiService.class);

    @Autowired
    private YoutubeAPI youTubeAPI;

    @Autowired
    private YoutubeChannelRepository youtubeChannelRepository;

    @Autowired
    private YoutubeChannelVideoRepository youtubeChannelVideoRepository;

    @Value("${ytchannel.search.max-results}")
    private long maxResultsToSearch;

    // SRP: Get a List of Youtube Channel Ids by UserName
    public List<String> getListChannelIdByUserName (
        String channelUserName
    ) {

        YouTube youtubeApi = youTubeAPI.getYouTubeAPI();
        String pageToken = null;

        YoutubeChannelAPI youTubeChannel = new YoutubeChannelAPI(youtubeApi);

        List<String> channelIds = new ArrayList<>();
        ChannelListResponse listChannel = null;

        do
        {
            listChannel = youTubeChannel
                .getChannelDetailsByUserName(
                    channelUserName,
                    pageToken
                );

            if (
                listChannel != null
                    && listChannel.getPageInfo().getTotalResults() > 0
            )
            {
                for (Channel channel : listChannel.getItems())
                {
                    channelIds.add(channel.getId());
                }

                pageToken = listChannel.getNextPageToken();
            }
        }
        while (listChannel != null && pageToken != null);

        return channelIds;
    }

    // SRP: Add a ChannelId to YoutubeChannelEntity that will be tracked
    @Transactional
    public BigInteger addChannelToTrackScan (
        String channelId
    )
        throws MessageUserException {
        if (channelId == null)
        {
            throw new MessageUserException("Invalid Parameter channelId", 400);
        }

        YoutubeChannelEntity channel = this.youtubeChannelRepository
            .findByDsChannelId(channelId);

        try
        {
            if (channel == null)
            {
                channel = new YoutubeChannelEntity();
                channel.setDsChannelId(channelId);

                this.youtubeChannelRepository.save(channel);
            } else
            {
                channel.setBlCompleted(false);
                this.youtubeChannelRepository.save(channel);
            }
        }
        catch (DataIntegrityViolationException e)
        {
            if (
                ExceptionUtils
                    .getRootCause(e)
                    .getMessage()
                    .matches(".*value too long.*")
            )
            {
                throw new MessageUserException(
                    "Invalid Parameter channelId",
                    400
                );
            } else
            {
                throw e;
            }
        }

        return channel.getCdYoutubeChannel();
    }

    // SRP: Get a list of youtube channel to update and search for videos
    public void startTrackScan () {

        // Get a list of all channels to scan that are not already being scanned
        List<YoutubeChannelEntity> listChannelEntity = this.youtubeChannelRepository
            .findByBlCompletedFalse();

        if (listChannelEntity != null && listChannelEntity.size() > 0)
        {
            YouTube youtubeAPI = youTubeAPI.getYouTubeAPI();
            YoutubeChannelAPI youtubeChannelAPI = new YoutubeChannelAPI(
                youtubeAPI
            );
            YoutubeSearchAPI youtubeSearchAPI = new YoutubeSearchAPI(
                youtubeAPI
            );

            for (YoutubeChannelEntity channelEntity : listChannelEntity)
            {
                Channel ytChannel = youtubeChannelAPI
                    .getChannelDetailsByChannelId(
                        channelEntity.getDsChannelId()
                    );

                if (ytChannel != null)
                {
                    this
                        .updateChannelInformation(
                            ytChannel,
                            channelEntity
                        );
                    this
                        .searchAllVideosByChannel(
                            channelEntity,
                            youtubeSearchAPI
                        );
                } else
                {
                    channelEntity.setBlCompleted(true);
                    channelEntity.setTmLastUpdate(new Date());
                    this.youtubeChannelRepository.save(channelEntity);
                }
            }
        }
    }

    // SRP: Update a channel information
    @Transactional
    public void updateChannelInformation (
        Channel ytChannel,
        YoutubeChannelEntity channelEntity
    ) {
        channelEntity.setDsName(ytChannel.getSnippet().getTitle());
        channelEntity
            .setDsDescription(ytChannel.getSnippet().getDescription());
        channelEntity.setDsCountry(ytChannel.getSnippet().getCountry());
        channelEntity
            .setTmPublishedAt(
                new Date(
                    ytChannel.getSnippet().getPublishedAt().getValue()
                )
            );
        channelEntity.setTmLastUpdate(new Date());

        this.youtubeChannelRepository.save(channelEntity);
    }

    public void searchAllVideosByChannel (
        YoutubeChannelEntity channelEntity
    ) {
        YouTube youtubeAPI = youTubeAPI.getYouTubeAPI();
        YoutubeSearchAPI youtubeSearchAPI = new YoutubeSearchAPI(
            youtubeAPI
        );
        this
            .searchAllVideosByChannel(
                channelEntity,
                youtubeSearchAPI
            );
    }

    // SRP: Search all videos by channel
    @Transactional
    public void searchAllVideosByChannel (
        YoutubeChannelEntity channelEntity,
        YoutubeSearchAPI youtubeSearchAPI
    ) {
        // Get the last pageToken saved to start the video search
        String pageToken = null;
        if (channelEntity.getDsPageToken() != null)
        {
            pageToken = channelEntity.getDsPageToken();
        }

        SearchListResponse listSearchResults = null;
        do
        {
            listSearchResults = youtubeSearchAPI
                .searchVideosByChannel(
                    channelEntity.getDsChannelId(),
                    pageToken,
                    maxResultsToSearch
                );

            if (listSearchResults != null)
            {
                pageToken = saveListSearchResult(
                    listSearchResults,
                    channelEntity,
                    pageToken
                );
            }
        }
        while (listSearchResults != null && pageToken != null);

        if (pageToken == null)
        {
            channelEntity.setBlCompleted(true);
            channelEntity.setTmLastUpdate(new Date());
            this.youtubeChannelRepository.save(channelEntity);
        }
    }

    // SRP: Save a single list of search results
    @Transactional
    public String saveListSearchResult (
        SearchListResponse listSearchResults,
        YoutubeChannelEntity channelEntity,
        String pageToken
    ) {
        for (SearchResult searchResult : listSearchResults.getItems())
        {
            try
            {
                if (searchResult.getId().getVideoId() == null)
                {
                    continue;
                }

                YoutubeChannelVideoEntity channelVideo = this.youtubeChannelVideoRepository
                    .findByDsVideoId(
                        searchResult.getId().getVideoId()
                    );

                if (channelVideo == null)
                {
                    channelVideo = new YoutubeChannelVideoEntity();
                }

                channelVideo.setYoutubeChannelEntity(channelEntity);
                channelVideo
                    .setDsVideoId(
                        searchResult.getId().getVideoId()
                    );
                channelVideo
                    .setDsTitle(
                        searchResult.getSnippet().getTitle()
                    );
                channelVideo
                    .setDsDescription(
                        searchResult.getSnippet().getDescription()
                    );
                channelVideo
                    .setTmPublishedAt(
                        new Date(
                            searchResult
                                .getSnippet()
                                .getPublishedAt()
                                .getValue()
                        )
                    );
                channelVideo.setTmRegistry(new Date());
                channelVideo.setTmLastUpdate(new Date());

                this.youtubeChannelVideoRepository
                    .save(channelVideo);
            }
            catch (Exception e)
            {
                logger
                    .error(
                        "Something when wrong with result of Channel {"
                            + channelEntity.getDsChannelId() + "}, Video {"
                            + (new Gson()).toJson(searchResult) + "}"
                    );
            }
        }

        pageToken = listSearchResults.getNextPageToken();

        channelEntity.setDsPageToken(pageToken);
        channelEntity.setTmLastUpdate(new Date());
        this.youtubeChannelRepository.save(channelEntity);

        return pageToken;
    }

    // SRP: Get a simple list of videos by channel, without basic information
    public ResponseDataListYoutubeChannelTrackDTO getListYoutubeChannel () {

        ResponseDataListYoutubeChannelTrackDTO responseData = new ResponseDataListYoutubeChannelTrackDTO();

        List<YoutubeChannelTrackInfoDTO> dataList = this.youtubeChannelRepository
            .findListYoutubeChannelTrackInfo();

        responseData.setData(dataList);
        responseData.setTotal(dataList.size());
        responseData.setOffset(0);
        responseData.setLimit(dataList.size());
        responseData.setTotalData(dataList.size());

        return responseData;
    }

    // SRP: Get a list of videos by channel
    public ResponseDataListYoutubeVideoInfoDTO getListYoutubeVideoByChannel (
        BigInteger channelId
    ) {

        ResponseDataListYoutubeVideoInfoDTO responseData = new ResponseDataListYoutubeVideoInfoDTO();

        List<YoutubeVideoInfoDTO> dataList = this.youtubeChannelVideoRepository
            .findListYoutubeVideoByChannel(
                channelId
            );

        responseData.setData(dataList);
        responseData.setTotal(dataList.size());
        responseData.setOffset(0);
        responseData.setLimit(dataList.size());
        responseData.setTotalData(dataList.size());

        return responseData;
    }
}
