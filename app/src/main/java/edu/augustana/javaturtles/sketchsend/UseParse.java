package edu.augustana.javaturtles.sketchsend;


import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;


//Code primarily taken from http://stackoverflow.com/questions/25026185/how-to-know-when-parse-initialize-has-already-been-called
/*
Attaches our application to our specific Datastore on Parse.com
 */
public class UseParse extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "9CyX7Z3iEe1RmHQ5Kq1OOsYgCBjc1o1qL3pfgZJ3", "buzay1iQNk7LpMrVpyfCVCtJodFaaHHfY6RsAFeq");

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });




    }
}
