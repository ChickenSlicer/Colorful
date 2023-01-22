package com.hyg.service.dao_related;

import com.hyg.dao.CommentDao;
import com.hyg.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hyg
 **/
@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public List<Comment> findAll(){
        return this.commentDao.findAll();
    }

    public boolean insert(Comment comment){
        if (comment.getComment().equals("") || comment.getTime().equals("")
                || comment.getFanhao().equals(""))
            return false;

        return this.commentDao.insert(comment) == 1;
    }

    /**
     * 依据fanhao来查询评论
     * @param fanhao
     * @return
     */
    public List<Comment> findByFanhao(String fanhao){
        if (fanhao.equals(""))
            return new ArrayList<>();

        return this.commentDao.findByFanhao(fanhao);
    }

    public List<Comment> findByUser(String username){
        if (username.equals(""))
            return new ArrayList<>();

        return this.commentDao.findByUser(username);
    }

    public boolean delete(String fanhao, String username, String time){
        return this.commentDao.delete(fanhao, username, time) == 1;
    }

    /**
     * 添加评论，需要fanhao, comment和username不为空
     * @param comment
     * @return
     */
    public boolean addComment(Comment comment){
        Date date = new Date();

        if (comment.getFanhao().equals("") || comment.getUsername().equals(""))
            return false;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        comment.setTime(formatter.format(date));

        return this.insert(comment);
    }

    /**
     * 删除评论，需要fanhao、time和username不为空
     * @param comment
     * @return
     */
    public boolean deleteComment(Comment comment){
        if (comment.getUsername().equals("") || comment.getTime().equals("") || comment.getFanhao().equals(""))
            return false;

        return this.delete(comment.getFanhao(), comment.getUsername(), comment.getTime());
    }

    /**
     * 删除该用户所有的评论，该方法仅为admin用户所使用
     * @param username
     * @return
     */
    public boolean deleteUserCollections(String username){
        List<Comment> comments = this.findByUser(username);
        boolean flag = true;

        if (comments.size() == 0)
            return true;

        for (Comment comment : comments)
            flag = flag && this.delete(comment.getFanhao(), comment.getUsername(), comment.getTime());

        return flag;
    }

    /**
     * 按fanhao删除评论，该方法仅为admin用户所使用
     * @param fanhao
     * @return
     */
    public boolean deleteByFanhao(String fanhao){
        List<Comment> comments = this.findByFanhao(fanhao);
        boolean flag = true;

        for (Comment comment : comments)
            flag = flag && this.delete(comment.getFanhao(), comment.getUsername(), comment.getTime());

        return flag;
    }
}
