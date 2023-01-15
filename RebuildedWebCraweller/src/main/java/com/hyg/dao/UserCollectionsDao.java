package com.hyg.dao;

import com.hyg.domain.UserCollections;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author hyg
 **/
@Mapper
public interface UserCollectionsDao {
    @Select("select * from collections")
    @Results(
            id = "collectionsMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "account_id", property = "accountId"),
                    @Result(column = "fanhao", property = "fanhao"),
            }
    )
    List<UserCollections> findAll();

    @Select("select * from collections where account_id=#{id}")
    @ResultMap("collectionsMap")
    List<UserCollections> findByAccountId(@Param("id") int id);

    @Select("select * from collections where account_id=#{id} limit #{position}, #{size}")
    @ResultMap("collectionsMap")
    List<UserCollections> findByAccountIdLimit(@Param("id") int id,
                                               @Param("position") int position,
                                               @Param("size") int size);

    @Insert("insert collections(account_id, fanhao) values(#{accountId}, #{fanhao})")
    int insert(@Param("accountId") int accountId, @Param("fanhao") String fanhao);

    @Delete("delete from collections where fanhao=#{fanhao} and account_id=#{accountId}")
    int delete(@Param("fanhao") String fanhao, @Param("accountId") int accountId);
}
