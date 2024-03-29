package com.alfa.mygithubuserapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alfa.mygithubuserapp.R
import com.alfa.mygithubuserapp.activity.DetailActivity
import com.alfa.mygithubuserapp.adapter.ListSearchAdapter
import com.alfa.mygithubuserapp.databinding.FragmentFollowBinding
import com.alfa.mygithubuserapp.response.ItemsSearch
import com.alfa.mygithubuserapp.viewmodel.DetailViewModel

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        when (arguments?.getInt(ARG_SECTION_NUMBER, 0)) {
            1 -> {
                detailViewModel.getFollowers(arguments?.getString(USERNAME))
                detailViewModel.followers.observe(requireActivity()) { users ->
                    setUserData(users)
                }
                detailViewModel.isLoading.observe(viewLifecycleOwner) {showLoading(it)}
            }

            2 -> {
                detailViewModel.getFollowing(arguments?.getString(USERNAME))
                detailViewModel.following.observe(requireActivity()) { users ->
                    setUserData(users)
                }
                detailViewModel.isLoading.observe(viewLifecycleOwner) {showLoading(it)}
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUserData(users: List<ItemsSearch>?) {
        val listUserAdapter = ListSearchAdapter(ArrayList())
        binding.rvFollow.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : ListSearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsSearch) {
                showSelectedUser(data)
            }
        })
        listUserAdapter.updateData(users ?: emptyList())
    }

    private fun showSelectedUser(user: ItemsSearch) {
        val detailUserIntent = Intent(activity, DetailActivity::class.java)
        detailUserIntent.putExtra(DetailActivity.EXTRA_USER, user.login)
        startActivity(detailUserIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}