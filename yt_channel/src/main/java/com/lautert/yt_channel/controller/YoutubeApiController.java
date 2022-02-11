package com.lautert.yt_channel.controller;

import java.math.BigInteger;
import java.util.List;

import com.lautert.yt_channel.dto.ResponseBody;
import com.lautert.yt_channel.dto.ResponseDataListYoutubeChannelTrackDTO;
import com.lautert.yt_channel.dto.ResponseDataListYoutubeVideoInfoDTO;
import com.lautert.yt_channel.exception.MessageUserException;
import com.lautert.yt_channel.service.YoutubeApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping(value = "/api")
@Api(
    value = "Youtube Channel API",
    description = "An API to get Youtube Channel information",
    tags =
    {
        "Youtube Channel"
    }
)
public class YoutubeApiController
{

    @Autowired
    YoutubeApiService youtubeApiService;

    @ApiOperation(
        value = "Get a list of youtube channel ids by username",
        httpMethod = "GET",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value =
    {
        @ApiResponse(
            code = 200,
            response = List.class,
            message = "List of youtube channel ids by UserName"
        )
    })
    @GetMapping(
        value = "/info/channels/{userName}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getListChannelIdByUserName (
        @PathVariable("userName")
        @ApiParam(
            value = "userName",
            required = true,
            example = "suportecanal"
        ) String userName
    )
        throws MessageUserException {
        try
        {
            List<String> response = youtubeApiService
                .getListChannelIdByUserName(userName);

            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        catch (MessageUserException e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response.setMessage(e.getReason());

            return new ResponseEntity<Object>(
                response,
                HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response
                .setMessage("Internal Server Error, please try again later");

            return new ResponseEntity<Object>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @ApiOperation(
        value = "Add a Youtube Channel to Track Scan",
        httpMethod = "POST",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value =
    {
        @ApiResponse(
            code = 200,
            response = List.class,
            message = "The youtube channel was added to Track Scan stack"
        )
    })
    @PostMapping(
        value = "/tasks/{youtubeChannelId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> addChannelToTrackScan (
        @PathVariable("youtubeChannelId")
        @ApiParam(
            value = "youtubeChannelId",
            required = true,
            example = "UCmqofm6gSYnyOG1KLRDLULA"
        ) String youtubeChannelId
    )
        throws MessageUserException {
        try
        {
            BigInteger cdYoutubeChannel = this.youtubeApiService
                .addChannelToTrackScan(youtubeChannelId);

            ResponseBody<BigInteger> response = new ResponseBody<>();
            response.setData(cdYoutubeChannel);

            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        catch (MessageUserException e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response.setMessage(e.getReason());

            return new ResponseEntity<Object>(
                response,
                HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response
                .setMessage("Internal Server Error, please try again later");

            return new ResponseEntity<Object>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @ApiOperation(
        value = "Get a list for Youtube Channel tracked with the information of the last scan and total videos founded",
        httpMethod = "GET",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value =
    {
        @ApiResponse(
            code = 200,
            response = List.class,
            message = "List with basic information of the Youtube Channel tracked"
        )
    })
    @GetMapping(
        value = "/tasks/",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getListYoutubeChannelTracked ()
        throws MessageUserException {
        try
        {
            ResponseDataListYoutubeChannelTrackDTO response = this.youtubeApiService
                .getListYoutubeChannel();

            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        catch (MessageUserException e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response.setMessage(e.getReason());

            return new ResponseEntity<Object>(
                response,
                HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response
                .setMessage("Internal Server Error, please try again later");

            return new ResponseEntity<Object>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @ApiOperation(
        value = "Get the list of videos of a youtube channel by cdYoutubeChannel",
        httpMethod = "GET",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value =
    {
        @ApiResponse(
            code = 200,
            response = List.class,
            message = "List of videos founded to the Youtube Channel"
        )
    })
    @GetMapping(
        value = "/tasks/{taskId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getListYoutubeChannelTracked (
        @PathVariable("taskId") BigInteger youtubeChannelId
    )
        throws MessageUserException {
        try
        {
            ResponseDataListYoutubeVideoInfoDTO response = this.youtubeApiService
                .getListYoutubeVideoByChannel(youtubeChannelId);

            return new ResponseEntity<Object>(response, HttpStatus.OK);
        }
        catch (MessageUserException e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response.setMessage(e.getReason());

            return new ResponseEntity<Object>(
                response,
                HttpStatus.BAD_REQUEST
            );
        }
        catch (Exception e)
        {
            ResponseBody<String> response = new ResponseBody<String>();
            response
                .setMessage("Internal Server Error, please try again later");

            return new ResponseEntity<Object>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
}
