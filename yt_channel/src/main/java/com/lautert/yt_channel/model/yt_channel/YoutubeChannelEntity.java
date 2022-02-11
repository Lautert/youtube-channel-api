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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.lautert.yt_channel.dto.YoutubeChannelTrackInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "youtube_channel", uniqueConstraints =
{
    @UniqueConstraint(columnNames =
    {
        "ds_channel_id"
    })
})
@NamedNativeQueries(
    {
        // @formatter:off
        @NamedNativeQuery(
            name = "getListYoutubeChannelTrackInfo",
            query = "SELECT yc.cd_youtube_channel, yc.ds_channel_id, yc.ds_name, yc.tm_registry, yc.tm_last_update, yc.bl_completed, "
                + " (SELECT COUNT(1) FROM youtube_channel_video ycv WHERE ycv.cd_youtube_channel = yc.cd_youtube_channel)"
                + " AS total_video FROM youtube_channel yc",
            resultSetMapping = "YoutubeChannelTrackInfoToDTO"
        ),
        // @formatter:on
    }
)
@SqlResultSetMapping(
    name = "YoutubeChannelTrackInfoToDTO",
    classes = @ConstructorResult(
        targetClass = YoutubeChannelTrackInfoDTO.class,
        columns =
        {
            @ColumnResult(name = "cd_youtube_channel", type = BigInteger.class),
            @ColumnResult(name = "ds_channel_id", type = String.class),
            @ColumnResult(name = "ds_name", type = String.class),
            @ColumnResult(name = "tm_registry", type = Date.class),
            @ColumnResult(name = "tm_last_update", type = Date.class),
            @ColumnResult(name = "bl_completed", type = Boolean.class),
            @ColumnResult(name = "total_video", type = Integer.class),
        }
    )
)
public class YoutubeChannelEntity implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_youtube_channel")
    private BigInteger cdYoutubeChannel;

    @Column(name = "ds_name")
    private String dsName;

    @Column(name = "ds_description")
    private String dsDescription;

    @Column(name = "ds_channel_id")
    private String dsChannelId;

    @Column(name = "tm_published_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tmPublishedAt;

    @Column(name = "ds_country")
    private String dsCountry;

    @Column(name = "tm_registry")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date tmRegistry = new Date();

    @Column(name = "tm_last_update")
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private Date tmLastUpdate = new Date();

    @Column(name = "ds_page_token")
    private String dsPageToken;

    @Column(name = "bl_completed")
    @Builder.Default
    private Boolean blCompleted = false;

}
