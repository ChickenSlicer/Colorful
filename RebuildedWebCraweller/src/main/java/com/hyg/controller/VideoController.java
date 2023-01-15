package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyg.domain.transfer.AddAllocateUnit;
import com.hyg.domain.TaskInfo;
import com.hyg.domain.VideoInformation;
import com.hyg.domain.transfer.AddMovieAuthorUnit;
import com.hyg.enums.ServiceType;
import com.hyg.service.dao_related.AllocateService;
import com.hyg.service.util_service.TaskQueueService;
import com.hyg.service.util_service.UsefulService;
import com.hyg.service.util_service.VideoAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 旧的前端对应接口
 * 但新前端也有部分功能用到该Controller中的接口
 */
@CrossOrigin
@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private TaskQueueService taskQueue;
    @Autowired
    private UsefulService usefulService;
    @Autowired
    private AllocateService allocateService;
    @Autowired
    private VideoAddressService videoAddressService;

    @RequestMapping("/sortByName")
    public String videosSortByName(){
        return JSON.toJSONString(videoAddressService.videoSortByName());
    }

    @RequestMapping("/allocate")
    public String allocate(@RequestBody String type){
        type = type.replaceAll("=", "");
        int cata = Integer.parseInt(type);
        List<String> fanhaos = allocateService.getAllocateFanhao(cata);

        return JSON.toJSONString(fanhaos);
    }

    @RequestMapping("/addAllocate")
    public String addAllocate(@RequestBody String JSONObj){
        AddAllocateUnit parameter = JSON.parseObject(JSONObj, AddAllocateUnit.class);
        boolean flag = allocateService.addAllocate(parameter.getType(), parameter.getFanhao());

        if (flag)
            return "true";
        else
            return "false";
    }

    @RequestMapping("/getAllocate")
    public String getAllocate(@RequestBody String JSONObj){
        AddAllocateUnit parameter = JSON.parseObject(JSONObj, AddAllocateUnit.class);
        List<VideoInformation> infos = usefulService.computeAllocateByName(parameter.getType());

        return JSON.toJSONString(infos);
    }

    @RequestMapping("/getAllocateByDate")
    public String getAllocateByDate(@RequestBody String JSONObj){
        AddAllocateUnit parameter = JSON.parseObject(JSONObj, AddAllocateUnit.class);
        List<VideoInformation> infos = usefulService.computeAllocateByDate(parameter.getType());

        return JSON.toJSONString(infos);
    }

    @RequestMapping("/addMovieAuthor")
    public String addMovieAuthor(@RequestBody String JSONObj){
        AddMovieAuthorUnit parameter = JSON.parseObject(JSONObj, AddMovieAuthorUnit.class);
        boolean result = usefulService.addMovieAuthor(parameter.getFanhao(), parameter.getStarName());
        return result + "";
    }

    @RequestMapping("/deleteAllocate")
    public void deleteAllocate(@RequestBody String JSONObj){
        AddAllocateUnit parameter = JSON.parseObject(JSONObj, AddAllocateUnit.class);
        allocateService.deleteAllocate(parameter.getType(), parameter.getFanhao());
    }

    @RequestMapping("/sortByDate")
    public String sortByDate(){
        return JSON.toJSONString(videoAddressService.videoSortByDate());
    }

    @RequestMapping("/prepareInfo")
    public void prepareInformation(){
        videoAddressService.prepareRedis();
    }

    @RequestMapping("/allStars")
    public String allStars(){
        Set<String> starInfo = usefulService.getAllStarName();

        if (starInfo.size() == 0)
            videoAddressService.prepareRedis();

        return JSON.toJSONString(starInfo);
    }

    @RequestMapping("/addSeriesTask")
    public void addSeriesTask(@RequestBody JSONObject seriesName){
        String series = seriesName.get("series").toString();
        System.out.println("已经添加系列任务，参数为" + series);
        TaskInfo task = new TaskInfo(series, ServiceType.SERIES_WORK);
        taskQueue.addTask(task);
    }

    @RequestMapping("/addStarTask")
    public void addStarTask(@RequestBody JSONObject starName){
        String star = starName.get("star").toString();
        System.out.println("已添加女优任务，参数为" + star);
        TaskInfo task = new TaskInfo(star, ServiceType.STAR_WORK);
        taskQueue.addTask(task);
    }

    @RequestMapping("/addRecommend")
    public void addRecommend(){
        System.out.println("已添加推荐任务");
        TaskInfo task = new TaskInfo("", ServiceType.RECOMMEND_WORK);
        taskQueue.addTask(task);
    }

    @RequestMapping("/addUpdateTask")
    public void addUpdateTask(){
        System.out.println("已添加更新任务");
        TaskInfo task = new TaskInfo("", ServiceType.UPDATE_WORK);
        taskQueue.addTask(task);
    }

    @RequestMapping("/addNew")
    public void addNew(){
        System.out.println("已添加新成员任务");
        TaskInfo task = new TaskInfo("", ServiceType.ADD_NEW);
        taskQueue.addTask(task);
    }

    @RequestMapping("/flushRedis")
    public void flushRedis(){
        usefulService.redisFlushAll();
    }
}
