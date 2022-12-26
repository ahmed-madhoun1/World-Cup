package com.ahmedmadhoun.worldcup.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.databinding.FragmentInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : Fragment(R.layout.fragment_info) {

    private var _binding: FragmentInfoBinding? = null

    private val binding get() = _binding!!

    private val viewModel: NationalTeamsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInfoBinding.bind(view)

        binding.apply {
            startBtn.setOnClickListener {
                findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToHomeFragment())
            }

            restartBtn.setOnClickListener {
                viewModel.deleteAllNationalTeams().let {
                    if (it.isCompleted) {
                        Toast.makeText(requireContext(), "Data Deleted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}