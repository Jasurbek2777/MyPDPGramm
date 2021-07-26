package com.example.mypdpgramm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypdpgramm.R
import com.example.mypdpgramm.databinding.ChatItemBinding
import com.example.mypdpgramm.models.Message
import com.example.mypdpgramm.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class RvAdapter(
    var context: Context,
    var list: ArrayList<User>,
    var currentUser: User,
    var itemClick: itemOnCLick,
    var changed1: changed
) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var item: ChatItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun onBind(user: User, position: Int) {
            val database3 = FirebaseDatabase.getInstance()
            val lastMessages = ArrayList<Message>()
            var lastMessage = Message()
            val lastMessages1 = ArrayList<Message>()
            val myRef3 = database3.getReference("LastMessages12")
            val myRef4 = database3.getReference("LastMessages12")
            myRef3.child(user.uid + currentUser.uid)
            myRef4.child(currentUser.uid + user.uid)

            myRef3.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    for (i in children) {
                        val value = i.getValue(Message::class.java)
                        if (value?.from == currentUser.uid && value?.to == list[position].uid ||
                            value?.to == currentUser.uid && value?.from == list[position].uid
                        ) {
                            lastMessages.add(value!!)
                        }
                    }
                    if (lastMessages.size > 0 && lastMessages1.size == 0) {
                        lastMessage = lastMessages[0]
                    } else if (lastMessages1.size > 0 && lastMessages.size == 0) {
                        lastMessage = lastMessages1[0]
                    } else if (lastMessages.size > 0 && lastMessages1.size > 0) {
                        if (lastMessages[lastMessages.size - 1].id?.toLong()!! > lastMessages1[0].id?.toLong()!!) {
                            lastMessage = lastMessages[lastMessages.size - 1]
                        } else {
                            lastMessage = lastMessages1[0]
                        }
                    }

                    if (lastMessage.time != null) {
                        changed1.itemChanged(lastMessage,user)
                        item.itemMessage.text = lastMessage.text
                        item.itemTime.text = lastMessage.time
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            myRef4.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    for (i in children) {
                        val value = i.getValue(Message::class.java)
                        if (value?.from == currentUser.uid && value?.to == list[position].uid ||
                            value?.to == currentUser.uid && value?.from == list[position].uid
                        ) {
                            lastMessages1.add(value!!)

                        }
                    }
                    if (lastMessages.size > 0 && lastMessages1.size == 0) {
                        lastMessage = lastMessages[0]
                    }
                    else if (lastMessages1.size > 0 && lastMessages.size == 0) {
                        lastMessage = lastMessages1[0]
                    }
                    else if (lastMessages.size > 0 && lastMessages1.size > 0) {
                        lastMessage =
                            if (lastMessages[lastMessages.size - 1].id?.toLong()!! > lastMessages1[0].id?.toLong()!!) {
                                lastMessages[lastMessages.size - 1]
                            } else {
                                lastMessages1[0]
                            }
                    }

                    if (lastMessage.time != null) {
                        changed1.itemChanged(lastMessage,user)
                        item.itemMessage.text = lastMessage.text
                        item.itemTime.text = lastMessage.time
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            if (lastMessages.size > 0 && lastMessages1.size == 0) {
                lastMessage = lastMessages[0]
            }
            else if (lastMessages1.size > 0 && lastMessages.size == 0) {
                lastMessage = lastMessages1[0]
            }
            else if (lastMessages.size > 0 && lastMessages1.size > 0) {
                if (lastMessages[lastMessages.size - 1].id?.toLong()!! > lastMessages1[lastMessages1.size - 1].id?.toLong()!!) {
                    lastMessage = lastMessages[lastMessages.size - 1]
                } else {
                    lastMessage = lastMessages1[lastMessages1.size - 1]
                }
            }

            if (lastMessage.time != null) {
                item.itemMessage.text = lastMessage.text
                item.itemTime.text = lastMessage.time
            }


            if (item.itemMessage.text == "none") {
                item.itemTime.text = "--:--"
            }

            if (user.online!!) {
                item.indicator.setBackgroundResource(R.drawable.online)
            } else {
                item.indicator.setBackgroundResource(R.drawable.ofline)
            }

            item.itemName.text = user.name
            Picasso.get().load(user.photoUrl).placeholder(R.drawable.holder).into(item.itemImage)
            item.root.setOnClickListener {
                itemClick.itemClick(user, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh = Vh(
        ChatItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface itemOnCLick {
        fun itemClick(people: User, position: Int)
    }

    interface changed {
        fun itemChanged(message: Message, user: User)
    }

}