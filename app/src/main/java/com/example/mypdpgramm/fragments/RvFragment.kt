package com.example.mypdpgramm.fragments

import android.R.attr.bitmap
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypdpgramm.R
import com.example.mypdpgramm.adapters.RvAdapter
import com.example.mypdpgramm.databinding.FragmentRvBinding
import com.example.mypdpgramm.models.Message
import com.example.mypdpgramm.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.InputStream
import java.net.URL


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RvFragment : Fragment() {
    lateinit var lastMessages: ArrayList<Message>
    lateinit var binding: FragmentRvBinding
    private var param1: Int? = null
    private var param2: User? = null
    lateinit var userList: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getSerializable(ARG_PARAM2) as User?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lastMessages = ArrayList()
        binding = FragmentRvBinding.inflate(inflater, container, false)
        val database = FirebaseDatabase.getInstance()

        if (param1 == 0) {
            val myRef = database.getReference("Users")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var child = dataSnapshot.children
                    userList = ArrayList()
                    for (child in child) {
                        val value = child.getValue(User::class.java)
                        if (value?.uid != param2?.uid && value?.name != null)
                            userList.add(value!!)
                    }

                    binding.rv.adapter =
                        RvAdapter(
                            requireContext(),
                            userList,
                            param2!!,
                            object : RvAdapter.itemOnCLick {
                                override fun itemClick(people: User, position: Int) {
                                    val bundle = Bundle()
                                    bundle.putSerializable("param1", people)
                                    bundle.putSerializable("param2", param2)
                                    findNavController().navigate(R.id.oneFragment, bundle)
                                }

                            },
                            object : RvAdapter.changed {
                                override fun itemChanged(message: Message, user: User) {
                                    if (param2?.uid != message.to)
                                        notify(message, user)

                                }
                            })
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


        }
        return binding.root
    }

    fun notify(message1: Message, user: User) {
        val ID = "1"
        var bitmap: Bitmap
        val url = URL(user.photoUrl)
        try {


            bitmap = BitmapFactory.decodeStream(url.content as InputStream)
        } catch (e: Exception) {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.google)
        }
        val notification =
            NotificationCompat.Builder(binding.root.context, ID)
                .setSmallIcon(R.mipmap.g12)
                .setLargeIcon(bitmap)
                .setContentTitle("${user.name}")
                .setContentText(message1.text)
                .build()
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ID, "name", importance)
            channel.description = "description"
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(ID.toInt(), notification)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, user: User) =
            RvFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putSerializable(ARG_PARAM2, user)
                }
            }
    }

}