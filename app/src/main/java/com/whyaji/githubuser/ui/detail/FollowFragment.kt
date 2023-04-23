package com.whyaji.githubuser.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.whyaji.githubuser.R
import com.whyaji.githubuser.databinding.FragmentFollowBinding
import com.whyaji.githubuser.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val followViewModel by viewModels<FollowViewModel>()
    private lateinit var adapter: FollowAdapter
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER)
        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)

        adapter = FollowAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.adapter = adapter
        }

        showLoading(true)
        when (index) {
            0 -> followViewModel.setListFollowers(username)
            1 -> followViewModel.setListFollowing(username)
        }

        followViewModel.listFollow.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setItems(it)
                showLoading(false)
            }
            showNotFound(it.isEmpty())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean){
        binding.givLoading.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showNotFound(state: Boolean){
        if (state) {
            binding.tvNotFound.visibility = View.VISIBLE
        } else {
            binding.tvNotFound.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val EXTRA_USERNAME = "extra_username"
    }
}