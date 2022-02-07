package com.lautert.yt_channel.repository.yt_channel;

import java.math.BigInteger;
import java.util.List;

import com.lautert.yt_channel.dto.YoutubeVideoInfoDTO;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelVideoEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeChannelVideoRepository extends CrudRepository<YoutubeChannelVideoEntity, BigInteger> {

    YoutubeChannelVideoEntity findByDsVideoId(String dsVideoId);

    @Query(name = "getListYoutubeVideoInfo", nativeQuery = true)
    List<YoutubeVideoInfoDTO> findListYoutubeVideoByChannel (
        @Param("channelId") Long channelId
    );
}
