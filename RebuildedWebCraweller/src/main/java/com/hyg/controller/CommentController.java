package com.hyg.controller;

import com.alibaba.fastjson.JSON;
import com.hyg.domain.Account;
import com.hyg.domain.Comment;
import com.hyg.service.dao_related.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hyg
 * 视频评论的接口
 **/
@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping("/findAll")
    public String findAll(){
        return JSON.toJSONString(this.commentService.findAll());
    }

    @RequestMapping("/findByUser")
    public String findByUser(@RequestBody String info){
        Account userInfo = JSON.parseObject(info, Account.class);

        return JSON.toJSONString(this.commentService.findByUser(userInfo.getName()));
    }

    @RequestMapping("/findByFanhao")
    public String findByFanhao(@RequestBody String info){
        Comment comment = JSON.parseObject(info, Comment.class);

        return JSON.toJSONString(this.commentService.findByFanhao(comment.getFanhao()));
    }

    /**
     * 添加评论
     * 需要的属性: fanhao, username
     * @param info
     * @return
     */
    @RequestMapping("/addComment")
    public boolean addComment(@RequestBody String info){
        Comment comment = JSON.parseObject(info, Comment.class);

        return this.commentService.addComment(comment);
    }

    @RequestMapping("/deleteComment")
    public boolean deleteComment(@RequestBody String info){
        Comment comment = JSON.parseObject(info, Comment.class);

        return this.commentService.deleteComment(comment);
    }
}
