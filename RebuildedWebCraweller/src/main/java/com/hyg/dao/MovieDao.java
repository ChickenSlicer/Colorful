package com.hyg.dao;

import com.hyg.domain.Movie;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MovieDao {
    @Select("select * from movie")
    @Results(
            id = "MovieMap",
            value = {
                    @Result(id = true, column = "id", property = "id"),
                    @Result(column = "star_id", property = "starId"),
                    @Result(column = "fanhao", property = "fanHao"),
                    @Result(column = "magnet",property = "magnet"),
                    @Result(column = "introduction",property = "introduction")
            }
    )
    List<Movie> findAll();

    @Select("select * from movie where star_id=#{starId}")
    @ResultMap("MovieMap")
    List<Movie> findMovieByStar(Integer starId);

    @Select("select * from movie where fanhao like concat('%',#{fanhao},'%')")
    @ResultMap("MovieMap")
    List<Movie> findMovieByFH(String fanhao);

    @Insert("insert movie(star_id,fanhao,magnet,introduction) values(#{starId},#{fanHao},#{magnet},#{introduction})")
    int insertMovie(Movie movie);

    @Update("update movie set star_id=#{starId},fanhao=#{fanHao},magnet=#{magnet},introduction=#{introduction} where id=#{id}")
    int updateMovie(Movie movie);

    @Delete("delete from movie where id=#{id}")
    int delete(@Param("id") int id);
}
