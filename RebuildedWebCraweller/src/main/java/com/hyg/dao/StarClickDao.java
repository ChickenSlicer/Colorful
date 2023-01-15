package com.hyg.dao;

import com.hyg.domain.StarClickTimes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StarClickDao {
    @Select("select * from star_click")
    @Results(
            id = "starClickMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "times", property = "times"),
                    @Result(column = "star_name", property = "starName"),
                    @Result(column = "username", property = "username"),
            }
    )
    List<StarClickTimes> findAll();

    @Update("update star_click set times=#{times} where id=#{id}")
    int update(@Param("id") int id, @Param("times") int times);

    @Select("select * from star_click where star_name=#{starName} and username=#{username}")
    @ResultMap("starClickMap")
    List<StarClickTimes> findByName(@Param("starName") String starName, @Param("username") String username);

    @Select("select * from star_click where star_name=#{starName}")
    @ResultMap("starClickMap")
    List<StarClickTimes> findByStarName(@Param("starName") String starName);

    @Select("select * from star_click where username=#{username}")
    @ResultMap("starClickMap")
    List<StarClickTimes> findByUsername(@Param("username") String username);

    @Insert("insert star_click (star_name, times, username) values (#{starName}, #{times}, #{username})")
    int insert(StarClickTimes click);

    @Delete("delete from star_click where id=#{id}")
    int delete(@Param("id") int id);
}
