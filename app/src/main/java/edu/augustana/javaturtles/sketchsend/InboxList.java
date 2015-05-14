package edu.augustana.javaturtles.sketchsend;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;


public class InboxList extends ActionBarActivity {

    private static final String TAG = "Inbox";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_list);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Drawings");
        query.whereEqualTo("toUser", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                   createInbox(parseObjects);
                } else {
                    Log.d("Inbox", "Error:");
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_user) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createInbox(List<ParseObject> fromQuery) {
        List<String> fromUser = new ArrayList<String>();
        final List<String> serializedDrawing = new ArrayList<String>();
        for (int i = 0; i < fromQuery.size(); i++) {
            fromUser.add(fromQuery.get(i).getString("fromUser") + " " + i);
            serializedDrawing.add(fromQuery.get(i).getString("drawingString"));
        }
        ListView inboxList = (ListView) findViewById(R.id.inboxList);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, fromUser);
        inboxList.setAdapter(adapter);

//        AdapterView.OnItemClickListener inboxOnClickListener = null;
        inboxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(TAG, "Got to Intent");
                Intent redraw = new Intent(InboxList.this, ReceivedDrawing.class);
                redraw.putExtra("stringToRedraw", serializedDrawing.get(position));
                Log.w(TAG,"STARTING REDRAW");
                startActivity(redraw);
            }
        });
        adapter.notifyDataSetChanged();
    }

    //Dialog for "Add" button
}
