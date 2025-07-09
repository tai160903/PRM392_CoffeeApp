package com.example.prm392_coffeeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_coffeeapp.R;
import com.example.prm392_coffeeapp.entity.OrderItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<OrderItem> cartItems;

    public CartAdapter(List<OrderItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem item = cartItems.get(position);
//        holder.tvName.setText(item.getProductName());
        holder.tvQty.setText("x" + item.getQuantity());
        holder.tvTotal.setText((item.getQuantity() * item.getPrice()) + "đ");
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty, tvTotal;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvQty = itemView.findViewById(R.id.tvItemQty);
            tvTotal = itemView.findViewById(R.id.tvItemTotal);
        }
    }
}

