package com.lautert.yt_channel.scheduler;

import com.lautert.yt_channel.service.YoutubeApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerTrackScan
{

    private static final int TIME_TO_TRACK = 1000 * 60 * 2;

    @Autowired
    private YoutubeApiService youtubeApiService;

    @Scheduled(fixedDelay = TIME_TO_TRACK, initialDelay = TIME_TO_TRACK)
    public void scheduleFixedDelayTask () {
        youtubeApiService.startTrackScan();
    }

    // @Scheduled(fixedDelay = 1000)
    // public void timer () {
    //     System.out
    //         .println(
    //             "Fixed rate task - " + System.currentTimeMillis() / 1000
    //         );
    // }
}
