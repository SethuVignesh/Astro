package com.newtra.astro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.newtra.astro.BeanObjects.Channels;
import com.newtra.astro.BeanObjects.Event;
import com.newtra.astro.adapters.MyAdapter;
import com.newtra.headerlist.TableFixHeaders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarListener;

public class ChannelListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {
    TableFixHeaders tableFixHeaders;
    public static MyAdapter myAdapter;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        channelsArrayList = AstroUtils.getChannelList(ChannelListActivity.this);
        channelsArrayList.put(0, new Channels(0, "NAME", "0", null));
        myAdapter = new MyAdapter(this, channelsArrayList);
        tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
        tableFixHeaders.setAdapter(myAdapter);
        tableFixHeaders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getId();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.openDrawer(GravityCompat.START);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                drawer.closeDrawer(GravityCompat.START);

            }
        }, 2000);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        CloudRequestServices.startActionFoo(ChannelListActivity.this);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 7);
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Date date, int position) {
                //do something
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("CH number", ContextCompat.getColor(this, R.color.blue_grey_500), R.drawable.num);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("CH name", ContextCompat.getColor(this, R.color.blue_grey_600), R.drawable.name);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("Favorites", ContextCompat.getColor(this, R.color.blue_grey_700), R.drawable.fav);
        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                Toast.makeText(ChannelListActivity.this, "Item " + index + " clicked", Toast.LENGTH_SHORT).show();
                sortData(index);
            }
        });

//        CloudRequestServices.startActionBaz(ChannelListActivity.this, channelIds);
    }

    public static HashMap<Integer, Channels> channelsArrayList = new HashMap<>();
    public static ArrayList<Event> eventArrayList = new ArrayList<>();

    private BroadcastReceiver channelListUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            channelsArrayList = AstroUtils.getChannelList(ChannelListActivity.this);
            channelsArrayList.put(0, new Channels(0, "NAME", "0", null));
            myAdapter = new MyAdapter(ChannelListActivity.this, channelsArrayList);
            tableFixHeaders.setAdapter(myAdapter);
            sortData(0);
        }
    };
    private BroadcastReceiver eventListUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            channelsArrayList = AstroUtils.getChannelList(ChannelListActivity.this);
            channelsArrayList.put(0, new Channels(0, "NAME", "0", null));
            myAdapter = new MyAdapter(ChannelListActivity.this, channelsArrayList);
            tableFixHeaders.setAdapter(myAdapter);

            ArrayList<Event> eventArrayList = intent.getParcelableArrayListExtra(AstroConstants.EVENTS_LIST);

            for (Event event : eventArrayList) {
                String stbId = AstroUtils.getStbId(event.getChannelId(), ChannelListActivity.this);
                if (stbId != null) {
                    Channels channels = channelsArrayList.get(Integer.parseInt(stbId));
                    if (channels != null) {
                        ArrayList<Event> eventArrayList1 = channels.getEventArrayList();
                        if (eventArrayList1 != null) {
                            eventArrayList1.add(event);
                        } else {
                            eventArrayList1 = new ArrayList<>();
                            eventArrayList1.add(event);
                        }
                    }
                }

            }




        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(ChannelListActivity.this).registerReceiver(channelListUpdated,
                new IntentFilter(AstroConstants.CHANNEL_LIST_UPDATED));
        LocalBroadcastManager.getInstance(ChannelListActivity.this).registerReceiver(eventListUpdated,
                new IntentFilter(AstroConstants.EVENTS_LIST_UPDATED));
        sortData(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(ChannelListActivity.this).unregisterReceiver(channelListUpdated);
        LocalBroadcastManager.getInstance(ChannelListActivity.this).unregisterReceiver(eventListUpdated);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.channel_list, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        View vItem = findViewById(R.id.action_sort);
//        PopupMenu popupMenu = new PopupMenu(ChannelListActivity.this, vItem);
//        popupMenu.inflate(R.menu.popup_inbox);
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                sortData(item.getTitle() + "");
//                return true;
//            }
//        });
//        return true;
//
//
//    }

    private void sortData(int sortBy) {
        Channels channels = new Channels();
        switch (sortBy) {
            case 0:
                channelsArrayList.remove(0);
                ArrayList<Channels> mapValues = new ArrayList<>(channelsArrayList.values());
                Collections.sort(mapValues, channels.new ChannelNumberComparator());
                mapValues.add(0, new Channels(0, "NAME", "0", null));
                myAdapter = new MyAdapter(ChannelListActivity.this, mapValues);
                break;
            case 1:
                channelsArrayList.remove(0);
                ArrayList<Channels> mapValues2 = new ArrayList<>(channelsArrayList.values());
                Collections.sort(mapValues2, channels.new ChannelNameComparator());
                mapValues2.add(0, new Channels(0, "NAME", "0", null));
                myAdapter = new MyAdapter(ChannelListActivity.this, mapValues2);
                break;
            case 2:
                channelsArrayList.remove(0);
                ArrayList<Channels> mapValues3 = new ArrayList<>(channelsArrayList.values());
                ArrayList<Channels> arrayList = new ArrayList<>();
                ArrayList<String> favList = AstroUtils.getFavorites(ChannelListActivity.this);

                for (Channels channels1 : mapValues3) {
                    if (favList.contains(channels1.getChannelStbNumber())) {
                        arrayList.add(channels1);
                    }
                }
                arrayList.add(0, new Channels(0, "NAME", "0", null));
                myAdapter = new MyAdapter(ChannelListActivity.this, arrayList);

                break;
            default:
                List<Channels> mapValues4 = new ArrayList<>(channelsArrayList.values());
                Collections.sort(mapValues4, channels.new ChannelNameComparator());
                myAdapter = new MyAdapter(ChannelListActivity.this, channelsArrayList);
                break;
        }
        tableFixHeaders.setAdapter(myAdapter);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.about_us) {
            // Handle the camera action
        } else if (id == R.id.sign_out) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private GoogleApiClient mGoogleApiClient;

    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
// [START_EXCLUDE]
                        AstroUtils.removeAllFavorites(ChannelListActivity.this);
                        finish();
// [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
