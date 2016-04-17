package com.reddevil.loadshedding;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.reddevil.loadshedding.adapter.ViewPagerAdapter;
import com.reddevil.loadshedding.datastorage.DataRetriever;
import com.reddevil.loadshedding.helper.AppConfig;
import com.reddevil.loadshedding.helper.Routine;
import com.reddevil.loadshedding.helper.TimeParser;
import com.reddevil.loadshedding.networking.ScheduleUpdater;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,NavigationView.OnNavigationItemSelectedListener{

    private static String TAG = "MainActivity";
    ViewPager mPager;
    ViewPagerAdapter adapter;

    private String viewPagerTitles[] = {"Group 1","Group 2","Group 3","Group 4","Group 5","Group 6","Group 7"};
    private int numberOfPages = 7;
    private TextView groupNameVew,descriptionLabel,hoursLabel,minutesLabel,secondsLabel;
    private Handler mHandler;
    private Runnable mRunnable;
    private boolean isTimeUpdaterStarted = true;

    //making it static field to deal with it later
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
            //App is running for first time after installation, Fetching routine and updating it
            final ScheduleUpdater updater = new ScheduleUpdater(this);
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
                    updateTime();
                }
            });
            updater.update();
        }
        else
        {
            //App has been run at least once after installation,thus fetching routine from database
            loadsheddingRoutine = new Routine();
            DataRetriever retriever = new DataRetriever(this);
            loadsheddingRoutine = retriever.retrieveData();
            setupViewpager();
            updateTime();
        }

    }

    private void updateTime()
    {
        isTimeUpdaterStarted = true;
        /***** GET CURRENT GROUP HERE ****/
        int currentGroup = 4;

        HashMap<String,String> schedulesList = loadsheddingRoutine.getRoutine(currentGroup);
        final TimeParser parser = new TimeParser();
        parser.setRoutineData(schedulesList);

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                int[] remainingTime = parser.getRemainingTime();
                hoursLabel.setText(addPadding(remainingTime[0]));
                minutesLabel.setText(addPadding(remainingTime[1]));
                secondsLabel.setText(addPadding(remainingTime[2]));
                if (remainingTime[3] == 0)
                {
                    descriptionLabel.setText("Power goes after");
                }
                else
                {
                    descriptionLabel.setText("Power comes after");
                }

                mHandler.postDelayed(this,1000);
            }
        };
        mHandler.post(mRunnable);
    }

    private String addPadding(int num)
    {
        if (num < 10){
            return String.format("%02d",num);
        }
        return String.valueOf(num);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isTimeUpdaterStarted)
        {
            updateTime();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        isTimeUpdaterStarted = false;
        mHandler.removeCallbacks(mRunnable);
    }

    private void setupViewpager()
    {
        Log.v(TAG,"Setting up viewpager");
        //creating viewpager adapter
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),numberOfPages, loadsheddingRoutine);
        //assingning view Pager view and setting the adapter
        mPager =(ViewPager)findViewById(R.id.pager);
        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(adapter);
    }

    private void setupViews()
    {
        //Setting up views
        TextView timeRemaining = (TextView) findViewById(R.id.time_remaining_label);
        hoursLabel = (TextView) findViewById(R.id.hours);
        minutesLabel = (TextView) findViewById(R.id.minutes);
        secondsLabel = (TextView) findViewById(R.id.seconds);
        groupNameVew = (TextView) findViewById(R.id.groupName);
        descriptionLabel = (TextView) findViewById(R.id.time_remaining_label);

        //Setting custom fonts
        Typeface customFonts = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Light.ttf");
        timeRemaining.setTypeface(customFonts);
        hoursLabel.setTypeface(customFonts);
        minutesLabel.setTypeface(customFonts);
        secondsLabel.setTypeface(customFonts);
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

            default:
                break;

        }
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

