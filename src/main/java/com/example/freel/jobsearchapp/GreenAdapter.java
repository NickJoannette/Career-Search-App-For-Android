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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.freel.jobsearchapp.R;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * We couldn't come up with a good name for this class. Then, we realized
 * that this lesson is about RecyclerView.
 *
 * RecyclerView... Recycling... Saving the planet? Being green? Anyone?
 * #crickets
 *
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 *
 * If you don't like our puns, we named this Adapter GreenAdapter because its
 * contents are green.
 */



public class GreenAdapter extends RecyclerView.Adapter {



    private static final String TAG = GreenAdapter.class.getSimpleName();

    public static ArrayList<TabLayout.OnTabSelectedListener> tabListeners = new ArrayList<TabLayout.OnTabSelectedListener>();

    private String temp;
    static int position;
    public static ArrayList<DisplayResultsFragmentPagerAdapter> dps;
    public static ArrayList<String> jobs;
    public static ArrayList<String> titles;
    public static ArrayList<String> job_keys;
    public static ArrayList<String> urls;
    RecyclerView rv;
    ArrayList<Integer> savedtabs = new ArrayList<Integer>();

    public static ArrayList<TabLayout> tabLayouts = new ArrayList<TabLayout>();
    int i = 0;
    public static int cpos;
    private int mNumberItems;
    private final int VIEW_ITEM = 1;
    Context context;
    private final int VIEW_PROG = 0;
    public boolean isP = false;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    public boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    Scanner sc;

    /**
     * Constructor for GreenAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @// numberOfItems Number of items to display in list
     */


    public void setLoaded() {
        loading = false;
    }

    public void setLoading() {
        loading = true;
    }

    public GreenAdapter(final Context context, ArrayList<String> joblist, ArrayList<String> titlelist, ArrayList<String> keylist, ArrayList<String> urllist, RecyclerView recyclerView) {
        urls = urllist;
        jobs = joblist;
        titles = titlelist;
        job_keys = keylist;
        this.context = context;
        rv = recyclerView;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {


                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }



    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param //viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param //viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                    can use this viewType integer to provide a different layout. See
     *                    {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                    for more details.
     * @return A new NumberViewHolder that holds the View for each list item
     */



    @Override
    public int getItemViewType(int position) {

        return jobs.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.test_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        */

        //DisplayResultsActivity.GetJobListings2 getJobListings2Task = new DisplayResultsActivity.GetJobListings2();
        // getJobListings2Task.execute();

        /*View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        viewHolder = new NumberViewHolder(view);
        return viewHolder;*/



        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_list_item, parent, false);

            vh = new NumberViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);

            vh = new ProgressViewHolder(v);

        }
        return vh;
    }


    public boolean loadMore() {
        if (jobs.size() < job_keys.size())
            return true;
        else return false;

    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProgressViewHolder) {

            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else{
                final int currentholder;
            ((NumberViewHolder)holder).listJobListingTextView.setText(Integer.toString(position));
            ((NumberViewHolder)holder).tabadapter = new DisplayResultsFragmentPagerAdapter(context, ((DisplayResultsActivity)context).getSupportFragmentManager(),jobs.get(position),(urls.get(position)+"\n\n"));

            ((NumberViewHolder)holder).viewPager.setAdapter(((NumberViewHolder)holder).tabadapter);
            ((NumberViewHolder)holder).viewPager.setId((int) (Math.random() * 10000));
            ((NumberViewHolder)holder).respectiveTabLayout.setupWithViewPager(((NumberViewHolder)holder).viewPager);

            ((NumberViewHolder)holder).respectiveTabLayout.getTabAt(0).setIcon(R.drawable.ic_bulletin_list_icon);
            ((NumberViewHolder)holder).respectiveTabLayout.getTabAt(1).setIcon(R.drawable.ic_phone_receiver);
           // holder.setIsRecyclable(false);

            ((NumberViewHolder)holder).viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

                @Override
                public void onPageScrolled(int x, float f, int y)
                {

                }

                @Override
                public void onPageScrollStateChanged(int x)
                {


                }

                @Override
                public void onPageSelected(int tab)
                {
                   LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv.getLayoutManager();

                    DisplayResultsFragmentPagerAdapter adapter = (DisplayResultsFragmentPagerAdapter) ((NumberViewHolder)holder).tabadapter;

                    //
                    //                          //  rv.smoothScrollToPosition(position);
                   //((NumberViewHolder)holder).tabadapter
                   // if (tab == 1) {savedtabs.add(position);}
                    //rv.smoothScrollToPosition(position);
                    }



            });

