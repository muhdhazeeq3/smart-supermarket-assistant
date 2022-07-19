package com.example.ppp_a167536_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.ar.sceneform.AnchorNode;
//import com.google.ar.sceneform.rendering.ModelRenderable;
//import com.google.ar.sceneform.rendering.Renderable;
//import com.google.ar.sceneform.rendering.RenderableInstance;
//import com.google.ar.sceneform.ux.ArFragment;
//import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FileDownloadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
//import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.Callable;

public class TdModelFragment extends Fragment implements View.OnClickListener{

    Button dl;

    DatabaseReference mydata;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_3dmodel, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        dl = (Button) view.findViewById(R.id.downloadBtn);
        dl.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        scanCode();
    }

    private void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");

        integrator.forSupportFragment(TdModelFragment.this).setPrompt("Sila Imbas Kod Bar Barangan Yang Ingin Dilihat").setCaptureActivity(CaptureAct.class).initiateScan();

//        integrator.initiateScan();
    }

    private void viewModel(String itemLink, String title) {
        Intent sceneViewerIntent;
        sceneViewerIntent = new Intent(Intent.ACTION_VIEW);

        Uri intentUri =
                Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                .appendQueryParameter("file", itemLink)
                .appendQueryParameter("mode", "3d_preferred")
                .appendQueryParameter("title", title)
                .build();
        sceneViewerIntent.setData(intentUri);
        sceneViewerIntent.setPackage("com.google.ar.core");

        startActivity(sceneViewerIntent);
    }

    private void searchItem(String id){
        DatabaseReference ref = mDatabase.child("Item");

        Query searchQuery = ref.orderByKey().equalTo(id);

        Log.d("searchTest", id);
        Log.d("queryResult", searchQuery.toString());

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        searchQuery.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                Log.d("snap", snapshot.toString());
                                if(snapshot.child("itdmodel").exists()) {

                                    Log.d("BscanQuery", "test");

                                    String itemLink = snapshot.child("itdmodel").getValue().toString();
                                    String title = snapshot.child("iname").getValue().toString();

                                    Log.d("ar3d", itemLink);

                                    viewModel(itemLink, title);

                                }  else{
                                    Toast.makeText(getContext(),"Barangan Yang Diimbas Tiada Model Tiga Dimensi!", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(),"Barangan Tidak Dapat Dijumpai!", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        String itemLink;
        if (result != null){
            if(result.getContents() != null){

                itemLink = result.getContents();

                searchItem(itemLink);
            }
            else {
                Toast.makeText(getContext(),"No Results", Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode, data);
        }

    }
}
