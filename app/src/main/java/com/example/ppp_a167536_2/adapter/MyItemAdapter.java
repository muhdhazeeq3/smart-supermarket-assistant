package com.example.ppp_a167536_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppp_a167536_2.Interface.IItemClickListener;
import com.example.ppp_a167536_2.Models.ItemData;
import com.example.ppp_a167536_2.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyItemAdapter extends RecyclerView.Adapter<com.example.ppp_a167536_2.adapter.MyItemAdapter.MyViewHolder> {

    private Context context;
    private List<ItemData> itemDataList;

    public MyItemAdapter(Context context, List<ItemData> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_item.setText(itemDataList.get(position).getName());
        Picasso.get().load(itemDataList.get(position).getImage()).into(holder.img_item);

        holder.setiItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(context, ""+itemDataList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (itemDataList != null ? itemDataList.size():0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_item;
        ImageView img_item;

        IItemClickListener iItemClickListener;

        public void setiItemClickListener(IItemClickListener iItemClickListener) {
            this.iItemClickListener = iItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_item = (TextView) itemView.findViewById(R.id.tvTitle);
            img_item = (ImageView) itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iItemClickListener.onItemClickListener(view, getAdapterPosition());
        }
    }
}
