package com.example.mypdpgramm.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mypdpgramm.R
import com.example.mypdpgramm.adapters.ViewPagerAdapter
import com.example.mypdpgramm.databinding.FragmentHomeBinding
import com.example.mypdpgramm.databinding.TabItemBinding
import com.example.mypdpgramm.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    var categoryList = ArrayList<String>()
    private var param1: User? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as User?
            param2 = it.getString(ARG_PARAM2)
        }
        val myRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid.toString())

        val date = Date()
        val dateFormat = SimpleDateFormat("HH:mm")
        val curremtDate = dateFormat.format(date)
        param1?.online = true
        param1?.lastSeen = curremtDate
        myRef.setValue(param1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadCategory()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        loadCategory()

        binding.viewPager.adapter = ViewPagerAdapter(param1!!, categoryList, requireActivity())

        TabLayoutMediator(binding.tab, binding.viewPager) { tab, position ->
            tab.text = loadCategory()[position]
        }.attach()
        setTabs()
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                val tab_item = TabItemBinding.bind(customView!!)

                tab_item.itemTv.setTextColor(Color.WHITE)
                tab_item.itemTv.setBackgroundResource(R.drawable.round)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab!!.customView
                val tab_item = TabItemBinding.bind(customView!!)

                tab_item.itemTv.setTextColor(Color.parseColor("#848484"))
                tab_item.itemTv.setBackgroundResource(R.drawable.round2)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
        return binding.root
    }

    private fun loadCategory(): ArrayList<String> {
        categoryList = ArrayList()
        categoryList.add("Chats")
        categoryList.add("Groups")
        return categoryList
    }

    private fun setTabs() {

        for (i in 0 until binding.tab.tabCount) {

            val tab_item: TabItemBinding =
                TabItemBinding.inflate(LayoutInflater.from(requireContext()))


            tab_item.itemTv.text = categoryList[i]
            binding.tab.getTabAt(i)?.customView = tab_item.root
            if (i == 0) {
                tab_item.itemTv.setTextColor(Color.WHITE)
                tab_item.itemTv.setBackgroundResource(R.drawable.round)

            } else {
                tab_item.itemTv.setTextColor(Color.parseColor("#848484"))
                tab_item.itemTv.setBackgroundResource(R.drawable.round2)
            }

        }

    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()

        val myRef = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid.toString())

        val date = Date()
        val dateFormat = SimpleDateFormat("HH:mm")
        val curremtDate = dateFormat.format(date)
        param1?.online = false
        param1?.lastSeen = curremtDate
        myRef.setValue(
            param1
        )
    }
}