package edu.augustana.javaturtles.sketchsend;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ReceivedDrawing extends ActionBarActivity {

    private ReceivedDrawingViewer theReceivedViewer;
    public String serializedDrawing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_drawing);

        theReceivedViewer = (ReceivedDrawingViewer) findViewById(R.id.the_received_viewer);

        Bundle bundle = getIntent().getExtras();
        serializedDrawing = bundle.getString("stringToRedraw");

        theReceivedViewer.setDrawingString(serializedDrawing);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_received_drawing, menu);
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
