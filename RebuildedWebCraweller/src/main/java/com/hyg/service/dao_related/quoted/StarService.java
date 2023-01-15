package com.hyg.service.dao_related.quoted;

import com.hyg.dao.StarDao;
import com.hyg.domain.Fanhao;
import com.hyg.domain.Movie;
import com.hyg.domain.Star;
import com.hyg.domain.StarFanhao;
import com.hyg.service.dao_related.FanhaoService;
import com.hyg.service.dao_related.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StarService {
    @Autowired
    private StarDao starDao;
    @Autowired
    private FanhaoService fanhaoService;
    @Autowired
    private MovieService movieService;

    public List<Star> findAll(){
        return starDao.findAll();
    }

    public List<Star> findStarByName(String name){
        return starDao.findStarByName(name);
    }

    public int insertStar(Star star){
        return starDao.insertStar(star);
    }

    public int updateStar(Star star){
        return starDao.updateStar(star);
    }

    public List<Star> findStarByID(int ID){
        return starDao.findStarByID(ID);
    }

    public List<StarFanhao> findFanhaoByStar(String starName){
        List<Star> stars = this.findStarByName(starName);

        if (stars.size() == 0)
            return new ArrayList<>();

        List<StarFanhao> result = new ArrayList<>();

        for (Star star : stars) {
            StarFanhao starFanhao = new StarFanhao(star.getName());
            List<Movie> movies = movieService.findMovieByStar(star.getId());

            if (movies.size() == 0) {
                result.add(starFanhao);
                continue;
            }

            for (Movie movie : movies) {
                List<Fanhao> byFanhao = fanhaoService.findByFanhao(movie.getFanHao());

                if (byFanhao.size() == 0)
                    continue;

                Fanhao fanhao = byFanhao.get(0);
                starFanhao.addFanhao(fanhao);
            }

            result.add(starFanhao);
        }

        return result;
    }
}
