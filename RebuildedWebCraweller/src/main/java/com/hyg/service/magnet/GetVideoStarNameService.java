package com.hyg.service.magnet;

import com.hyg.domain.Movie;
import com.hyg.domain.Star;
import com.hyg.service.dao_related.MovieService;
import com.hyg.service.dao_related.quoted.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GetVideoStarNameService {
    @Autowired
    MovieService movieService;
    @Autowired
    StarService starService;

    public Set<String> getStarName(String fanhao){
        List<Movie> movieByFH = movieService.findMovieByFH(fanhao);

        if (movieByFH.size() == 0)
            return new HashSet<>();

        Set<String> result = new HashSet<>();

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
