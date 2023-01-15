package com.hyg.dao;

import com.hyg.domain.UserHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface HistoryDao {
    @Select("select * from history")
    @Results(
            id = "historyMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "fanhao", property = "fanhao"),
                    @Result(column = "time", property = "time"),
                    @Result(column = "endtime", property = "endTime"),
            }
    )
    List<UserHistory> findAll();

    @Insert("insert history (username, fanhao, time, endtime) values (#{username}, #{fanhao}, #{time}, #{endTime})")
    int insert(UserHistory history);

    @Select("select * from history where username=#{username}")
    @ResultMap("historyMap")
    List<UserHistory> findByUser(String username);

    @Delete("delete from history where username=#{username} and fanhao=#{fanhao} and time=#{time}")
    int delete(@Param("username") String username, @Param("fanhao") String fanhao, @Param("time") String time);

    @Select("select * from history where username=#{username} order by time desc limit #{position}, #{size}")
    @ResultMap("historyMap")
    List<UserHistory> findByUserLimit(@Param("username") String username,
                                      @Param("size") int size,
                                      @Param("position") int position);

    @Update("update history set time=#{time}, endtime=#{endTime} where id=#{id}")
    int update(@Param("time") String time, @Param("id") int id, @Param("endTime") int endTime);
}
