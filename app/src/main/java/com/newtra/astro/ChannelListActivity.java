package com.newtra.astro;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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

        getSupportActionBar().setTitle("Astro Guide");

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        channelsMap = AstroUtils.getChannelList(ChannelListActivity.this);
        channelsMap.put(0, new Channels(0, "NAME", "0", null, null));
        myAdapter = new MyAdapter(this, channelsMap);
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
                ("Number", ContextCompat.getColor(this, R.color.blue_grey1), R.drawable.num);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("Name", ContextCompat.getColor(this, R.color.blue_grey2), R.drawable.name);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("Categ", ContextCompat.getColor(this, R.color.blue_grey3), R.drawable.category);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("Lang", ContextCompat.getColor(this, R.color.blue_grey4), R.drawable.lang);
        BottomNavigationItem bottomNavigationItem4 = new BottomNavigationItem
                ("Fav", ContextCompat.getColor(this, R.color.blue_grey5), R.drawable.fav);
        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);
        bottomNavigationView.addTab(bottomNavigationItem4);
        bottomNavigationView.disableShadow();
        bottomNavigationView.callOnClick();
        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                filterData(index);
            }
        });

//        CloudRequestServices.startActionBaz(ChannelListActivity.this, channelIds);
    }

    public static HashMap<Integer, Channels> channelsMap = new HashMap<>();
    public static ArrayList<Event> eventArrayList = new ArrayList<>();

    private BroadcastReceiver channelListUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            channelsMap = AstroUtils.getChannelList(ChannelListActivity.this);
            channelsMap.put(0, new Channels(0, "NAME", "0", null, null));
            myAdapter = new MyAdapter(ChannelListActivity.this, channelsMap);
            tableFixHeaders.setAdapter(myAdapter);
            filterData(0);
        }
    };
//    private BroadcastReceiver eventListUpdated = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
////            channelsMap = AstroUtils.getChannelList(ChannelListActivity.this);
////            channelsMap.put(0, new Channels(0, "NAME", "0", null));
////            myAdapter = new MyAdapter(ChannelListActivity.this, channelsMap);
////            tableFixHeaders.setAdapter(myAdapter);
//
//            ArrayList<Event> eventArrayList = intent.getParcelableArrayListExtra(AstroConstants.EVENTS_LIST);
//            for(Event event:eventArrayList){
//
//
//            }
//
//
//            for (Event event : eventArrayList) {
//                String stbId = AstroUtils.getStbId(event.getChannelId(), ChannelListActivity.this);
//                if (stbId != null) {
//                    Channels channels = channelsMap.get(Integer.parseInt(stbId));
//                    if (channels != null) {
//                        ArrayList<Event> eventArrayList1 = channels.getEventArrayList();
//                        if (eventArrayList1 != null) {
//                            eventArrayList1.add(event);
//                        } else {
//                            eventArrayList1 = new ArrayList<>();
//                            eventArrayList1.add(event);
//                        }
//                    }
//                }
//
//            }
//
//
//
//
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(ChannelListActivity.this).registerReceiver(channelListUpdated,
                new IntentFilter(AstroConstants.CHANNEL_LIST_UPDATED));
//        LocalBroadcastManager.getInstance(ChannelListActivity.this).registerReceiver(eventListUpdated,
//                new IntentFilter(AstroConstants.EVENTS_LIST_UPDATED));
        filterData(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(ChannelListActivity.this).unregisterReceiver(channelListUpdated);
//        LocalBroadcastManager.getInstance(ChannelListActivity.this).unregisterReceiver(eventListUpdated);
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
//                filterData(item.getTitle() + "");
//                return true;
//            }
//        });
//        return true;
//
//
//    }

    private void filterData(int filterBy) {
        Channels channels = new Channels();
        switch (filterBy) {
            case 0:
                channelsMap.remove(0);
                ArrayList<Channels> mapValues = new ArrayList<>(channelsMap.values());
                Collections.sort(mapValues, channels.new ChannelNumberComparator());
                mapValues.add(0, new Channels(0, "NAME", "0", null, null));
                myAdapter = new MyAdapter(ChannelListActivity.this, mapValues);
                break;

            case 1:
                channelsMap.remove(0);
                ArrayList<Channels> mapValues2 = new ArrayList<>(channelsMap.values());
                Collections.sort(mapValues2, channels.new ChannelNameComparator());
                mapValues2.add(0, new Channels(0, "NAME", "0", null, null));
                myAdapter = new MyAdapter(ChannelListActivity.this, mapValues2);
                break;
            case 2:
                Dialog dialog = onCreateDialogSingleChoice(2);
                dialog.show();
                break;
            case 3:
                Dialog dialog2 = onCreateDialogSingleChoice(3);
                dialog2.show();
                break;
            case 4:
                channelsMap.remove(0);
                ArrayList<Channels> mapValues3 = new ArrayList<>(channelsMap.values());
                ArrayList<Channels> arrayList = new ArrayList<>();
                ArrayList<String> favList = AstroUtils.getFavorites(ChannelListActivity.this);

                for (Channels channels1 : mapValues3) {
                    if (favList.contains(channels1.getChannelStbNumber())) {
                        arrayList.add(channels1);
                    }
                }
                arrayList.add(0, new Channels(0, "NAME", "0", null, null));
                myAdapter = new MyAdapter(ChannelListActivity.this, arrayList);

                break;
            default:
                List<Channels> mapValues4 = new ArrayList<>(channelsMap.values());
                Collections.sort(mapValues4, channels.new ChannelNameComparator());
                myAdapter = new MyAdapter(ChannelListActivity.this, channelsMap);
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
                        Intent intent = new Intent(ChannelListActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
// [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    CharSequence[] array;

    public Dialog onCreateDialogSingleChoice(final int type) {

//Initialize the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//Source of the data in the DIalog

        String title = "Select Category";
        switch (type) {
            case 2:
                array = CloudRequestServices.categHashMap.keySet().toArray(new CharSequence[CloudRequestServices.categHashMap.size()]);
                title = "Select Category";

                break;
            case 3:
                array = CloudRequestServices.langHashMap.keySet().toArray(new CharSequence[CloudRequestServices.langHashMap.size()]);
                title = "Select Language";
                break;
        }

        builder.setTitle(title)


// Set the dialog title

// Specify the list array, the items to be selected by default (null for none),
// and the listener through which to receive callbacks when items are selected
                .setItems(array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    CharSequence selectedStr = array[i];
                                    ArrayList<String> selectedId;
                                    if (type == 2) {
                                        selectedId = CloudRequestServices.categHashMap.get(selectedStr.toString());

                                    } else {
                                        selectedId = CloudRequestServices.langHashMap.get(selectedStr.toString());

                                    }
                                    ArrayList<Channels> selectedChannels = new ArrayList<>();
                                    for (String str : selectedId) {
                                        selectedChannels.add(channelsMap.get(Integer.parseInt(str)));
                                    }

//                        ArrayList<Channels> mapValues = new ArrayList<>(channelsMap.values());
//                        Collections.sort(mapValues, channels.new ChannelNumberComparator());
                                    selectedChannels.add(0, new Channels(0, "NAME", "0", null, null));
                                    myAdapter = new MyAdapter(ChannelListActivity.this, selectedChannels);
                                    tableFixHeaders.setAdapter(myAdapter);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                    }
                );

        return builder.create();
    }
}
