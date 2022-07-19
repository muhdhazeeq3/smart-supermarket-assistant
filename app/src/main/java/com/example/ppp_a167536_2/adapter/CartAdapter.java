package com.example.ppp_a167536_2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppp_a167536_2.Models.Barang;
import com.example.ppp_a167536_2.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<com.example.ppp_a167536_2.adapter.CartAdapter.CartViewHolder> {

    private Context context;
    private List<Barang> cartDataList;

    public CartAdapter(Context context, List<Barang> cartDataList) {
        this.context = context;
        this.cartDataList = cartDataList;
    }

    @NonNull
    @NotNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View cartList = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list,null);

        CartAdapter.CartViewHolder cart = new CartAdapter.CartViewHolder(cartList);
        return cart;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.CartViewHolder holder, int position) {
        String iprice = String.valueOf(cartDataList.get(position).getIprice());
        String iquantity = String.valueOf(cartDataList.get(position).getIquantity());

        holder.tvNumber.setText(String.valueOf(position + 1 + "."));
        holder.tvItemName.setText(cartDataList.get(position).getIname());
        holder.tvItemPrice.setText(iprice);
        holder.tvQuantity.setText(iquantity);
    }

    @Override
    public int getItemCount() {
        return (cartDataList != null ? cartDataList.size():0);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvItemName;
        public TextView tvItemPrice;
        public TextView tvQuantity;
        public TextView tvNumber;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tv_item_list);
            tvItemPrice = itemView.findViewById(R.id.tv_price_list);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvNumber = itemView.findViewById(R.id.tv_number);
            itemView.setOnClickListener(this);
        }

        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        TextView itemQuantity;

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.scan_result_delete,null);

//            itemImage = itemView.findViewById(R.id.iv_item);
            itemName = itemView.findViewById(R.id.tv_item_name_delete);
            itemPrice = itemView.findViewById(R.id.tv_item_price_delete);
            itemQuantity = itemView.findViewById(R.id.tv_quantity_delete);

            itemName.setText(cartDataList.get(getAdapterPosition()).getIname());
            itemPrice.setText(String.valueOf(cartDataList.get(getAdapterPosition()).getIprice()));
            itemQuantity.setText(String.valueOf(cartDataList.get(getAdapterPosition()).getIquantity()));

//            searchItem(itemID);

            builder.setView(itemView);

            builder.setTitle("Adakah Anda Ingin Membuang Barangan Ini Dari Troli?");
            builder.setPositiveButton("Buang Barangan Dari Troli", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d("clickDelete", itemName.toString());



//                        getActivity().finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            positiveButtonLL.weight = 1.0f;
            positiveButtonLL.gravity = Gravity.CENTER;
            positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
            positiveButton.setLayoutParams(positiveButtonLL);

 
        }
    }
}
