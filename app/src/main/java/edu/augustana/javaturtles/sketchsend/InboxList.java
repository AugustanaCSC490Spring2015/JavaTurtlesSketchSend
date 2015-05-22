package edu.augustana.javaturtles.sketchsend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



public class InboxList extends ActionBarActivity {

    private static final String TAG = "Inbox";
    private ActionBar myBar;
    private Boolean status;
    private int newIndex = -1;
    private List<ParseObject> copyOfQueryResults;
    private List<String> combinedToDisplay;
    private List<String> serializedDrawing;
    private ArrayAdapter adapter;
    private ListView inboxList;
    private ParseQuery<ParseObject> query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_list);

        myBar = getSupportActionBar();
        myBar.setTitle("");
        
        queryParse();
    }
/*
   Queries Parse.com to find any entries that contain a toUser field
   equal to the currentUser.  When hte query is finished it calls createInbox
   passing in the query results in the form of List<ParseObjects>
 */
    public void queryParse() {
        query = ParseQuery.getQuery("Drawings");
        query.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    createInbox(parseObjects);
                } else {
                }
            }
        });

    }
/*
    Takes the List<ParseObjects> as a parameter and uses those objects to
    create the data to be viewed by the receiver.  It then updates the ListView
    with the data and stores the serialized String (drawing) in another ArrayList
    with the same index as the User and timestamp information.

 */
    public void createInbox(List<ParseObject> queryResults) {
        copyOfQueryResults = queryResults;
        List<String> fromUsers = new ArrayList<String>();
        List<String> timeStamps = new ArrayList<String>();
        combinedToDisplay = new ArrayList<>();
        serializedDrawing = new ArrayList<>();
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd hh:mm a");

        Collections.sort(copyOfQueryResults, new CustomComparator());

        for (ParseObject drawing: queryResults) {
            fromUsers.add(drawing.getString("fromUser"));
            Date dateToFormat = drawing.getCreatedAt();
            String formattedDate = DATE_FORMAT.format(dateToFormat);
            timeStamps.add(formattedDate);
            serializedDrawing.add(drawing.getString("drawingString"));
        }

        for (int i = 0; i < fromUsers.size(); i++) {
            combinedToDisplay.add("From: " + fromUsers.get(i) + "\nReceived: " + timeStamps.get(i));
        }
        inboxList = (ListView) findViewById(R.id.inboxList);
        adapter = new ArrayAdapter<>(this, R.layout.inbox_item, combinedToDisplay);
        //TREVOR trying to set boolean value + set drawable right
        /*
        for (ParseObject drawing: queryResults){
            newIndex++;
            status = drawing.getBoolean("newDrawing");
            if (status == null){

            }
            setDrawables(drawing, newIndex);
            */
        inboxList.setAdapter(adapter);
        //OnItemClickListener launches intent to draw the received image in ReceivedDrawing
        inboxList.setOnItemClickListener(inboxClickListener);
        //OnItemLingClickListener prompts user to delete drawing.  They can either delete
        //the drawing which will delete it on Parse.com and requery Parse.com to update
        //the ListView
        inboxList.setOnItemLongClickListener(longInboxClickListener);
    }


    /*
    public Drawable setDrawables(ParseObject drawing, int position) {
        Drawable img = getResources().getDrawable(R.drawable.newdrawing);
        if (drawing.getBoolean("newDrawing")) {
            inboxList.getItemAtPosition(position)
        }else {
           int img = R.drawable.
        }
    }
    */

    AdapterView.OnItemClickListener inboxClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent redraw = new Intent(InboxList.this, ReceivedDrawing.class);
            redraw.putExtra("stringToRedraw", serializedDrawing.get(position));
            startActivity(redraw);
        }
    };

    AdapterView.OnItemLongClickListener longInboxClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final int copyOfPosition = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(InboxList.this);

            builder.setMessage("Do you want to delete this drawing?");

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        // called when "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel(); // dismiss dialog
                        }
                    }
            );

            builder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener()
                    {
                        // called when "Delete" Button is clicked
                        public void onClick(DialogInterface dialog, int id)
                        {
                            toDelete(copyOfQueryResults, copyOfPosition);
                        }
                    } // end OnClickListener
            );builder.show();

            return true;
        }
    };

    public void toDelete(List<ParseObject> parseList, int position) {
        ParseObject parseObjectToDelete = parseList.get(position);
        parseObjectToDelete.deleteInBackground();
        serializedDrawing.remove(position);
        combinedToDisplay.remove(position);
        adapter.notifyDataSetChanged();
        //queryParse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inbox_list,menu);
        return true;
    }
/*
    Deletes all drawings in Inbox
    Code is redundant but maintains the recursive nature for updating
    the ListView
 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh){
            queryParse();
        }
        if(id == R.id.delete_all_drawings) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Drawings");
            query.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        for (ParseObject drawing:parseObjects) {
                            drawing.deleteInBackground();
                        }
                        combinedToDisplay.clear();
                        serializedDrawing.clear();
                        adapter.notifyDataSetChanged();
                    } else {
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}

class CustomComparator implements Comparator<ParseObject> {

    @Override
    public int compare(ParseObject lhs, ParseObject rhs) {
        return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
    }
}
