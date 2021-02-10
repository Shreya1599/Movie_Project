package javaproject.controller;


import javaproject.model.genredata;
import javaproject.model.movie;
import javaproject.model.rating;
import javaproject.model.user;
import javaproject.utilities.parseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.lang.*;
import java.util.*;

@RestController                                                        //creation of rest API or web services
public class movie_rater {

            @Autowired                                                      //creates and bindes objects for the classes automatically
            parseData pdata;
    ArrayList<user> user_data = new ArrayList<user>();
    ArrayList<rating> rate_data = new ArrayList<rating>();
    ArrayList<movie> movie_data = new ArrayList<movie>();
    genredata genre_data[] = new genredata[20];
    Map<Integer, String> map1 = new HashMap<Integer, String>();               //contains movie details
    Map<Integer, String> map2 = new HashMap<Integer, String>();               // contains genre details




    @PostConstruct
    public void init(){                                          // used init method to load the given data
        try{                                                     //one time calling
            pdata.fetchuserdata(user_data);
            pdata.fetchratingdata(rate_data);
            pdata.fetchMoviedata(movie_data);
            pdata.fetchgenredata(genre_data);
            //map for storing movie and movieid
            pdata.loadMovieDataIntoMap(map1,movie_data);
            //map for storing genre and genreid
            pdata.loadGenreDataIntoMap(map2,genre_data);
            Collections.sort(rate_data, rating.comp);
        }
        catch (Exception e){
            System.out.println("Not able to load Files:");
        }

    }
    @GetMapping(value = "/getMostWatchedOrActiveUsers/{name}" ,produces = MediaType.APPLICATION_JSON_VALUE)   //associate URl with the method
    public String mostWatchedOrActiveUsers(@PathVariable("name") String name){
        int[] p = pdata.MostActiveUser_Movie(rate_data, user_data.size(), movie_data.size());
        System.out.println("Most Active User: " + p[0]);                         //returing id of user
        System.out.println("Most Watched Movie: " + p[1]);                       //returing id of movie
        if("Watched".equals(name)){
            return String.valueOf(p[0]);
        }
        if("Active".equals(name)){
            return String.valueOf(p[1]);
        }
        return "No data Found";
    }

    @GetMapping(value = "/getMostWatchedgenre" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> getMostWatchedGenre(){
        int MaxGenreId = pdata.mostwatchedgenre(rate_data, movie_data);
        Map<String,String> genreMap=new HashMap<>();
        genreMap.put("MostWatchedgenre",map2.get(MaxGenreId));
        return genreMap;
    }


    @GetMapping(value = "/getHighestRatedGenre" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> gethighestRatedGenre()
    {
        Map<String,String> genreMap=new HashMap<>();
        int HighestRatedGenre = pdata.getHighestRatedGenre(rate_data, movie_data);
        genreMap.put("HighestRatedGenre",map2.get(HighestRatedGenre));
        return genreMap;

    }


     @GetMapping(value = "/getTopMovieByGenre" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> getTopMovieBygenre() {
        Map<String,String> genreMap=new HashMap<>();
        int TopMovieByGenre[] = pdata.getTopMovieByGenre(rate_data, movie_data);
        for (int i = 0; i < 19; i++) {
            genreMap.put(map2.get(i),map1.get(TopMovieByGenre[i]));
        }
        return genreMap;
    }

    @GetMapping(value = "/getTopMovieByYear" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,List<String>> getTopMovieByYear() {
        Map<String,List<String>> TopMovieByGenre = pdata.getTopMovieByYear(rate_data, movie_data,map2);
        return TopMovieByGenre;
    }
}



