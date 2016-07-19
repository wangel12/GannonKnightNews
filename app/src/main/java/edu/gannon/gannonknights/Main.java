package edu.gannon.gannonknights;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Main extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mDrawerMenu;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        mTitle = mDrawerTitle = getTitle();
        mDrawerMenu = getResources().getStringArray(R.array.menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_view);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.custom_menu_list, mDrawerMenu));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        String title = getResources().getString(R.string.app_name);
        this.getActionBar().setTitle(title);
        this.getActionBar().setDisplayHomeAsUpEnabled(false);   //disable back button
        this.getActionBar().setHomeButtonEnabled(false);

        //Call Fragment
        /*
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DefaultFragment fragment = new DefaultFragment();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
        */
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //Action bar on item click events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.menu_icon:
                // create intent to perform web search for this planet
                Log.d("Hello", "Menu clicked");
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
            //Log.d("Hello", String.valueOf(position));
            //Get the option name and compare
            String text = ((TextView) arg1.findViewById(android.R.id.text1)).getText().toString();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle data = new Bundle();
            data.putString("Topic",text);
            if(position == 0){
                Log.d("Click", "Home Clicked");
                MenuFragment fragment = new MenuFragment();
                fragment.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

            else if(position == 1){
                Log.d("Click", "News Clicked 2");
                NewsFragment fragment = new NewsFragment();
                fragment.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, fragment, String.valueOf(position));
                fragmentTransaction.addToBackStack("Home");
                fragmentTransaction.commit();
            }

            else if(position == 2){
                Log.d("Click", "Opinion Clicked");
                OpinionFragment OpF = new OpinionFragment();
                OpF.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, OpF, String.valueOf(position));
                fragmentTransaction.addToBackStack("Opinion");
                fragmentTransaction.commit();
            }
            else if(position == 3){
                Log.d("Click", "Features Clicked");
                FeaturesFragment FeF = new FeaturesFragment();
                FeF.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, FeF, String.valueOf(position));
                fragmentTransaction.addToBackStack("Features");
                fragmentTransaction.commit();
            }
            else if(position == 4){
                Log.d("Click", "Arts Clicked");
                ArtsFragment ArtsF = new ArtsFragment();
                ArtsF.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, ArtsF, String.valueOf(position));
                fragmentTransaction.addToBackStack("Arts");
                fragmentTransaction.commit();
            }
            else if(position == 5){
                Log.d("Click", "Sports Clicked");
                SportsFragment SportsF = new SportsFragment();
                SportsF.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, SportsF, String.valueOf(position));
                fragmentTransaction.addToBackStack("Sports");
                fragmentTransaction.commit();
            }
            else if(position == 6){
                Log.d("Click", "Opinion Clicked");
                BlogsFragment BlogsF = new BlogsFragment();
                BlogsF.setArguments(data);
                fragmentTransaction.replace(R.id.content_frame, BlogsF, String.valueOf(position));
                fragmentTransaction.addToBackStack("Option");
                fragmentTransaction.commit();
            }
            else if(position == 7){
                Intent in = new Intent(Main.this, RegisterActivity.class);
                startActivity(in);
            }

            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

}