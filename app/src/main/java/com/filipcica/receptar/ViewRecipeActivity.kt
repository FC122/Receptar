package com.filipcica.receptar

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.filipcica.receptar.databinding.ActivitySaveRecipeBinding
import com.filipcica.receptar.databinding.ActivityViewRecipeBinding

class ViewRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recipeViewGoBackBtn.setOnClickListener{
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        val imageView=binding.showImage
        val name=binding.textViewName
        val desc=binding.textViewDescription
        val ing=binding.textViewIngridients
        val steps=binding.textViewSteps

        val bundle=intent.getBundleExtra("recipe")
        name.text=bundle!!.getString("name","Default")
        desc.text="Description: \n"+bundle!!.getString("desc","Default")
        ing.text="Ingridients: \n"+bundle!!.getString("ing","Default")
        steps.text="Steps: \n"+bundle!!.getString("steps","Default")

        val url=bundle!!.getString("url","Default")
        Glide.with(this).load(url).into(imageView)
        imageView.scaleType= ImageView.ScaleType.CENTER_CROP
    }
}