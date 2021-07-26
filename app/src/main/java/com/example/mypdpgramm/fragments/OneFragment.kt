package com.example.mypdpgramm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypdpgramm.R
import com.example.mypdpgramm.adapters.MessageAdapter
import com.example.mypdpgramm.adapters.RvAdapter
import com.example.mypdpgramm.databinding.FragmentOneBinding
import com.example.mypdpgramm.models.Message
import com.example.mypdpgramm.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OneFragment : Fragment() {
    lateinit var binding: FragmentOneBinding
    private var param1: User? = null
    lateinit var lastMessage: Message
    private var param2: User? = null
    lateinit var myRef: DatabaseReference
    lateinit var messages: ArrayList<Message>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as User
            param2 = it.getSerializable(ARG_PARAM2) as User

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lastMessage = Message()
        binding = FragmentOneBinding.inflate(inflater, container, false)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        Picasso.get().load(param1?.photoUrl).into(binding.itemImage)
        binding.itemName.text = param1?.name
        val database = FirebaseDatabase.getInstance()
        myRef = database.getReference("Message")

        binding.send.setOnClickListener {
            val date = Date()
            val dateFormat = SimpleDateFormat("HH:mm")
            val curremtDate = dateFormat.format(date)
            val text = binding.et.text.toString()
            if (text != "" && text.isNotBlank()) {
                binding.et.setText("")
                val message = Message( System.currentTimeMillis().toString(),text, curremtDate, param1?.uid, param2?.uid)
                lastMessage = message
                val key = myRef.push().key
                myRef.child("$key").setValue(message)
                val database3 = FirebaseDatabase.getInstance()
                val myRef3 = database3.getReference("LastMessages12")
                myRef3.child(param1?.uid+ param2?.uid).setValue(lastMessage)
                var currentUser = param1
                currentUser?.lastMessage = lastMessage
                } else {
                Toast.makeText(requireContext(), "Text is empty please write something !!!", Toast.LENGTH_SHORT).show()
                }
        }

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {
                messages = ArrayList()

                for (child in data.children) {
                    child.children
                    val value = child.getValue((Message()::class.java))
                    if (value != null) {
                        if (param2?.uid == value.to && param1?.uid == value.from || param1?.uid == value.to && param2?.uid == value.from)
                            messages.add(value)
                    }
                }
                val myRef = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().uid.toString())
                if (messages.size > 0)
                    param2?.lastMessage = messages[messages.size - 1]
                myRef.setValue(
                    param2
                )
                binding.rv.adapter = MessageAdapter(messages, object : MessageAdapter.itemOnCLick {
                    override fun itemClick(people: User, position: Int) {

                    }
                }, param1!!)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val database2 = FirebaseDatabase.getInstance()
        val myRef2 = database2.getReference("Users")

        myRef2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var child = dataSnapshot.children
                for (child in child) {
                    val value = child.getValue(User::class.java)
                    if (value?.uid == param1?.uid && value?.name != null) {
                        if (value.online!!)
                            binding.status.text = "online"
                        else {
                            binding.status.text = "last seen at ${value.lastSeen}"
                        }
                        break
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}