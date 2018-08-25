package com.example.rahuldzeus.phonerecord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsViewHolder>{
    private Context context;
    @NonNull
    private List<FeedResponse> feedResponsesList;

    public FeedsAdapter(Context applicationContext, List<FeedResponse> feedResponseList) {
        this.context=applicationContext;
        this.feedResponsesList=feedResponseList;
    }
    @Override
    public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.news_feed_items, null);
        return new FeedsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FeedsViewHolder holder, int position) {
        FeedResponse rows=feedResponsesList.get(position);
        holder.fileAddress.setText(rows.getFileAddress());  /*Setting the values*/
        holder.username.setText(rows.getUsername());
        holder.message.setText(rows.getMessage());
    }
    @Override
    public int getItemCount() {
        return feedResponsesList.size();
    }
    class FeedsViewHolder extends RecyclerView.ViewHolder{
        TextView username,fileAddress,message;

        public FeedsViewHolder(View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            fileAddress=itemView.findViewById(R.id.file_address);       /*Finding the LAYOUT COMPONENTS*/
            message=itemView.findViewById(R.id.response_message);


        }
    }
}

