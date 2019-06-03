package me.dragon.shout.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.dragon.shout.R

class ShoutsRecyclerViewAdapter: RecyclerView.Adapter<ShoutsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shout_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* Todo */
    }

    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v)
}