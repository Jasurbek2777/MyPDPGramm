package com.example.mypdpgramm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypdpgramm.databinding.GroupItemBinding
import com.example.mypdpgramm.models.Group
import com.example.mypdpgramm.models.User

class GroupRvAdapter (var list: ArrayList<Group>, var itemClick: itemOnCLick) :
    RecyclerView.Adapter<GroupRvAdapter.Vh>() {
    inner class Vh(var item: GroupItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: Group, position: Int) {
            item.tv.text = user.name
            item.root.setOnClickListener {
                itemClick.itemClick(user, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        GroupItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface itemOnCLick {
        fun itemClick(people: Group, position: Int)
    }
}