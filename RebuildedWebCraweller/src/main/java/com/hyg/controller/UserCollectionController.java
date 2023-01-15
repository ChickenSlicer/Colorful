package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.transfer.AddUserCollection;
import com.hyg.service.dao_related.quoted.UserCollectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyg
 * 用户收藏对应接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/userCollections")
public class UserCollectionController {
    @Autowired
    private UserCollectionsService userCollectionsService;

    /**
     * 获取所有收藏
     * 需要username属性
     * @param info
     * @return
     */
    @RequestMapping("/getAllCollections")
    public String getAllCollections(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return JSON.toJSONString(userCollectionsService.getCollectionsByUsername(userInfo.getUsername()));
    }

    /**
     * 收藏方法
     * 用到的属性: username, fanhao
     * @param info
     * @return
     */
    @RequestMapping("/add")
    public boolean add(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return userCollectionsService.add(userInfo.getUsername(), userInfo.getFanhao());
    }

    /**
     * 按页获取收藏
     * 需要用到的属性有username, position, size
     * @param info
     * @return
     */
    @RequestMapping("/getCollections")
    public String getCollections(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return JSON.toJSONString(userCollectionsService.findByAccountIdLimit(userInfo.getUsername(),
                userInfo.getPosition(), userInfo.getSize()));
    }

    /**
     * 桌面端按页获取收藏
     * @param info
     * @return
     */
    @RequestMapping("/desktopGetCollections")
    public String desktopGetCollections(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return JSON.toJSONString(userCollectionsService.desktopFindByAccountIdLimit(userInfo.getUsername(),
                userInfo.getPosition(), userInfo.getSize()));
    }

    /**
     * 获取最大页数
     * 用到的属性有username, size
     * @param info
     * @return
     */
    @RequestMapping("/maxPage")
    public int maxPage(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return userCollectionsService.getMaxPage(userInfo.getUsername(), userInfo.getSize());
    }

    /**
     * 取消收藏
     * 需要属性: username, fanhao
     * @param info
     */
    @RequestMapping("/delete")
    public boolean delete(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return userCollectionsService.deleteUserCollection(userInfo.getUsername(), userInfo.getFanhao());
    }

    /**
     * 检查作品是否已被收藏
     * 需要的属性: username. fanhao
     * @param info
     * @return
     */
    @RequestMapping("/check")
    public boolean check(@RequestBody String info){
        AddUserCollection userInfo = JSON.parseObject(info, AddUserCollection.class);

        return userCollectionsService.checkCollections(userInfo.getUsername(), userInfo.getFanhao());
    }
}
