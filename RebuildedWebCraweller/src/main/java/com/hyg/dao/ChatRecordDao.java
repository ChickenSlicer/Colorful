package com.hyg.dao;

import com.hyg.domain.ChatRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatRecordDao {
    @Select("select * from chat_record")
    @Results(
            id = "chatRecordMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "message", property = "message"),
                    @Result(column = "time", property = "time"),
                    @Result(column = "type", property = "type"),
                    @Result(column = "reply_to", property = "replyTo"),
                    @Result(column = "speak_to", property = "speakTo"),
            }
    )
    List<ChatRecord> findAll();

    @Select("select * from chat_record where id=#{id}")
    List<ChatRecord> findById(@Param("id") long id);

    @Insert("insert chat_record (username, message, time, type, reply_to, speak_to) " +
            "values (#{username}, #{message}, #{time}, #{type}, #{replyTo}, #{speakTo})")
    int insert(ChatRecord record);

    @Select("select * from chat_record where username=#{username}")
    @ResultMap("chatRecordMap")
    List<ChatRecord> findByUser(@Param("username") String username);

    @Delete("delete from chat_record where id=#{id}")
    int delete(@Param("id") long id);

    @Select("select * from chat_record order by time desc limit #{position}, #{size}")
    @ResultMap("chatRecordMap")
    List<ChatRecord> findLimited(@Param("size") int size, @Param("position") int position);
}
