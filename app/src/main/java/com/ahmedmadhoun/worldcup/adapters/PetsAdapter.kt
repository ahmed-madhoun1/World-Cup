package com.ahmedmadhoun.worldcup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import kotlinx.android.synthetic.main.item_pet.view.*
import javax.inject.Inject


class PetsAdapter @Inject constructor(
    var listener: OnItemClickListener?
) : RecyclerView.Adapter<PetsAdapter.PetsViewHolder>() {

    class PetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsViewHolder =
        PetsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_pet,
                parent,
                false
            )
        )

    interface OnItemClickListener {
        fun onItemClick(selectedItem: Pet)
    }
    private val diffCallback = object : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(oldItem: Pet, newItem: Pet): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Pet, newItem: Pet): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var pets: List<Pet>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onBindViewHolder(holder: PetsViewHolder, position: Int) {
        holder.itemView.apply {
            val item = pets[position]
            this.tvAPI.text = item.API
            this.tvDescription.text = item.Description
            this.tvCategory.text = item.Category
            this.tvLink.text = item.Link
            this.tvLink.setOnClickListener {
                listener?.onItemClick(item)
            }
        }

    }

    override fun getItemCount(): Int =
        pets.size

}