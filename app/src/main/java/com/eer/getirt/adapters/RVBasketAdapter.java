package com.eer.getirt.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eer.getirt.R;
import com.eer.getirt.models.BasketProduct;

import java.util.ArrayList;

/**
 * A usual RecyclerViewAdapter except changeDataList method.
 * Created by Ergun on 20.02.2016.
 */
public class RVBasketAdapter extends RecyclerView.Adapter<RVBasketAdapter.BasketViewHolder> {

    public class BasketViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewQuantity;

        ImageButton imageButtonPlus;
        ImageButton imageButtonMinus;

        public BasketViewHolder(View itemView) {
            super(itemView);
            textViewProductName = (TextView)itemView.findViewById(R.id.basket_item_text_view_product_name);
            textViewProductPrice = (TextView)itemView.findViewById(R.id.basket_item_text_view_product_price);
            textViewQuantity = (TextView)itemView.findViewById(R.id.basket_item_text_view_quantity);


            imageButtonPlus = (ImageButton)itemView.findViewById(R.id.basket_item_image_button_plus);
            imageButtonMinus = (ImageButton)itemView.findViewById(R.id.basket_item_image_button_minus);
        }
    }
    ArrayList<BasketProduct> basketProducts;
    public RVBasketAdapter(ArrayList<BasketProduct> basketProducts){
        this.basketProducts = basketProducts;
    }

    @Override
    public BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_basket_item, parent, false);
        BasketViewHolder bvh = new BasketViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BasketViewHolder holder, int position) {
        BasketProduct bp = basketProducts.get(position);
        holder.textViewProductName.setText(bp.getProductName());
        holder.textViewProductPrice.setText(bp.getProductPriceStr());
        holder.textViewQuantity.setText(bp.getProductQuantity());
    }

    @Override
    public int getItemCount() {
        return basketProducts.size();
    }


    /**
     * It changes the datalist and notifies the adapter. It is useful because it
     * gives us permission to change the datalist of the adapter wherever we can
     * reach the adapter.
     * @param basketProducts
     */
    public void changeDataList(ArrayList<BasketProduct> basketProducts){
        this.basketProducts = basketProducts;
        notifyDataSetChanged();
    }

}
