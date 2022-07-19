package com.example.ppp_a167536_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BeverageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beverage_detail);

        Intent intent = getIntent();

        TextView tvName = findViewById(R.id.tv_beverage_name_detail);
        tvName.setText(intent.getStringExtra("beverageName"));
    }
}
