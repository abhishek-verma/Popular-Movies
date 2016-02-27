package com.abhishek.popularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abhishek.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 2/21/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewItemViewHolder> {
    private final List<DataObjects.ReviewData> reviewDatas;
    private final Context mContext;

    public ReviewAdapter(ArrayList<DataObjects.ReviewData> data, Context context){
        reviewDatas = data;
        mContext = context;
    }

    @Override
    public ReviewItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View reviewView = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewItemViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ReviewItemViewHolder holder, int position) {
        holder.authorNameTxtV.setText(reviewDatas.get(position).author);
        holder.contentTxtV.setText(reviewDatas.get(position).content);
    }

    @Override
    public int getItemCount() {
        if (reviewDatas == null) return 0;

        return reviewDatas.size();
    }

    public class ReviewItemViewHolder extends RecyclerView.ViewHolder {

        final TextView authorNameTxtV;
        final TextView contentTxtV;

        public ReviewItemViewHolder(View itemView) {
            super(itemView);

            authorNameTxtV = (TextView) itemView.findViewById(R.id.reviewAuthorNameTxtV);
            contentTxtV = (TextView) itemView.findViewById(R.id.reviewContentTxtV);
        }
    }
}
