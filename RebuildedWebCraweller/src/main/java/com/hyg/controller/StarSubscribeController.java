package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import com.hyg.domain.StarSubscribe;
import com.hyg.service.dao_related.quoted.StarSubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyg
 * star订阅对应接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/subscribe")
public class StarSubscribeController {
    @Autowired
    private StarSubscribeService starSubscribeService;

    @RequestMapping("/add")
    public boolean add(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return starSubscribeService.addSubscribe(subscribe.getUsername(), subscribe.getStarName());
    }

    @RequestMapping("/remove")
    public boolean remove(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return starSubscribeService.deleteStarSubscribe(subscribe.getUsername(),
                subscribe.getStarName());
    }

    @RequestMapping("/check")
    public boolean check(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return starSubscribeService.check(subscribe.getUsername(), subscribe.getStarName());
    }

    @RequestMapping("/getUserSubscribes")
    public String getUserSubscribes(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return JSON.toJSONString(starSubscribeService.getAllSubscribe(subscribe.getUsername()));
    }

    @RequestMapping("/getUpdatedStars")
    public String getUpdatedStars(@RequestBody String info){
        Account account = JSON.parseObject(info, Account.class);

        return JSON.toJSONString(starSubscribeService.getUpdatedSubscribes(account.getName()));
    }

    @RequestMapping("/resetUpdatedStar")
    public void resetUpdatedStar(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);
        starSubscribeService.resetUpdateStatus(subscribe.getUsername(), subscribe.getStarName());
    }
}
