package com.ahmedmadhoun.worldcup.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.item_national_team.view.*
import javax.inject.Inject


class NationalTeamsEliminationAdapter @Inject constructor(
    private val groups: Map<Int, List<NationalTeam>>,
    val listener: OnItemClickListener? = null,
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
        fun onItemClick(selectedItem: NationalTeam, itemView: View, itemName: Boolean)
    }

    override fun onBindViewHolder(holder: NationalTeamsViewHolder, position: Int) {
        holder.itemView.apply {
            val team1: NationalTeam = groups[position]?.get(0)!!
            val team2: NationalTeam = groups[position]?.get(1)!!
            row1.team1.text = team1.name
            row2.team2.text = team2.name
            if (team1.round >= round.round + 1 || team1.round == Rounds.Winner.round) {
                (row1.checkbox1 as CheckBox).isSelected = true
            }
            if (team2.round >= round.round + 1 || team2.round == Rounds.Winner.round) {
                (row2.checkbox2 as CheckBox).isSelected = true
            }
            row1.checkbox1.setOnCheckedChangeListener {buttonView, isChecked ->
                listener?.onItemClick(groups[position]?.get(0)!!, buttonView, isChecked)
            }
            row2.checkbox2.setOnCheckedChangeListener {buttonView, isChecked ->
                listener?.onItemClick(groups[position]?.get(1)!!, buttonView, isChecked)
            }
        }
    }

    override fun getItemCount(): Int =
        groups.size

}