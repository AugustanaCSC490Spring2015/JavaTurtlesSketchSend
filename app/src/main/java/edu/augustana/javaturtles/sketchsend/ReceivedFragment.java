package edu.augustana.javaturtles.sketchsend;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceivedFragment extends Fragment {

    private ReceivedDrawingViewer drawingViewer;
    public int test=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_received, container, false);

        drawingViewer = (ReceivedDrawingViewer) view.findViewById(R.id.the_received_viewer);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    // when paused, MainGameFragment stops the game
    @Override
    public void onPause()
    {
        super.onPause();
    }

    // when MainActivity is over, releases game resources
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
