package com.example.mypdpgramm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypdpgramm.R
import com.example.mypdpgramm.databinding.GroupMessageItemSenderBinding
import com.example.mypdpgramm.databinding.GroupMessageItemUserBinding
import com.example.mypdpgramm.models.GroupMessage
import com.example.mypdpgramm.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class GroupChatAdapter(
    var list: ArrayList<GroupMessage>,
    var itemClick: itemOnCLick,
    var currentUser: User
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var database = FirebaseDatabase.getInstance()
    var myRef2 = database.getReference("Users")
    var users = ArrayList<User>()

    inner class FromVh(var item: GroupMessageItemSenderBinding) :
        RecyclerView.ViewHolder(item.root) {

        fun onBind(user: GroupMessage, position: Int) {
            myRef2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val children = dataSnapshot.children
                    users = ArrayList()
                    for (i in children) {
                        val value = i.getValue(User::class.java)
                        if (value != null) {
                            users.add(value)
                        }
                    }
                    item.tv.text = user.text
                    item.time.text = user.time
                    for (i in users) {
                        if (i.uid == user.groupId) {
                            Picasso.get().load(i.photoUrl).into(item.userImage)
                            item.name.text = i.name
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
//            item.tv.text = user.text
//            item.time.text = user.time
//            for (i in users) {
//                if (i.uid == user.groupId) {
//                    Picasso.get().load(i.photoUrl).placeholder(R.drawable.holder)
//                        .into(item.userImage)
//                    item.name.text = i.name
//                }
//            }
        }
    }

    inner class ToVh(var item: GroupMessageItemUserBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: GroupMessage, position: Int) {
            myRef2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val children = dataSnapshot.children
                    users = ArrayList()
                    for (i in children) {
                        val value = i.getValue(User::class.java)
                        if (value != null) {
                            users.add(value)
                        }
                    }
                    item.tv.text = user.text
                    item.time.text = user.time
                    for (i in users) {
                        if (i.uid == user.groupId) {
                            Picasso.get().load(i.photoUrl).into(item.userImage)
                            item.name.text = i.name
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }


    override fun getItemCount(): Int = list.size

    interface itemOnCLick {
        fun itemClick(people: User, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            return FromVh(
                GroupMessageItemSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return ToVh(
            GroupMessageItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val fromVh = holder as FromVh
            fromVh.onBind(list[position], position)
        } else {
            val toVh = holder as ToVh
            toVh.onBind(list[position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].groupId == currentUser.uid) {
            return 2
        }
        return 1
    }
}