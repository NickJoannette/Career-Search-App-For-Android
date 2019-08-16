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
public class URLTextFragment extends Fragment {
    View v;
    public TextView URLtextview;



    public URLTextFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    public void setText(String text){

        URLtextview.setText(text);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.contactinfofragmenttab,container, false);
        Bundle contactInfo = getArguments();
        String URL = contactInfo.getString("URL");
        URLtextview = (TextView) rootview.findViewById(R.id.contact_info_tab_text);
        URLtextview.setText(URL);

        return rootview;

    }

}
