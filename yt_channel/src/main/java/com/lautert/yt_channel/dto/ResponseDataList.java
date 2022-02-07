package com.lautert.yt_channel.dto;

import java.util.List;

public abstract class ResponseDataList<T>
{

    private int offset;
    private int limit;
    private int total;
    private int total_data;
    private List<T> data;

    public int getOffset () {
        return this.offset;
    }

    public void setOffset (
        int offset
    ) {
        this.offset = offset;
    }

    public int getLimit () {
        return this.limit;
    }

    public void setLimit (
        int limit
    ) {
        this.limit = limit;
    }

    public int getTotal () {
        return this.total;
    }

    public void setTotal (
        int total
    ) {
        this.total = total;
    }

    public int getTotalData () {
        return this.total_data;
    }

    public void setTotalData (
        int total_data
    ) {
        this.total_data = total_data;
    }

    public List<T> getData () {
        return this.data;
    }

    public void setData (
        List<T> data
    ) {
        this.data = data;
    }
}
