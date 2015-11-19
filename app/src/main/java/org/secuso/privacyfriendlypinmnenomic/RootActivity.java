package org.secuso.privacyfriendlypinmnenomic;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.secuso.privacyfriendlypin.R;

/**
 * Created by yonjuni on 18.11.15.
 */
public class RootActivity extends ActionBarActivity {

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#024265")));
        actionBar.setSubtitle(R.string.action_enter_pin);

        drawerList = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[4];

        drawerItem[0] = new ObjectDrawerItem(R.mipmap.ic_action_dialpad, "Enter PIN", "");
        drawerItem[1] = new ObjectDrawerItem(R.mipmap.ic_action_replay, "Practise", "");
        drawerItem[2] = new ObjectDrawerItem(R.mipmap.ic_action_help, "Help", "");
        drawerItem[3] = new ObjectDrawerItem(R.mipmap.ic_action_about, "About", "");

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);
        drawerList.setAdapter(adapter);

        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new EnterPinFragment(), "EnterPinFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void addDrawerItems() {
        String[] mNavigationDrawerItemTitles = {getString(R.string.action_enter_pin), getString(R.string.action_practise), getString(R.string.action_help), getString(R.string.about)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mNavigationDrawerItemTitles);
        drawerList.setAdapter(adapter);

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.action_navigation);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new EnterPinFragment();
                break;
            case 1:
                fragment = new PractiseFragment();
                break;
            case 2:
                fragment = new HelpFragment();
                break;
            case 3:
                fragment = new AboutFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            drawerList.setItemChecked(position, true);
            drawerList.setSelection(position);
            drawerLayout.closeDrawer(drawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {

        Context mContext;
        int layoutResourceId;
        ObjectDrawerItem data[] = null;

        public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {

            super(mContext, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItem = convertView;

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            listItem = inflater.inflate(layoutResourceId, parent, false);

            ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
            TextView textViewName = (TextView) listItem.findViewById(R.id.textViewName);
            TextView textViewDescription = (TextView) listItem.findViewById(R.id.textViewDescription);

            ObjectDrawerItem folder = data[position];


            imageViewIcon.setImageResource(folder.icon);
            textViewName.setText(folder.name);
            textViewDescription.setText(folder.description);

            return listItem;
        }

    }

}
