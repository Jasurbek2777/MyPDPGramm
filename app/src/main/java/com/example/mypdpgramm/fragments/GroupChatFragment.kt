package com.example.mypdpgramm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypdpgramm.adapters.GroupChatAdapter
import com.example.mypdpgramm.adapters.MessageAdapter
import com.example.mypdpgramm.databinding.FragmentGroupChatBinding
import com.example.mypdpgramm.models.Group
import com.example.mypdpgramm.models.GroupMessage
import com.example.mypdpgramm.models.Message
import com.example.mypdpgramm.models.User
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var currentGroup: Group

class GroupChatFragment : Fragment() {
    lateinit var binding: FragmentGroupChatBinding
    private var param1: String? = null
    private var param2: User? = null
    lateinit var myRef: DatabaseReference
    lateinit var myRef2: DatabaseReference
    lateinit var messages: ArrayList<GroupMessage>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param2 = it.getSerializable(ARG_PARAM2) as User
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        messages = ArrayList()
        binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Groups")
        myRef2 = database.getReference("GroupsMessages")
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children
                for (i in children) {
                    val value = i.getValue(Group::class.java)
                    if (value?.id == param1) {
                        binding.itemName.text = value?.name
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })


        binding.send.setOnClickListener {
            val date = Date()
            val dateFormat = SimpleDateFormat("HH:mm")
            val curremtDate = dateFormat.format(date)
            val text = binding.et.text.toString()
            if (text != "" && text.isNotBlank()) {
                binding.et.setText("")
                val message = GroupMessage(text, curremtDate, param1, param2?.uid)
                val key = myRef2.push().key
                myRef2.child("$key").setValue(message)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Text is empty please write something !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        myRef2.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                messages = ArrayList()
                for (child in data.children) {
                    child.children
                    val value = child.getValue((GroupMessage()::class.java))
                    if (value != null) {
                        if (param1 == value.from)
                            messages.add(value)
                    }
                }

                binding.rv.adapter = GroupChatAdapter(messages, object : GroupChatAdapter.itemOnCLick {
                        override fun itemClick(people: User, position: Int) {

                        }
                    }, param2!!)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        binding.rv.adapter = GroupChatAdapter(messages, object : GroupChatAdapter.itemOnCLick {
            override fun itemClick(people: User, position: Int) {

            }
        }, param2!!)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}