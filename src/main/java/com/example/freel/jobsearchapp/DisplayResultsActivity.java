/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.freel.jobsearchapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



public class DisplayResultsActivity extends AppCompatActivity {

    private static final int NUM_LIST_ITEMS = 100;
    static boolean noMoreJobs = false;

   static com.example.freel.jobsearchapp.GreenAdapter mAdapter;
   static LinearLayoutManager layoutManager;
   static RecyclerView mNumbersList;
    int indeedPageNumber = 1;

    static String country_selection;
    FragmentPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        mNumbersList = (RecyclerView) findViewById(R.id.rv_data);
        layoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(layoutManager);

        ArrayList<String> jobs = MainActivity.getJobs();
        ArrayList<String> titles = MainActivity.getTitles();
        ArrayList<String> job_keys = MainActivity.getJobKeys();
        ArrayList<String> urls = MainActivity.getUrls();

        country_selection = getIntent().getStringExtra("country_choice");
        mAdapter = new com.example.freel.jobsearchapp.GreenAdapter( this, jobs, titles, job_keys, urls, mNumbersList);
        final Handler handler = new Handler();
        mNumbersList.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                mAdapter.jobs.add(null);
                mAdapter.notifyItemInserted(mAdapter.jobs.size()-1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.jobs.remove(mAdapter.jobs.size() - 1);
                        mAdapter.notifyItemRemoved(mAdapter.jobs.size());
                        //   remove progress item
                        GetJobListings2 getMoreJobListings = new GetJobListings2();
                        getMoreJobListings.execute();

                        //add items one by one

                       // GetMoreJobKeys getMoreJobKeys = new GetMoreJobKeys(++MainActivity.pagenum);
                       // getMoreJobKeys.execute();

                           // mAdapter.notifyDataSetChanged();
                           // mAdapter.setLoaded();

                        }

                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                , 2500);

            };
        });

    }



    public class GetJobListings2 extends AsyncTask<Void, Void, Void> {

        private String job_listing = "";
        private Context activity;
        // private ArrayList<String> job_keys;
        private int listIndex;
        TextView textview;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate(voids);

            mAdapter.notifyDataSetChanged();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            //ArrayList<String> jobs = new ArrayList<String>();
            int i = 0;
            int offset = 0;
            int doFor = 10;
            if (mAdapter.job_keys.size() - mAdapter.jobs.size() < 5){doFor = mAdapter.job_keys.size() - mAdapter.jobs.size();}
            while (i<doFor) {


                try {
                    String job_listing = "";
                    Document document;
                    Scanner reader;
                    document = Jsoup.connect("https://www.indeed.com/viewjob?jk=" + mAdapter.job_keys.get(mAdapter.jobs.size())).get();
                    mAdapter.urls.add("https://www.indeed.com/viewjob?jk=" + mAdapter.job_keys.get(mAdapter.jobs.size()));
                    String doc = document.toString();
                    String slice;
                    String title = document.title(); // Get rid of - Indeed.com suffix
                    //title = title.substring(0, title.indexOf("- Indeed"));
                    Elements underlines = document.select("li:not(:has(a)),br>p:not(:has(li))");
                    if (underlines.size()<1){
                        underlines = document.select("p");
                    }

                    for (int j = 0; j < underlines.size(); j++) {
                        job_listing += "\u2022 " + (underlines.get(j).text()) + "\n\n";
                    }

                    mAdapter.jobs.add(job_listing);
                    mAdapter.titles.add(title);
                    if (mAdapter.jobs.size()>mAdapter.job_keys.size()-15 && noMoreJobs==false)
                    {
                        GetMoreJobKeys getMoreJobKeys = new GetMoreJobKeys(indeedPageNumber);
                        getMoreJobKeys.execute();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                publishProgress();
                i++;
            }

            mAdapter.setLoaded();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public static class GetMoreJobKeys extends AsyncTask<Void, Void, ArrayList<String>> {

        // static private TextView textview;
        // static private MainActivity activity;

        private int pages;

        public GetMoreJobKeys(int pgs) {

            // job_keys = new ArrayList<String>();
            pages = pgs;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            // progressBar2.setVisibility(View.VISIBLE);

        }
        @Override
        protected void onProgressUpdate(Void... voids) {
            super.onProgressUpdate(voids);

           mAdapter.notifyDataSetChanged();

        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            String site = "";
            try {

                switch (country_selection)
                {
                    case "United States":
                        site = "http://www.indeed.com/jobs?q=";
                        break;
                    case "France":
                        site ="http://www.indeed.fr/emplois?q=";
                        break;
                    case "Canada":
                        site = "http://www.indeed.ca/jobs?q=";
                        break;
                    default: site = "http://www.indeed.com/jobs?q=";
                        break;

                }

                Scanner reader = new Scanner(MainActivity.searchBarText);
                Document document;

                while (reader.hasNext()) {

                    site += reader.next() + "%20";

                }
                site += "%20$60000&l=United%20States&start=" + String.valueOf((pages*50)-50) +"1&limit=50";

                document = Jsoup.connect(site).get();
                String doc = document.toString();
                String slice;
                if (doc.contains("jobKeysWithInfo['")){
                    slice = doc.substring(doc.indexOf("jobKeysWithInfo['"), doc.indexOf("if (vjk && !jobKeysWithInfo."));

                // System.out.print (slice + "\n\n\n\n\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n\n\n\n\n\n+++++++++++++++++++\n");

                reader = new Scanner(slice);

                while (reader.hasNext()) {
                    String line = reader.nextLine();
                    reader.nextLine();
                    mAdapter.job_keys.add(line.substring(line.indexOf("['") + 2, line.indexOf("']")));
                    if (!reader.hasNext()) break;
                    //System.out.println(job_keys.size());

                }}else noMoreJobs = true;
                publishProgress();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
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
}