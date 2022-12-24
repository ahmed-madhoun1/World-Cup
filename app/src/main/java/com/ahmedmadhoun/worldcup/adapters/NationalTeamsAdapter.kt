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
import kotlin.properties.Delegates

enum class Rounds(val round: Int) {
    Round32(0),
    Round16(1),
    Round8(2),
    Round4(3),
    Round2(4),
    Winner(5),
}

class NationalTeamsAdapter @Inject constructor(
    private val groups: Map<Int, List<NationalTeam>>,
    var listener: OnItemClickListener?,
    val round: Rounds
) : RecyclerView.Adapter<NationalTeamsAdapter.NationalTeamsViewHolder>() {

    class NationalTeamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NationalTeamsViewHolder =
        NationalTeamsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_national_team,
                parent,
                false
            )
        )

    interface OnItemClickListener {
        fun onItemClick(selectedItem: NationalTeam, itemView: View, itemName: View)
    }

    var checkRound by Delegates.notNull<Boolean>()

    override fun onBindViewHolder(holder: NationalTeamsViewHolder, position: Int) {

        holder.itemView.apply {
            rowHeader.tvTitleGroup.text = when (groups[position]?.get(0)?.group) {
                0 -> {
                    "Group A"
                }
                1 -> {
                    "Group B"
                }
                2 -> {
                    "Group C"
                }
                3 -> {
                    "Group D"
                }
                4 -> {
                    "Group E"
                }
                5 -> {
                    "Group F"
                }
                6 -> {
                    "Group G"
                }
                7 -> {
                    "Group H"
                }
                else -> {
                    ""
                }
            }
            val team1: NationalTeam = groups[position]?.get(0)!!
            val team2: NationalTeam = groups[position]?.get(1)!!
            val team3: NationalTeam = groups[position]?.get(2)!!
            val team4: NationalTeam = groups[position]?.get(3)!!

            row1.team1.text = team1.name
            row2.team2.text = team2.name
            row3.team3.text = team3.name
            row4.team4.text = team4.name



            if (team1.round >= round.round + 1 || team1.round == Rounds.Winner.round) {
                (row1.team1 as MaterialTextView).setTextColor(Color.WHITE)
                row1.setBackgroundResource(R.color.grayDark)
            }
            if (team2.round >= round.round + 1 || team2.round == Rounds.Winner.round) {
                (row2.team2 as MaterialTextView).setTextColor(Color.WHITE)
                row2.setBackgroundResource(R.color.grayDark)
            }
            if (team3.round >= round.round + 1 || team3.round == Rounds.Winner.round) {
                (row3.team3 as MaterialTextView).setTextColor(Color.WHITE)
                row3.setBackgroundResource(R.color.grayDark)
            }
            if (team4.round >= round.round + 1 || team4.round == Rounds.Winner.round) {
                (row4.team4 as MaterialTextView).setTextColor(Color.WHITE)
                row4.setBackgroundResource(R.color.grayDark)
            }

            if (listener != null) {
                row1.setOnClickListener {
                    listener?.onItemClick(groups[position]?.get(0)!!, row1, row1.team1)
                }
                row2.setOnClickListener {
                    listener?.onItemClick(groups[position]?.get(1)!!, row2, row2.team2)
                }
                row3.setOnClickListener {
                    listener?.onItemClick(groups[position]?.get(2)!!, row3, row3.team3)
                }
                row4.setOnClickListener {
                    listener?.onItemClick(groups[position]?.get(3)!!, row4, row4.team4)
                }
            }
        }
    }



    override fun getItemCount(): Int =
        groups.size

    /*

    fun getRoundCheck(team: NationalTeam): Boolean {
        return when (round) {
            Rounds.Round32 -> {
                    (team.round == Rounds.Round16.round ||
                            team.round == Rounds.Round8.round ||
                            team.round == Rounds.Round4.round ||
                            team.round == Rounds.Round2.round)
            }
            Rounds.Round16 -> {
                    (team.round == Rounds.Round16.round ||
                            team.round == Rounds.Round8.round ||
                            team.round == Rounds.Round4.round ||
                            team.round == Rounds.Round2.round)
            }
            Rounds.Round8 -> {
                    (team.round == Rounds.Round4.round ||
                            team.round == Rounds.Round2.round)
            }
            Rounds.Round4 -> {
                    (team.round == Rounds.Round2.round)
            }
            Rounds.Round2 -> {
                (team.round == Rounds.Winner.round)
            }
            Rounds.Winner ->{
                true
            }
        }
    }
     */

}