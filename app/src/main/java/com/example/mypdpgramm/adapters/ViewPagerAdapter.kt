package com.example.mypdpgramm.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mypdpgramm.fragments.GroupsFragment
import com.example.mypdpgramm.fragments.RvFragment
import com.example.mypdpgramm.models.User

class ViewPagerAdapter(var currentUser: User, var list: ArrayList<String>, fm: FragmentActivity) :
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return RvFragment.newInstance(position, currentUser)
        else {
            return GroupsFragment.newInstance(position, currentUser)
        }
    }

}