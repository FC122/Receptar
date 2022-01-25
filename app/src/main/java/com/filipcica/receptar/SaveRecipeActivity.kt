package com.filipcica.receptar

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.filipcica.receptar.databinding.ActivityMainBinding
import com.filipcica.receptar.databinding.ActivitySaveRecipeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class SaveRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveRecipeBinding
    lateinit var storage:FirebaseStorage
    lateinit var ImageUri:Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySaveRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage=Firebase.storage
        val db=Firebase.firestore

        binding.addImage.setOnClickListener {
            selectImage()
        }

        binding.goBackBtn.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


        binding.saveRecipeBtn.setOnClickListener {
            val name=binding.editTextName.text.toString()
            val description=binding.editTextDescription .text.toString()
            val ingridients=binding.editTextIngridients.text.toString()
            val steps=binding.editTextSteps.text.toString()

            val recipe= hashMapOf(
                "name" to name,
                "description" to description,
                "ingridients" to ingridients,
                "steps" to steps,
                "url" to "idk",
                "id" to "id")

            val id=db.collection("recipes").document().id
            db.collection("recipes")
                .document(id)
                .set(recipe)

            db.collection("recipes").document(id).update("id",id)
               uploadImage(name,id)
        }

    }
    private fun uploadImage(name: String,id:String) {
        storage=Firebase.storage
        val db=Firebase.firestore

        val uploadTask:UploadTask
        var storageRef=storage.reference
        var imagesRef=storageRef.child("image/${name}")
        uploadTask = imagesRef.putFile(ImageUri)
        val urlTask=uploadTask.continueWithTask{task->
            if(!task.isSuccessful){
                task.exception?.let { throw  it }
            }
            imagesRef.downloadUrl
        }.addOnCompleteListener{task->
            if(task.isSuccessful){
                val url=task.result
                db.collection("recipes").document(id).update("url",url.toString())
            }
        }
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    private fun selectImage() {
        val intent= Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode== RESULT_OK){
            ImageUri=data?.data!!
            binding.addImage.setImageURI(ImageUri)

            binding.addImage.scaleType=ImageView.ScaleType.CENTER_CROP
        }
    }
}