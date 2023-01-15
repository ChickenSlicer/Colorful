package com.hyg.dao;

import com.hyg.domain.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountDao {
    @Select("select * from account")
    @Results(
            id = "accountMap",
            value = {
                    @Result(id = true, column = "username", property = "name"),
                    @Result(column = "passwd", property = "password"),
                    @Result(column = "id", property = "id"),
            }
    )
    List<Account> findAll();

    @Insert("insert account (username, passwd, id) values (#{name}, #{password}, #{id})")
    int insertAccount(Account account);

    @Select("select * from account where username=#{name}")
    @ResultMap("accountMap")
    List<Account> findByName(String name);

    @Update("update account set passwd=#{password} where username=#{name}")
    int update(@Param("password") String password, @Param("name") String name);

    @Delete("delete from account where username=#{username}")
    int delete(@Param("username") String username);
}
