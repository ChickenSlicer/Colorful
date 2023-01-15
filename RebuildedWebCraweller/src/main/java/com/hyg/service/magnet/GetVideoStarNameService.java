package com.hyg.service.magnet;

import com.hyg.domain.Movie;
import com.hyg.domain.Star;
import com.hyg.service.dao_related.MovieService;
import com.hyg.service.dao_related.quoted.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetVideoStarNameService {
    @Autowired
    MovieService movieService;
    @Autowired
    StarService starService;

    public List<String> getStarName(String fanhao){
        List<Movie> movieByFH = movieService.findMovieByFH(fanhao);

        if (movieByFH.isEmpty())
            return new ArrayList<>();

        List<String> result = new ArrayList<>();

        for (Movie movie : movieByFH) {
            if (!movie.getFanHao().equals(fanhao))
                continue;

            List<Star> starByID = starService.findStarByID(movie.getStarId());

            for (Star star : starByID)
                result.add(star.getName());
        }

        return result;
    }
}
