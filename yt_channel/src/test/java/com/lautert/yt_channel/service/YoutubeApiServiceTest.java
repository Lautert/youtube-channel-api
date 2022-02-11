package com.lautert.yt_channel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;

import com.lautert.yt_channel.api.youtube.YoutubeAPI;
import com.lautert.yt_channel.application.SpringWebApplication;
import com.lautert.yt_channel.exception.MessageUserException;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelEntity;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelVideoEntity;
import com.lautert.yt_channel.repository.yt_channel.YoutubeChannelRepository;
import com.lautert.yt_channel.repository.yt_channel.YoutubeChannelVideoRepository;

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

    @Autowired
    YoutubeChannelVideoRepository youtubeChannelVideoRepository;

    @Test
    public void assertThatyoutubeAPIIsNotNull () {
        assertNotNull(youTubeAPI);
    }

    @Test
    public void assertThatYoutubeApiServiceIsNotNull () {
        assertNotNull(youtubeApiService);
    }

    @Test
    public void assertThatYoutubeChannelRepositoryIsNotNull () {
        assertNotNull(youtubeChannelRepository);
    }

    @Test
    public void assertThatYoutubeChannelVideoRepositoryIsNotNull () {
        assertNotNull(youtubeChannelVideoRepository);
    }

    // START addChannelToTrackScan
    @Test
    public void assertThatWillGenereteAMessageExceptionIfParameterIsNull () {
        try
        {
            youtubeApiService.addChannelToTrackScan(null);
        }
        catch (MessageUserException e)
        {
            assertEquals(
                e.getMessage(),
                "Invalid Parameter channelId"
            );
        }
    }

    @Test
    public void assertThatWillGenereteAMessageExceptionIfParameterVeryBig () {
        try
        {
            youtubeApiService.addChannelToTrackScan("a".repeat(300));
        }
        catch (MessageUserException e)
        {
            assertEquals(
                e.getMessage(),
                "Invalid Parameter channelId"
            );
        }
    }

    @Test
    public void assertThatAYoutubeChannelWasSavedToDatabase () {
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
    // END addChannelToTrackScan

    // START startTrackScan
    @Test
    public void assertThatWillDoNothingBecauseTheListOfChannelIsInvalid () {

        String channelId = "blueberry";

        BigInteger cdYoutubechannel = this.youtubeApiService
            .addChannelToTrackScan(channelId);

        assertNotNull(cdYoutubechannel);

        YoutubeChannelEntity channelEntity = null;
        int count = 0;

        while (count < 2)
        {
            channelEntity = this.youtubeChannelRepository
                .findById(cdYoutubechannel)
                .get();

            assertNotNull(channelEntity);
            assertEquals(
                channelId,
                channelEntity.getDsChannelId()
            );
            assertNull(channelEntity.getDsName());
            assertNull(channelEntity.getDsDescription());

            if (count < 1)
            {
                assertFalse(channelEntity.getBlCompleted());
                this.youtubeApiService.startTrackScan();
            } else
            {
                assertTrue(channelEntity.getBlCompleted());
            }
            count++;
        }
    }

    @Test
    public void assertThatWillUpdateYoutubeChannelInformations ()
        throws Exception {
        String channelId = "UCmqofm6gSYnyOG1KLRDLULA";

        BigInteger cdYoutubechannel = this.youtubeApiService
            .addChannelToTrackScan(channelId);

        assertNotNull(cdYoutubechannel);

        YoutubeChannelEntity channelEntity = this.youtubeChannelRepository
            .findById(cdYoutubechannel)
            .get();

        assertNotNull(channelEntity);
        assertEquals(
            channelId,
            channelEntity.getDsChannelId()
        );
        assertNull(channelEntity.getDsName());
        assertNull(channelEntity.getDsDescription());
        assertFalse(channelEntity.getBlCompleted());

        this.youtubeApiService.startTrackScan();

        channelEntity = this.youtubeChannelRepository
            .findById(cdYoutubechannel)
            .get();

        assertNotNull(channelEntity);
        assertEquals(
            channelEntity.getDsName(),
            "Vida de Suporte"
        );
        assertEquals(
            channelEntity.getDsDescription(),
            "Canal de desenhos animados do Vida de Suporte, que mostra de maneira bem humorada situações de quem trabalha como Técnico de Informática."
        );
        assertEquals(
            channelEntity.getTmPublishedAt(),
            (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .parse("2014-07-06 22:19:28")
        );
        assertTrue(channelEntity.getBlCompleted());

        List<YoutubeChannelVideoEntity> videos = this.youtubeChannelVideoRepository
            .findByYoutubeChannelEntity(channelEntity);

        assertEquals(
            videos.size(),
            50
        );
    }
    // END startTrackScan

}
