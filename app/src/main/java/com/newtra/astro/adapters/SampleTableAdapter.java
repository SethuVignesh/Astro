package com.newtra.astro.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newtra.astro.AstroUtils;
import com.newtra.astro.ChannelActivity;
import com.newtra.astro.R;
import com.newtra.headerlist.adapters.BaseTableAdapter;
import com.squareup.picasso.Picasso;


/**
 * This class implements the main functionalities of the TableAdapter in
 * Mutuactivos.
 *
 * @author Brais Gabín
 */
public abstract class SampleTableAdapter extends BaseTableAdapter {
    private final Context context;
    private final LayoutInflater inflater;

    /**
     * Constructor
     *
     * @param context The current context.
     */
    public SampleTableAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Returns the context associated with this array adapter. The context is
     * used to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Quick access to the LayoutInflater instance that this Adapter retreived
     * from its Context.
     *
     * @return The shared LayoutInflater.
     */
    public LayoutInflater getInflater() {
        return inflater;
    }

    public LayoutInflater getInflator() {
        return inflater;
    }

    String display;

    @Override
    public View getView(final int row, final int column, View converView, ViewGroup parent) {
//        if (converView == null) {
        converView = inflater.inflate(getLayoutResource(row, column), parent, false);

//        }
        try {
            if (column == -1 && row > -1) {
                String[] url = getCellString(row, column).split("__");
                ImageView imageView = (ImageView) converView.findViewById(R.id.imageViewChannelicon);
                Picasso.with(context).load(url[1]).into(imageView);

                ImageView imageView2 = (ImageView) converView.findViewById(R.id.imageViewFav);

                if (AstroUtils.isFavorite(context, url[0].split(" ")[1])) {
                    imageView2.setVisibility(View.VISIBLE);
                } else {
                    imageView2.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        display = getCellString(row, column).split("__")[0];
        setText(converView, display);


        converView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (column == -1 && row > -1) {
                    TextView textView = (TextView) view.findViewById(android.R.id.text1);
                    String str = textView.getText().toString();
                    String channelStbId = str.split(" ")[1];


                    Intent intent = new Intent(context, ChannelActivity.class);
                    intent.putExtra("channelId", channelStbId);
                    context.startActivity(intent);
//                    Toast.makeText(context,""+str,Toast.LENGTH_SHORT).show();

                }
            }
        });


        return converView;
    }

    /**
     * Sets the text to the view.
     *
     * @param view
     * @param text
     */

    private void setText(View view, String text) {
        ((TextView) view.findViewById(android.R.id.text1)).setText(text);
    }

    /**
     * @param row    the title of the row of this header. If the column is -1
     *               returns the title of the row header.
     * @param column the title of the column of this header. If the column is -1
     *               returns the title of the column header.
     * @return the string for the cell [row, column]
     */
    public abstract String getCellString(int row, int column);

    public abstract int getLayoutResource(int row, int column);
}
