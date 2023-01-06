package com.example.fixedlabelle.HypeProductRecycler

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fixedlabelle.DetailActivity
import com.example.fixedlabelle.R

class AdapterHypeProduct (private val newlist : ArrayList<dataclassHype>)
    : RecyclerView.Adapter<AdapterHypeProduct.hypeviewholder>(){

        class hypeviewholder(view : View) : RecyclerView.ViewHolder(view){
            val imgcr : ImageView = view.findViewById(R.id.gambar_product)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): hypeviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return hypeviewholder(view)
    }
    override fun onBindViewHolder(
        holder: hypeviewholder,
        position: Int
    ) {
        val data = newlist[position]
        Glide.with(holder.imgcr.context).load(data.imgrc).into(holder.imgcr)
        holder.imgcr.setOnClickListener{
            val intent = Intent(holder.imgcr.context , DetailActivity::class.java)
            //intent.putExtra()
        }
    }

    override fun getItemCount(): Int {
        return newlist.size
    }
}