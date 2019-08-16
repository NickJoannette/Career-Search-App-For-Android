package com.example.freel.jobsearchapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomLocationFragment extends Fragment {
    public static Spinner countrySpinner;

    public interface OnDataPass {
        public void onDataPass(String data);
    }

    public CustomLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.customlocationfragment_tab,container, false);


        countrySpinner = (Spinner) rootview.findViewById(R.id.countrySpinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootview.getContext(),
                R.array.country_array, R.layout.country_spinner_item);
                // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.country_spinner_menu_item);
                // Apply the adapter to the spinner

        countrySpinner.setAdapter(adapter);


        AdapterView.OnItemSelectedListener selectedListener = (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("France")) {
                    MainActivity.searchBar.setHint("recherchez");
                    MainActivity.internshipCheckBox.setText("stage");
                    MainActivity.salaryTextView.setText("€" + String.valueOf(MainActivity.salaryTextViewAmount) + "+");
                    MainActivity.searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_france,0,0,0);
                    MainActivity.selectedCountry = "France";
                }

                if (parent.getItemAtPosition(position).toString().equals("United States")) {
                    MainActivity.searchBar.setHint("search");
                    MainActivity.internshipCheckBox.setText("internship/co-op");
                    MainActivity.salaryTextView.setText("$"+String.valueOf(MainActivity.salaryTextViewAmount) + "+");
                    MainActivity.searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_united_states,0,0,0);
                    MainActivity.selectedCountry = "United States";

                }

                if (parent.getItemAtPosition(position).toString().equals("United Kingdom")) {

                    MainActivity.searchBar.setHint("search");
                    MainActivity.internshipCheckBox.setText("internship/co-op");
                    MainActivity.salaryTextView.setText("£"+String.valueOf(MainActivity.salaryTextViewAmount) + "+");
                    MainActivity.searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_united_kingdom,0,0,0);
                    MainActivity.selectedCountry = "United Kingdom";}

                if (parent.getItemAtPosition(position).toString().equals("Germany")) {
                    MainActivity.searchBar.setHint("Suche");
                    MainActivity.internshipCheckBox.setText("praktikum");
                    MainActivity.salaryTextView.setText("£"+String.valueOf(MainActivity.salaryTextViewAmount) + "+");
                   // MainActivity.searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_united_kingdom,0,0,0);
                    MainActivity.selectedCountry = "Germany";}



                if (parent.getItemAtPosition(position).toString().equals("Canada")) {
                    MainActivity.searchBar.setHint("search");
                    MainActivity.internshipCheckBox.setText("internship/co-op");
                    MainActivity.salaryTextView.setText("$"+String.valueOf(MainActivity.salaryTextViewAmount) + "+");
                    MainActivity.searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_canada,0,0,0);
                    MainActivity.selectedCountry = "Canada";}

                if (parent.getItemAtPosition(position).toString().equals("China")){
                    MainActivity.searchBar.setHint("输入搜索字词");
                    MainActivity.internshipCheckBox.setText("实习");
                    MainActivity.salaryTextView.setText("¥"+String.valueOf(MainActivity.salaryTextViewAmount) + "+");
                    MainActivity.searchBar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_china,0,0,0);
                    MainActivity.selectedCountry = "China";}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        countrySpinner.setOnItemSelectedListener(selectedListener);

        return rootview;

    }

}
