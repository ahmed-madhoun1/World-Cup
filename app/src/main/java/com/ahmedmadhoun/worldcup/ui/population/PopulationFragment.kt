package com.ahmedmadhoun.worldcup.ui.population

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.adapters.PopulationAdapter
import com.ahmedmadhoun.worldcup.data.remote.population_response.PopulationOb
import com.ahmedmadhoun.worldcup.databinding.FragmentPopulationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PopulationFragment : Fragment(R.layout.fragment_population),
    PopulationAdapter.OnItemClickListener {
    private var _binding: FragmentPopulationBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PopulationViewModel by viewModels()

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPopulationBinding.bind(view)


        val populationAdapter = PopulationAdapter(this)
        binding.apply {
            recyclerView.apply {
                adapter = populationAdapter
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getPopulationStateFlow.collectLatest {
                    if (it.isNotEmpty()) {
                        populationAdapter.population = it
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        viewModel.insetPopulationIntoDatabase(it)
                    } else {
                        recyclerView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), "No PopulationOb", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onItemClick(selectedItem: PopulationOb) {

    }

}