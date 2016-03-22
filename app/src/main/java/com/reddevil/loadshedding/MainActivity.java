package com.reddevil.loadshedding;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.reddevil.loadshedding.adapter.ViewPagerAdapter;
import com.reddevil.loadshedding.datastorage.StorageHelper;
import com.reddevil.loadshedding.helper.AppConfig;
import com.reddevil.loadshedding.helper.Routine;
import com.reddevil.loadshedding.networking.ScheduleUpdater;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,NavigationView.OnNavigationItemSelectedListener{

    private static String TAG = "MainActivity";
    ViewPager pager;
    ViewPagerAdapter adapter;

    private String viewPagerTitles[] = {"Group 1","Group 2","Group 3","Group 4","Group 5","Group 6","Group 7"};
    private int numberOfPages = 7;
    private TextView groupNameVew;

    public static Routine loadsheddingRoutine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupNavigationDrawer();

        //Checking App Configuration Here
        final AppConfig config = new AppConfig(this);
        if(!config.getAppInstalledStatus())
        {
            Log.v(TAG,"App is not installed, Fetching routine");
            ScheduleUpdater updater = new ScheduleUpdater(this);
            updater.setListener(new ScheduleUpdater.OnScheduleUpdatedListener() {
                @Override
                public void isScheduleUpdated(Boolean isUpdated, String message) {
                    Log.v(TAG,"IsScheduleUpdated Called");
                    if(isUpdated)
                    {
                        config.setAppInstallStatus(true);
                    }
                }

                @Override
                public void getUpdatedRoutine(Routine routine) {
                    Log.v(TAG,"GetUpdatedRoutine Called");
                    loadsheddingRoutine = routine;
                    setupViewpager();
                }
            });
            updater.update();
        }
        else
        {
            Log.v(TAG,"App is installed, Fetching routine");
            loadsheddingRoutine = new Routine();
            prepareRoutineData();
            setupViewpager();
        }

    }

    private void setupViewpager()
    {
        Log.v(TAG,"Setting up viewpager");
        //creating viewpager adapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),numberOfPages, loadsheddingRoutine);
        //assingning view pager view and setting the adapter
        pager =(ViewPager)findViewById(R.id.pager);
        pager.addOnPageChangeListener(this);
        pager.setAdapter(adapter);
    }


    private void prepareRoutineData()
    {
        StorageHelper helper = new StorageHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projections = {
                StorageHelper.COLUMN_GROUP,
                StorageHelper.COLUMN_DAY_SUNDAY,
                StorageHelper.COLUMN_DAY_MONDAY,
                StorageHelper.COLUMN_DAY_TUESDAY,
                StorageHelper.COLUMN_DAY_WEDNESDAY,
                StorageHelper.COLUMN_DAY_THURSDAY,
                StorageHelper.COLUMN_DAY_FRIDAY,
                StorageHelper.COLUMN_DAY_SATURDAY
        };

        Cursor cursor = db.query(StorageHelper.TABLE_NAME,projections,null,null,null,null,null);
        if(cursor != null && cursor.moveToFirst())
        {
            HashMap<String,String> routineMap;
            do {
                routineMap = new HashMap<>();

                int groupId = cursor.getInt(cursor.getColumnIndex(StorageHelper.COLUMN_GROUP));
                String sunRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_SUNDAY));
                String monRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_MONDAY));
                String tueRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_TUESDAY));
                String wedRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_WEDNESDAY));
                String thuRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_THURSDAY));
                String friRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_FRIDAY));
                String satRoutine = cursor.getString(cursor.getColumnIndex(StorageHelper.COLUMN_DAY_SATURDAY));

                routineMap.put("sunday",sunRoutine);
                routineMap.put("monday",monRoutine);
                routineMap.put("tuesday",tueRoutine);
                routineMap.put("wednesday",wedRoutine);
                routineMap.put("thursday",thuRoutine);
                routineMap.put("friday",friRoutine);
                routineMap.put("saturday",satRoutine);

                loadsheddingRoutine.addRoutine(groupId,routineMap);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void setupViews()
    {
        //Setting up views
        TextView timeRemaining = (TextView) findViewById(R.id.time_remaining_label);
        TextView hours = (TextView) findViewById(R.id.hours);
        TextView minutes = (TextView) findViewById(R.id.minutes);
        TextView seconds = (TextView) findViewById(R.id.seconds);
        groupNameVew = (TextView) findViewById(R.id.groupName);

        //Setting custom fonts
        Typeface customFonts = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");
        timeRemaining.setTypeface(customFonts);
        hours.setTypeface(customFonts);
        minutes.setTypeface(customFonts);
        seconds.setTypeface(customFonts);
    }


    private void setupNavigationDrawer()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Setting up navigation drawer
        DrawerLayout container = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,container,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_closed);
        container.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    //Viewpager Methods

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        groupNameVew.setText(viewPagerTitles[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //Navigation Methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.home:
                Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_LONG).show();
                break;
            case R.id.favourites:

            case R.id.auto_update:



                Toast.makeText(getApplicationContext(),"update",Toast.LENGTH_LONG).show();
                break;
        }
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

