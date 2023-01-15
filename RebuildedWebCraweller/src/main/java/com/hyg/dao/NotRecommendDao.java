package com.hyg.dao;

import com.hyg.domain.NotRecommend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotRecommendDao {
    @Select("select * from not_recommend")
    @Results(
            id = "notRecommendMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "fanhao", property = "fanhao"),
                    @Result(column = "username", property = "username"),
                    @Result(column = "deprecated", property = "deprecated"),
            }
    )
    List<NotRecommend> findAll();

    @Insert("insert not_recommend (fanhao, username, deprecated) values (#{fanhao}, #{username}, #{deprecated})")
    int insert(NotRecommend item);

    @Select("select * from not_recommend where username=#{username} and deprecated=0")
    @ResultMap("notRecommendMap")
    List<NotRecommend> findByUser(@Param("username") String username);

    @Select("select * from not_recommend where username=#{username}")
    @ResultMap("notRecommendMap")
    List<NotRecommend> findByUserAll(@Param("username") String username);

    @Update("update not_recommend set deprecated=#{deprecated} where id=#{id}")
    int update(@Param("id") int id, @Param("deprecated") int deprecated);

    @Delete("delete from not_recommend where id=#{id}")
    int delete(@Param("id") int id);
}