/*
            ((NumberViewHolder)holder).respectiveTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()){
                        case 0: ((NumberViewHolder)holder).urlTextView.setVisibility(View.GONE);
                            ((NumberViewHolder)holder).listJobListingTextView.setText(jobs.get(position));
                            ((NumberViewHolder)holder).listJobListingTextView.setVisibility(View.VISIBLE);
                            holder.setIsRecyclable(true);
                            savedtabs.removeAll(Collections.singleton(position));
                            break;

                        case 1: ((NumberViewHolder)holder).listJobListingTextView.setVisibility(View.GONE);
                            ((NumberViewHolder)holder).urlTextView.setVisibility(View.VISIBLE);
                            holder.setIsRecyclable(false);
                            savedtabs.add(position);

                            break;

                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
*/
            ((NumberViewHolder) holder).bind(position);

        }


    }
    //mNumberItems = getJobs().size();


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return jobs.size();

    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Cache of the children views for a list item.
     */

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.loadmoreBar);
        }
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView listIndexTextView;
        TextView listJobListingTextView;
        TextView urlTextView;
        TabLayout respectiveTabLayout;
        ViewPager viewPager;
        DisplayResultsFragmentPagerAdapter tabadapter;
        public int givePosition() {

            return getLayoutPosition();
        }

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View that you inflated in
         *                 {@link GreenAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public NumberViewHolder(View itemView) {
            super(itemView);
            // GreenAdapter.this.notifyItemInserted(getItemCount()+1);
            listIndexTextView = (TextView) itemView.findViewById(R.id.test_list_item);
            listJobListingTextView = (TextView) itemView.findViewById(R.id.text_text_item);
            urlTextView = (TextView) itemView.findViewById(R.id.URL_text_view);
            respectiveTabLayout = (TabLayout) itemView.findViewById(R.id.tabLayout2);
            viewPager = (ViewPager) itemView.findViewById(R.id.viewpagerdisplayresults);



        }


        void bind(int listIndex) {

            final int ndex = listIndex;
            listIndexTextView.setText(titles.get(listIndex));

            if (savedtabs.contains(ndex))viewPager.setCurrentItem(1);


            //listJobListingTextView.setText(jobs.get(listIndex));
          //  if (savedtabs.contains(ndex)) respectiveTabLayout.getTabAt(1).select();

            urlTextView.setText(urls.get(listIndex));
          //  urlTextView.setVisibility(View.GONE);


            // listIndexTextView.setText("Indx: " + String.valueOf(getLayoutPosition())+"jobs: "+String.valueOf(jobs.size()) +  ": keys: "+ String.valueOf(job_keys.size()));
            /*try {
                listJobListingTextView.setText(jobs.get(listIndex));
            } catch (IndexOutOfBoundsException exception) {
                listJobListingTextView.setVisibility(View.INVISIBLE);

                try {
                    Thread.sleep(425);
                } catch (Exception e) {
                }*/
                ;
                /*try {
                    listJobListingTextView.setText(MainActivity.getJobs().get(listIndex));
                    listJobListingTextView.setVisibility(View.VISIBLE);

                } catch (IndexOutOfBoundsException ex) {
                    listJobListingTextView.setText(MainActivity.getJobs().get(listIndex))


                }*/

            }

        }


    }



