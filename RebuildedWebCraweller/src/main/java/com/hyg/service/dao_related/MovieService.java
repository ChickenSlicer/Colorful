package com.hyg.service.dao_related;

import com.hyg.dao.MovieDao;
import com.hyg.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieDao movieDao;

    public List<Movie> findAll(){
        return movieDao.findAll();
    }

    public List<Movie> findMovieByStar(Integer starId){
        return movieDao.findMovieByStar(starId);
    }

    public List<Movie> findMovieByFH(String fanhao){
        return movieDao.findMovieByFH(fanhao);
    }

    public int insertMovie(Movie movie){
        return movieDao.insertMovie(movie);
    }

    public int updateMovie(Movie movie){
        return movieDao.updateMovie(movie);
    }

    public boolean delete(int id){
        return this.movieDao.delete(id) == 1;
    }
}
