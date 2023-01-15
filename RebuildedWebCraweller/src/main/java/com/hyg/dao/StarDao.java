package com.hyg.dao;

import com.hyg.domain.Star;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StarDao {
    @Select("select * from star")
    List<Star> findAll();

    @Select("select * from star where name like concat('%',#{name},'%')")
//    @Select("select * from star where name = #{name}")
    List<Star> findStarByName(String name);

    @Insert("insert star values(#{id},#{name},#{score},#{url})")
    int insertStar(Star star);

    @Update("update star set name=#{name},score=#{score} where id=#{id}")
    int updateStar(Star star);

    @Select("select * from star where id=#{id}")
    List<Star> findStarByID(int id);
}
