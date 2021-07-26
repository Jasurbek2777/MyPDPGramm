package com.example.mypdpgramm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.component1
import androidx.recyclerview.widget.RecyclerView
import com.example.mypdpgramm.databinding.ChatItemBinding
import com.example.mypdpgramm.databinding.MessageItemSenderBinding
import com.example.mypdpgramm.databinding.MessageItemUserBinding
import com.example.mypdpgramm.models.Message
import com.example.mypdpgramm.models.User
import com.squareup.picasso.Picasso

class MessageAdapter(
    var list: ArrayList<Message>,
    var itemClick: itemOnCLick,
    var currentUser: User
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class FromVh(var item: MessageItemSenderBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: Message, position: Int) {
            item.tv.text = user.text
            item.time.text=user.time
        }
    }

    inner class ToVh(var item: MessageItemUserBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: Message, position: Int) {
            item.tv.text = user.text
            item.time.text=user.time
        }
    }


    override fun getItemCount(): Int = list.size

    interface itemOnCLick {
        fun itemClick(people: User, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return FromVh(
                MessageItemSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ToVh(
            MessageItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val fromVh = holder as FromVh
            fromVh.onBind(list[position],position)
        } else {
            val toVh = holder as ToVh
            toVh.onBind(list[position],position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].from == currentUser.uid) {
            return 2
        }
        return 1
    }
}