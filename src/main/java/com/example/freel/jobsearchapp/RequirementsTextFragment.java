package com.example.freel.jobsearchapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequirementsTextFragment extends Fragment {
    View v;
    public TextView txtOne;



    public RequirementsTextFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    public void setText(String text){

        txtOne.setText(text);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.requirementsfragmenttab,container, false);
        Bundle joblisting = getArguments();
        String jl = joblisting.getString("joblisting");
        txtOne = (TextView) rootview.findViewById(R.id.requirements_tab_text);
        txtOne.setText(jl);

        return rootview;

    }

}
