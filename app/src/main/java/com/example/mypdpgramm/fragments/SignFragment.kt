package com.example.mypdpgramm.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypdpgramm.R
import com.example.mypdpgramm.databinding.FragmentSignBinding
import com.example.mypdpgramm.models.Message
import com.example.mypdpgramm.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignFragment : Fragment() {
    lateinit var binding: FragmentSignBinding
    private lateinit var auth: FirebaseAuth
    private var param1: String? = null
    lateinit var database: FirebaseDatabase
    lateinit var currentUser: User
    private var param2: String? = null
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = FirebaseAuth.getInstance()
        binding = FragmentSignBinding.inflate(inflater, container, false)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        database = FirebaseDatabase.getInstance()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.signByGoogle.setOnClickListener {

            signIn()
        }

        return binding.root

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!


                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("Users").child(auth.uid.toString())
                    val account1 = auth.currentUser
                    val date = Date()
                    val dateFormat = SimpleDateFormat("HH:mm")
                    val curremtDate = dateFormat.format(date)
                    currentUser = User(
                        account1?.displayName,
                        account1?.photoUrl.toString(),
                        account1?.email,
                        account1?.uid,
                        curremtDate,
                        online = true,
                        signed = true
                    )
                    myRef.setValue(
                        currentUser
                    )
                    var bundle = Bundle()
                    bundle.putSerializable("param1", currentUser)
                    findNavController().navigate(R.id.homeFragment2, bundle)
                    binding.root
                } else {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()

                    binding.root
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}