package com.example.ppp_a167536_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.ppp_a167536_2.adapter.DeviceFragmentPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton signout;
    TextView totalHarga;
    DatabaseReference cartRef;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String uid = mAuth.getCurrentUser().getUid();

        cartRef = mDatabase.child("userTotalCartSession").child(uid);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child("totalCart").getValue()!=null) {
                    totalHarga.setText(snapshot.child("totalCart").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        totalHarga = findViewById(R.id.id_harga);

//        toolbar = findViewById(R.id.toolbar_main);
        tabLayout = findViewById(R.id.tabs_main);
        viewPager = findViewById(R.id.view_pager_main);
        signout = findViewById(R.id.btn_signout);
        signout.setOnClickListener(this);

        setSupportActionBar(toolbar);
        setupViewPager();

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_add_shopping_cart_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_account_balance_wallet_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_layers_24);


        // Live detection and tracking
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
                        .enableClassification()  // Optional
                        .build();

        ObjectDetector objectDetector = ObjectDetection.getClient(options);
    }

    private void setupViewPager()
    {
        DeviceFragmentPagerAdapter adapter = new DeviceFragmentPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new HomeFragment(), "Home");
        adapter.addFrag(new SearchingFragment(), "Cari");
        adapter.addFrag(new CalculateFragment(), "Kira");
        adapter.addFrag(new TdModelFragment(), "3dModel");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(MainActivity.this, "You have to login to continue", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, Login.class);

            startActivity(intent);
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_signout){
            signOut();
        }
    }

    private void signOut() {
        Log.d("signout","nice");
        mAuth.signOut();

        Intent intent = new Intent(MainActivity.this, Login.class);

        startActivity(intent);
    }
}