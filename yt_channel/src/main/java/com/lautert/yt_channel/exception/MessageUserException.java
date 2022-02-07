package com.lautert.yt_channel.exception;

public class MessageUserException extends RuntimeException{

    private int code;
    private String reason;

    public MessageUserException(String message, int code) {
        super(message);

        this.reason = message;
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public int getCode() {
        return code;
    }
}
