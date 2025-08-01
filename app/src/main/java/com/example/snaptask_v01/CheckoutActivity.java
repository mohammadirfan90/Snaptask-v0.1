package com.example.snaptask_v01;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class CheckoutActivity extends AppCompatActivity {

    TextView tvTitle, tvPrice, tvFreelancer;
    ImageView ivGigImage;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvTitle = findViewById(R.id.tvGigTitle);
        tvPrice = findViewById(R.id.tvGigPrice);
        tvFreelancer = findViewById(R.id.tvFreelancerName);
        ivGigImage = findViewById(R.id.ivGigImage); // optional

        String title = getIntent().getStringExtra("gigTitle");
        int price = getIntent().getIntExtra("gigPrice", 0);
        String imageUrl = getIntent().getStringExtra("gigImageUrl");
        String freelancerName = getIntent().getStringExtra("freelancerName");

        tvTitle.setText(title);
        tvPrice.setText("৳" + price);
        tvFreelancer.setText("By: " + freelancerName);

// Load image if you're showing it (using Glide)
        Glide.with(this).load(imageUrl).into(ivGigImage);

    }
}