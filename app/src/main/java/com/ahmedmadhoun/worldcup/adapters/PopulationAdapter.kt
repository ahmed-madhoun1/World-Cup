package com.ahmedmadhoun.worldcup.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import kotlinx.android.synthetic.main.item_population.view.*
import javax.inject.Inject


class PopulationAdapter @Inject constructor(
    var listener: OnItemClickListener?
) : RecyclerView.Adapter<PopulationAdapter.PopulationViewHolder>() {

    class PopulationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopulationViewHolder =
        PopulationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_population,
                parent,
                false
            )
        )

    interface OnItemClickListener {
        fun onItemClick(selectedItem: PopulationOb)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<PopulationOb>() {
        override fun areItemsTheSame(oldItem: PopulationOb, newItem: PopulationOb): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PopulationOb, newItem: PopulationOb): Boolean =
            oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var population: List<PopulationOb>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onBindViewHolder(holder: PopulationViewHolder, position: Int) {
        holder.itemView.apply {
            val item = population[position]
            this.tvNation.text = item.Nation
            this.tvYear.text = item.Year
            this.tvPopulation.text = item.Population.toString()
        }

    }

    override fun getItemCount(): Int =
        population.size

}