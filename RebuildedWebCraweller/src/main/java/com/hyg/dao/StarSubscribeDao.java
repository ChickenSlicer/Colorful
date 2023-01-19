package com.hyg.dao;

import com.hyg.domain.StarSubscribe;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StarSubscribeDao {
    @Select("select * from star_subscribe")
    @Results(
            id = "starSubscribeMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "starname", property = "starName"),
                    @Result(column = "starId", property = "starId"),
                    @Result(column = "updated", property = "updated"),
            }
    )
    List<StarSubscribe> findAll();

    @Select("select * from star_subscribe where starname=#{starName}")
    @ResultMap("starSubscribeMap")
    List<StarSubscribe> findByStarName(@Param("starName") String starName);

    @Select("select * from star_subscribe where username=#{username}")
    @ResultMap("starSubscribeMap")
    List<StarSubscribe> findByUsername(@Param("username") String username);

    @Select("select * from star_subscribe where username=#{username} limit #{position}, #{size}")
    @ResultMap("starSubscribeMap")
    List<StarSubscribe> findByUsernameLimited(@Param("username") String username,
                                              @Param("position") int position, @Param("size") int size);

    @Insert("insert star_subscribe (username, starname, starId, updated) values " +
            "(#{username}, #{starName}, #{starId}, #{updated})")
    int insert(StarSubscribe subscribe);

    @Update("update star_subscribe set updated=#{updated} where id=#{id}")
    int update(@Param("id") long id, @Param("updated") int updated);

    @Delete("delete from star_subscribe where id=#{id}")
    int delete(@Param("id") int id);
}
