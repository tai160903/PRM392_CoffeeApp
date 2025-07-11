package com.example.prm392_coffeeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_coffeeapp.R;
import com.example.prm392_coffeeapp.dao.CartItemDao;
import com.example.prm392_coffeeapp.database.AppDatabase;
import com.example.prm392_coffeeapp.entity.CartItem;
import com.example.prm392_coffeeapp.entity.Product;

import java.util.List;

public class ProductCustomerAdapter extends RecyclerView.Adapter<ProductCustomerAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnAddToCartClickListener {
        void onAddToCart(Product product);
    }
    private OnAddToCartClickListener onAddToCartClickListener;
    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.onAddToCartClickListener = listener;
    }

    public ProductCustomerAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_customer, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(product.getPrice() + "$");
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.ivProductImage);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            // Insert CartItem into database
            new Thread(() -> {
                CartItemDao cartItemDao = AppDatabase.getInstance(context).cartItemDao();
                CartItem cartItem = new CartItem(
                        product.getUuid(),
                        product.getName(),
                        1, // Default quantity is 1
                        product.getPrice()
                );
                cartItemDao.insert(cartItem);
                // Show toast on UI thread
                ((android.app.Activity) context).runOnUiThread(() ->
                    Toast.makeText(context, product.getName() + " added to cart", Toast.LENGTH_SHORT).show()
                );
            }).start();

            if (onAddToCartClickListener != null) {
                onAddToCartClickListener.onAddToCart(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvProductPrice;
        Button btnAddToCart;
        ImageView ivProductImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            ivProductImage = itemView.findViewById(R.id.imgProduct);
        }
    }
}