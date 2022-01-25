package com.filipcica.receptar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ReciperRecyclerAdapter(
    private val items:List<Recipe>,
    private val listener:OnItemClickListener,
    private val longListener:OnItemLongClickListener):

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return RecipeViewHolder(
           LayoutInflater.from(parent.context).inflate(R.layout.recipe_recycler_view,parent,false)
       )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RecipeViewHolder->{
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

   inner class RecipeViewHolder constructor( itemView: View):
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener{
        private val recipeImage: ImageView= itemView.findViewById(R.id.recipePhoto)
        private val recipeName: TextView=itemView.findViewById(R.id.recipeName)
        private val recipeDesc: TextView=itemView.findViewById(R.id.recipeDescription)

        fun bind(recipe:Recipe){
            Glide.with(itemView.context)
                .load(recipe.url)
                .into(recipeImage)
            recipeImage.scaleType=ImageView.ScaleType.CENTER_CROP
            recipeName.text=recipe.name
            recipeDesc.text=recipe.description
        }
        init{
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position=adapterPosition
            if(position!=RecyclerView.NO_POSITION){
            listener.onItemClick(items[position])
            }
        }

       override fun onLongClick(v: View?): Boolean {
           val position=adapterPosition
           itemView.setBackgroundColor(Color.GRAY)
           if(position!=RecyclerView.NO_POSITION){
               longListener.onItemLongClick(items[position])
           }
           return true
       }

   }
    interface OnItemClickListener{
        fun onItemClick(recipe:Recipe)
    }
    interface OnItemLongClickListener{
        fun onItemLongClick(recipe: Recipe)
    }
}