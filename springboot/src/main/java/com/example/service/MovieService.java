package com.example.service;

import com.example.entity.Comment;
import com.example.entity.Movie;
import com.example.mapper.CommentMapper;
import com.example.mapper.MovieMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Resource
    private MovieMapper movieMapper;
    @Resource
    private CommentMapper commentMapper;



    public void add(Movie movie) {
        movieMapper.insert(movie);
    }

    public void update(Movie movie) {
        movieMapper.updateById(movie);
    }


    public void deleteById(Integer id) {
        movieMapper.deleteById(id);
    }

    //select all
    public List<Movie> selectAll(Movie movie){

        return movieMapper.selectAll(movie);
    }

    /**
     *
     * @param pageNum  current page number
     * @param pageSize  size of each page
     * @return  Paginated object
     */

    public PageInfo<Movie> selectPage(Movie movie, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Movie> list = movieMapper.selectAll(movie);
        for(Movie m : list){
           this.setScore(m);
        }
        return PageInfo.of(list);
    }


    public Movie selectById(Integer id) {
        Movie movie = movieMapper.selectById(id);
        this.setScore(movie);
        return movie;
    }

    //random recommendation
    public List<Movie> selectRecommended(Integer movieId) {
        List<Movie> movies = this.selectAll(null);
        //Exclude the movie from the current detail page
        movies = movies.stream().filter(movie -> !movie.getId().equals(movieId)).collect(Collectors.toList());
        Collections.shuffle(movies); //random recommendation
        List<Movie> movieList = movies.stream().limit(3).collect(Collectors.toList());
        for (Movie movie : movieList){
            this.setScore(movie);
        }
        return movieList;
    }

    // ===============================
// New CF-based method with fallback
// ===============================
    public List<Movie> selectRecommendedCFWithFallback(Integer movieId, Integer userId) {
        // 1) Build user->(movie->score) from comment table
        Map<Integer, Map<Integer, Double>> userRatings = buildUserRatings();

        // 2) If user has no rating data => fallback
        if (!userRatings.containsKey(userId) || userRatings.get(userId).isEmpty()) {
            return selectRecommended(movieId);
        }

        // 3) Predict for all un-rated movies
        Map<Integer, Double> predictions = new HashMap<>();
        List<Movie> allMovies = this.selectAll(null);

        for (Movie m : allMovies) {
            Integer mid = m.getId();
            // skip if user already rated
            if (userRatings.get(userId).containsKey(mid)) {
                continue;
            }
            double predicted = predictRating(userId, mid, userRatings);
            if (predicted > 0) {
                predictions.put(mid, predicted);
            }
        }

        // 4) Sort by predicted rating desc
        List<Map.Entry<Integer, Double>> sorted = new ArrayList<>(predictions.entrySet());
        sorted.sort((a,b)-> Double.compare(b.getValue(), a.getValue()));

        // 5) Build final recommended list, exclude current movie
        List<Movie> result = new ArrayList<>();
        for (Map.Entry<Integer, Double> e : sorted) {
            Integer mid = e.getKey();
            if (mid.equals(movieId)) continue; // skip the detail movie

            Movie mo = movieMapper.selectById(mid);
            if (mo != null) {
                setScore(mo);
                result.add(mo);
            }
            if (result.size() >= 3) break; // top 3
        }

        // If CF didn't produce anything => fallback random
        if (result.isEmpty()) {
            return selectRecommended(movieId);
        }
        return result;
    }

    // Build user->(movie->score) from comment table
    private Map<Integer, Map<Integer, Double>> buildUserRatings() {
        Map<Integer, Map<Integer, Double>> userRatings = new HashMap<>();
        // read all comments
        List<Comment> allComments = commentMapper.selectAllComments();

        for (Comment c : allComments) {
            Integer u = c.getUserId();
            Integer m = c.getMovieId();
            Double s = c.getScore();
            if (u == null || m == null || s == null) continue;

            userRatings.putIfAbsent(u, new HashMap<>());
            userRatings.get(u).put(m, s);
        }
        return userRatings;
    }

    // The core user-based CF rating prediction
    private double predictRating(Integer userId, Integer movieId,
                                 Map<Integer, Map<Integer, Double>> userRatings) {
        // gather similarities
        List<Map.Entry<Integer, Double>> simList = new ArrayList<>();
        Map<Integer, Double> targetMap = userRatings.get(userId);

        for (Integer otherUser : userRatings.keySet()) {
            if (otherUser.equals(userId)) continue;
            double sim = cosineSimilarity(targetMap, userRatings.get(otherUser));
            simList.add(new AbstractMap.SimpleEntry<>(otherUser, sim));
        }

        // Print the collected similarity information
        System.out.println("[CF] user=" + userId + " similarity list size=" + simList.size());

        // sort desc by sim
        simList.sort((a,b)-> Double.compare(b.getValue(), a.getValue()));

        // Weighted average among top K neighbors
        int K = 2;
        double numerator = 0.0;
        double denominator = 0.0;
        int used = 0;

        for (Map.Entry<Integer, Double> entry : simList) {
            if (used >= K) break;
            Integer other = entry.getKey();
            double sim = entry.getValue();
            if (!userRatings.get(other).containsKey(movieId)) continue;
            double otherScore = userRatings.get(other).get(movieId);

            //Print the similarity of each neighbor and their rating for the movie
            System.out.println("[CF]   neighborUser=" + other +
                    ", sim=" + sim +
                    ", theirScore=" + otherScore);

            numerator += sim * otherScore;
            denominator += Math.abs(sim);
            used++;
        }

        double predicted = (denominator == 0) ? 0 : numerator / denominator;

        // final predicted result
        System.out.println("[CF] <= predictRating done: user=" + userId +
                ", movie=" + movieId +
                ", predictedScore=" + predicted);

        return predicted;
    }

    private double cosineSimilarity(Map<Integer, Double> mapA, Map<Integer, Double> mapB) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (Map.Entry<Integer, Double> e : mapA.entrySet()) {
            Integer mid = e.getKey();
            double a = e.getValue();
            normA += a*a;
            if (mapB.containsKey(mid)) {
                double b = mapB.get(mid);
                dot += a*b;
            }
        }
        for (Double val : mapB.values()) {
            normB += val*val;
        }
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA)*Math.sqrt(normB));
    }

    private void setScore(Movie movie){
        //Check how many reviews there are for the current movie
        int total = commentMapper.selectTotal(movie.getId());
        movie.setCommentNum(total);
        if(total== 0){
            movie.setScore(0D);
        }else{
            //Calculate the average score for the movie
            double sum = commentMapper.selectSum(movie.getId());
            BigDecimal score = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(total), 1, RoundingMode.HALF_UP);
            movie.setScore(score.doubleValue());
        }
    }

    // top 10 rating
    public List<Movie> selectRanking() {
        List<Movie> movieList = movieMapper.selectAll(null);
        for(Movie movie : movieList){
            setScore(movie);
        }
        return movieList.stream().sorted((m1, m2) -> m2.getScore().compareTo(m1.getScore())).limit(10).toList();
    }
}





