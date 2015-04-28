package edu.augustana.javaturtles.sketchsend;


import android.app.ListActivity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;


public class ContactList extends ActionBarActivity {

    private static final String YOUR_CONTACTS = "Your Contacts";
    private static final String TAG = "ContactList";

    private EditText newContact;
    private ArrayList<String> contactsList;
    private SharedPreferences savedContacts;
    private int contactNumber;
    private ArrayAdapter<String> adapter;
    private ListView contactsListView;
    private int totalContacts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactsList = new ArrayList<String>();
        newContact = (EditText) findViewById(R.id.contactsEditText);
        contactsListView = (ListView) findViewById(R.id.contactsListView);
        savedContacts = getSharedPreferences(YOUR_CONTACTS, MODE_PRIVATE);
        initializeContacts();

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, contactsList);
        contactsListView.setAdapter(adapter);
        contactsListView.setChoiceMode(CHOICE_MODE_MULTIPLE);

        Button addContactButton = (Button) findViewById(R.id.addContact);
        addContactButton.setOnClickListener(addContactButtonListener);


    }

    //    listener for addContactButton
    //also handles checking if the contact is a viable account on parse.com
    //does not allow for duplicate contacts
public View.OnClickListener addContactButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (newContact.getText().length() > 0) {
            final String possibleContact = newContact.getText().toString();
            final ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", possibleContact);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> parseUsers, com.parse.ParseException e) {
                    if (e == null) {
                        try {
                            if (contactsList.contains(possibleContact)) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Contact already exists", Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                if (query.count() == 1) {
                                    addContact(possibleContact);
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "That is not a valid account", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        } catch (com.parse.ParseException e1) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Oops an error occurred!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Oops an error occurred!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            });
            newContact.setText("");
        }
    }
};


    public void addContact(String newContact){
        contactsList.add(newContact);
        contactNumber = contactsList.size()-1;
        totalContacts++;
        SharedPreferences.Editor preferencesEditor = savedContacts.edit();
        preferencesEditor.putString("contact" + contactNumber, newContact);
        preferencesEditor.putInt("totCont", totalContacts);

        preferencesEditor.apply();

        adapter.notifyDataSetChanged();

    }

    public void deleteContact(String contactToDelete) {
        contactsList.remove(contactToDelete);
        //might need to do something about changing how contactNumber keys are created?
        totalContacts--;
        SharedPreferences.Editor preferencesEditor = savedContacts.edit();

        preferencesEditor.apply();

        adapter.notifyDataSetChanged();
    }

    public void initializeContacts(){
        String nameTest;
        totalContacts = savedContacts.getInt("totCont", 0);
        for( int i = 0; i < totalContacts; i++){
            nameTest = savedContacts.getString("contact" + i, "Logan Kruse");
            Log.d("INIT TEST", nameTest);
            contactsList.add(nameTest);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
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



}
