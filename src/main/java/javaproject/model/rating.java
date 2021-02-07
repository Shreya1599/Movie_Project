package javaproject.model;


import java.util.Comparator;

//rating java
public class rating {
    public int id;
    public int movieid;
    public int rate;
    public int time;

    //Trying to sort acc. to rate col in rating file
    public static Comparator<rating> comp = new Comparator<rating>() {

        public int compare(rating s1, rating s2) {

            int rating1 = s1.rate;
            int rating2 = s2.rate;

            /*For ascending order*/
            return rating2-rating1;

            /*For descending order*/
            //rollno2-rollno1;
        }};
}

