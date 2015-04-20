package edu.augustana.javaturtles.sketchsend;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class DrawFragment extends Fragment {

    private DrawingView drawingView; // gameView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_draw, container, false);

        drawingView = (DrawingView) view.findViewById(R.id.mainDrawingView);
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
        drawingView.stopGame();
    }

    // when MainActivity is over, releases game resources
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        drawingView.releaseResources();
    }
}
