package edu.augustana.javaturtles.sketchsend;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;


public class ContactList extends ActionBarActivity {

    private static final String TAG = "ContactList";

    private ArrayList<String> contacts;
    private ArrayAdapter<String> adapter;
    private ListView contactsListView;
    private int totalContacts;
    private ActionBar myBar;
    private String serializedDrawing;
    private ParseObject user;
    private Bundle extras;
    private boolean fromIntent = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        extras = getIntent().getExtras();
        fromIntent = extras.getBoolean("setSendVisible");

        if (fromIntent) {
            ImageButton sendButton = (ImageButton) findViewById(R.id.sendDrawing);
            sendButton.setVisibility(View.VISIBLE);
            sendButton.setOnClickListener(sendDrawing);
        }

        user = ParseUser.getCurrentUser();

        myBar = getSupportActionBar();
        myBar.setTitle("Your Contacts");

        contacts = new ArrayList<String>();
        contactsListView = (ListView) findViewById(R.id.contactsListView);

        initializeContacts();

        contactsListView.setChoiceMode(CHOICE_MODE_MULTIPLE);

        ImageButton addContactButton = (ImageButton) findViewById(R.id.addContact);
        addContactButton.setOnClickListener(addContactButtonListener);

        contactsListView.setOnItemLongClickListener(itemLongClickListener);

    }

    //listener for sendButton
    //only visible when DrawActivity launches contacts
    //sends the selected contacts the drawing
    public View.OnClickListener sendDrawing = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<ParseObject> individualDrawings = new ArrayList<ParseObject>();
            if (extras != null) {
                serializedDrawing = extras.getString("stringToDraw");
            } else {
                serializedDrawing = "";
            }
            List<String> contacts = getSelectedContacts();
            // contacts = getSelectedContacts();
            for (int i = 0; i < contacts.size(); i++) {
                ParseObject drawing = new ParseObject("Drawings");
                drawing.put("fromUser", ParseUser.getCurrentUser().getUsername());
                drawing.put("toUser", contacts.get(i));
                drawing.put("drawingString", serializedDrawing);
                individualDrawings.add(drawing);
            }
            ParseObject.saveAllInBackground(individualDrawings);

            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    };

    //listener for addContactButton
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
                                        if (contacts.contains(possibleContact)) {
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
    //longClickListener to delete contacts
    //launches an AlertDialog to ask whether or not the user
    //wants to delete the contact
    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final String itemClicked = ((TextView) view).getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(ContactList.this);

            builder.setMessage("Do you want to delete " + itemClicked + "?");

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        // called when "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel(); // dismiss dialog
                        }
                    }
            );

            builder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener() {
                        // called when "Cancel" Button is clicked
                        public void onClick(DialogInterface dialog, int id) {
                            deleteContact(itemClicked);
                        }
                    } // end OnClickListener
            );
            builder.show();
            return true;
        }
    };

    //adds a contact and updates the totalContacts to represent the
    //number of contacts in the List.  Updates the ListView
    public void addContact(String newContact) {
        contacts.add(newContact);
        totalContacts++;
        adapter.notifyDataSetChanged();

    }

    //returns an ArrayList from the selected contacts
    public List<String> getSelectedContacts() {
        int count = contactsListView.getCount();
        List<String> contacts = new ArrayList<String>();
        SparseBooleanArray checked = contactsListView.getCheckedItemPositions();

        for (int i = 0; i < count; i++) {
            if (checked.get(i)) {
                contacts.add(this.contacts.get(i));
            }
        }
        return contacts;
    }

    //deletes a selected contact, updates the totalContacts and updates the ListView
    //you are unable to delete the last contact to avoid a null pointer exception
    public void deleteContact(String contactToDelete) {
        if (totalContacts > 1) {
            //get index to delete from array list
            contacts.remove(contactToDelete);
            //Update + store total number of saved contacts
            totalContacts--;
            adapter.notifyDataSetChanged();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "You don't want to delete all your friends!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Pulls the field from Parse.com that contacts the serialized String that is
    //the contacts ArrayList.  Deserializes the String into an ArrayList and attaches that
    //ArrayList to contactsListView and updates contactsListView
    public void initializeContacts() {
        String serializedContacts = user.getString("contactList");
        Gson gson = new Gson();
        contacts = new ArrayList<String>();
        Type collectionType = new TypeToken<Collection<String>>() {
        }.getType();
        contacts = gson.fromJson(serializedContacts, collectionType);
        totalContacts = contacts.size();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item, contacts);
        contactsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Takes the ArrayList contactList, serializes it to a String, and updates
    //the field on Parse.com
    public void serializeContacts() {
        Gson contactsGson = new Gson();
        String serializedContacts = contactsGson.toJson(contacts);
        user.put("contactList", serializedContacts);
        user.saveInBackground();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    //Uses to save the new contactsList into Parse.com upon leaving
    //the Activity
    @Override
    protected void onPause() {
        super.onPause();
        serializeContacts();
    }

    //Uses to save the new contactsList into Parse.com upon leaving
    //the Activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        serializeContacts();
    }
}
