package com.example.snaptask_v01.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.snaptask_v01.CheckoutActivity;
import com.example.snaptask_v01.R;
import com.example.snaptask_v01.adapter.GigAdapter;
import com.example.snaptask_v01.model.Gig;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class SearchGigFragment extends Fragment {
    private RecyclerView rvSearchResults;
    private GigAdapter gigAdapter;
    private List<Gig> gigList = new ArrayList<>();
    private FirebaseFirestore db;

    EditText etSearch;

    List<Gig> fullGigList = new ArrayList<>(); // all gigs
    List<Gig> filteredGigList = new ArrayList<>(); // visible list


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_gig, container, false);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        etSearch = view.findViewById(R.id.etSearch);

        db = FirebaseFirestore.getInstance();


        // Initialize lists
        fullGigList = new ArrayList<>();
        filteredGigList = new ArrayList<>();
        gigAdapter = new GigAdapter(filteredGigList, gig -> {
            String freelancerUid = gig.getUid();

            db.collection("users") // or "freelancers" depending on your structure
                    .document(freelancerUid)
                    .get()
                    .addOnSuccessListener(userSnapshot -> {
                        if (userSnapshot.exists()) {
                            String freelancerName = userSnapshot.getString("FullName"); // adjust field name if different

                            Intent intent = new Intent(getContext(), CheckoutActivity.class);
                            intent.putExtra("gigTitle", gig.getTitle());
                            intent.putExtra("gigPrice", gig.getPrice());
                            intent.putExtra("gigImageUrl", gig.getImageUrl());
                            intent.putExtra("freelancerName", freelancerName);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "Freelancer not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to fetch freelancer info", Toast.LENGTH_SHORT).show();
                    });
        });



        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(gigAdapter);
        // Load all gigs once
        loadAllGigs();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGigs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        return view;
    }

    private void loadAllGigs() {
        db.collection("gigs")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    fullGigList.clear();
                    filteredGigList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Gig gig = doc.toObject(Gig.class);
                        fullGigList.add(gig);
                    }
                    filteredGigList.addAll(fullGigList);
                    gigAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load gigs", Toast.LENGTH_SHORT).show();
                });
    }



    private void filterGigs(String query) {
        filteredGigList.clear();

        if (query.trim().isEmpty()) {
            filteredGigList.addAll(fullGigList);
        } else {
            for (Gig gig : fullGigList) {
                if (gig.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredGigList.add(gig);
                } else if (gig.getTags() != null) {
                    for (String tag : gig.getTags()) {
                        if (tag.toLowerCase().contains(query.toLowerCase())) {
                            filteredGigList.add(gig);
                            break;
                        }
                    }
                }
            }
        }

        gigAdapter.notifyDataSetChanged();
    }

}