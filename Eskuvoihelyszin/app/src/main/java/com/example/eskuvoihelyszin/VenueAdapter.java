package com.example.eskuvoihelyszin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.ViewHolder> implements Filterable {

    private ArrayList<Venue> venueItemsData;
    private ArrayList<Venue> venueItemsDataAll;
    private Context context;
    private int lastposition = -1;

    public VenueAdapter(ArrayList<Venue> venueItemsData, Context context) {
        this.venueItemsData = venueItemsData;
        this.venueItemsDataAll = venueItemsData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_venues, parent, false));
    }

    @Override
    public void onBindViewHolder(VenueAdapter.ViewHolder holder, int position) {
        Venue venue = venueItemsData.get(position);

        holder.bindTo(venue);

        if(holder.getAdapterPosition() > lastposition){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.venues_animation);
            holder.itemView.startAnimation(anim);
            lastposition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return venueItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return venueFilter;
    }

    private Filter venueFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Venue> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = venueItemsDataAll.size();
                results.values = venueItemsDataAll;
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Venue venue : venueItemsDataAll){
                    if(venue.getCity().toLowerCase().contains(filterPattern)){
                        filteredList.add(venue);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            venueItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private TextView textViewCity;
        private TextView textViewInfo;
        private TextView textViewPrice;
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewName = itemView.findViewById(R.id.venue_name);
            this.textViewCity = itemView.findViewById(R.id.venue_city);
            this.textViewInfo = itemView.findViewById(R.id.venue_info);
            this.imageView = itemView.findViewById(R.id.venue_image);
            this.textViewPrice = itemView.findViewById(R.id.venue_price);
        }

        public void bindTo(Venue venue) {
            textViewName.setText(venue.getName());
            textViewCity.setText(venue.getCity());
            textViewInfo.setText(venue.getInfo());
            textViewPrice.setText(venue.getPrice());
            Glide.with(context).load(venue.getImageResource()).into(imageView);
            itemView.findViewById(R.id.venue_button).setOnClickListener(view -> ((VenuesActivity)context).visit(venue));
        }
    }
}


