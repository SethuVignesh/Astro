package com.newtra.astro;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.newtra.astro.BeanObjects.ChannelExtRef;
import com.newtra.astro.BeanObjects.Channels;
import com.newtra.astro.BeanObjects.Event;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//import static com.theatro.managersapp.activity.StoreListFragment.announcementToday;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CloudRequestServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.theatro.managersapp.vpApp.action.FOO";
    private static final String ACTION_BAZ = "com.theatro.managersapp.vpApp.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.theatro.managersapp.vpApp.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.theatro.managersapp.vpApp.extra.PARAM2";
    private static Context mContext;

    public CloudRequestServices() {
        super("TCMRequestServices");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(@NonNull Context context) {
        mContext = context;
        Intent intent = new Intent(context, CloudRequestServices.class);
        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionBaz(@NonNull Context context, int channelId) {
        mContext = context;
        Intent intent = new Intent(context, CloudRequestServices.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM2, channelId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            if (intent != null) {
                final String action = intent.getAction();
                if (ACTION_FOO.equals(action)) {
                    String channelsJson = getChannelList();
                    HashMap<Integer, Channels> channelsArrayList = parseChannelsJson(channelsJson);


                    AstroUtils.saveChanneslList(channelsArrayList, mContext, true);

                    ArrayList<Channels> channelsArrayList1 = new ArrayList<>(channelsArrayList.values());
                    broadcastEdit(AstroConstants.CHANNEL_LIST_UPDATED, null, channelsArrayList1);

////                    ArrayList<Integer> channelIds = new ArrayList<>();
////                    ArrayList<Channels> mapValues = new ArrayList<>(channelsMap.values());
////                    for (Channels channels : mapValues) {
//////                        channelIds.add(channels.getChannelId());
//////                        String eventssJson = getEventList(channels.getChannelId());
//                    String eventssJson = AstroConstants.eventsJson;
//                    ArrayList<Event> eventArrayList = parseEventsJson(eventssJson);
////                        AstroUtils.saveEventList(eventArrayList, mContext);
//                    broadcastEdit(AstroConstants.EVENTS_LIST_UPDATED, eventArrayList);
//                    }
//                    myAdapter.notifyDataSetChanged();
                }
                if (ACTION_BAZ.equals(action)) {
                    int channelId = intent.getIntExtra(EXTRA_PARAM2, 0);
                    String eventssJson = getEventList(channelId);
                    ArrayList<Event> eventArrayList = parseEventsJson(eventssJson);
//                    AstroUtils.saveEventList(eventArrayList, mContext);
//                    broadcastEdit(AstroConstants.EVENTS_LIST_UPDATED);
                    Intent intent2 = new Intent(AstroConstants.EVENTS_LIST);
                    intent2.setClass(mContext, ChannelActivity.class);
                    intent2.putParcelableArrayListExtra(AstroConstants.EVENTS_LIST, eventArrayList);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcastEdit(String type, ArrayList<Event> eventArrayList, ArrayList<Channels> channelsArrayList) {
        Intent intent;
        if (type.equals(AstroConstants.CHANNEL_LIST_UPDATED)) {
            intent = new Intent(AstroConstants.CHANNEL_LIST_UPDATED);
        } else {
            intent = new Intent(AstroConstants.EVENTS_LIST_UPDATED);
        }
        intent.setClass(mContext, ChannelListActivity.class);
        if (eventArrayList != null)
            intent.putParcelableArrayListExtra(AstroConstants.EVENTS_LIST, eventArrayList);
        if (channelsArrayList != null)
            intent.putParcelableArrayListExtra(AstroConstants.CHANNEL_LIST, channelsArrayList);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(ArrayList<Integer> param2) {

    }

    private String getChannelList() {
//        return AstroConstants.channelListJson;
        return getStoreListHttpRequestAndResponse();
    }

    private String getEventList(int channelIds) {
////        return AstroConstants.eventsJson;
//        String channelId = "0";
//        for (Integer integer : channelIds) {
//            channelId = integer + "," + channelId;
//        }
        String completeUrl = "http://ams-api.astro.com.my/ams/v3/getEvents?periodStart=2017-08-08%2010:00&periodEnd=2017-08-10%2012:00&channelId=%5B" + channelIds + "%5D";


        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(completeUrl));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                //..more logic
                return responseString;
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception ex) {
            return null;
        }
    }

    public static HashMap<String, ArrayList<String>> langHashMap = new HashMap<>();
    public static HashMap<String, ArrayList<String>> categHashMap = new HashMap<>();

    private HashMap<Integer, Channels> parseChannelsJson(String channelsListJson) {
        HashMap<Integer, Channels> channelsArrayList = new HashMap<>();
        if (channelsListJson != null) {
            try {
                String eventssJson = AstroConstants.eventsJson;
                ArrayList<Event> eventArrayList = parseEventsJson(eventssJson);


                JSONObject jsonObject = new JSONObject(channelsListJson);
                JSONArray jsonArray = jsonObject.getJSONArray("channel");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject channelObject = jsonArray.getJSONObject(i);
                    int channelId = channelObject.getInt("channelId");
                    String channelTitle = channelObject.getString("channelTitle");
                    String channelStbNumber = channelObject.getString("channelStbNumber");
                    String lang = channelObject.getString("channelLanguage");
                    String categ = channelObject.getString("channelCategory");

                    ArrayList<String> arrayList = langHashMap.get(lang);
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                        arrayList.add(channelStbNumber);
                        langHashMap.put(lang, arrayList);
                    } else
                        arrayList.add(channelStbNumber);
                    ArrayList<String> arrayList2 = categHashMap.get(categ);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList<>();
                        arrayList2.add(channelStbNumber);
                        categHashMap.put(categ, arrayList2);
                    } else
                        arrayList2.add(channelStbNumber);

                    JSONArray channelExtRefArr = channelObject.getJSONArray("channelExtRef");
                    ArrayList<ChannelExtRef> channelExtRefArrayList = new ArrayList<>();
                    for (int j = 0; j < channelExtRefArr.length(); j++) {
                        JSONObject channelExtRefObj = channelExtRefArr.getJSONObject(j);
                        String system = channelExtRefObj.getString("system");
                        String subSystem = channelExtRefObj.getString("subSystem");
                        String value = channelExtRefObj.getString("value");
                        ChannelExtRef channelExtRef = new ChannelExtRef(system, subSystem, value);
                        channelExtRefArrayList.add(channelExtRef);
                    }

                    Channels channels = new Channels(channelId, channelTitle, channelStbNumber, channelExtRefArrayList, eventArrayList);
                    channelsArrayList.put(Integer.parseInt(channelStbNumber), channels);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return channelsArrayList;

    }

    private ArrayList<Event> parseEventsJson(String eventsJson) {
        ArrayList<Event> arrayList = new ArrayList<>();
        if (eventsJson != null) {
            try {
                JSONObject jsonObject = new JSONObject(eventsJson);
                JSONArray jsonArray = jsonObject.getJSONArray("getevent");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject eventsObject = jsonArray.getJSONObject(i);
                    String eventID = eventsObject.getString("eventID");
                    int channelId = eventsObject.getInt("channelId");
                    String displayDateTime = eventsObject.getString("displayDateTime");
                    String displayDuration = eventsObject.getString("displayDuration");
                    String shortSynopsis = eventsObject.getString("shortSynopsis");
                    String contentImage = eventsObject.getString("contentImage");
                    String programmeTitle = eventsObject.getString("programmeTitle");
                    Event event = new Event(eventID, channelId, displayDateTime, displayDuration, shortSynopsis, contentImage, programmeTitle);
                    arrayList.add(event);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private String getStoreListHttpRequestAndResponse() {
        String completeUrl = "http://ams-api.astro.com.my/ams/v3/getChannels";


        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(completeUrl));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseString = out.toString();
                out.close();
                //..more logic
                return responseString;
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }

        } catch (Exception ex) {
            return null;
        }
    }

}
