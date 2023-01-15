package com.hyg.controller;

import com.hyg.domain.transfer.DeleteCollectionsUnit;
import com.hyg.service.auth_related.unquoted.RegisterService;
import com.hyg.service.dao_related.AccountService;
import com.hyg.service.dao_related.quoted.UserCollectionsService;
import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 包含账号登录、用户收藏增删的功能
 */
@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserCollectionsService userCollectionsService;

    /**
     * 用户登录
     * @param info
     * @return
     */
    @RequestMapping("/signIn")
    public boolean signIn(@RequestBody String info){
        Account signInInfo = JSON.parseObject(info, Account.class);

        return registerService.signIn(signInInfo);
    }

    /**
     * 用户注册
     * @param info
     * @return
     */
    @RequestMapping("/register")
    public boolean register(@RequestBody String info){
        Account registerInfo = JSON.parseObject(info, Account.class);

        return registerService.register(registerInfo);
    }

    /**
     * 修改用户密码
     * @param info
     * @return
     */
    @RequestMapping("/alterUserInfo")
    public boolean alterUserInfo(@RequestBody String info){
        Account alterInfo = JSON.parseObject(info, Account.class);
        return accountService.update(alterInfo.getName(), alterInfo.getPassword());
    }

    /**
     * 获取用户收藏信息
     * @param info
     * @return
     */
    @RequestMapping("/getUserCollections")
    public String getUserCollections(@RequestBody String info){
        Account userInfo = JSON.parseObject(info, Account.class);
        int accountId = accountService.getUserID(userInfo.getName());
        List<String> userCollections = userCollectionsService.getUserCollections(accountId);

        return JSON.toJSONString(userCollections);
    }

    /**
     * 删除某用户的一个收藏
     * @param info
     */
    @RequestMapping("/deleteUserCollections")
    public void deleteUserCollections(@RequestBody String info){
        DeleteCollectionsUnit deleteInfo = JSON.parseObject(info, DeleteCollectionsUnit.class);
        int accountId = accountService.getUserID(deleteInfo.getUsername());
        userCollectionsService.delete(accountId, deleteInfo.getFanhao());
    }
}
