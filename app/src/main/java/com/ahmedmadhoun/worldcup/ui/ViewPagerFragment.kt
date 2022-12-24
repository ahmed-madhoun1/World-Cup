package com.ahmedmadhoun.worldcup.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ahmedmadhoun.worldcup.R
import com.ahmedmadhoun.worldcup.data.local.NationalTeam
import com.ahmedmadhoun.worldcup.databinding.FragmentViewPagerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private var _binding: FragmentViewPagerBinding? = null


    private val binding get() = _binding!!

    private val viewModel: NationalTeamsViewModel by viewModels()

    private var page = 0

    val selectedList = mutableListOf<NationalTeam>()
    var list: MutableList<NationalTeam> = mutableListOf()
    var newList: MutableList<NationalTeam> = mutableListOf()
    var round8List: MutableList<NationalTeam> = mutableListOf()
    var round4List: MutableList<NationalTeam> = mutableListOf()
    var round2List: MutableList<NationalTeam> = mutableListOf()
    var round16List: MutableList<NationalTeam> = mutableListOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentViewPagerBinding.bind(view)

        binding.apply {
            viewPager.isUserInputEnabled = false
            viewPager.offscreenPageLimit = 20
            val viewPagerAdapter = ViewPagerAdapter(this@ViewPagerFragment)
            viewPager.adapter = viewPagerAdapter
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            fabPrev.visibility = View.GONE
                            page = 0
                        }
                        1 -> {
                            fabPrev.visibility = View.VISIBLE
                            page = 1
                            // here call function to show view maybe :/
                        }
                        2 -> {
                            fabPrev.visibility = View.VISIBLE
                            page = 2
                        }
                        3 -> {
                            fabPrev.visibility = View.VISIBLE
                            page = 3
                        }
                    }
                }
            })

            fabNext.setOnClickListener {
                if (page < 5) {
//                fabClicked()
                    page++
                    when (page) {
                        0 -> {
                            viewPager.currentItem = 0
                        }
                        1 -> {
                            viewPager.currentItem = 1
                            fabPrev.visibility = View.VISIBLE
                        }
                        2 -> {
                            viewPager.currentItem = 2
                            fabPrev.visibility = View.VISIBLE
                        }
                        3 -> {
                            viewPager.currentItem = 3
                            fabPrev.visibility = View.VISIBLE
                        }
                        4 -> {
                            viewPager.currentItem = 4
                            fabPrev.visibility = View.VISIBLE
                        }
                    }
                }
            }

            fabPrev.setOnClickListener {
                if (page > 0) {
//                fabClicked()
                    page--
                    when (page) {
                        0 -> {
                            viewPager.currentItem = 0
                            fabPrev.visibility = View.GONE
                        }
                        1 -> {
                            viewPager.currentItem = 1
                            fabPrev.visibility = View.VISIBLE
                        }
                        2 -> {
                            viewPager.currentItem = 2
                            fabPrev.visibility = View.VISIBLE
                        }
                        3 -> {
                            viewPager.currentItem = 3
                            fabPrev.visibility = View.VISIBLE
                        }
                        4 -> {
                            viewPager.currentItem = 4
                            fabPrev.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

    }

    var lastIndex = 0

    private inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 100

        override fun createFragment(position: Int): Fragment {
            return HomeFragment()
        }


    }


}


val ARG_OBJECT = "page"
