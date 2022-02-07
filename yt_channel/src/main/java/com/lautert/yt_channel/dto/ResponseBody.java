package com.lautert.yt_channel.dto;

import io.swagger.annotations.ApiModelProperty;

public class ResponseBody<T>
{

    @ApiModelProperty(
        value = "If the operation was successful, the data will be informed by this attribute"
    )
    private T data;

    @ApiModelProperty(
        value = "Case the request is not as expected, we will return the reason in this field"
    )
    private String message;

    public T getData () {
        return this.data;
    }

    public void setData (
        T data
    ) {
        this.data = data;
    }

    public String getMessage () {
        return this.message;
    }

    public void setMessage (
        String message
    ) {
        this.message = message;
    }
}
