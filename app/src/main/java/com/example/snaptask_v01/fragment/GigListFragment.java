package com.example.snaptask_v01.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.snaptask_v01.R;
import com.example.snaptask_v01.adapter.GigAdapter;
import com.example.snaptask_v01.model.Gig;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GigListFragment extends Fragment {

    //ChatGpt Start
    private RecyclerView recyclerView;
    private GigAdapter gigAdapter;
    private List<Gig> gigList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    //Bottom Sheet Declaration Start
    private BottomSheetDialog bottomSheetDialog;
    private ImageView imgGigPreview;
    private EditText etGigTitle, etGigDescription, etGigPrice, etGigTags;
    private Button btnPostGig;

    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imgGigPreview.setImageURI(selectedImageUri);
                }
            }
    );

    //Bottom Sheet Declaration End


    public GigListFragment() {
        // Required empty public constructor
    }
    //ChatGpt End



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gig_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewGigs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gigList = new ArrayList<>();
        gigAdapter = new GigAdapter(gigList);
        recyclerView.setAdapter(gigAdapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FloatingActionButton fabAddGig = view.findViewById(R.id.btnAddGig);
        fabAddGig.setOnClickListener(v -> showAddGigBottomSheet());


        loadGigs();
    }
    private void loadGigs() {
        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("gigs")
                .whereEqualTo("uid", currentUserId)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    gigList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Gig gig = doc.toObject(Gig.class);
                        gig.setId(doc.getId()); // set document ID
                        gigList.add(gig);
                    }
                    gigAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load gigs: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showAddGigBottomSheet() {
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_add_gig, null);
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();

        imgGigPreview = sheetView.findViewById(R.id.imgGigPreview);
        etGigTitle = sheetView.findViewById(R.id.etGigTitle);
        etGigDescription = sheetView.findViewById(R.id.etGigDescription);
        etGigTags = sheetView.findViewById(R.id.etGigTags);

        etGigPrice = sheetView.findViewById(R.id.etGigPrice);
        btnPostGig = sheetView.findViewById(R.id.btnPostGig);

        imgGigPreview.setOnClickListener(v -> openImagePicker());

        btnPostGig.setOnClickListener(v -> uploadGigToFirebase());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

//    private void uploadGigToFirebase() {
//        String title = etGigTitle.getText().toString().trim();
//        String desc = etGigDescription.getText().toString().trim();
//        String priceStr = etGigPrice.getText().toString().trim();
//
//        if (title.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || selectedImageUri == null) {
//            Toast.makeText(getContext(), "All fields and image are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int price = Integer.parseInt(priceStr);
//        String gigId = db.collection("gigs").document().getId();
//        String imagePath = "gig_images/" + gigId + ".jpg";
//
//        FirebaseStorage.getInstance().getReference(imagePath)
//                .putFile(selectedImageUri)
//                .continueWithTask(task -> {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//                    return FirebaseStorage.getInstance().getReference(imagePath).getDownloadUrl();
//                })
//                .addOnSuccessListener(uri -> {
//                    String imageUrl = uri.toString();
//
//                    Gig gig = new Gig();
//                    gig.setId(gigId);
//                    gig.setTitle(title);
//                    gig.setDescription(desc);
//                    gig.setPrice(price);
//                    gig.setImageUrl(imageUrl);
//                    gig.setUid(auth.getCurrentUser().getUid());
//                    gig.setTimestamp(new Date().getTime());
//
//                    db.collection("gigs").document(gigId)
//                            .set(gig)
//                            .addOnSuccessListener(aVoid -> {
//                                Toast.makeText(getContext(), "Gig posted!", Toast.LENGTH_SHORT).show();
//                                bottomSheetDialog.dismiss();
//                                loadGigs(); // refresh
//                            })
//                            .addOnFailureListener(e -> {
//                                Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            });
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//
//    }
    private void uploadGigToFirebase() {
        String title = etGigTitle.getText().toString().trim();
        String desc = etGigDescription.getText().toString().trim();
        String tagsStr = etGigTags.getText().toString().trim();//tags
        String priceStr = etGigPrice.getText().toString().trim();
        List<String> tagsList = new ArrayList<>();

        if (title.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || selectedImageUri == null) {
            Toast.makeText(getContext(), "All fields and image are required", Toast.LENGTH_SHORT).show();
            return;
        }

        //tags
        if (!tagsStr.isEmpty()) {
            for (String tag : tagsStr.split(",")) {
                tagsList.add(tag.trim().toLowerCase()); // normalize
            }
        }

        int price = Integer.parseInt(priceStr);

        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            String base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            OkHttpClient client = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "data:image/jpg;base64," + base64Image)
                    .addFormDataPart("upload_preset", "unsigned_preset") // we'll explain next
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/dhfoliknu/image/upload")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Cloudinary upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Cloudinary error: " + response.message(), Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String jsonResponse = response.body().string();
                    try {
                        JSONObject json = new JSONObject(jsonResponse);
                        String imageUrl = json.getString("secure_url");

                        saveGigToFirestore(title, desc, price, imageUrl, tagsList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            Toast.makeText(getContext(), "Image error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void saveGigToFirestore(String title, String desc, int price, String imageUrl,List<String> tagsList) {
        String gigId = db.collection("gigs").document().getId();

        Gig gig = new Gig();
        gig.setId(gigId);
        gig.setTitle(title);
        gig.setDescription(desc);
        gig.setPrice(price);
        gig.setImageUrl(imageUrl);
        gig.setTags(tagsList);
        gig.setUid(auth.getCurrentUser().getUid());
        gig.setTimestamp(new Date().getTime());

        db.collection("gigs").document(gigId)
                .set(gig)
                .addOnSuccessListener(aVoid -> {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Gig posted!", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                        loadGigs();
                    });
                })
                .addOnFailureListener(e -> {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                });
    }





}