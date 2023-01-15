package com.hyg.dao;

import com.hyg.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {
    @Select("select * from comment")
    @Results(
            id = "commentMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "fanhao", property = "fanhao"),
                    @Result(column = "comment", property = "comment"),
                    @Result(column = "time", property = "time"),
                    @Result(column = "username", property = "username"),
            }
    )
    List<Comment> findAll();

    @Insert("insert comment (fanhao, comment, time, username) values (#{fanhao}, #{comment}, #{time}, #{username})")
    int insert(Comment comment);

    @Select("select * from comment where username=#{username}")
    @ResultMap("commentMap")
    List<Comment> findByUser(@Param("username") String username);

    @Select("select * from comment where fanhao=#{fanhao}")
    @ResultMap("commentMap")
    List<Comment> findByFanhao(@Param("fanhao") String fanhao);

    @Delete("delete from comment where username=#{username} and fanhao=#{fanhao} and time=#{time}")
    int delete(@Param("fanhao") String fanhao, @Param("username") String username, @Param("time") String time);
}
