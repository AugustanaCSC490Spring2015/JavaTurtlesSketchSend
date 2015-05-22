package edu.augustana.javaturtles.sketchsend;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class MainMenu extends ActionBarActivity {

    private final String TAG = "SketchSend";

    private ImageButton sketchButton;
    private ImageButton inboxButton;
    private ImageButton contactsButton;
    private ParseUser currentUser;
    private ActionBar myBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        myBar = getSupportActionBar();
        myBar.setTitle("");


        UseParse parse = new UseParse();

        //Creating sketchButton, inboxButton, contactsButton, and attaching listeners
        sketchButton = (ImageButton) findViewById(R.id.sketchButton);
        sketchButton.setOnClickListener(sketchMenuButtonHandler);

        inboxButton = (ImageButton) findViewById(R.id.inboxButton);
        inboxButton.setOnClickListener(inboxButtonHandler);

        contactsButton = (ImageButton) findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(contactsButtonHandler);

        //gets current user from Parse
        currentUser = ParseUser.getCurrentUser();
        //if current user already exists, welcomes them back
        if (currentUser != null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Welcome back " + currentUser.getUsername(), Toast.LENGTH_SHORT);
            toast.show();

        } else {        //have user log in
            logIn();
        }
    }

    /*
    -Build alert dialog for user to input 'new user' information
    -name
    -email
    -password
     */
    public void createNewUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Create User");
        builder.setMessage("Please enter your Information");
        View dialogCustomView = inflater.inflate(R.layout.three_options_view, null);
        builder.setView(dialogCustomView);
        final EditText username = (EditText) dialogCustomView.findViewById(R.id.UsernameEditText3);
        final EditText email = (EditText) dialogCustomView.findViewById(R.id.EmailEditText3);
        final EditText password = (EditText) dialogCustomView.findViewById(R.id.PasswordEditText3);
        //In case it gives you an error for setView(View) try
        builder.setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //checks to make sure they entered
                if(password.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must enter a password", Toast.LENGTH_SHORT);
                    toast.show();
                    createNewUser();
                }
                //creates parse user object and sets all the fields for it
                currentUser = new ParseUser();
                currentUser.setUsername(username.getText().toString());
                currentUser.setEmail(email.getText().toString());
                currentUser.setPassword(password.getText().toString());

                //creates contact arraylist to associate with this user
                //adds user to their own contact list
                List<String> contactsList = new ArrayList<String>();
                contactsList.add(username.getText().toString());
                Gson contactsGson = new Gson();
                String serializedContacts = contactsGson.toJson(contactsList);

                currentUser.put("contactList", serializedContacts);
                finishSignIn();
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    /*
    -Creates alert dialog for user to log in
    -enter their username and password
    -if they don't already have an account, can go to createUser() method
     */
    private void logIn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setTitle("Log In");
        builder.setMessage("Please enter your Login Information");
        View dialogCustomView = inflater.inflate(R.layout.two_options_view, null);
        builder.setView(dialogCustomView);
        final EditText username = (EditText) dialogCustomView.findViewById(R.id.UsernameEditText2);
        final EditText password = (EditText) dialogCustomView.findViewById(R.id.PasswordEditText2);
        //In case it gives you an error for setView(View) try
        builder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //try to log in with info, if wrong credentials, recall logIn()
                try {
                    ParseUser.logIn(username.getText().toString(), password.getText().toString());
                    currentUser = ParseUser.getCurrentUser();
                    Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + currentUser.getUsername(), Toast.LENGTH_SHORT);
                    toast.show();
                } catch (ParseException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Login. Please Try Again", Toast.LENGTH_SHORT);
                    toast.show();
                    logIn();
                }
            }
        });
        //allows user to create new account if they don't have one already
        builder.setNegativeButton("Create Account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewUser();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /*
    -Welcomes new user if their credentials are OK
    -If not, calls createNewUser
     */
    private void finishSignIn() {
        Toast toast = Toast.makeText(getApplicationContext(), "Please wait while we check those credentials", Toast.LENGTH_SHORT);
        toast.show();
        currentUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Welcome, " + currentUser.getUsername(), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Combination of Credentials.  Please Try Again", Toast.LENGTH_SHORT);
                    toast.show();
                    createNewUser();
                }
            }
        });


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
            playGameIntent.putExtra("setSendVisible", false);
            startActivity(playGameIntent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    /*
    Allows user to Log out, which then calls logIn
    Also allows user to create a new account
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_user) {
            ParseUser.logOut();
            logIn();
            return true;
        }
        if (id == R.id.create_account) {
            ParseUser.logOut();
            createNewUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}