package com.eer.getirt.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eer.getirt.R;
import com.eer.getirt.models.Category;

import java.util.ArrayList;

/**
 * Created by Ergun on 20.02.2016.
 */
public class RVCategoryAdapter extends RecyclerView.Adapter<RVCategoryAdapter.CategoryViewHolder> {

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView textViewCategoryName;
        //ImageView imageViewCategoryImage;

        CategoryViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.category_item_layout_card_view);
            textViewCategoryName = (TextView)itemView.findViewById(R.id.category_item_text_view_name);
            //imageViewCategoryImage = (ImageView)itemView.findViewById(R.id.category_image_view_image);
        }

    }

    ArrayList<Category> categories;
    Context context;
    public RVCategoryAdapter(ArrayList<Category> categories, Context context){
        this.categories = categories;
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_category_item_layout, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(v);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.textViewCategoryName.setText(categories.get(position).getName());
        //holder.imageViewCategoryImage.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_example_product));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void changeDataList(ArrayList<Category> categoryList){
        this.categories = categoryList;
        notifyDataSetChanged();
    }

    public ArrayList<Category> getDataList(){
        return categories;
    }

}
