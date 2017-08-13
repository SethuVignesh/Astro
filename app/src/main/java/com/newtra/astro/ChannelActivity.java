package com.newtra.astro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.newtra.astro.BeanObjects.Channels;
import com.newtra.astro.BeanObjects.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {
    //    TextView textView;
    ListView listView;
    FloatingActionButton fab;
    private BroadcastReceiver eventListUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Event> eventArrayList = intent.getParcelableArrayListExtra(AstroConstants.EVENTS_LIST);
            ArrayList<String> title = new ArrayList<>();
            for (Event event : eventArrayList) {
//                textView.setText(event.getProgrammeTitle());
                title.add(event.getProgrammeTitle());
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(ChannelActivity.this, android.R.layout.simple_list_item_1, title);
            listView.setAdapter(arrayAdapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Eents list");
//        textView = (TextView) findViewById(R.id.textList);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
        final String channelStbId = getIntent().getStringExtra("channelId");
        Channels channels = ChannelListActivity.channelsMap.get(Integer.parseInt(channelStbId));
//        Toast.makeText(ChannelActivity.this,""+channelStbId,Toast.LENGTH_SHORT).show();
        String url = channels.getChannelExtRefArrayList().get(0).getValue();
        CloudRequestServices.startActionBaz(ChannelActivity.this, channels.getChannelId());
        ImageView imageView = (ImageView) findViewById(R.id.main_backdrop);
        Picasso.with(ChannelActivity.this).load(url).into(imageView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (AstroUtils.isFavorite(ChannelActivity.this, channelStbId)) {
            fab.setImageResource(R.drawable.fav3);
        } else {
            fab.setImageResource(R.drawable.fav4);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AstroUtils.isFavorite(ChannelActivity.this, channelStbId)) {
                    Snackbar.make(view, "Removed from Favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    AstroUtils.removeFavorites(channelStbId, ChannelActivity.this);
                    fab.setImageResource(R.drawable.fav4);
                } else {
                    Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    AstroUtils.addFavorites(channelStbId, ChannelActivity.this);
                    fab.setImageResource(R.drawable.fav3);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(ChannelActivity.this).registerReceiver(eventListUpdated,
                new IntentFilter(AstroConstants.EVENTS_LIST));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(ChannelActivity.this).unregisterReceiver(eventListUpdated);
    }
}
