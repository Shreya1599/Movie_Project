package javaproject.utilities;


import javaproject.constants.Constants;
import javaproject.model.genredata;
import javaproject.model.movie;
import javaproject.model.rating;
import javaproject.model.user;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class parseData {


    public static void fetchuserdata(ArrayList<user> nuser_data) throws FileNotFoundException, IOException {
        String path = "/home/shreyagupta/Downloads/MovieProject/src/main/java/javaproject/user.csv";
        BufferedReader br = new BufferedReader(new FileReader(path));

        StringTokenizer token;
        String line;

        while ((line = br.readLine()) != null) {
            token = new StringTokenizer(line, "|");
            user temp= new user();
            temp.id = Integer.parseInt(token.nextToken());
            temp.age = Integer.parseInt(token.nextToken());
            temp.gender = token.nextToken();
            temp.occ = token.nextToken();
            temp.zip = token.nextToken();
            nuser_data.add(temp);
        }




    }

    public static void fetchratingdata(ArrayList<rating> rate_data) throws FileNotFoundException, IOException {
        File path = new File("/home/shreyagupta/Downloads/MovieProject/src/main/java/javaproject/ratings.data");
        Scanner scan1=new Scanner (path);
        while (scan1.hasNext()) {
            rating temp= new rating();
            temp.id = Integer.parseInt(scan1.next());
            temp.movieid = Integer.parseInt(scan1.next());
            temp.rate = Integer.parseInt(scan1.next());
            temp.time = Integer.parseInt(scan1.next());
            rate_data.add(temp);
        }


    }

    public static void fetchMoviedata(ArrayList<movie> movie_data) throws FileNotFoundException, IOException {
        String path = "/home/shreyagupta/Downloads/MovieProject/src/main/java/javaproject/movie.csv";
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values=line.split("\\|");
            movie temp = new movie();
            temp.id = Integer.parseInt(values[0]);
            temp.title =values[1];
            temp.date = values[2];
            temp.imdb = values[4];
            for (int j = 0; j <19; j++) {
                temp.genre.add(Integer.parseInt(values[5+j]));
            }
            movie_data.add(temp);
        }

    }


    public static void fetchgenredata(genredata[] genre_data) throws FileNotFoundException, IOException {
        String path = "/home/shreyagupta/Downloads/MovieProject/src/main/java/javaproject/genre.csv";
        BufferedReader br = new BufferedReader(new FileReader(path));

        StringTokenizer token;
        String line;
        int i = 0;
        int k = 0;
        while ((line = br.readLine()) != null && i < 19) {
            token = new StringTokenizer(line, "|");

            genre_data[i] = new genredata();
            genre_data[i].genre = token.nextToken();
            genre_data[i].genreid = Integer.parseInt(token.nextToken());

            i++;
        }




    }

    public static int[] MostActiveUser_Movie(ArrayList<rating>rate_data,int NumUsers,int NumMovies) {
        int[] UserFreq=new int[NumUsers+1];                          //checking the frequency of each user in rating.data file
        int[] MovieFreq=new int[NumMovies+1];                       // checking the frequency of each movie in rating.data file
        int MaxUser=0,MaxMovie=0,MaxUserId=0,MaxMovieId=0;
        for(int i=0;i<rate_data.size();i++)
        {
            UserFreq[rate_data.get(i).id]++;
            MovieFreq[rate_data.get(i).movieid]++;
        }
        for(int i=1;i<=NumUsers;i++)                             // checking for the maximum frequency for userID
        {
            if(MaxUser<UserFreq[i])
            {
                MaxUser=UserFreq[i];
                MaxUserId=i;
            }
        }
        for(int i=1;i<=NumMovies;i++)                          //checking for the maximum frequency for movie
        {
            if(MaxMovie<MovieFreq[i])
            {
                MaxMovie=MovieFreq[i];
                MaxMovieId=i;
            }
        }
        int[] Obj=new int[2];                             //storing both the values into the object
        Obj[0]=MaxUserId;
        Obj[1]=MaxMovieId;
        return Obj;
    }
    public static int  mostwatchedgenre(ArrayList<rating> rate_data,ArrayList<movie> movie_data) {
        int[] genreFreq=new int[19];
        for (int i = 0; i < rate_data.size(); i++) {
            int mid=rate_data.get(i).movieid;
            for(int j=0;j<19;j++){
                if(movie_data.get(mid).genre.get(j)==1){
                    genreFreq[j]++;
                }
            }
        }
        int MaxGenre=0;
        int MaxGenreId=0;
        for(int i=0;i<19;i++){
            if (MaxGenre < genreFreq[i]) {
                MaxGenre=genreFreq[i];
                MaxGenreId=i;
            }
        }
        return MaxGenreId;
    }

    public static int getHighestRatedGenre(ArrayList<rating> rate_data, ArrayList<movie> movie_data) {
        int[] GenreFreq=new int[19];
        for(int i=0;rate_data.get(i).rate==5;i++)                                 //filtering movie with rating of 5
        {
            int mid=rate_data.get(i).movieid;
            for(int j=0;j<19;j++)
            {
                if(movie_data.get(mid).genre.get(j)==1 )
                {
                    GenreFreq[j]++;
                }
            }
        }
        int MaxGenre=0,MaxGenreId=0;
        for(int i=0;i<19;i++)
        {
            if(MaxGenre<GenreFreq[i])
            {
                MaxGenre=GenreFreq[i];
                MaxGenreId=i;
            }
        }
        return MaxGenreId;
    }

    public static int[] getTopMovieByGenre(ArrayList<rating> rate_data, ArrayList<movie> movie_data) {
        int obj[]=new int[20];
        for(int i=0;i<rate_data.size();i++)
        {
            int mid=rate_data.get(i).movieid;
            for(int j=0;j<19;j++)
            {
                if(movie_data.get(mid).genre.get(j)==1 && obj[j]==0)
                {
                    obj[j]=mid;
                }
            }
        }
        return obj;
    }

    public Map getTopMovieByYear(ArrayList<rating> rate_data, ArrayList<movie> movie_data,Map<Integer,String> map2){
        Map<String,List<String>> movielistbyYear=new HashMap<>();
        Map<Integer,Integer> movie_rating=new LinkedHashMap<>();
        boolean moviearray[]=new boolean[movie_data.size()];
         //parseMovieData(movie_data);
        for(int i=0;i<rate_data.size();i++){
            if(movie_rating.get(rate_data.get(i).movieid)==null){
                movie_rating.put(rate_data.get(i).movieid,rate_data.get(i).rate);
            }
        }
        Set<Integer> keys=movie_rating.keySet();
        Iterator it = keys.iterator();
        for(;it.hasNext();it.next())
        {
            int movie_id= (int)it.next();
            //int rating=movie_rating.get(movie_id-1).intValue();
            if(!moviearray[movie_id-1]){
                if(movielistbyYear.get(movie_data.get(movie_id-1).date)==null){
                    List<String> moviename=new ArrayList<String>();
                    moviename.add(movie_data.get(movie_id-1).title);
                    movielistbyYear.put(movie_data.get(movie_id-1).date,moviename);
                }
                else {
                    List<String> moviename=movielistbyYear.get(movie_data.get(movie_id-1).date);
                    moviename.add(movie_data.get(movie_id-1).title);
                    movielistbyYear.put(movie_data.get(movie_id-1).date,moviename);
                }
                moviearray[movie_id-1]=true;
            }
        }
         return movielistbyYear;
    }


    public void loadMovieDataIntoMap(Map map1, List<movie> movie_data){
        for (int i = 0; i < movie_data.size(); i++) {                                // creating key & value pair by using id and tittle
            map1.put(movie_data.get(i).id, movie_data.get(i).title);
        }
    }

    public void loadGenreDataIntoMap(Map map2, genredata[] genre_data){              //creating key & value pair by using genreid and genre
        for (int i = 0; i < 19; i++) {
            map2.put(genre_data[i].genreid, genre_data[i].genre);
        }
    }
}





