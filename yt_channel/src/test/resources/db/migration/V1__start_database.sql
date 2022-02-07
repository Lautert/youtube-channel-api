CREATE TABLE youtube_channel(
    cd_youtube_channel SERIAL,
    ds_name VARCHAR(255),
    ds_description VARCHAR(255),
    ds_channel_id VARCHAR(255) NOT NULL,
    tm_published_at TIMESTAMP,
    ds_country VARCHAR(255),

    ds_page_token VARCHAR(255),
    bl_completed BOOLEAN NOT NULL DEFAULT FALSE,

    tm_registry TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tm_last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_ytc_cd_youtube_channel PRIMARY KEY (cd_youtube_channel),

    CONSTRAINT uk_ytc_ds_channel_id UNIQUE (ds_channel_id)
);
COMMENT ON TABLE youtube_channel IS 'alias ytc';
CREATE INDEX ix_ytc_ds_channel_id ON youtube_channel(ds_channel_id);
CREATE INDEX ix_ytc_tm_registry ON youtube_channel(tm_registry);


CREATE TABLE youtube_channel_video(
    cd_youtube_channel_video SERIAL,
    cd_youtube_channel INTEGER NOT NULL,

    ds_video_id VARCHAR(255) NOT NULL,
    ds_title VARCHAR(255),
    ds_description VARCHAR(255),
    tm_published_at TIMESTAMP,

    tm_registry TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tm_last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_ycv_cd_youtube_channel_video PRIMARY KEY (cd_youtube_channel_video),

    CONSTRAINT fk_ycv_cd_youtube_channel FOREIGN KEY (cd_youtube_channel)
        REFERENCES youtube_channel(cd_youtube_channel)
        ON DELETE CASCADE,

    CONSTRAINT uk_ycv_ds_video_id UNIQUE (ds_video_id)
);

COMMENT ON TABLE youtube_channel_video IS 'alias ycv';
CREATE INDEX ix_ycv_ds_video_id ON youtube_channel_video(ds_video_id);
CREATE INDEX ix_ycv_tm_registry ON youtube_channel_video(tm_registry);
