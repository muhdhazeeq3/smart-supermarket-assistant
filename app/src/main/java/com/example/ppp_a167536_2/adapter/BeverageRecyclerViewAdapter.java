package com.example.ppp_a167536_2.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppp_a167536_2.Beverage;
import com.example.ppp_a167536_2.BeverageDetailActivity;
import com.example.ppp_a167536_2.Interface.IFirebaseLoadListener;
import com.example.ppp_a167536_2.Interface.IItemClickListener;
import com.example.ppp_a167536_2.Models.Barang;
import com.example.ppp_a167536_2.Models.ItemGroup;
import com.example.ppp_a167536_2.R;
import com.example.ppp_a167536_2.SearchingFragment;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class BeverageRecyclerViewAdapter extends  RecyclerView.Adapter<com.example.ppp_a167536_2.adapter.BeverageRecyclerViewAdapter.BeverageViewHolder> {

    AlertDialog dialog;
    IFirebaseLoadListener iFirebaseLoadListener;
    RecyclerView my_recycler_view;
    DatabaseReference mydata;
    FirebaseRecyclerOptions options;
    RecyclerView recyclerView;

    ImageView itemLocation;
    TextView itemLTest;


    public List<Beverage> beverageList;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public BeverageRecyclerViewAdapter(Context context, List<Beverage> beverageList) {
        this.context = context;
        this.beverageList = beverageList;
    }

    @NonNull
    @Override
    public BeverageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        View beverage_row = LayoutInflater.from(parent.getContext()).inflate(R.layout.beverage_row,null);

        BeverageViewHolder beverageVH = new BeverageViewHolder(beverage_row);
        return beverageVH;
    }

    @Override
    public void onBindViewHolder(@NonNull BeverageViewHolder holder, int position) {
        holder.tvBeverageName.setText(beverageList.get(position).getName());
        holder.imgViewBeverageImage.setImageResource(beverageList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return beverageList.size();
    }

    public class BeverageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvBeverageName;
        public ImageView imgViewBeverageImage;

        public BeverageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBeverageName = itemView.findViewById(R.id.tv_result_food);
            imgViewBeverageImage = itemView.findViewById(R.id.img_beverage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String kategori = beverageList.get(getAdapterPosition()).getName();

            Toast.makeText(itemView.getContext(),"Pilihan yang dipilih: " + kategori, Toast.LENGTH_SHORT).show();

            searchItem(kategori);

//            notifyDataSetChanged();

        }
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder{

        public TextView tvResultName;
        public TextView tvResultPrice;
        public ImageView imgResult;
        public CardView cvResult;

//        public ImageView imgLocation;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvResultName = itemView.findViewById(R.id.tv_result_name);
            tvResultPrice = itemView.findViewById(R.id.tv_result_price);
            imgResult = itemView.findViewById(R.id.img_result);
            cvResult = itemView.findViewById(R.id.cv_result);

//            imgLocation = itemView.findViewById(R.id.img_directory_item);

//            itemView.setOnClickListener(this);
        }

    }


    private void searchItem(String cat){
        DatabaseReference ref = mDatabase.child("Item");

        Log.d("BsItem", "test");

        Query searchQuery = ref.orderByChild("icategory").equalTo(cat);

        Log.d("AsItem", searchQuery.toString());

        recyclerView = ((Activity)context).findViewById(R.id.rv_result_food);

        options = new FirebaseRecyclerOptions.Builder<Barang>().setQuery(searchQuery, Barang.class).build();
        FirebaseRecyclerAdapter<Barang, ResultViewHolder> firebaseRecyclerAdapter =
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

}
