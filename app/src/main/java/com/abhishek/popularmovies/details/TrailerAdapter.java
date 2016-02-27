package com.abhishek.popularmovies.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishek.popularmovies.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 2/21/2016.
 */
public class TrailerAdapter extends
        RecyclerView.Adapter<TrailerAdapter.TrailerItemViewHolder> {

    private final Context mContext;
    private final List<DataObjects.TrailerData> trailerData;

    public TrailerAdapter(ArrayList<DataObjects.TrailerData> data, Context context) {
        trailerData = data;
        mContext = context;
    }

    // Here you inflate the view and return the ViewHolder
    @Override
    public TrailerAdapter.TrailerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View trailerView = inflater.inflate(R.layout.trailer_list_item, parent, false);
        return new TrailerItemViewHolder(trailerView, new OnTrailerClickedListener() {
            @Override
            public void onTrailerClicked(Uri youtubeUri) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=cxLG2wtE7TM")));
            }
        });
    }

    @Override
    public void onBindViewHolder(TrailerItemViewHolder holder, int position) {
        holder.trailerNameTxtV.setText(trailerData.get(position).trailerName);
        Picasso.with(mContext).load(trailerData
                .get(position).youtubeThumb)
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.trailerThumbImgV);
        holder.trailerUrlStr = trailerData.get(position).sourceUrl;
    }

    @Override
    public int getItemCount() {
        if(trailerData == null) return 0;

        return trailerData.size();
    }

    public interface OnTrailerClickedListener {
        void onTrailerClicked(Uri youtubeUri);
    }

    public class TrailerItemViewHolder extends
            RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnTrailerClickedListener listener;
        private final TextView trailerNameTxtV;
        private final ImageView trailerThumbImgV;
        public String trailerUrlStr;

        public TrailerItemViewHolder(View itemView, OnTrailerClickedListener listener) {
            super(itemView);

            trailerThumbImgV = (ImageView) itemView.findViewById(R.id.trailerThumbImgV);
            trailerNameTxtV = (TextView) itemView.findViewById(R.id.trailerNameTxtV);
            this.listener = listener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onTrailerClicked(Uri.parse(trailerUrlStr));
        }
    }
}
