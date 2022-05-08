package com.example.eskuvoihelyszin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoglalasAdapter extends RecyclerView.Adapter<FoglalasAdapter.ViewHolder> {

    private ArrayList<Foglalas> foglalasok;
    private Context context;
    private int lastposition = -1;

    public FoglalasAdapter(ArrayList<Foglalas> foglalasok, Context context) {
        this.foglalasok = foglalasok;
        this.context = context;
    }

    @NonNull
    @Override
    public FoglalasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoglalasAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_foglalasok, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoglalasAdapter.ViewHolder holder, int position) {
        Foglalas foglalas = foglalasok.get(position);

        holder.bindTo(foglalas);

        if(holder.getAdapterPosition() > lastposition){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.foglalas_animation);
            holder.itemView.startAnimation(anim);
            lastposition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return foglalasok.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewVenueId;
        private TextView textViewUserName;
        private TextView textViewUserEmail;
        private TextView textViewDate;
        private TextView textViewPhonenumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textViewVenueId = itemView.findViewById(R.id.venue_id);
            this.textViewUserName = itemView.findViewById(R.id.user_name);
            this.textViewUserEmail = itemView.findViewById(R.id.user_email);
            this.textViewDate = itemView.findViewById(R.id.foglalas_date);
            this.textViewPhonenumber = itemView.findViewById(R.id.phonenumber);

        }

        public void bindTo(Foglalas foglalas) {

            textViewVenueId.setText(foglalas.getVenueId());
            textViewUserName.setText(foglalas.getUserDisplayName());
            textViewUserEmail.setText(foglalas.getUserEmail());
            textViewDate.setText(foglalas.getDate());
            textViewPhonenumber.setText(foglalas.getPhoneNumber());
            itemView.findViewById(R.id.delete).setOnClickListener(view -> ((FoglalasByUserActivity)context).deleteFoglalas(foglalas));
        }
    }
}
