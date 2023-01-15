package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.StarClickTimes;
import com.hyg.domain.VideoClickTimes;
import com.hyg.service.dao_related.StarClickService;
import com.hyg.service.dao_related.VideoClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyg
 * 计算star和fanhao点击量的接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/click")
public class ClickTimeController {
    @Autowired
    private VideoClickService videoClickService;
    @Autowired
    private StarClickService starClickService;

    /**
     * 添加fanhao点击次数记录
     * 需要属性: username, fanhao
     * @param info
     */
    @RequestMapping("/addFanhaoTimes")
    public void addTimes(@RequestBody String info){
        VideoClickTimes click = JSON.parseObject(info, VideoClickTimes.class);
        videoClickService.addTime(click.getFanhao(), click.getUsername());
    }

    /**
     * 获取某fanhao的点击次数
     * @param info
     * @return
     */
    @RequestMapping("/getFanhaoTimes")
    public int getTimes(@RequestBody String info){
        VideoClickTimes click = JSON.parseObject(info, VideoClickTimes.class);
        return videoClickService.getFanhaoTimes(click.getFanhao());
    }

    /**
     * 获取点击量前10的fanhao信息
     * @return
     */
    @RequestMapping("/top10Fanhao")
    public String fanhaoTop10(){
        return JSON.toJSONString(videoClickService.getTop10());
    }

    /**
     * 添加对应star的点击次数
     * 需要starName和username属性
     * @param info
     */
    @RequestMapping("/addStarTimes")
    public void addStarTimes(@RequestBody String info){
        StarClickTimes clickTimes = JSON.parseObject(info, StarClickTimes.class);
        starClickService.addTimes(clickTimes.getStarName(), clickTimes.getUsername());
    }

    /**
     * 获取对应star的点击次数
     * @param info
     * @return
     */
    @RequestMapping("/getStarTimes")
    public int getStarTimes(@RequestBody String info){
        StarClickTimes clickInfo = JSON.parseObject(info, StarClickTimes.class);

        return starClickService.getTimes(clickInfo.getStarName());
    }

    @RequestMapping("/getUserStarTimes")
    public int getUserStarTimes(@RequestBody String info){
        StarClickTimes clickInfo = JSON.parseObject(info, StarClickTimes.class);

        return starClickService.getUserStarClickTimes(clickInfo.getUsername(),
                clickInfo.getStarName());
    }

    /**
     * 获取点击量前10的star信息
     * @return
     */
    @RequestMapping("/getTop10Stars")
    public String getTop10Stars(){
        return JSON.toJSONString(starClickService.getTop10());
    }
}
