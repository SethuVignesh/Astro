package com.newtra.astro.BeanObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by sethugayu on 8/8/17.
 */

public class Event implements Parcelable {
    String eventID;
    int channelId;
    String displayDateTime;
    String displayDuration;
    String shortSynopsis;
    String contentImage;
    String programmeTitle;

    protected Event(Parcel in) {
        eventID = in.readString();
        channelId = in.readInt();
        displayDateTime = in.readString();
        displayDuration = in.readString();
        shortSynopsis = in.readString();
        contentImage = in.readString();
        programmeTitle = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getProgrammeTitle() {
        return programmeTitle;
    }

    public void setProgrammeTitle(String programmeTitle) {
        this.programmeTitle = programmeTitle;
    }


    public Event() {

    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getDisplayDateTime() {
        return displayDateTime;
    }

    public void setDisplayDateTime(String displayDateTime) {
        this.displayDateTime = displayDateTime;
    }

    public String getDisplayDuration() {
        return displayDuration;
    }

    public void setDisplayDuration(String displayDuration) {
        this.displayDuration = displayDuration;
    }

    public String getShortSynopsis() {
        return shortSynopsis;
    }

    public void setShortSynopsis(String shortSynopsis) {
        this.shortSynopsis = shortSynopsis;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public Event(String eventID, int channelId, String displayDateTime, String displayDuration, String shortSynopsis, String contentImage, String programmeTitle) {

        this.eventID = eventID;
        this.channelId = channelId;
        this.displayDateTime = displayDateTime;
        this.displayDuration = displayDuration;
        this.shortSynopsis = shortSynopsis;
        this.contentImage = contentImage;
        this.programmeTitle = programmeTitle;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getEventID());
        parcel.writeInt(getChannelId());
        parcel.writeString(getDisplayDateTime());
        parcel.writeString(getDisplayDuration());
        parcel.writeString(getShortSynopsis());
        parcel.writeString(getContentImage());
        parcel.writeString(getProgrammeTitle());
    }

    public class TimeComparator implements Comparator<Event> {

        @Override
        public int compare(Event lhs, Event rhs) {
            return lhs.getDisplayDateTime().compareToIgnoreCase(rhs.getDisplayDateTime());
        }
    }
}
