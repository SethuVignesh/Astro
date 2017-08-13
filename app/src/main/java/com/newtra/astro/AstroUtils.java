package com.newtra.astro;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.newtra.astro.BeanObjects.Channels;
import com.newtra.astro.BeanObjects.Event;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Sethu on 8/8/17.
 */

public class AstroUtils {
    private static final String FAV_CHANNELS = "fav_channels";
    private static final String CHANNEL_LIST = "channel_list";
    private static final String EVENT_LIST = "event_list";
    public static String PREF_FILE_NAME = "astro_pref";


    public static void addFavorites(String favChannelId, Context cxt) {
        ArrayList<String> favChannels = getFavorites(cxt);
        if (favChannels.contains(favChannelId) == false) {
            favChannels.add(favChannelId);
            SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefrencesEditor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(favChannels);
            prefrencesEditor.putString(FAV_CHANNELS, json);
            prefrencesEditor.commit();
        }
    }

    public static void removeFavorites(String favChannelId, Context cxt) {
        ArrayList<String> favChannels = getFavorites(cxt);
        if (favChannels.contains(favChannelId)) {
            favChannels.remove(favChannelId);
            SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor prefrencesEditor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(favChannels);
            prefrencesEditor.putString(FAV_CHANNELS, json);
            prefrencesEditor.commit();
        }
    }

    public static void removeAllFavorites(Context cxt) {
        ArrayList<String> favChannels = new ArrayList<>();
        SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefrencesEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favChannels);
        prefrencesEditor.putString(FAV_CHANNELS, json);
        prefrencesEditor.commit();

    }

    public static boolean isFavorite(Context cxt, String favChannelId) {
        ArrayList<String> favChannels = getFavorites(cxt);
        if (favChannels.contains(favChannelId)) {
            return true;
        }
        return false;
    }


    public static ArrayList<String> getFavorites(Context cxt) {
        SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(FAV_CHANNELS, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        if (arrayList == null) return new ArrayList<>();
        return arrayList;
    }


    public static void saveChanneslList(HashMap<Integer, Channels> channelsArrayList, Context cxt, boolean addHeader) {
//        if (addHeader)
//            channelsMap.add(0, new Channels(0, "NAME", "0", null));
        Channels channels = new Channels();
        ArrayList<Channels> mapValues = new ArrayList<>(channelsArrayList.values());
        Collections.sort(mapValues, channels.new ChannelNumberComparator());

        SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefrencesEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(channelsArrayList);
        prefrencesEditor.putString(CHANNEL_LIST, json);
        prefrencesEditor.commit();

    }


    public static ArrayList<Event> getEventList(Context cxt) {
        SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(EVENT_LIST, null);
        Type type = new TypeToken<ArrayList<Event>>() {
        }.getType();
        ArrayList<Event> arrayList = gson.fromJson(json, type);
        if (arrayList == null) arrayList = new ArrayList<>();
        return arrayList;
    }

    public static void saveEventList(ArrayList<Event> eventsArrayList, Context cxt) {
        HashMap<Integer, Channels> channelsArrayList = getChannelList(cxt);
        ArrayList<Channels> mapValues = new ArrayList<>(channelsArrayList.values());

        for (Channels channels : mapValues) {
            ArrayList<Event> eventArrayList = new ArrayList<>();
            for (Event event : eventsArrayList) {
                if (event.getChannelId() == channels.getChannelId()) {
                    eventArrayList.add(event);
                }
            }
            Event event = new Event();
            Collections.sort(eventArrayList, event.new TimeComparator());
            channels.setEventArrayList(eventArrayList);
        }
        saveChanneslList(channelsArrayList, cxt, false);

    }


    public static HashMap<Integer, Channels> getChannelList(Context cxt) {
        SharedPreferences preferences = cxt.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString(CHANNEL_LIST, null);
        Type type = new TypeToken<HashMap<Integer, Channels>>() {
        }.getType();
        HashMap<Integer, Channels> arrayList = gson.fromJson(json, type);
        if (arrayList == null) arrayList = new HashMap<>();

        return arrayList;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); // You can
            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }

    }

    public static String getStbId(int channelId, Context cxt) {
        HashMap<Integer, Channels> hashMap = getChannelList(cxt);
        ArrayList<Channels> mapValues = new ArrayList<>(hashMap.values());

        for (Channels channels : mapValues) {
            if (channels.getChannelId() == channelId) {
                return channels.getChannelStbNumber();
            }
        }
        return null;
    }
}
