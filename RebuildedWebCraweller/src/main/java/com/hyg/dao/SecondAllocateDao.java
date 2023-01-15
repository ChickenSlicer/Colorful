package com.hyg.dao;

import com.hyg.domain.Allocates;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SecondAllocateDao {
    @Select("select * from second_allocate")
    @Results(
            id = "allocateMap2",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "fanhao", property = "fanhao"),
            }
    )
    public abstract List<Allocates> findAll();

    @Insert("insert second_allocate(fanhao) values(#{fanhao})")
    public abstract void insertAllocate(String fanhao);

    @Delete("delete from second_allocate where fanhao=#{fanhao}")
    void delete(String fanhao);
}
