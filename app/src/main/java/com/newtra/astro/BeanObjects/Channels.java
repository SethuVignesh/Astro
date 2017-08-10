package com.newtra.astro.BeanObjects;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Sethu on 8/8/17.
 */

public class Channels {
    int channelId;
    String siChannelId;
    String channelTitle;
    String channelDescription;
    String channelLanguage;
    String channelColor1;
    String channelColor2;
    String channelColor3;
    String channelCategory;
    String channelStbNumber;
    boolean channelHD;
    int hdSimulcastChannel;
    String channelStartDate;
    String channelEndDate;
    ArrayList<ChannelExtRef> channelExtRefArrayList;
    ArrayList<LinearOttMapping> linearOttMappingArrayList;
    boolean isFavorite;
    ArrayList<Event> eventArrayList;

    public ArrayList<Event> getEventArrayList() {
        return eventArrayList;
    }

    public void setEventArrayList(ArrayList<Event> eventArrayList) {
        this.eventArrayList = eventArrayList;
    }

    public Channels() {

    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    public Channels(int channelId, String channelTitle, String channelStbNumber, ArrayList<ChannelExtRef> channelExtRefArrayList) {
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.channelStbNumber = channelStbNumber;
        this.channelExtRefArrayList = channelExtRefArrayList;
    }

    public int getChannelId() {

        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getSiChannelId() {
        return siChannelId;
    }

    public void setSiChannelId(String siChannelId) {
        this.siChannelId = siChannelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }

    public String getChannelLanguage() {
        return channelLanguage;
    }

    public void setChannelLanguage(String channelLanguage) {
        this.channelLanguage = channelLanguage;
    }

    public String getChannelColor1() {
        return channelColor1;
    }

    public void setChannelColor1(String channelColor1) {
        this.channelColor1 = channelColor1;
    }

    public String getChannelColor2() {
        return channelColor2;
    }

    public void setChannelColor2(String channelColor2) {
        this.channelColor2 = channelColor2;
    }

    public String getChannelColor3() {
        return channelColor3;
    }

    public void setChannelColor3(String channelColor3) {
        this.channelColor3 = channelColor3;
    }

    public String getChannelCategory() {
        return channelCategory;
    }

    public void setChannelCategory(String channelCategory) {
        this.channelCategory = channelCategory;
    }

    public String getChannelStbNumber() {
        return channelStbNumber;
    }

    public void setChannelStbNumber(String channelStbNumber) {
        this.channelStbNumber = channelStbNumber;
    }

    public boolean isChannelHD() {
        return channelHD;
    }

    public void setChannelHD(boolean channelHD) {
        this.channelHD = channelHD;
    }

    public int getHdSimulcastChannel() {
        return hdSimulcastChannel;
    }

    public void setHdSimulcastChannel(int hdSimulcastChannel) {
        this.hdSimulcastChannel = hdSimulcastChannel;
    }

    public String getChannelStartDate() {
        return channelStartDate;
    }

    public void setChannelStartDate(String channelStartDate) {
        this.channelStartDate = channelStartDate;
    }

    public String getChannelEndDate() {
        return channelEndDate;
    }

    public void setChannelEndDate(String channelEndDate) {
        this.channelEndDate = channelEndDate;
    }

    public ArrayList<ChannelExtRef> getChannelExtRefArrayList() {
        return channelExtRefArrayList;
    }

    public void setChannelExtRefArrayList(ArrayList<ChannelExtRef> channelExtRefArrayList) {
        this.channelExtRefArrayList = channelExtRefArrayList;
    }

    public ArrayList<LinearOttMapping> getLinearOttMappingArrayList() {
        return linearOttMappingArrayList;
    }

    public void setLinearOttMappingArrayList(ArrayList<LinearOttMapping> linearOttMappingArrayList) {
        this.linearOttMappingArrayList = linearOttMappingArrayList;
    }


    public class ChannelNumberComparator implements Comparator<Channels> {

        @Override
        public int compare(Channels lhs, Channels rhs) {
            int val = 0;
            if (Integer.parseInt(lhs.getChannelStbNumber()) > Integer.parseInt(rhs.getChannelStbNumber())) {
                val = 1;
            } else if (Integer.parseInt(lhs.getChannelStbNumber()) < Integer.parseInt(rhs.getChannelStbNumber())) {
                val = -1;
            }
            return val;


        }
    }

    public class ChannelNameComparator implements Comparator<Channels> {

        @Override
        public int compare(Channels lhs, Channels rhs) {
            return lhs.getChannelTitle().compareToIgnoreCase(rhs.getChannelTitle());
        }
    }
}
