package com.hyg.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hyg
 **/
public class DesktopPageContent {
    private List<VideoInformation> firstColumn;
    private List<VideoInformation> secondColumn;
    private List<VideoInformation> thirdColumn;

    public DesktopPageContent() {
        this.firstColumn = new ArrayList<>();
        this.secondColumn = new ArrayList<>();
        this.thirdColumn = new ArrayList<>();
    }

    public void addFirst(VideoInformation info){
        this.firstColumn.add(info);
    }

    public void addSecond(VideoInformation info){
        this.secondColumn.add(info);
    }

    public void addThird(VideoInformation info){
        this.thirdColumn.add(info);
    }

    public List<VideoInformation> getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(List<VideoInformation> firstColumn) {
        this.firstColumn = firstColumn;
    }

    public List<VideoInformation> getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(List<VideoInformation> secondColumn) {
        this.secondColumn = secondColumn;
    }

    public List<VideoInformation> getThirdColumn() {
        return thirdColumn;
    }

    public void setThirdColumn(List<VideoInformation> thirdColumn) {
        this.thirdColumn = thirdColumn;
    }

    @Override
    public String toString() {
        return "DesktopPageContent{" +
                "firstColumn=" + firstColumn +
                ", secondColumn=" + secondColumn +
                ", thirdColumn=" + thirdColumn +
                '}';
    }
}
