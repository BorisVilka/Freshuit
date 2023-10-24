package com.freshuit.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.freshuit.game.databinding.ListItemBinding

class MyAdapter: RecyclerView.Adapter<MyAdapter.Companion.MyHolder>() {

    var data = mutableListOf<Int>()
    val st = arrayOf(
        R.drawable.c1,
        R.drawable.c2,
        R.drawable.c3,
        R.drawable.c4,
        R.drawable.c5,
        R.drawable.c6,
    )
    val random = java.util.Random()

    init {
        for(i in 0..1000) data.add(st[random.nextInt(st.size)])
        notifyDataSetChanged()
    }


    companion object {
        class MyHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.imageView7.setImageResource(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}