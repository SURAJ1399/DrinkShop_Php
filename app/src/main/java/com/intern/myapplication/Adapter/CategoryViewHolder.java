package com.intern.myapplication.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.intern.myapplication.R;

import org.w3c.dom.Text;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    ImageView img_product;
    TextView txt_menu_name;
    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        img_product=(ImageView)itemView.findViewById(R.id.image_product);
        txt_menu_name=itemView.findViewById(R.id.txt_menu_name);
    }
}
