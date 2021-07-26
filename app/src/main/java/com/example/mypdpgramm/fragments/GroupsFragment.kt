package com.example.mypdpgramm.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypdpgramm.R
import com.example.mypdpgramm.adapters.GroupRvAdapter
import com.example.mypdpgramm.databinding.DialogBinding
import com.example.mypdpgramm.databinding.FragmentGroupsBinding
import com.example.mypdpgramm.models.Group
import com.example.mypdpgramm.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupsFragment : Fragment() {
    lateinit var groups: ArrayList<Group>
    lateinit var binding: FragmentGroupsBinding
    private var param1: String? = null
    private var param2: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getSerializable(ARG_PARAM2) as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        groups = ArrayList()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Groups")

        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        binding.add.setOnClickListener {
            val builder = AlertDialog.Builder(binding.root.context)
            val dialogBinding = DialogBinding.inflate(inflater)
            builder.setView(dialogBinding.root)
            builder.setPositiveButton(
                "Qo'shish"
            ) { dialog, which ->
                val desc = dialogBinding.et.text.toString()
                if (desc.isNotEmpty()) {
                    val key = myRef.push().key
                    val date = Date()
                    val dateFormat = SimpleDateFormat("HH:mm")
                    val curremtDate = dateFormat.format(date)
                    myRef.child(key.toString()).setValue(Group(curremtDate, desc, 1))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Group name can not be empty",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }.setNegativeButton(
                "Yopish"
            ) { _, _ -> }.create()

            builder.show()

        }
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val children = dataSnapshot.children
                groups = ArrayList()
                for (i in children) {
                    val value = i.getValue(Group::class.java)
                    if (value != null)
                        groups.add(value)
                }
                binding.rv.adapter = GroupRvAdapter(groups, object : GroupRvAdapter.itemOnCLick {
                    override fun itemClick(group: Group, position: Int) {
                        val bundle = Bundle()
                        bundle.putString("param1", group.id)
                        bundle.putSerializable("param2", param2)
                        findNavController().navigate(R.id.groupChatFragment, bundle)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        val anim2 = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
        binding.rv.startAnimation(anim2)
        binding.rv.adapter = GroupRvAdapter(groups, object : GroupRvAdapter.itemOnCLick {
            override fun itemClick(group: Group, position: Int) {
                val bundle = Bundle()
                bundle.putString("param1", group.id)
                bundle.putString("param2", param1)
                findNavController().navigate(R.id.groupChatFragment, bundle)
            }
        })
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: User) =
            GroupsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, param2)
                }
            }
    }
}