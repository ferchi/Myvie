package com.jfsb.myvie.main.profile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.FragmentHomeBinding
import com.jfsb.myvie.databinding.FragmentProfileBinding
import com.jfsb.myvie.first.LoginActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.combine
import java.io.ByteArrayOutputStream
import com.jfsb.myvie.main.collection.Collection
import com.jfsb.myvie.main.collection.CollectionAdapter

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    val current = Firebase.auth.currentUser!!.email!!
    var username = "Username"

    // Variables para obtener y cambiar de las imagenes de perfil
    val TAKE_IMG_CODE = 1046
    lateinit var vista: View
    lateinit var storageChild: String
    lateinit var databaseChild: String

    lateinit var rev: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarProfile)

        loadData()

        var isShow = true
        var scrollRange = -1
        binding.appBarProfile.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                binding.collapsingToolbarProfile.title = username
                isShow = true
            } else if (isShow){
                binding.collapsingToolbarProfile.title = " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })

        binding.rivImageProfile.setOnLongClickListener {
            changeImg()
        }

        binding.tvProfileDescription.setOnLongClickListener {
            DialogDescription().show(parentFragmentManager,"Descripcion")
            false
        }

        binding.cardProfileLogout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun loadData(){

        rev = binding.rvProfileCollections

        loadImg()

        db.collection("Users").document(current).get().addOnCompleteListener { result ->
            val name = result.result!!.get("name").toString()
            val collectionCount = result.result!!.get("collections").toString() + " Colecciones"
            val followersCount = result.result!!.get("followers").toString() + " Seguidores"
            val description = result.result!!.get("description").toString()

            username = name
            binding.tvProfileUsername.text = name
            binding.tvProfileCollections.text = collectionCount
            binding.tvProfileFollowers.text = followersCount
            binding.tvProfileDescription.text = description
        }

        db.collection("Collections").whereEqualTo("author", current).orderBy("name").get().addOnCompleteListener { collections ->
             val list = collections.result!!.toObjects(Collection::class.java)

            rev.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context,3)
                adapter = CollectionAdapter(list,requireActivity())
            }
        }

    }

    override fun onStart() {
        super.onStart()

        loadData()
    }

    private fun changeImg(): Boolean {

        vista = binding.rivImageProfile
        databaseChild = "imgProfile"
        storageChild = "profileImages"

        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, TAKE_IMG_CODE)
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMG_CODE) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data?.data)

                    binding.rivImageProfile.setImageBitmap(bitmap)

                    handleUpload(bitmap)
                }
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uid: String = current
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child(storageChild)
            .child("$uid.jpeg")

        ref.putBytes(baos.toByteArray())
            .addOnSuccessListener {
                getDownloadUrl(ref)
            }
            .addOnFailureListener() {
                Log.e("Errorimg", "onFailure", it.cause)
            }
    }

    private fun getDownloadUrl(ref: StorageReference) {
        ref.downloadUrl.addOnSuccessListener {
            setUserProfileUrl(it)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val request: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setPhotoUri(uri)
            .build()

        user.updateProfile(request)
            .addOnSuccessListener {
                db.collection("Users").document(current).update(databaseChild,uri.toString())
                loadImg()
                Toast.makeText(requireContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Actualización fallida", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadImg() {

        db.collection("Users").document(current).addSnapshotListener{
                result, error ->
            val urlImg = result!!.get("imgProfile").toString()

            try {
                Glide.with(requireActivity())
                    .load(urlImg)
                    .transform(CenterCrop())
                    .into(binding.rivImageProfile)
                //Picasso.get().load(urlImg).into(binding.rivImageProfile)

            } catch (e: Exception) {

            }
        }
    }

}