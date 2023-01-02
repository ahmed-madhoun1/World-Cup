package com.ahmedmadhoun.worldcup.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.adapters.NationalTeamsAdapter
import com.ahmedmadhoun.worldcup.adapters.NationalTeamsEliminationAdapter
import com.ahmedmadhoun.worldcup.adapters.Rounds
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.databinding.FragmentHomeBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

val selectedList = mutableListOf<NationalTeam>()
val list: MutableList<NationalTeam> = mutableListOf()
val newList: MutableList<NationalTeam> = mutableListOf()
val round8List: MutableList<NationalTeam> = mutableListOf()
val round4List: MutableList<NationalTeam> = mutableListOf()
val round2List: MutableList<NationalTeam> = mutableListOf()
val round16List: MutableList<NationalTeam> = mutableListOf()
val round32List: MutableList<NationalTeam> = mutableListOf()

@SuppressLint("LogNotTimber")
@AndroidEntryPoint
class HomeFragment @Inject constructor() : Fragment(R.layout.fragment_home),
    NationalTeamsAdapter.OnItemClickListener,
    NationalTeamsEliminationAdapter.OnItemClickListener {

    private var page: Int = 0

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: NationalTeamsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)






        requireActivity().actionBar?.title = ""

        viewModel.getData(0)

        observeOnData()

        setupRecyclerView()

        binding.apply {

            submitBtn.setOnClickListener {
                findNavController().popBackStack()
            }

            fabNext.setOnClickListener {
                if (page < 5) {
                    page++
                    when (page) {
                        0 -> {
                            fabNext.visibility = View.VISIBLE
                            submitBtn.visibility = View.GONE
                            tvScreenName.text = "Group Stage"
                            progressBall1.setImageResource(R.mipmap.ball_red)
                            progressBar.progress = 40
                            if (!viewModel.isRecyclerViewActive.value!!) {
                                viewModel.getData(0)
                            }
                        }
                        1 -> {
                            submitBtn.visibility = View.GONE
                            fabNext.visibility = View.VISIBLE
                            tvScreenName.text = "Round Of 16"
                            progressBall2.setImageResource(R.mipmap.ball_red)
                            progressBar.progress = 80
                            if (!viewModel.isRecyclerViewActive.value!!) {
                                viewModel.getData(1)
                            }
                        }
                        2 -> {
                            submitBtn.visibility = View.GONE
                            fabNext.visibility = View.VISIBLE
                            tvScreenName.text = "Quarter-Finals"
                            progressBall3.setImageResource(R.mipmap.ball_red)
                            progressBar.progress = 120
                            if (!viewModel.isRecyclerViewActive.value!!) {
                                viewModel.getData(2)
                            }
                        }
                        3 -> {
                            submitBtn.visibility = View.GONE
                            fabNext.visibility = View.VISIBLE
                            tvScreenName.text = "Semi-Finals"
                            progressBall4.setImageResource(R.mipmap.ball_red)
                            progressBar.progress = 160
                            if (!viewModel.isRecyclerViewActive.value!!) {
                                viewModel.getData(3)
                            }
                        }
                        4 -> {
                            fabNext.visibility = View.VISIBLE
                            tvScreenName.text = "Finals"
                            progressBar.progress = 160
                            if (!viewModel.isRecyclerViewActive.value!!) {
                                viewModel.getData(4)
                            }
                        }
                    }
                    observeOnData()
                }
            }
        }

    }

    override fun onItemClick(selectedItem: NationalTeam, itemView: View, itemName: Boolean) {
        val checkList: List<NationalTeam> = selectedList.filter {
            it.group == selectedItem.group
        }
        selectedList.mapIndexed { index, nationalTeam ->
            if (selectedItem.id == nationalTeam.id) {
                selectedList.removeAt(index)
//                list.add(selectedItem)
                (itemView as CheckBox).isSelected = !itemName
                return
            }
        }
        if (selectedItem.round == Rounds.Round32.round && checkList.size >= 2) {
            Toast.makeText(context, getString(R.string.round_32_message_error), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (selectedItem.round != Rounds.Round32.round && checkList.isNotEmpty()) {
            Toast.makeText(
                context,
                getString(R.string.eliminated_rounds_error_message),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        selectedList.add(selectedItem.copy(round = selectedItem.round + 1))
        (itemView as CheckBox).isSelected = !itemName
//        list.remove(selectedItem)

    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupAdapter(round: Rounds, list: List<NationalTeam>) {
        val adapter = NationalTeamsEliminationAdapter(list.groupBy {
            it.group
        }, this@HomeFragment, round)
        newList.addAll(list)
        selectedList.clear()
        binding.recyclerView.adapter = adapter
    }

    private fun setupAdapterWhenRecyclerDeactivate(list: MutableList<NationalTeam>, round: Rounds) {
        val adapter = NationalTeamsEliminationAdapter(list.groupBy {
            it.group
        }, null, round)
        binding.recyclerView.adapter = adapter
    }

    private fun observeOnData() {
        lifecycleScope.launchWhenStarted {
            viewModel.nationalTeams.asFlow().collectLatest { nationalTeams ->
                if (nationalTeams.isEmpty()) {
                    startSelectingTeams()
                } else {
                    viewModel.setIsRecyclerViewActive(false)
                    displayDataFromDatabase(nationalTeams)
                }
            }
        }
    }


    private fun setupRound16() {
        val dList = list
        if (selectedList.size == 16) {
            val keysOfB = selectedList.map { it.id }
            dList.removeAll {
                it.id in keysOfB
            }
            selectedList.forEach {
                round32List.add(it)
            }
            round32List.addAll(dList)
            round16List.addAll(selectedList)
            selectedList.clear()
        }
    }

    private fun setupRound8() {
        if (selectedList.size == 8) {
            selectedList.forEach {
                when (it.group) {
                    1 -> {
                        it.group = 0
                    }
                    2 -> {
                        it.group = 1
                    }
                    3 -> {
                        it.group = 1
                    }
                    4 -> {
                        it.group = 2
                    }
                    5 -> {
                        it.group = 2
                    }
                    6 -> {
                        it.group = 3
                    }
                    7 -> {
                        it.group = 3
                    }
                }
            }
            val keysOfB = selectedList.map { it.id }
            round16List.removeAll {
                it.id in keysOfB
            }
            selectedList.forEach {
                round16List.add(it)
                if(it.name == "ARGENTINA"){
                    FirebaseInAppMessaging.getInstance().triggerEvent("new_event")
                }


            }
            round8List.addAll(selectedList)
            selectedList.clear()
        }
    }

    private fun setupRound4() {
        if (selectedList.size == 4) {
            selectedList.forEach {
                when (it.group) {
                    1 -> {
                        it.group = 0
                    }
                    2 -> {
                        it.group = 1
                    }
                    3 -> {
                        it.group = 1
                    }
                }
            }


            val keysOfB = selectedList.map { it.id }
            round8List.removeAll {
                it.id in keysOfB
            }
            selectedList.forEach {
                round8List.add(it)
            }
            round4List.addAll(selectedList)
            selectedList.clear()
        }
    }

    private fun setupRound2() {
        if (selectedList.size == 2) {
            selectedList.forEach {
                when (it.group) {
                    1 -> {
                        it.group = 0
                    }
                }
            }
            val keysOfB = selectedList.map { it.id }
            round4List.removeAll {
                it.id in keysOfB
            }
            selectedList.forEach {
                round4List.add(it)
            }
            round2List.addAll(selectedList)
            selectedList.clear()
        }
    }

    private fun setupWinner() {
        if (selectedList.size == 1) {
            val keysOfB = selectedList.map { it.id }
            round2List.removeAll {
                it.id in keysOfB
            }
            selectedList.forEach {
                round2List.add(it.copy(round = Rounds.Winner.round))
            }
            setupFinal()
        }
    }

    private fun setupFinal() {
        binding.recyclerView.visibility = View.GONE
        binding.cardView.visibility = View.GONE
        binding.fabNext.visibility = View.VISIBLE
        binding.submitBtn.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.progressBall1.visibility = View.GONE
        binding.progressBall2.visibility = View.GONE
        binding.progressBall3.visibility = View.GONE
        binding.progressBall4.visibility = View.GONE
        binding.progressESim5.visibility = View.GONE
        binding.tvWinner.visibility = View.VISIBLE
        binding.tvWinner.text = selectedList.first().name
        // insert into database
        round32List.forEach {
            viewModel.insertNationalTeam(it.copy(listType = 0))
        }
        round16List.forEach {
            viewModel.insertNationalTeam(it.copy(listType = 1))
        }
        round8List.forEach {
            viewModel.insertNationalTeam(it.copy(listType = 2))
        }
        round4List.forEach {
            viewModel.insertNationalTeam(it.copy(listType = 3))
        }
        round2List.forEach {
            viewModel.insertNationalTeam(it.copy(listType = 4))
        }
        Toast.makeText(requireActivity(), "Items Added", Toast.LENGTH_SHORT).show()
    }

    private fun startSelectingTeams() {
        if (page == 0) {
            list.addAll(
                mutableListOf(
                    NationalTeam(id = 1, name = "QATAR", round = 0, group = 0, listType = 0),
                    NationalTeam(id = 2, name = "ECUADOR", round = 0, group = 0, listType = 0),
                    NationalTeam(id = 13, name = "SENEGAL", round = 0, group = 0, listType = 0),
                    NationalTeam(
                        id = 11,
                        name = "NETHERLANDS",
                        round = 0,
                        group = 0,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 132,
                        name = "ENGLAND",
                        round = 0,
                        group = 1,
                        listType = 0
                    ),
                    NationalTeam(id = 14, name = "IRAN", round = 0, group = 1, listType = 0),
                    NationalTeam(id = 15, name = "USA", round = 0, group = 1, listType = 0),
                    NationalTeam(id = 16, name = "WALES", round = 0, group = 1, listType = 0),
                    NationalTeam(
                        id = 17,
                        name = "ARGENTINA",
                        round = 0,
                        group = 2,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 18,
                        name = "SAUDI ARABIA",
                        round = 0,
                        group = 2,
                        listType = 0
                    ),
                    NationalTeam(id = 81, name = "MEXICO", round = 0, group = 2, listType = 0),
                    NationalTeam(id = 19, name = "POLAND", round = 0, group = 2, listType = 0),
                    NationalTeam(id = 61, name = "FRANCE", round = 0, group = 3, listType = 0),
                    NationalTeam(
                        id = 121,
                        name = "AUSTRALIA",
                        round = 0,
                        group = 3,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 1123,
                        name = "DENMARK",
                        round = 0,
                        group = 3,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 1435,
                        name = "TUNISIA",
                        round = 0,
                        group = 3,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 121412,
                        name = "SPAIN",
                        round = 0,
                        group = 4,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 1214124,
                        name = "COSTA RICA",
                        round = 0,
                        group = 4,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 3461,
                        name = "GERMANY",
                        round = 0,
                        group = 4,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 143568,
                        name = "JAPAN",
                        round = 0,
                        group = 4,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 5367351,
                        name = "BELGIUM",
                        round = 0,
                        group = 5,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 132453,
                        name = "CANADA",
                        round = 0,
                        group = 5,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 23581,
                        name = "MOROCCO",
                        round = 0,
                        group = 5,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 180869,
                        name = "CROATIA",
                        round = 0,
                        group = 5,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 1608,
                        name = "BRAZIL",
                        round = 0,
                        group = 6,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 1570,
                        name = "SERBIA",
                        round = 0,
                        group = 6,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 12004,
                        name = "SWITZERLAND",
                        round = 0,
                        group = 6,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 17563,
                        name = "CAMEROON",
                        round = 0,
                        group = 6,
                        listType = 0
                    ),
                    NationalTeam(id = 304, name = "GHANA", round = 0, group = 7, listType = 0),
                    NationalTeam(
                        id = 109871,
                        name = "PORTUGAL",
                        round = 0,
                        group = 7,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 12134076,
                        name = "SOUTH KOREA",
                        round = 0,
                        group = 7,
                        listType = 0
                    ),
                    NationalTeam(
                        id = 983214,
                        name = "URUGUAY",
                        round = 0,
                        group = 7,
                        listType = 0
                    ),
                )
            )
            val adapter = NationalTeamsAdapter(list.groupBy {
                it.group
            }, this@HomeFragment, Rounds.Round32)
            binding.recyclerView.adapter = adapter
            selectedList.clear()
        } else if (selectedList.size == 16 && page == 1) {
            setupRound16()
            setupAdapter(Rounds.Round16, round16List)
        } else if (selectedList.size == 8 && page == 2) {
            setupRound8()
            setupAdapter(Rounds.Round8, round8List)
        } else if (selectedList.size == 4 && page == 3) {
            setupRound4()
            setupAdapter(Rounds.Round4, round4List)
        } else if (selectedList.size == 2 && page == 4) {
            setupRound2()
            setupAdapter(Rounds.Round2, round2List)
        } else if (selectedList.size == 1) {
            setupWinner()
        } else if (page == 5) {

        } else {
            Toast.makeText(
                requireActivity(),
                "Please select all required teams",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun displayDataFromDatabase(nationalTeams: List<NationalTeam>) {
        when (nationalTeams[0].listType) {
            0 -> {
                list.addAll(nationalTeams)
                round32List.addAll(nationalTeams)
            }
            1 -> {
                round16List.addAll(nationalTeams)
            }
            2 -> {
                round8List.addAll(nationalTeams)
            }
            3 -> {
                round4List.addAll(nationalTeams)
            }
            4 -> {
                round2List.addAll(nationalTeams)
            }
        }
        if (page == 0) {
            val adapter = NationalTeamsAdapter(list.groupBy {
                it.group
            }, this@HomeFragment, Rounds.Round32)
            binding.recyclerView.adapter = adapter
        } else if (round16List.isNotEmpty() && page == 1) {
            sortByGroup(round16List, Rounds.Round16)
            setupAdapterWhenRecyclerDeactivate(round16List, Rounds.Round16)
        } else if (round8List.isNotEmpty() && round16List.isNotEmpty() && page == 2) {
            sortByGroup(round8List, Rounds.Round8)
            setupAdapterWhenRecyclerDeactivate(round8List, Rounds.Round8)
        } else if (round4List.isNotEmpty() && round8List.isNotEmpty() && page == 3) {
            sortByGroup(round4List, Rounds.Round4)
            setupAdapterWhenRecyclerDeactivate(round4List, Rounds.Round4)
        } else if (round2List.isNotEmpty() && round4List.isNotEmpty() && page == 4) {
            val group = 0
            round2List.forEachIndexed { index, nationalTeam ->
                round2List[index] = nationalTeam.copy(group = group)
            }
            setupAdapterWhenRecyclerDeactivate(round2List, Rounds.Round2)
        }

    }

    private fun sortByGroup(
        list: MutableList<NationalTeam>,
        round: Rounds
    ): MutableList<NationalTeam> {
        var group = 0
        val round8Items = list.filter { it.round == round.round }
        val round4Items = list.filter { it.round == round.round + 1 }
        list.clear()
        round8Items.zip(round4Items).forEach { pair ->
            pair.first.group = group
            pair.second.group = group
            list.add(pair.first)
            list.add(pair.second)
            group += 1
        }
        return list
    }

}