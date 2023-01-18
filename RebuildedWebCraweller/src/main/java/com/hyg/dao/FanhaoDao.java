package com.hyg.dao;

import com.hyg.domain.Fanhao;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FanhaoDao {
    @Select("select * from fanhao")
    @Results(
            id = "fanhaoMap",
            value = {
                    @Result(id = true, column = "fanhao", property = "fanhao"),
                    @Result(column = "magnet_length", property = "magnet_length"),
                    @Result(column = "magnet_heat", property = "magnet_heat")
            }
    )
    List<Fanhao> findAll();

    @Insert("insert fanhao (fanhao, magnet_length, magnet_heat) values (#{fanhao}, #{magnet_length}, #{magnet_heat})")
    int saveFanhao(Fanhao fanhao);

    @Update("update fanhao set magnet_length=#{magnet_length}, magnet_heat=#{magnet_heat} where fanhao=#{fanhao}")
    int update(Fanhao fanhao);

    @Delete("delete from fanhao where fanhao=#{fanhao}")
    int delete(Fanhao fanhao);

    //模糊查询
    @Select("select * from fanhao where fanhao like concat('%',#{fanhao},'%')")
    List<Fanhao> findByFanhao(String fanhao);

    @Select("select * from fanhao limit #{position}, #{size}")
    List<Fanhao> findFanhaoLimited(@Param("position") int position, @Param("size") int size);
}
