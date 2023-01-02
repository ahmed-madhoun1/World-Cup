package com.ahmedmadhoun.worldcup.ui.pets

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.adapters.PetsAdapter
import com.ahmedmadhoun.worldcup.data.remote.pets_response.Pet
import com.ahmedmadhoun.worldcup.databinding.FragmentPetsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PetsFragment : Fragment(R.layout.fragment_pets), PetsAdapter.OnItemClickListener {

    private var _binding: FragmentPetsBinding? = null

    private val binding get() = _binding!!

    private val viewModel: PetsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPetsBinding.bind(view)

        val petsAdapter = PetsAdapter(this)
        binding.apply {
            recyclerView.apply {
                adapter = petsAdapter
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getPetsStateFlow.collectLatest {
                    if (it.isNotEmpty()) {
                        petsAdapter.pets = it
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        viewModel.insetPetsIntoDatabase(it)
                    } else {
                        recyclerView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        Toast.makeText(requireActivity(), "No PopulationOb", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onItemClick(selectedItem: Pet) {
        startActivity(Intent(ACTION_VIEW).apply {
            this.data = Uri.parse(selectedItem.Link)
        })
    }


}