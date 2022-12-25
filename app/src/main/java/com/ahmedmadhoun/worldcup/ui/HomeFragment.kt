package com.ahmedmadhoun.worldcup.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.adapters.NationalTeamsAdapter
import com.ahmedmadhoun.worldcup.adapters.NationalTeamsEliminationAdapter
import com.ahmedmadhoun.worldcup.adapters.Rounds
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.databinding.FragmentHomeBinding
import com.google.android.datatransport.cct.internal.LogEvent
import com.google.android.material.textview.MaterialTextView
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

        viewModel.getData(0)

        observeOnData()

        setupRecyclerView()

        binding.apply {
            fabNext.setOnClickListener {
                if (page < 4) {
                    page++
                    when (page) {
                        0 -> {
                            page = 0
                            if (!viewModel.isRecyclerViewActive.value!! && round32List.isEmpty()) {
                                viewModel.getData(0)
                            }
                        }
                        1 -> {
                            page = 1
                            fabPrev.visibility = View.VISIBLE
                            if (!viewModel.isRecyclerViewActive.value!!&& round16List.isEmpty()) {
                                viewModel.getData(1)
                            }
                        }
                        2 -> {
                            page = 2
                            fabPrev.visibility = View.VISIBLE
                            if (!viewModel.isRecyclerViewActive.value!!&& round8List.isEmpty()) {
                                viewModel.getData(2)
                            }
                        }
                        3 -> {
                            page = 3
                            fabPrev.visibility = View.VISIBLE
                            if (!viewModel.isRecyclerViewActive.value!!&& round4List.isEmpty()) {
                                viewModel.getData(3)
                            }
                        }
                        4 -> {
                            page = 4
                            fabPrev.visibility = View.VISIBLE
                            if (!viewModel.isRecyclerViewActive.value!!&& round2List.isEmpty()) {
                                viewModel.getData(4)
                            }
                        }
                    }
                    observeOnData()
                }
            }
            fabPrev.setOnClickListener {
                if (page > 0) {
                    page--
                    observeOnData()
                    when (page) {
                        0 -> {
                            page = 0
                            fabPrev.visibility = View.GONE
                        }
                        1 -> {
                            page = 1
                            fabPrev.visibility = View.VISIBLE
                        }
                        2 -> {
                            page = 2
                            fabPrev.visibility = View.VISIBLE
                        }
                        3 -> {
                            page = 3
                            fabPrev.visibility = View.VISIBLE
                        }
                        4 -> {
                            page = 4
                            fabPrev.visibility = View.VISIBLE
                        }
                    }
                }
            }
            fabDeleteAll.setOnClickListener {
                viewModel.deleteAllNationalTeams()
            }
        }

    }

    override fun onItemClick(selectedItem: NationalTeam, itemView: View, itemName: View) {
        val checkList: List<NationalTeam> = selectedList.filter {
            it.group == selectedItem.group
        }
        selectedList.mapIndexed { index, nationalTeam ->
            if (selectedItem.id == nationalTeam.id) {
                selectedList.removeAt(index)
//                list.add(selectedItem)
                (itemName as MaterialTextView).setTextColor(Color.parseColor("#3D3D3D"));
                itemView.setBackgroundResource(R.color.gray)
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
//        list.remove(selectedItem)
        selectedList.add(selectedItem.copy(round = selectedItem.round + 1))
        (itemName as MaterialTextView).setTextColor(Color.WHITE)
        itemView.setBackgroundResource(R.color.grayDark)
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
        }, this@HomeFragment, round)
        binding.recyclerView.adapter = adapter
    }

    private fun observeOnData() {
        lifecycleScope.launchWhenStarted {
            viewModel.nationalTeams.asFlow().collectLatest { nationalTeams ->
                if (nationalTeams.isNullOrEmpty()) {
                    fabClicked(nationalTeams)
                    Log.e("nationalTeams", "Read from static ")
                } else {
                    viewModel.setIsRecyclerViewActive(false)
                    Log.e("nationalTeams", "Read from database ")
                    // deactivate recycler view
                    fabClicked(nationalTeams)
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
            Log.e("TAG", "list: ${dList}")
            Log.e("TAG", "list: ${dList.size}")
            Log.e("TAG", "round32List: ${round32List}")
            Log.e("TAG", "round32List: ${round32List.size}")
            Log.e("TAG", "selectedList: ${selectedList.size}")
            Log.e("TAG", "selectedList: ${selectedList}")
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
            }
            round8List.addAll(selectedList)
            Log.e("TAG", "round16List: ${round16List}")
            Log.e("TAG", "round16List: ${round16List.size}")
            Log.e("TAG", "selectedList: ${selectedList.size}")
            Log.e("TAG", "selectedList: ${selectedList}")
            Log.e("TAG", "round8List: ${round8List.size}")
            Log.e("TAG", "round8List: ${round8List}")
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
            Log.e("TAG", "round8List: ${round8List}")
            Log.e("TAG", "round8List: ${round8List.size}")
            Log.e("TAG", "selectedList: ${selectedList.size}")
            Log.e("TAG", "selectedList: ${selectedList}")
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
            Log.e("TAG", "round4List: ${round4List}")
            Log.e("TAG", "round4List: ${round4List.size}")
            Log.e("TAG", "selectedList: ${selectedList.size}")
            Log.e("TAG", "selectedList: ${selectedList}")
            Log.e("TAG", "round2List: ${round2List.size}")
            Log.e("TAG", "round2List: ${round2List}")
            selectedList.clear()
        }
    }

    private fun setupRound16Deactivate() {
        var group = 0
        val round8Items = round16List.filter { it.round == Rounds.Round16.round }
        val round4Items = round16List.filter { it.round == Rounds.Round8.round }
        round16List.clear()
        round8Items.zip(round4Items).forEach { pair ->
            pair.first.group = group
            pair.second.group = group
            round16List.add(pair.first)
            round16List.add(pair.second)
            group += 1
        }
        Log.e("asdfsassdf", "round32List: ${round32List}")
        Log.e("asdfsassdf", "round32List: ${round32List.size}")
        Log.e("asdfsassdf", "round16List: ${round16List}")
        Log.e("asdfsassdf", "round16List: ${round16List.size}")
        Log.e("asdfsassdf", "round8List: ${round8List}")
        Log.e("asdfsassdf", "round8List: ${round8List.size}")
        Log.e("asdfsassdf", "round4List: ${round4List}")
        Log.e("asdfsassdf", "round4List: ${round4List.size}")
        Log.e("asdfsassdf", "round2List: ${round2List}")
        Log.e("asdfsassdf", "round2List: ${round2List.size}")
    }

    private fun setupRound8Deactivate() {
        Log.e("asdfsassdf", "round32List: ${round32List}")
        Log.e("asdfsassdf", "round32List: ${round32List.size}")
        Log.e("asdfsassdf", "round16List: ${round16List}")
        Log.e("asdfsassdf", "round16List: ${round16List.size}")
        Log.e("asdfsassdf", "round8List: ${round8List}")
        Log.e("asdfsassdf", "round8List: ${round8List.size}")
        Log.e("asdfsassdf", "round4List: ${round4List}")
        Log.e("asdfsassdf", "round4List: ${round4List.size}")
        Log.e("asdfsassdf", "round2List: ${round2List}")
        Log.e("asdfsassdf", "*************************************************")
        var group = 0
        val round8Items = round8List.filter { it.round == Rounds.Round8.round }
        val round4Items = round8List.filter { it.round == Rounds.Round4.round }
        round8List.clear()
        round8Items.zip(round4Items).forEach { pair ->
            pair.first.group = group
            pair.second.group = group
            round8List.add(pair.first)
            round8List.add(pair.second)
            group += 1
        }
        Log.e("asdfsassdf", "round32List: ${round32List}")
        Log.e("asdfsassdf", "round32List: ${round32List.size}")
        Log.e("asdfsassdf", "round16List: ${round16List}")
        Log.e("asdfsassdf", "round16List: ${round16List.size}")
        Log.e("asdfsassdf", "round8List: ${round8List}")
        Log.e("asdfsassdf", "round8List: ${round8List.size}")
        Log.e("asdfsassdf", "round4List: ${round4List}")
        Log.e("asdfsassdf", "round4List: ${round4List.size}")
        Log.e("asdfsassdf", "round2List: ${round2List}")
        Log.e("asdfsassdf", "round2List: ${round2List.size}")
    }

    private fun setupRound4Deactivate() {
        var group = 0
        val round8Items = round4List.filter { it.round == Rounds.Round4.round }
        val round4Items = round4List.filter { it.round == Rounds.Round2.round }
        round4List.clear()
        round8Items.zip(round4Items).forEach { pair ->
            pair.first.group = group
            pair.second.group = group
            round4List.add(pair.first)
            round4List.add(pair.second)
            group += 1
        }
        Log.e("asdfsassdf", "round32List: ${round32List}")
        Log.e("asdfsassdf", "round32List: ${round32List.size}")
        Log.e("asdfsassdf", "round16List: ${round16List}")
        Log.e("asdfsassdf", "round16List: ${round16List.size}")
        Log.e("asdfsassdf", "round8List: ${round8List}")
        Log.e("asdfsassdf", "round8List: ${round8List.size}")
        Log.e("asdfsassdf", "round4List: ${round4List}")
        Log.e("asdfsassdf", "round4List: ${round4List.size}")
        Log.e("asdfsassdf", "round2List: ${round2List}")
        Log.e("asdfsassdf", "round2List: ${round2List.size}")
    }

    private fun setupRound2Deactivate() {
        val group = 0
        round2List.forEachIndexed { index, nationalTeam ->
            round2List[index] = nationalTeam.copy(group = group)
        }
        Log.e("asdfsassdf", "round32List: ${round32List}")
        Log.e("asdfsassdf", "round32List: ${round32List.size}")
        Log.e("asdfsassdf", "round16List: ${round16List}")
        Log.e("asdfsassdf", "round16List: ${round16List.size}")
        Log.e("asdfsassdf", "round8List: ${round8List}")
        Log.e("asdfsassdf", "round8List: ${round8List.size}")
        Log.e("asdfsassdf", "round4List: ${round4List}")
        Log.e("asdfsassdf", "round4List: ${round4List.size}")
        Log.e("asdfsassdf", "round2List: ${round2List}")
        Log.e("asdfsassdf", "round2List: ${round2List.size}")
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
            selectedList.clear()
        }
    }

    private fun setupFinal() {
        // insert into database
        lifecycleScope.launchWhenStarted {
            round32List.forEach {
                viewModel.insertNationalTeam(it.copy(listType = 0))
            }
            Log.e("FINAL", "setupFinal: ${round32List}")
            Log.e("FINAL", "setupFinal: ${round32List.size}")
        }
        lifecycleScope.launchWhenStarted {
            round16List.forEach {
                viewModel.insertNationalTeam(it.copy(listType = 1))
            }
            Log.e("FINAL", "setupFinal: ${round16List}")
            Log.e("FINAL", "setupFinal: ${round16List.size}")
        }
        lifecycleScope.launchWhenStarted {
            round8List.forEach {
                val item = it.copy(listType = 2)
                viewModel.insertNationalTeam(item)
                Log.e("FINALAA", "setupFinal: ${item}")
            }
            Log.e("FINAL", "setupFinal: ${round8List}")
            Log.e("FINALAA", "setupFinal: ${round8List.size}")
        }
        lifecycleScope.launchWhenStarted {
            round4List.forEach {
                viewModel.insertNationalTeam(it.copy(listType = 3))
            }
            Log.e("FINAL", "setupFinal: ${round4List}")
            Log.e("FINAL", "setupFinal: ${round4List.size}")
        }
        lifecycleScope.launchWhenStarted {
            round2List.forEach {
                viewModel.insertNationalTeam(it.copy(listType = 4))
            }
            Log.e("FINAL", "setupFinal: ${round2List}")
            Log.e("FINAL", "setupFinal: ${round2List.size}")
        }
        Toast.makeText(requireActivity(), "Items Added", Toast.LENGTH_SHORT).show()
    }

    private fun fabClicked(nationalTeams: List<NationalTeam>) {
        if (!viewModel.isRecyclerViewActive.value!!) {
            Log.e("FFFFFFFF", "I AMMM FUCKIN HEREEEEEEEEEEE")
            when (nationalTeams[0].listType) {
                0 -> {
                    list.addAll(nationalTeams)
                    round32List.addAll(nationalTeams)
                    Log.e("asdfsadf1", "round32List=> $round32List size => ${round32List.size}")
                }
                1 -> {
                    round16List.addAll(nationalTeams)
                    Log.e("asdfsadf", "round16List=> $round16List size => ${round16List.size}")

                }
                2 -> {
                    round8List.addAll(nationalTeams)
                    Log.e(
                        "ggggggggggggg",
                        "round8List=> $nationalTeams size => ${nationalTeams.size}"
                    )

                }
                3 -> {
                    round4List.addAll(nationalTeams)
                    Log.e("LISTS", "round4List=> $round4List size => ${round4List.size}")

                }
                4 -> {
                    round2List.addAll(nationalTeams)
                    Log.e("LISTS", "round2List=> $round2List size => ${round2List.size}")

                }
            }
            if (page == 0) {
                val adapter = NationalTeamsAdapter(list.groupBy {
                    it.group
                }, this@HomeFragment, Rounds.Round32)
                binding.recyclerView.adapter = adapter
            } else if (round16List.isNotEmpty() && page == 1) {
                setupRound16Deactivate()
                setupAdapterWhenRecyclerDeactivate(round16List, Rounds.Round16)
            } else if (round8List.isNotEmpty() && round16List.isNotEmpty() && page == 2) {
                setupRound8Deactivate()
                setupAdapterWhenRecyclerDeactivate(round8List, Rounds.Round8)
            } else if (round4List.isNotEmpty() && round8List.isNotEmpty() && page == 3) {
                setupRound4Deactivate()
                setupAdapterWhenRecyclerDeactivate(round4List, Rounds.Round4)
            } else if (round2List.isNotEmpty() && round4List.isNotEmpty() && page == 4) {
                setupRound2Deactivate()
                setupAdapterWhenRecyclerDeactivate(round2List, Rounds.Round2)
            }
        } else {
            if (page == 0) {
                Log.e("NEW", "First page => $page ")
                Log.e("NEW", "selectedList before => $selectedList ")
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
                Log.e("NEW", "selectedList after => $selectedList ")
                Log.e(
                    "NEW",
                    "********************************************************************************************************************************************************************************************** ",
                )
            } else if (page == 1) {
                Log.e("NEW", "Second page => $page ")
                Log.e("NEW", "selectedList before => $selectedList ")
                setupRound16()
                setupAdapter(Rounds.Round16, round16List)
                Log.e("round32List", "round32List: ${round32List}")
                Log.e("round32List", "round16List: ${round16List}")
                selectedList.clear()
                Log.e("NEW", "selectedList after => $selectedList ")
                Log.e(
                    "NEW",
                    "********************************************************************************************************************************************************************************************** ",
                )

            } else if (round16List.isNotEmpty() && page == 2) {
                Log.e("NEW", "Third page => $page ")
                Log.e("NEW", "selectedList before => $selectedList ")
                Log.e("NEW", "round8List => $round8List ")
                setupRound8()
                Log.e("NEW", "round8List sorting ?=> $round8List ")
                setupAdapter(Rounds.Round8, round8List)
                selectedList.clear()
                Log.e("NEW", "selectedList after => $selectedList ")
                Log.e(
                    "NEW",
                    "********************************************************************************************************************************************************************************************** ",
                )
            } else if (round8List.isNotEmpty() && page == 3) {
                Log.e("NEW", "Fourth page => $page ")
                Log.e("NEW", "selectedList before => $selectedList ")
                setupRound4()
                setupAdapter(Rounds.Round4, round4List)
                selectedList.clear()
                Log.e("NEW", "selectedList after => $selectedList ")
                Log.e(
                    "NEW",
                    "********************************************************************************************************************************************************************************************** ",
                )

            } else if (round4List.isNotEmpty() && page == 4) {
                Log.e("NEW", "Fifth page => $page ")
                Log.e("NEW", "selectedList before => $selectedList ")
                setupRound2()
                setupAdapter(Rounds.Round2, round2List)
                selectedList.clear()
                Log.e("NEW", "selectedList after => $selectedList ")
                Log.e(
                    "NEW",
                    "********************************************************************************************************************************************************************************************** ",
                )
            } else if (selectedList.size == 1 && round2List.isNotEmpty()) {
                setupWinner()
                setupFinal()
            } else {
                Toast.makeText(requireActivity(), "Select team", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sortByGroup(list: MutableList<NationalTeam>, round: Rounds): MutableList<NationalTeam> {
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