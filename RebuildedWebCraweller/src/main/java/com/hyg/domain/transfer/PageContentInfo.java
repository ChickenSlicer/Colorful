package com.hyg.domain.transfer;

/**
 * @author hyg
 **/
public class PageContentInfo {
    private int pageNum;
    private int pageSize;
    private int type;   //为1表示按名称排序，为2表示按日期排序

    public PageContentInfo(int pageNum, int pageSize, int type) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.type = type;
    }

    public PageContentInfo() {
    }

    @Override
    public String toString() {
        return "PageContentInfo{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", type=" + type +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
