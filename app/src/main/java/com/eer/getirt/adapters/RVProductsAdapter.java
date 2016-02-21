package com.eer.getirt.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eer.getirt.R;
import com.eer.getirt.models.Product;

import java.util.ArrayList;

/**
 * @see RVBasketAdapter
 * Created by Ergun on 20.02.2016.
 */
public class RVProductsAdapter extends RecyclerView.Adapter<RVProductsAdapter.ProductsViewHolder> {

    public class ProductsViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView textViewPrice;
        TextView textViewName;

        public ProductsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.product_item_layout_card_view);
            textViewPrice = (TextView)itemView.findViewById(R.id.product_item_text_view_price);
            textViewName = (TextView)itemView.findViewById(R.id.product_item_text_view_name);
        }
    }

    Context context;
    ArrayList<Product> products = new ArrayList<>();
    public RVProductsAdapter(ArrayList<Product> products, Context context){
        this.products = products;
        this.context = context;
    }

    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_product_item_layout, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(v);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {
        holder.textViewName.setText(products.get(position).getProductName());
        holder.textViewPrice.setText(products.get(position).getProductPriceStr());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void changeDataList(ArrayList<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    public ArrayList<Product> getDataList(){
        return products;
    }


}
