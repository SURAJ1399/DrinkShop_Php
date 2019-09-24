package com.intern.myapplication.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.intern.myapplication.Model.Category;
import com.intern.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter  <CategoryViewHolder>
 {
     Context context;
     List<Category> categoryList;

     public CategoryAdapter(Context context, List<Category> categoryList) {
         this.context = context;
         this.categoryList = categoryList;
     }

     @NonNull
     @Override
     public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View view= LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);

         return new CategoryViewHolder(view);
     }

     @Override
     public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
         Picasso.with(context).load(categoryList.get(i).getLink()).into(categoryViewHolder.img_product);
         categoryViewHolder.txt_menu_name.setText(categoryList.get(i).getName());

     }



     @Override
     public int getItemCount() {
         return categoryList.size();
     }
 }
