package com.ahmedmadhoun.worldcup.ui.info

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.databinding.FragmentInfoBinding
import com.ahmedmadhoun.worldcup.ui.home.*
import com.google.firebase.inappmessaging.FirebaseInAppMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class InfoFragment : Fragment(R.layout.fragment_info) {

    private var _binding: FragmentInfoBinding? = null

    private val binding get() = _binding!!

    private val viewModel: NationalTeamsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInfoBinding.bind(view)


        round32List.clear()
        round16List.clear()
        round8List.clear()
        round4List.clear()
        round2List.clear()
        selectedList.clear()
        list.clear()

        binding.apply{
            banner2.setOnClickListener {
                findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToHomeFragment())
            }
            banner1.setOnClickListener {
                viewModel.deleteAllNationalTeams()
            }
        }



//        binding.apply {
//            startBtn.setOnClickListener {
//                findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToHomeFragment())
//            }
//
//            restartBtn.setOnClickListener {
//                viewModel.deleteAllNationalTeams()
//            }
//
//            petsBtn.setOnClickListener {
//                findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToPetsFragment())
//            }
//
//            populationBtn.setOnClickListener {
//                findNavController().navigate(InfoFragmentDirections.actionInfoFragmentToPopulationFragment())
//            }

        }

    }

