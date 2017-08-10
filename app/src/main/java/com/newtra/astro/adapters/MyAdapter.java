package com.newtra.astro.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.newtra.astro.BeanObjects.Channels;
import com.newtra.astro.BeanObjects.Event;
import com.newtra.astro.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter extends SampleTableAdapter {

    private final int width;
    private final int height;
    private ArrayList<Channels> channelsArrayList;
    private ArrayList<Event> eventsArrayList;
    private String timeDisplay[] = {"12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM", "12 AM"};

    public MyAdapter(Context context, HashMap<Integer, Channels> channelsArrayList) {
        super(context);

        Resources resources = context.getResources();

        width = resources.getDimensionPixelSize(R.dimen.table_width);
        height = resources.getDimensionPixelSize(R.dimen.table_height);
        this.channelsArrayList = new ArrayList<Channels>(channelsArrayList.values());
    }

    public MyAdapter(Context context, ArrayList<Channels> channelsArrayList) {
        super(context);

        Resources resources = context.getResources();

        width = resources.getDimensionPixelSize(R.dimen.table_width);
        height = resources.getDimensionPixelSize(R.dimen.table_height);
        this.channelsArrayList = channelsArrayList;
    }

    public MyAdapter(Context context) {
        super(context);

        Resources resources = context.getResources();

        width = resources.getDimensionPixelSize(R.dimen.table_width);
        height = resources.getDimensionPixelSize(R.dimen.table_height);
    }

    @Override
    public int getRowCount() {
        return channelsArrayList.size() - 1;
    }

    @Override
    public int getColumnCount() {
        return 25;
    }

    @Override
    public int getWidth(int column) {
        return width;
    }

    @Override
    public int getHeight(int row) {
        return height;
    }

    @Override
    public String getCellString(int row, int column) {
        if (channelsArrayList.size() < 0) return "-";
        if (column < 0 && row < 0) {
            return "CHANNELS";
        }
        if (column < 0) {
            String url = "test";
            try {
                url = channelsArrayList.get(row + 1).getChannelExtRefArrayList().get(0).getValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "CH " + channelsArrayList.get(row + 1).getChannelStbNumber() + "__" + url;
        } else if (row < 0)
            return timeDisplay[column];
        else
            try {
                return channelsArrayList.get(row + 1).getEventArrayList().get(column + 1).getProgrammeTitle();
            } catch (Exception e) {
                return "Lorem (" + row + ", " + column + ")";
            }
    }

    @Override
    public int getLayoutResource(int row, int column) {
        int layoutResource;
        if (column >= 0) {
            switch (getItemViewType(row, column)) {
                case 0:
                    layoutResource = R.layout.item_table1_header;
                    break;
                case 1:
                    layoutResource = R.layout.item_table1;
                    break;
                default:
                    throw new RuntimeException("wtf?");

            }
        } else if (column < 0 && row < 0) {
            layoutResource = R.layout.item_table1;
        } else
            layoutResource = R.layout.item_table2;
        return layoutResource;
    }

    @Override
    public int getItemViewType(int row, int column) {
        if (row < 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public View getView(int row, int column, View converView, ViewGroup parent) {
        View view = super.getView(row, column, converView, parent);


        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}