package com.hyg.domain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoInformation {
    private String videoName;
    private String absoluteAddress;
    private String requestUrl;
    private String nginxRequestUrl;
    private String nginxPath;
    private String lastModified;
    private Date date;
    private String pictureUrl;
    private String shortedVideoName;
    private String historyTime;
    private List<String> starName;
    private int startTime;

    public VideoInformation() {
        this.startTime = 0;
    }

    @Override
    public String toString() {
        return "VideoInformation{" +
                "videoName='" + videoName + '\'' +
                ", absoluteAddress='" + absoluteAddress + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", nginxRequestUrl='" + nginxRequestUrl + '\'' +
                ", nginxPath='" + nginxPath + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", date=" + date +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", shortedVideoName='" + shortedVideoName + '\'' +
                ", historyTime='" + historyTime + '\'' +
                ", starName=" + starName +
                ", startTime=" + startTime +
                '}';
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setNginxRequestUrl(String nginxRequestUrl) {
        this.nginxRequestUrl = nginxRequestUrl;
    }

    public List<String> getStarName() {
        return starName;
    }

    public void setStarName(List<String> starName) {
        this.starName = starName;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime;
    }

    public String getShortedVideoName() {
        return shortedVideoName;
    }

    public void setShortedVideoName(String shortedVideoName) {
        this.shortedVideoName = shortedVideoName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        setLastModified(new SimpleDateFormat("yyyy-MM-dd").format(date));
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getNginxRequestUrl() {
        return nginxRequestUrl;
    }

    public String getNginxPath() {
        return nginxPath;
    }

    public void setNginxPath(String nginxPath) {
        this.nginxPath = nginxPath;
        this.nginxRequestUrl = nginxPath + this.videoName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
        this.requestUrl = "/fanhao/video?message=" + this.videoName;
    }

    public String getAbsoluteAddress() {
        return absoluteAddress;
    }

    public void setAbsoluteAddress(String absoluteAddress) {
        this.absoluteAddress = absoluteAddress;
    }
}
