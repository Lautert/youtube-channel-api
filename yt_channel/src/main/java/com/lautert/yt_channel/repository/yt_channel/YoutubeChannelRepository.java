package com.lautert.yt_channel.repository.yt_channel;

import java.math.BigInteger;
import java.util.List;

import com.lautert.yt_channel.dto.YoutubeChannelTrackInfoDTO;
import com.lautert.yt_channel.model.yt_channel.YoutubeChannelEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeChannelRepository
    extends CrudRepository<YoutubeChannelEntity, BigInteger>
{

    YoutubeChannelEntity findByDsChannelId (
        String dsChannelId
    );

    List<YoutubeChannelEntity> findByBlCompletedFalse ();

    @Query(name = "getListYoutubeChannelTrackInfo", nativeQuery = true)
    List<YoutubeChannelTrackInfoDTO> findListYoutubeChannelTrackInfo ();
}
