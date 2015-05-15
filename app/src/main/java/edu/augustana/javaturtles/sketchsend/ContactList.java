package edu.augustana.javaturtles.sketchsend;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;


public class ContactList extends ActionBarActivity {

    private static final String YOUR_CONTACTS = "Your Contacts";
    private static final String TAG = "ContactList";

    private ArrayList<String> contactsList;
    private SharedPreferences savedContacts;
    private int contactNumber;
    private ArrayAdapter<String> adapter;
    private ListView contactsListView;
    private int totalContacts;
    private ActionBar myBar;
    private String serializedDrawing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        myBar = getSupportActionBar();
        myBar.setTitle("Your Contacts");

        contactsList = new ArrayList<String>();
        contactsListView = (ListView) findViewById(R.id.contactsListView);
        savedContacts = getSharedPreferences(YOUR_CONTACTS, MODE_PRIVATE);
        initializeContacts();

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, contactsList);
        contactsListView.setAdapter(adapter);
        contactsListView.setChoiceMode(CHOICE_MODE_MULTIPLE);

        ImageButton addContactButton = (ImageButton) findViewById(R.id.addContact);
        addContactButton.setOnClickListener(addContactButtonListener);

        contactsListView.setOnItemLongClickListener(itemLongClickListener);

    }

    //    listener for addContactButton
    //also handles checking if the contact is a viable account on parse.com
    //does not allow for duplicate contacts
public View.OnClickListener addContactButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ContactList.this);

        builder.setTitle("Add a new contact:");

        final EditText input = new EditText(ContactList.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().length() > 0) {
                    final String possibleContact = input.getText().toString();
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
                    input.setText("");
            } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You didn't enter any characters!", Toast.LENGTH_SHORT);
                    toast.show();
                }
           }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }
};

    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final String itemClicked = ((TextView) view).getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(ContactList.this);

            builder.setMessage("Do you want to delete " + itemClicked + "?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                            {
                                // called when "Cancel" Button is clicked
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    dialog.cancel(); // dismiss dialog
                                }
                            }
                    );

            builder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener()
                    {
                        // called when "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id)
                        {
                            deleteContact(itemClicked);
                        }
                    } // end OnClickListener
            );builder.show();

            return true;
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

    public List<String> getSelectedContacts(){
        int count = contactsListView.getCount();
        List<String> contacts = new ArrayList<String>();
        SparseBooleanArray checked = contactsListView.getCheckedItemPositions();

        for (int i=0; i < count; i++){
            if (checked.get(i)){
                contacts.add(contactsList.get(i));
            }
        }
        return contacts;
    }

    public void deleteContact(String contactToDelete) {
        //get index to delete from array list
        contactNumber = contactsList.indexOf(contactToDelete);
        contactsList.remove(contactToDelete);

        SharedPreferences.Editor prefs = savedContacts.edit();

        //Update + store total number of saved contacts
        totalContacts--;
        prefs.putInt("totCont", totalContacts);

        prefs.remove("contact" + contactNumber); // remove contact from save

        for (int i = 0; i < contactsList.size(); i++){
            prefs.putString("contact" + i, contactsList.get(i));
        }

        prefs.apply(); // saves the changes

        // rebind ArrayList to ListView to show updated list
        adapter.notifyDataSetChanged();
    }

    public void initializeContacts(){
        String nameTest;
        totalContacts = savedContacts.getInt("totCont", 0);
        for( int i = 0; i < totalContacts; i++){
            nameTest = savedContacts.getString("contact" + i, "Logan Kruse");
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

        if (id== R.id.selected_contacts){
            List<ParseObject> individualDrawings = new ArrayList<ParseObject>();
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                serializedDrawing = extras.getString("stringToDraw");
                Log.w(TAG, "Has Drawing");
            } else {
                serializedDrawing = "";
                Log.w(TAG, "Null for Drawing");
            }
            List<String> contacts= getSelectedContacts();
            // contacts = getSelectedContacts();
            for (int i = 0; i < contacts.size() ; i++){
                ParseObject drawing = new ParseObject("Drawings");
                Log.w(TAG, "for loop tick " + i);
                drawing.put("fromUser", ParseUser.getCurrentUser().getUsername());
                drawing.put("toUser", contacts.get(i));
                drawing.put("drawingString", serializedDrawing);
                individualDrawings.add(drawing);
            }
            ParseObject.saveAllInBackground(individualDrawings);

        }

        return super.onOptionsItemSelected(item);
    }



}
