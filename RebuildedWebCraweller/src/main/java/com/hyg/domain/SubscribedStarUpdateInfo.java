package com.hyg.domain;

/**
 * @author hyg
 **/
public class SubscribedStarUpdateInfo {
    private String starname;
    private String latestMessage;
    private String date;

    public SubscribedStarUpdateInfo() {
    }

    public SubscribedStarUpdateInfo(String starname, String latestMessage, String date) {
        this.starname = starname;
        this.latestMessage = latestMessage;
        this.date = date;
    }

    @Override
    public String toString() {
        return "SubscribedStarUpdateInfo{" +
                "starname='" + starname + '\'' +
                ", latestMessage='" + latestMessage + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getStarname() {
        return starname;
    }

    public void setStarname(String starname) {
        this.starname = starname;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
