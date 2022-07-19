package com.example.ppp_a167536_2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppp_a167536_2.Interface.IFirebaseCartListener;
import com.example.ppp_a167536_2.Models.Barang;
import com.example.ppp_a167536_2.adapter.CartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CalculateFragment extends Fragment implements View.OnClickListener, IFirebaseCartListener {

    Button scanBtn;
    Button btnClear;
    ImageView itemImage;
    TextView itemName;
    TextView itemPrice;
    EditText itemQuantity;

    AlertDialog dialog;
    IFirebaseCartListener iFirebaseCartListener;
    RecyclerView my_recycler_view;
    DatabaseReference mydata;
    DatabaseReference mycart;

    LinearLayoutManager linearLayoutManager;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.fragment_calculate, container, false);

        scanBtn = (Button) view.findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Log.d("kkkkk","a");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        mydata = FirebaseDatabase.getInstance().getReference("userShoppingSession").child(uid);
        mycart = FirebaseDatabase.getInstance().getReference("userTotalCartSession").child(uid).child("totalCart");
        dialog = new SpotsDialog.Builder().setContext(getContext()).build();
        iFirebaseCartListener = this;

        my_recycler_view = view.findViewById(R.id.rvCart);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new GridLayoutManager(getContext(),1));

        getFirebaseCart();
        
        return view;

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.scanBtn) {
            scanCode();
        }else {
            clearCart();
        }

    }

    private void clearCart() {
        mydata.removeValue();
        mycart.setValue(0);

    }

    private void scanCode() {

        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");

        integrator.forSupportFragment(CalculateFragment.this).setPrompt("Sila Imbas Kod Bar Barangan Yang Ingin Dibeli").setCaptureActivity(CaptureAct.class).initiateScan();

//        integrator.initiateScan();
    }

    Boolean itemExist;
    private boolean searchItem(String id){
        DatabaseReference ref = mDatabase.child("Item");
        Query searchQuery = ref.orderByKey().equalTo(id);
        Boolean test;
        Log.d("searchTest", id);

//        Barang newItem = new Barang();

        ref.child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    searchQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                            Log.d("BscanQuery", "test");
                            Log.d("snapCalculate", snapshot.toString());

                            Picasso.get().load(snapshot.child("iimage").getValue().toString()).into(itemImage);
                            itemName.setText(snapshot.child("iname").getValue().toString());
                            itemPrice.setText(snapshot.child("iprice").getValue().toString());

                            itemExist = true;

                            Log.d("bool3", itemExist.toString());
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
//                    Toast.makeText(getContext(),"Barangan Tidak Dapat Dijumpai!", Toast.LENGTH_LONG).show();
                    itemExist = false;
                    Log.d("bool4", itemExist.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        test = itemExist;
        return test;
    }

    private void addItem(String id, String iname, double iprice, int iquantity){

        Log.d("addItem",iname + " " + iprice);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        Barang barang = new Barang(iname, iprice, iquantity);

        mDatabase.child("userShoppingSession").child(uid).child(id).setValue(barang);

        double totalPrice = iprice * iquantity;

        totalItem(totalPrice);

    }


    private void totalItem(double totalPrice) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        mycart.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {

                Log.d("trans1", "1");
                String cart = null;
                if(mutableData.getValue()!=null) {
                    cart = mutableData.getValue().toString();
                }


                if (cart == null) {
                    // Node doesn't exist or its value isn't known yet => return success
                    mDatabase.child("userTotalCartSession").child(uid).child("totalCart").setValue(totalPrice);

                    Log.d("trans2", "2");

                    return Transaction.success(mutableData);
                }
                double totalCart = Double.parseDouble(cart);

                Log.d("trans3", "3");

                totalCart += totalPrice;
//                int count = stat.getStatP();
//                int statP = count + 1;
//                stat = new Stat(statP);

                Log.d("trans4", "4");

                // Set value and report transaction success
                mutableData.setValue(totalCart);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d("transaction", "statTransaction:onComplete:" + databaseError);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        String itemID;

        itemExist = true;

        if (result != null){
            if(result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View itemView = LayoutInflater.from(getContext()).inflate(R.layout.scan_result_quantity_1,null);
                itemID = result.getContents();

                itemImage = itemView.findViewById(R.id.iv_item);
                itemName = itemView.findViewById(R.id.tv_item_name_delete);
                itemPrice = itemView.findViewById(R.id.tv_item_price_delete);
                itemQuantity = itemView.findViewById(R.id.ev_quantity);

                searchItem(itemID);

                Log.d("bool1", itemExist.toString());

                if (searchItem(itemID)){
                    builder.setView(itemView);

                    builder.setTitle("Scanning Result");
                    builder.setPositiveButton("Imbas Semula", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scanCode();
                        }
                    });
                    builder.setNeutralButton("Ambil Barangan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("beforeSearch", itemID);

                            if (!validateQuantity()) {

                                Toast.makeText(getContext(), "Sila isi kuantiti barangan yang ingin diambil!", Toast.LENGTH_SHORT).show();

                                scanCode();
                            }


//                        String price = itemPrice.getText().toString();
                            double price = Double.parseDouble(itemPrice.getText().toString());
                            int quantity = Integer.parseInt(itemQuantity.getText().toString());
                            addItem(itemID,itemName.getText().toString(), price, quantity);

                            Log.d("afterSearch", itemID);

//                        getActivity().finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();


                }else {
                    Toast.makeText(getContext(),"Barangan Tidak Dapat Dijumpai!", Toast.LENGTH_LONG).show();
                }
                Log.d("bool2", itemExist.toString());
            }
            else {
                Toast.makeText(getContext(),"No Results", Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode, data);
        }

    }

    private void getFirebaseCart() {
        dialog.show();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        mydata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List <Barang> itemCart = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Log.d("bcart", "test");

                    String iname = snapshot.child("iname").getValue(String.class);
                    double iprice = snapshot.child("iprice").getValue(Double.class);
                    int iquantity = snapshot.child("iquantity").getValue(Integer.class);

                    Log.d("cart", iname + " " + iprice + " " + iquantity);

                    Barang iCart = new Barang(iname, iprice, iquantity);

                    Log.d("acart", iname);

                    itemCart.add(iCart);


                }
                iFirebaseCartListener.onFirebaseLoadSuccess(itemCart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseCartListener.onFirebaseLoadFailure(databaseError.getMessage());
            }
        });
    }

    private boolean validateQuantity() {
        boolean valid = true;

        String iquantity = itemQuantity.getText().toString();
        if (TextUtils.isEmpty(iquantity)) {
            itemQuantity.setError("Required.");
            valid = false;
        } else {
            itemQuantity.setError(null);
        }

        return valid;
    }

    @Override
    public void onFirebaseLoadSuccess(List<Barang> itemGroupList) {
        CartAdapter adapter = new CartAdapter(getContext(), itemGroupList);
        my_recycler_view.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onFirebaseLoadFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
