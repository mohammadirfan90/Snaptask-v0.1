package com.example.snaptask_v01.adapter;
//
import com.bumptech.glide.Glide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snaptask_v01.R;
import com.example.snaptask_v01.model.Gig;
import com.google.android.material.button.MaterialButton;

import java.util.List;
//
//
//public class GigAdapter extends RecyclerView.Adapter<GigAdapter.GigViewHolder> {
//
//    private List<Gig> gigList;
//
//    public GigAdapter(List<Gig> gigList) {
//        this.gigList = gigList;
//    }
//
//    @NonNull
//    @Override
//    public GigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.gig_item, parent, false);
//        return new GigViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull GigViewHolder holder, int position) {
//        Gig gig = gigList.get(position);
//        holder.tvTitle.setText(gig.getTitle());
//        holder.tvDescription.setText(gig.getDescription());
//        holder.tvPrice.setText("৳" + gig.getPrice());
//        // Inside onBindViewHolder
//        Glide.with(holder.itemView.getContext())
//                .load(gig.getImageUrl())
//                .placeholder(R.drawable.ic_work)
//                .into(holder.imgGig);
//    }
//
//    @Override
//    public int getItemCount() {
//        return gigList.size();
//    }
//
//    public static class GigViewHolder extends RecyclerView.ViewHolder {
//        TextView tvTitle, tvDescription, tvPrice;
//        ImageView imgGig;
//        public GigViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tvGigTitle);
//            tvDescription = itemView.findViewById(R.id.tvGigDescription);
//            tvPrice = itemView.findViewById(R.id.tvGigPrice);
//            imgGig = itemView.findViewById(R.id.imgGig);
//        }
//    }
//}

public class GigAdapter extends RecyclerView.Adapter<GigAdapter.GigViewHolder> {

    private List<Gig> gigList;
    private boolean showHireButton;
    private OnHireClickListener hireClickListener;

    public interface OnHireClickListener {
        void onHireClick(Gig gig);
    }

    // Constructor for freelancer (no CTA)
    public GigAdapter(List<Gig> gigList) {
        this.gigList = gigList;
        this.showHireButton = false;
    }

    // Constructor for client with CTA
    public GigAdapter(List<Gig> gigList, OnHireClickListener listener) {
        this.gigList = gigList;
        this.showHireButton = true;
        this.hireClickListener = listener;
    }

    @NonNull
    @Override
    public GigViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gig_item, parent, false);
        return new GigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GigViewHolder holder, int position) {
        Gig gig = gigList.get(position);
        holder.tvTitle.setText(gig.getTitle());
        holder.tvDescription.setText(gig.getDescription());
        holder.tvPrice.setText("৳" + gig.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(gig.getImageUrl())
                .placeholder(R.drawable.ic_work)
                .into(holder.imgGig);

        if (showHireButton) {
            holder.btnHire.setVisibility(View.VISIBLE);
            holder.btnHire.setOnClickListener(v -> {
                if (hireClickListener != null) {
                    hireClickListener.onHireClick(gig);
                }
            });
        } else {
            holder.btnHire.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return gigList.size();
    }

    public static class GigViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvPrice;
        ImageView imgGig;
        MaterialButton btnHire;

        public GigViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvGigTitle);
            tvDescription = itemView.findViewById(R.id.tvGigDescription);
            tvPrice = itemView.findViewById(R.id.tvGigPrice);
            imgGig = itemView.findViewById(R.id.imgGig);
            btnHire = itemView.findViewById(R.id.btnHire);
        }
    }
}
