package com.filipcica.receptar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.filipcica.receptar.databinding.ActivityMainBinding
import com.filipcica.receptar.databinding.ActivitySaveRecipeBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity(),ReciperRecyclerAdapter.OnItemClickListener,ReciperRecyclerAdapter.OnItemLongClickListener {
    private lateinit var db:FirebaseFirestore
    private lateinit var recipeRecyclerView:RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeRecyclerView=binding.recyclerView;
        recipeRecyclerView.layoutManager= LinearLayoutManager(this)
        recipeRecyclerView.setHasFixedSize(true)

        binding.addRecipeBtn.setOnClickListener {
            val intent = Intent(this, SaveRecipeActivity::class.java)
            startActivity(intent)
        }
            recipeArrayList= arrayListOf<Recipe>()
            getUserData()
    }

    private fun getUserData() {
        db=Firebase.firestore
        db.collection("recipes")
            .addSnapshotListener{ snapshot,e->
                for(recipeSnapshot in snapshot!!.documentChanges){
                    when(recipeSnapshot.type){
                        DocumentChange.Type.ADDED-> {
                            val recipe=recipeSnapshot.document.toObject(Recipe::class.java)
                            recipeArrayList.add(recipe!!)
                        }
                        DocumentChange.Type.REMOVED->{
                            val recipe=recipeSnapshot.document.toObject(Recipe::class.java)
                            recipeArrayList.add(recipe!!)
                        }
                    }
                }
                recipeRecyclerView.adapter=ReciperRecyclerAdapter(recipeArrayList,this,this)
                binding.recipeCount.text=recipeArrayList.count().toString()+" recipes"
            }
    }

    override fun onItemClick(recipe: Recipe) {
        val intent=Intent(this,ViewRecipeActivity::class.java)
        val bundle=Bundle()
        bundle.putString("name",recipe.name)
        bundle.putString("desc",recipe.description)
        bundle.putString("ing",recipe.ingredients)
        bundle.putString("steps",recipe.steps)
        bundle.putString("url",recipe.url)
        intent.putExtra("recipe",bundle)
        startActivity(intent)
    }

    override fun onItemLongClick(recipe: Recipe) {
        binding.removeRecipeBtn.setOnClickListener {
            val id = recipe.id.toString()
            db.collection("recipes").document(id).delete()
            val intent = Intent(this, MainActivity::class.java)
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }
}