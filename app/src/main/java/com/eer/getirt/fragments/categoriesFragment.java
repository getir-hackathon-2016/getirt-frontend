package com.eer.getirt.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eer.getirt.R;
import com.eer.getirt.adapters.RVCategoryAdapter;
import com.eer.getirt.connections.ConnectionManager;
import com.eer.getirt.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Fragment for categories, it only has a recycler view in it.
 * Created by Ergun on 20.02.2016.
 */
public class CategoriesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.categories_rv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(gridLayoutManager);

        ArrayList<Category> dummyCategories = Category.getDummyData();
        RVCategoryAdapter rvCategoryAdapter = new RVCategoryAdapter(dummyCategories, getActivity());
        rv.setAdapter(rvCategoryAdapter);

        new GetCategoriesAsyncTask(rvCategoryAdapter).execute();

        return rootView;

    }

    public class GetCategoriesAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        RVCategoryAdapter rvCategoryAdapter;

        GetCategoriesAsyncTask(RVCategoryAdapter rvCategoryAdapter){
            this.rvCategoryAdapter = rvCategoryAdapter;
        }

        ProgressDialog progress = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            progress.setMessage("Kategoriler çekiliyor.");
            progress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return ConnectionManager.getAllCategories();
        }

        @Override
        protected void onPostExecute(JSONObject resultObject) {
            boolean result = false;
            String message = "";
            ArrayList<Category> categories = new ArrayList<>();
            try {
                result = resultObject.getBoolean("result");
                if(!result) {
                    message = resultObject.getString("message");
                    View v = ((Activity) getContext()).findViewById(R.id.categories_rv);
                    Snackbar
                            .make(v, message, Snackbar.LENGTH_SHORT)
                            .show();
                }else{
                    JSONArray jsonCategoriesArray = resultObject.getJSONArray("kategoriler");
                    for(int i = 0; i < jsonCategoriesArray.length(); i++){
                        JSONObject jsonCategory = jsonCategoriesArray.getJSONObject(i);
                        String name = jsonCategory.getString("ad");
                        String id = jsonCategory.getString("_id");
                        Category cat = new Category(id, name);
                        categories.add(cat);
                    }
                    rvCategoryAdapter.changeDataSet(categories);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            progress.dismiss();
        }

    }
}
