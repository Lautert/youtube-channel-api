package com.lautert.yt_channel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigInteger;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.lautert.yt_channel.api.youtube.YoutubeAPI;
import com.lautert.yt_channel.api.youtube.YoutubeChannelAPI;
import com.lautert.yt_channel.application.SpringWebApplication;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelEntity;
import com.lautert.yt_channel.repository.yt_channel.YoutubeChannelRepository;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = SpringWebApplication.class)
@Testcontainers
public class YoutubeApiServiceTest
{
    private static final Logger logger = LoggerFactory
        .getLogger(YoutubeApiServiceTest.class);

    @Autowired
    private YoutubeAPI youTubeAPI;

    @Autowired
    YoutubeApiService youtubeApiService;

    @Autowired
    YoutubeChannelRepository youtubeChannelRepository;

    @Test
    public void assertThatYoutubeApiServiceIsNotNull () {
        logger.info("assertThatYoutubeApiServiceIsNotNull");
        assertNotNull(youtubeApiService);
    }

    @Test
    public void assertThatAYoutubeChannelIdWasSavedToDatabase () {
        logger.info("assertThatAYoutubeChannelIdWasSavedToDatabase");

        String channelId = "UC-lHJZR3Gqxm24_Vd_AJ5Yw";

        BigInteger cdYoutubechannel = this.youtubeApiService
            .addChannelToTrackScan(channelId);

        assert (cdYoutubechannel != null);
        YoutubeChannelEntity channelEntity = this.youtubeChannelRepository
            .findById(cdYoutubechannel)
            .get();

        assertNotNull(channelEntity);
        assertEquals(
            channelId,
            channelEntity.getDsChannelId()
        );
    }

    @Test
    public void assertThatWillUpdateYoutubeChannelInformations () {
        logger.info("assertThatWillUpdateYoutubeChannelInformations");

        String channelId = "UCmqofm6gSYnyOG1KLRDLULA";

        BigInteger cdYoutubechannel = this.youtubeApiService
            .addChannelToTrackScan(channelId);

        assertNotNull(cdYoutubechannel);

        YoutubeChannelEntity channelEntity = this.youtubeChannelRepository
            .findById(cdYoutubechannel)
            .get();

        YouTube youtubeAPI = youTubeAPI.getYouTubeAPI();
        YoutubeChannelAPI youtubeChannelAPI = new YoutubeChannelAPI(
            youtubeAPI
        );

        Channel ytChannel = youtubeChannelAPI
            .getChannelDetailsByChannelId(
                channelEntity.getDsChannelId()
            );

        assertNotNull(ytChannel);

        this.youtubeApiService
            .updateChannelInformation(
                ytChannel,
                channelEntity
            );

        channelEntity = this.youtubeChannelRepository
            .findById(cdYoutubechannel)
            .get();

        assertEquals(
            "Vida de Suporte",
            channelEntity.getDsName()
        );
        assertNotNull(channelEntity.getTmPublishedAt());
    }
}
