package com.hyg.dao;

import com.hyg.domain.VideoClickTimes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoClickTimesDao {
    @Select("select * from fanhao_click")
    @Results(
            id = "clickMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "times", property = "times"),
                    @Result(column = "fanhao", property = "fanhao"),
                    @Result(column = "username", property = "username"),
            }
    )
    List<VideoClickTimes> findAll();

    @Insert("insert fanhao_click (fanhao, times, username) values (#{fanhao}, #{times}, #{username})")
    int insert(VideoClickTimes click);

    @Select("select * from fanhao_click where fanhao=#{fanhao}")
    @ResultMap("clickMap")
    List<VideoClickTimes> findByFanhao(@Param("fanhao") String fanhao);

    @Select("select * from fanhao_click where fanhao=#{fanhao} and username=#{username}")
    @ResultMap("clickMap")
    List<VideoClickTimes> findByFanhaoAndUsername(@Param("fanhao") String fanhao,
                                                  @Param("username") String username);

    @Select("select * from fanhao_click where username=#{username}")
    @ResultMap("clickMap")
    List<VideoClickTimes> findByUser(@Param("username") String username);

    @Update("update fanhao_click set times=#{times} where id=#{id}")
    int update(@Param("id") int id, @Param("times") int times);

    @Delete("delete from fanhao_click where id=#{id}")
    int delete(@Param("id") int id);
}
