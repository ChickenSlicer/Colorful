package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import com.hyg.domain.UserHistory;
import com.hyg.domain.transfer.UserHistoryLimit;
import com.hyg.service.dao_related.quoted.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyg
 * 播放历史记录对应接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/history")
public class VideoHistoryController {
    @Autowired
    private HistoryService historyService;

    /**
     * 获取某个用户的历史记录
     * @param info
     * @return
     */
    @RequestMapping("/getUserHistory")
    public String getUserHistory(@RequestBody String info){
        Account userInfo = JSON.parseObject(info, Account.class);

        return JSON.toJSONString(historyService.findByUser(userInfo.getName()));
    }

    @RequestMapping("/getUserHistoryLimit")
    public String getUserHistoryLimit(@RequestBody String info){
        UserHistoryLimit userInfo = JSON.parseObject(info, UserHistoryLimit.class);

        return JSON.toJSONString(historyService.findByUserLimit(userInfo.getUsername(),
                userInfo.getSize(),
                userInfo.getPosition()));
    }

    @RequestMapping("/desktopGetUserHistoryLimit")
    public String desktopGetUserHistoryLimit(@RequestBody String info){
        UserHistoryLimit userInfo = JSON.parseObject(info, UserHistoryLimit.class);

        return JSON.toJSONString(historyService.desktopFindByUserLimit(userInfo.getUsername(),
                userInfo.getSize(),
                userInfo.getPosition()));
    }

    /**
     * 为某用户添加历史记录
     * 只需设置username和fanhao
     * @param info
     */
    @RequestMapping("/add")
    public void addHistory(@RequestBody String info){
        UserHistory history = JSON.parseObject(info, UserHistory.class);
        historyService.add(history);
    }

    /**
     * 删除某用户迄今为止的所有历史记录
     * 只需要name属性的内容
     * @param info
     * @return
     */
    @RequestMapping("/removeAll")
    public boolean removeAll(@RequestBody String info){
        Account confirmInfo = JSON.parseObject(info, Account.class);
        return historyService.removeAll(confirmInfo);
    }

    /**
     * 删除某用户的某一历史记录
     * @param info
     * @return
     */
    @RequestMapping("/remove")
    public boolean remove(@RequestBody String info){
        UserHistory history = JSON.parseObject(info, UserHistory.class);
        return historyService.remove(history);
    }

    /**
     * 获取最大页数，用到的有size和username两个属性
     * @param info
     * @return
     */
    @RequestMapping("/maxPage")
    public int maxPage(@RequestBody String info){
        UserHistoryLimit history = JSON.parseObject(info, UserHistoryLimit.class);
        return historyService.maxPage(history.getSize(), history.getUsername());
    }
}
