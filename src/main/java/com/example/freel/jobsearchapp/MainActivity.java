package com.example.freel.jobsearchapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    ViewPager locationViewPager; // create the viewpager
    Spinner countrySpinner; // declare the spinner for the countries
    TextView noResultsTextView; //
    ProgressBar progressBar;
    ProgressBar progressBar2;
    Button searchButton;

    // Declare the relevant UI components of the activity
    static SeekBar salarySeekBar;
    static TextView salaryTextView;
    static CheckBox internshipCheckBox;
    static AutoCompleteTextView searchBar;
    static String selectedCountry;
    static int salaryTextViewAmount = 30000;
    static ArrayList<String> urls = new ArrayList<String>();
    static ArrayList<String> indeed_job_keys = new ArrayList<String>();

    static String searchBarText;
    static ArrayList<String> jobs = new ArrayList<String>();
    static ArrayList<String> titles = new ArrayList<String>();


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    static ArrayList<String> getJobs(){
        return jobs;
    }
    static ArrayList<String> getJobKeys(){
        return indeed_job_keys;
    }

    void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static ArrayList<String> getTitles()
    {
        return titles;
    }

    public static void setTitles(String title)
    {
        getTitles().add(title);
    }

    public static ArrayList<String> getUrls(){return urls;}



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Reset incase user returned to main activiy for a new search
        jobs.clear();
        titles.clear();


        // Default selected country to US - maybe should change this dependent on user's GPS
        selectedCountry = "United States";

        locationViewPager = (ViewPager) findViewById(R.id.locationViewPager);
        MainFragmentPagerAdapter tabadapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());
        locationViewPager.setAdapter(tabadapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.Location_TabLayout);
        tabLayout.setupWithViewPager(locationViewPager);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2); // Identifying all visual elements of the page
        countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.country_array, R.layout.country_spinner_item); // Creates dropdown adapter?


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.country_spinner_menu_item);

        // Apply the adapter to the spinner
        countrySpinner.setAdapter(adapter);

        searchButton = (Button) findViewById(R.id.button);
        internshipCheckBox = (CheckBox) findViewById(R.id.internshipCheckBox);
        salarySeekBar = (SeekBar) findViewById(R.id.salarySeekBar);
        noResultsTextView = (TextView) findViewById(R.id.noResultsTextView);
        salaryTextView = (TextView) findViewById(R.id.salaryTextView);
        // Default to US salary display
        salaryTextView.setText("$"+String.valueOf(salaryTextViewAmount) + "+");
        salarySeekBar.setMax(100);
        searchBar = (AutoCompleteTextView) findViewById(R.id.searchBar);

        searchBar.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {

            // onEditorAction watch to hide the flashign input bar on off-click
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                    return true;
                }
                 return false;
            }
        });

        // onChangeListener for the salary seek bar. Re-updates according to selected country
        salarySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    String txt = "";

                    salaryTextViewAmount = 30000 + (progress * 2500);

                    if (selectedCountry.equals("United States")){ txt = "$" + String.valueOf(salaryTextViewAmount) + "+";
                        salaryTextView.setText(txt);}

                    else if (selectedCountry.equals("France")){ txt = "€" + String.valueOf(salaryTextViewAmount) + "+";
                        salaryTextView.setText(txt);}

                    else if (selectedCountry.equals("Canada")){ txt = "$" + String.valueOf(salaryTextViewAmount) + "+";
                        salaryTextView.setText(txt);}

                    else if (selectedCountry.equals("United Kingdom")){ txt = "£" + String.valueOf(salaryTextViewAmount) + "+";
                        salaryTextView.setText(txt);}

                    else if (selectedCountry.equals("Germany")){ txt = "€" + String.valueOf(salaryTextViewAmount) + "+";
                        salaryTextView.setText(txt);}

                    else if (selectedCountry.equals("China")){ txt = "¥" + String.valueOf(salaryTextViewAmount) + "+";
                        salaryTextView.setText(txt);}

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        } );
        // Hide the keyboard when the user clicks away from the searchar
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        searchButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                // When the search button is clicked; pass page = 1 argument to a new isntance of the getjobkeys class
                searchBarText = searchBar.getText().toString();

                GetIndeedJobKeys getIndeedJobKeys = new GetIndeedJobKeys(1);
                getIndeedJobKeys.execute();

                GetJobListings getJobListings = new GetJobListings(v.getContext(), 5);
                getJobListings.execute();

            }
        });

    }

    public class GetIndeedJobKeys extends AsyncTask<Void, Void, ArrayList<String>> {

        private int pages;

        public GetIndeedJobKeys(int pgs) {
            pages = 1;
        }

        @Override
        protected void onPreExecute()
        {
            // Clear the old job keys array to prepare for new ones
            getJobKeys().clear();

            super.onPreExecute();
            // Assure that the "no results found" text is hidden during search
            noResultsTextView.setVisibility(View.INVISIBLE);
            // Set the circular loading progress bar's visibility to true
            progressBar2.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... voids) {

            super.onProgressUpdate(voids);
            // On completion, if the activity has not changed (case where no resuls were found)
            progressBar2.setVisibility(View.INVISIBLE);

            noResultsTextView.setText("No results were found for: " + searchBarText);
            noResultsTextView.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            // Begin conditional decisions based on user interaction with UI to determine the Indeed URL to search
            String site = "";
            try {
                switch (selectedCountry)
                {
                    case "United States":
                         site = "http://www.indeed.com/jobs?q=";
                        break;
                    case "France":
                         site ="http://www.indeed.fr/emplois?q=";
                         break;

                    case "Germany":
                        site ="http://de.indeed.com/jobs?q=";
                        break;

                    case "United Kingdom":
                        site ="http://www.indeed.co.uk/jobs?q=";
                        break;

                    case "Canada":
                        site = "http://www.indeed.ca/jobs?q=";
                        break;

                    case "China":
                        site = "http://cn.indeed.com/jobs?q=";
                        break;

                    default: site = "http://www.indeed.com/jobs?q=";
                    break;

                }

                Scanner reader = new Scanner(searchBarText);
                Document document;

                while (reader.hasNext()) {

                    site += reader.next() + "%20";

                }

                switch (selectedCountry)
                {
                    case "United States":
                        site += "&$"+ String.valueOf(salaryTextViewAmount) + "&l=United%20States&start=0" + String.valueOf((pages*50)-50) +"1&limit=50";
                        break;

                    case "France":
                        site += "&€"+ String.valueOf(salaryTextViewAmount) + "&l=France&start=0" + String.valueOf((pages*50)-50) +"1&limit=50";
                        break;

                    case "Germany":
                        site += "&€"+ String.valueOf(salaryTextViewAmount) + "&l=Germany&start=0" + String.valueOf((pages*50)-50) +"1&limit=50";
                        break;

                    case "United Kingdom":
                        site += "&€"+ String.valueOf(salaryTextViewAmount) + "&l=england&start=0" + String.valueOf((pages*50)-50) +"1&limit=50";
                        break;

                    case "China":
                        site += "&¥"+ String.valueOf(salaryTextViewAmount) + "&l=china&start=0" + String.valueOf((pages*50)-50) +"1&limit=50";
                        break;

                    case "Canada":
                        site += "&$"+ String.valueOf(salaryTextViewAmount) + "&l=Canada&start=0" + String.valueOf((pages*50)-50) +"1&limit=50";
                        break;

                }

                if (internshipCheckBox.isChecked()) site+= "&jt=internship";
                document = Jsoup.connect(site).get();
                String doc = document.toString();
                String slice;
                if (doc.contains("jobKeysWithInfo['")) {
                    slice = doc.substring(doc.indexOf("jobKeysWithInfo['"), doc.indexOf("if (vjk && !jobKeysWithInfo."));
                    reader = new Scanner(slice);

                    while (reader.hasNext()) {
                        String line = reader.nextLine();
                        reader.nextLine();
                        indeed_job_keys.add(line.substring(line.indexOf("['") + 2, line.indexOf("']")));
                        if (!reader.hasNext()) break;
                    }
                } else publishProgress();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return indeed_job_keys;
        }

        @Override
        protected void onPostExecute(ArrayList<String> jk) {
            super.onPostExecute(jk);

           // Intent goToListingsIntent = new Intent(activity, DisplayResultsActivity.class);
           // goToListingsIntent.putStringArrayListExtra("job_keys", jk);

            // textview = (TextView)activity.findViewById(R.id.textView);
            //textview.setText(job_keys.get(0));
        }


    }

    public class GetJobListings extends AsyncTask<Void, Void, Void>
    {

        private String job_listing = "";
        private Context activity;
        // private ArrayList<String> job_keys;
        private int listIndex;
        TextView textview;

        public GetJobListings(Context context, int index)
        {
            activity = context;
            // job_keys = jobkeys;
            listIndex = index;

        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
         //   progressBar.setVisibility(VISIBLE);

        //    progressBar.setMax(listIndex);

        }

        @Override
        protected void onProgressUpdate(Void...voids) {
            super.onProgressUpdate(voids);

            // Update the ProgressBar
           // progressBar.incrementProgressBy(1);
        }

        @Override
        protected Void doInBackground(Void...voids)
        {


            int offset=0;
            if (indeed_job_keys.size()>0 && indeed_job_keys.size() < listIndex) {
                listIndex = indeed_job_keys.size();
            }
            if(indeed_job_keys.size()!=0){
            for (int i = 0; i < listIndex; i++){

            try {

                String job_listing = "";
                Document document;
                Scanner reader;
                document = Jsoup.connect("https://www.indeed.com/viewjob?jk=" + indeed_job_keys.get(i)).get();
                urls.add("https://www.indeed.com/viewjob?jk=" + indeed_job_keys.get(i));
                String doc = document.toString();
                String slice;
                String title = document.title(); // Get rid of - Indeed.com suffix
                title = title.substring(0,title.indexOf("- Indeed"));
                Elements underlines = document.select("li:not(:has(a)),br>p:not(:has(li))");
                  if (underlines.size() < 1)
                {

                    underlines = document.select("p");
                }

                for (int j = 0; j < underlines.size(); j ++)
                {
                    job_listing += "\u2022 " + (underlines.get(j).text())+"\n\n";
                }

                setTitles(title);
                jobs.add(job_listing);


            } catch (IOException e) {
                e.printStackTrace(); }}}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if (indeed_job_keys.size()!=0) {
                Intent goToListingsIntent = new Intent(activity, DisplayResultsActivity.class);
                goToListingsIntent.putStringArrayListExtra("jobs", jobs);
                goToListingsIntent.putStringArrayListExtra("job_keys", indeed_job_keys);
                goToListingsIntent.putExtra("country_choice", selectedCountry);
              // goToListingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(goToListingsIntent);
                progressBar2.setVisibility(View.INVISIBLE);
            }
            else {progressBar2.setVisibility(View.INVISIBLE); }

            //progressBar.setVisibility(View.INVISIBLE);
            //progressBar.incrementProgressBy(-progressBar.getProgress());
        }



    }



}


