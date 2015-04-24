package edu.augustana.javaturtles.sketchsend;


import android.app.Application;
import com.parse.Parse;


/**
 * Created by Benjamin on 4/20/2015.
 */

//Code primarily taken from http://stackoverflow.com/questions/25026185/how-to-know-when-parse-initialize-has-already-been-called
public class UseParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9CyX7Z3iEe1RmHQ5Kq1OOsYgCBjc1o1qL3pfgZJ3", "buzay1iQNk7LpMrVpyfCVCtJodFaaHHfY6RsAFeq");





    }
}
