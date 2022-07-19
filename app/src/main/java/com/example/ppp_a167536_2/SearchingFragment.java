package com.example.ppp_a167536_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppp_a167536_2.Interface.IFirebaseLoadListener;
import com.example.ppp_a167536_2.Models.Barang;
import com.example.ppp_a167536_2.Models.ItemData;
import com.example.ppp_a167536_2.Models.ItemGroup;
import com.example.ppp_a167536_2.adapter.BeverageRecyclerViewAdapter;
import com.example.ppp_a167536_2.adapter.MyItemGroupAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class SearchingFragment extends Fragment implements IFirebaseLoadListener {

    AlertDialog dialog;
    IFirebaseLoadListener iFirebaseLoadListener;
    RecyclerView my_recycler_view;
    DatabaseReference itemData;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions options;
    ImageView itemLocation;

    LinearLayoutManager linearLayoutManager;

    public SearchingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_searching, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        itemData = FirebaseDatabase.getInstance().getReference();

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Beverage> allBeverageInfor = getAllBeverageInfor();
        BeverageRecyclerViewAdapter beverageRecyclerViewAdapter = new BeverageRecyclerViewAdapter(getContext(),allBeverageInfor);
        recyclerView.setAdapter(beverageRecyclerViewAdapter);

        my_recycler_view = view.findViewById(R.id.rv_result_food);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new GridLayoutManager(getContext(),3));

        SearchView searchView = view.findViewById(R.id.sv_item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchItem(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchItem(s);
                return false;
            }
        });


            return view;

    }

    private void searchItem(String SearchText) {
        DatabaseReference ref = itemData.child("Item");

        Log.d("BsItem", "test");

        Query searchQuery = ref.orderByChild("iname").startAt(SearchText).endAt(SearchText + "\uf8ff");

        Log.d("AsItem", searchQuery.toString());

        recyclerView = getActivity().findViewById(R.id.rv_result_food);

        options = new FirebaseRecyclerOptions.Builder<Barang>().setQuery(searchQuery, Barang.class).build();
        FirebaseRecyclerAdapter<Barang, BeverageRecyclerViewAdapter.ResultViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Barang, BeverageRecyclerViewAdapter.ResultViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BeverageRecyclerViewAdapter.ResultViewHolder myViewHolder, int i, @NonNull final Barang barang) {
                        String rname = barang.getIname();
                        String rprice = String.valueOf(barang.getIprice());

                        Log.d("sItemName", rname);

                        myViewHolder.tvResultName.setText(rname);
                        myViewHolder.tvResultPrice.setText(rprice);
                        Picasso.get().load(barang.getIimage()).into(myViewHolder.imgResult);

                        myViewHolder.cvResult.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String dItem = barang.getIlocation();
//                                String test = barang.getIimage();

                                Log.d("image", barang.getIimage());
                                Log.d("directory", dItem);

                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                View itemView = LayoutInflater.from(view.getContext()).inflate(R.layout.directory_item,null);

                                itemLocation = itemView.findViewById(R.id.img_directory_item);
//                                itemLTest = itemView.findViewById(R.id.tv_directory);

                                Picasso.get().load(dItem).into(itemLocation);
//                                itemLTest.setText(dItem);

                                builder.setView(itemView);

                                TextView title = new TextView(view.getContext());

                                title.setText("Lokasi Barangan");
                                title.setBackgroundColor(Color.DKGRAY);
                                title.setPadding(10, 10, 10, 10);
                                title.setGravity(Gravity.CENTER);
                                title.setTextColor(Color.WHITE);
                                title.setTextSize(20);

                                builder.setCustomTitle(title);

//                                builder.setTitle("Lokasi Barangan");

                                builder.show();
//                                AlertDialog dialog = builder.create();
//                                dialog.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public BeverageRecyclerViewAdapter.ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row,parent, false);

                        return new BeverageRecyclerViewAdapter.ResultViewHolder(v);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        /*recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));*/
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

   @Override
    public void onFirebaseLoadSuccess(List<ItemGroup> itemGroupList) {
        MyItemGroupAdapter adapter = new MyItemGroupAdapter(getContext(), itemGroupList);
        my_recycler_view.setAdapter(adapter);

        dialog.dismiss();
    }

   @Override
    public void onFirebaseLoadFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    public List<Beverage> getAllBeverageInfor()
    {
        List<Beverage> allBeverage = new ArrayList<Beverage>();

        allBeverage.add(new Beverage("Daging", R.drawable.search_meat));
        allBeverage.add(new Beverage("Sayur", R.drawable.search_vege));
        allBeverage.add(new Beverage("Minuman", R.drawable.search_beverages));
        allBeverage.add(new Beverage("Ikan", R.drawable.search_fish));
        allBeverage.add(new Beverage("Buah", R.drawable.search_fruit));

        return allBeverage;
    }


}