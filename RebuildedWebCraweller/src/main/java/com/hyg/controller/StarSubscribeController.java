package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import com.hyg.domain.StarSubscribe;
import com.hyg.domain.transfer.CheckSubscribeInList;
import com.hyg.service.dao_related.quoted.StarSubscribeService;
import com.hyg.service.util_service.VideoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @Autowired
    private VideoAddressService videoAddressService;

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
    
    @RequestMapping("/checkInArray")
    public String checkInArray(@RequestBody String info){
        CheckSubscribeInList checkInfo = JSON.parseObject(info, CheckSubscribeInList.class);
        List<Boolean> result = starSubscribeService.checkInList(checkInfo.getStars(),
                checkInfo.getUsername());

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getUserSubscribes")
    public String getUserSubscribes(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return JSON.toJSONString(starSubscribeService.getAllSubscribe(subscribe.getUsername()));
    }

    /**
     * 这里面StarSubscribe类进行了复用，starId属性存放size
     * @param info
     * @return 最大页数
     */
    @RequestMapping("/maxPage")
    public int maxPage(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return starSubscribeService.maxPage(subscribe.getUsername(), subscribe.getStarId());
    }

    /**
     * 这里面StarSubscribe类进行了复用，starId属性存放size，updated存放position
     * @param info
     * @return
     */
    @RequestMapping("/getByPage")
    public String getByPage(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return JSON.toJSONString(starSubscribeService.getSubscribePage(subscribe.getUsername(),
                subscribe.getStarId(), subscribe.getUpdated()));
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

    @RequestMapping("/starWork")
    public String starWork(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return JSON.toJSONString(videoAddressService.starWorkSortByDate(subscribe.getStarName()));
    }

    /**
     * 重置某用户订阅的所有star的更新状态
     */
    @RequestMapping("/resetAll")
    public void resetAll(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);
        starSubscribeService.resetAllUpdateStatus(subscribe.getUsername());
    }

    /**
     * 大小不超过20的动态视频
     * @param info
     * @return
     */
    @RequestMapping("/recent")
    public String recent(@RequestBody String info){
        StarSubscribe subscribe = JSON.parseObject(info, StarSubscribe.class);

        return JSON.toJSONString(videoAddressService.recent(subscribe.getStarName()));
    }
}
