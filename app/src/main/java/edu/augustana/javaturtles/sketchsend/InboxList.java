package edu.augustana.javaturtles.sketchsend;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
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
import java.util.Date;
import java.util.List;



public class InboxList extends ActionBarActivity {

    private static final String TAG = "Inbox";
    private ActionBar myBar;

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
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Drawings");
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
        final List<ParseObject> copyOfQueryResults = queryResults;

        List<String> fromUsers = new ArrayList<String>();
        List<String> timeStamps = new ArrayList<String>();
        final List<String> combinedToDisplay = new ArrayList<String>();
        final List<String> serializedDrawing = new ArrayList<String>();
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HH:mm");
        for (ParseObject drawing:queryResults) {
            fromUsers.add(drawing.getString("fromUser"));
            Date dateToFormat = drawing.getCreatedAt();
            String formattedDate = DATE_FORMAT.format(dateToFormat);
            timeStamps.add(formattedDate);
            serializedDrawing.add(drawing.getString("drawingString"));
        }
        for (int i = 0; i < fromUsers.size(); i++) {
            combinedToDisplay.add("From: " + fromUsers.get(i) + "   Received: " + timeStamps.get(i));
        }
        ListView inboxList = (ListView) findViewById(R.id.inboxList);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.inbox_item, combinedToDisplay);
        inboxList.setAdapter(adapter);

        //OnItemClickListener launches intent to draw the received image in ReceivedDrawing
        inboxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent redraw = new Intent(InboxList.this, ReceivedDrawing.class);
                redraw.putExtra("stringToRedraw", serializedDrawing.get(position));
                startActivity(redraw);
            }
        });
        //OnItemLingClickListener prompts user to delete drawing.  They can either delete
        //the drawing which will delete it on Parse.com and requery Parse.com to update
        //the ListView
        inboxList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                ParseObject parseObjectToDelete = copyOfQueryResults.get(copyOfPosition);
                                parseObjectToDelete.deleteInBackground();
                                serializedDrawing.remove(copyOfPosition);
                                combinedToDisplay.remove(copyOfPosition);
                                queryParse();
                            }
                        } // end OnClickListener
                );builder.show();

                return true;
            }
        });
        adapter.notifyDataSetChanged();
    }
}
