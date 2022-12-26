package com.ahmedmadhoun.worldcup.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.item_national_team.view.*
import javax.inject.Inject


class NationalTeamsEliminationAdapter @Inject constructor(
    private val groups: Map<Int, List<NationalTeam>>,
    val listener: OnItemClickListener,
    val round: Rounds
) : RecyclerView.Adapter<NationalTeamsEliminationAdapter.NationalTeamsViewHolder>() {

    class NationalTeamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NationalTeamsViewHolder =
        NationalTeamsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_national_team_elimination,
                parent,
                false
            )
        )

    interface OnItemClickListener {
        fun onItemClick(selectedItem: NationalTeam, itemView: View, itemName: View)
    }

    override fun onBindViewHolder(holder: NationalTeamsViewHolder, position: Int) {
        holder.itemView.apply {
            val team1: NationalTeam = groups[position]?.get(0)!!
            val team2: NationalTeam = groups[position]?.get(1)!!
            row1.team1.text = team1.name
            row2.team2.text = team2.name
            if (team1.round >= round.round + 1 || team1.round == Rounds.Winner.round) {
                (row1.team1 as MaterialTextView).setTextColor(Color.WHITE)
                row1.setBackgroundResource(R.drawable.team_shape_selected)
            }
            if (team2.round >= round.round + 1 || team2.round == Rounds.Winner.round) {
                (row2.team2 as MaterialTextView).setTextColor(Color.WHITE)
                row2.setBackgroundResource(R.drawable.team_shape_selected)
            }
            row1.setOnClickListener {
                listener.onItemClick(groups[position]?.get(0)!!, row1, row1.team1)
            }
            row2.setOnClickListener {
                listener.onItemClick(groups[position]?.get(1)!!, row2, row2.team2)
            }
        }
    }

    override fun getItemCount(): Int =
        groups.size

}