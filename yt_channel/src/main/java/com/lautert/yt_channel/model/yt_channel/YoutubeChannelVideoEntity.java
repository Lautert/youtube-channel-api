package com.lautert.yt_channel.model.yt_channel;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.lautert.yt_channel.dto.YoutubeVideoInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "youtube_channel_video", uniqueConstraints =
{
    @UniqueConstraint(columnNames =
    {
        "ds_video_id"
    })
})
@NamedNativeQueries(
    {
        // @formatter:off
        @NamedNativeQuery(
            name = "getListYoutubeVideoInfo",
            query = "SELECT ycv.ds_video_id, ycv.ds_title, ycv.ds_description, ycv.tm_published_at"
                + " FROM youtube_channel_video ycv"
                + " WHERE (ycv.cd_youtube_channel = :channelId)"
                + " ORDER BY ycv.tm_published_at DESC",
            resultSetMapping = "YoutubeVideoInfoToDTO"
        ),
        // @formatter:on
    }
)
@SqlResultSetMapping(
    name = "YoutubeVideoInfoToDTO",
    classes = @ConstructorResult(
        targetClass = YoutubeVideoInfoDTO.class,
        columns =
        {
            @ColumnResult(name = "ds_video_id", type = String.class),
            @ColumnResult(name = "ds_title", type = String.class),
            @ColumnResult(name = "ds_description", type = String.class),
            @ColumnResult(name = "tm_published_at", type = Date.class)
        }
    )
)
public class YoutubeChannelVideoEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_youtube_channel_video")
    private BigInteger cdYoutubeChannelVideo;

    @ManyToOne
    @JoinColumn(name = "cd_youtube_channel")
    private YoutubeChannelEntity youtubeChannelEntity;

    @Column(name = "ds_video_id")
    private String dsVideoId;

    @Column(name = "ds_title")
    private String dsTitle;

    @Column(name = "ds_description")
    private String dsDescription;

    @Column(name = "tm_published_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tmPublishedAt;

    @Column(name = "tm_registry")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date tmRegistry = new Date();

    @Column(name = "tm_last_update")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date tmLastUpdate = new Date();

}
