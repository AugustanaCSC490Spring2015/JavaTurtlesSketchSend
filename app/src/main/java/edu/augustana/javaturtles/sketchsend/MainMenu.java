package edu.augustana.javaturtles.sketchsend;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseObject;


public class MainMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
       /* try {
            Parse.enableLocalDatastore(this);
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
         Parse.initialize(this, "9CyX7Z3iEe1RmHQ5Kq1OOsYgCBjc1o1qL3pfgZJ3", "buzay1iQNk7LpMrVpyfCVCtJodFaaHHfY6RsAFeq"); */

        //do something on Parse.com
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        //Creating sketchButton, inboxButton, contactsButton, and attaching listeners
        Button sketchButton=(Button) findViewById(R.id.sketchButton);
        sketchButton.setOnClickListener(sketchMenuButtonHandler);

        Button inboxButton=(Button) findViewById(R.id.inboxButton);
        inboxButton.setOnClickListener(inboxButtonHandler);

        Button contactsButton=(Button) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(contactsButtonHandler);

    }

    //Listener for sketchMenuButton
    View.OnClickListener sketchMenuButtonHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent playGameIntent=new Intent(MainMenu.this, DrawActivity.class);
            startActivity(playGameIntent);
        }
    };

    //Listener for inboxButton
    View.OnClickListener inboxButtonHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent playGameIntent=new Intent(MainMenu.this, InboxList.class);
            startActivity(playGameIntent);
        }
    };

    //Listener for contactsButton
    View.OnClickListener contactsButtonHandler=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent playGameIntent=new Intent(MainMenu.this, ContactList.class);
            startActivity(playGameIntent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}