package com.example.ppp_a167536_2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppp_a167536_2.Models.ItemData;
import com.example.ppp_a167536_2.Models.ItemGroup;
import com.example.ppp_a167536_2.R;

import java.util.List;

public class MyItemGroupAdapter extends RecyclerView.Adapter<com.example.ppp_a167536_2.adapter.MyItemGroupAdapter.MyViewHolder> {

    private Context context;
    private List<ItemGroup> dataList;

    public MyItemGroupAdapter(Context context, List<ItemGroup> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_group, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item_title.setText(dataList.get(position).getHeaderTitle());

        List<ItemData> itemData = dataList.get(position).getListItem();

        MyItemAdapter itemListAdapter = new MyItemAdapter(context, itemData);
        holder.recycler_view_tem_list.setHasFixedSize(true);
        holder.recycler_view_tem_list.setLayoutManager(new GridLayoutManager(context, 2));
        holder.recycler_view_tem_list.setAdapter(itemListAdapter);
        holder.recycler_view_tem_list.setNestedScrollingEnabled(false);

    }

    @Override
    public int getItemCount() {
        return (dataList != null ? dataList.size():0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title;
        RecyclerView recycler_view_tem_list;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = (TextView) itemView.findViewById(R.id.itemTitle);
            recycler_view_tem_list = (RecyclerView) itemView.findViewById(R.id.recycler_view_list);
        }
    }
}
