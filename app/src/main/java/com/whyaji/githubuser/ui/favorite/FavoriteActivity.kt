package com.whyaji.githubuser.ui.favorite

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.whyaji.githubuser.R
import com.whyaji.githubuser.data.local.FavoriteUser
import com.whyaji.githubuser.data.response.ItemsItem
import com.whyaji.githubuser.databinding.ActivityFavoriteBinding
import com.whyaji.githubuser.ui.detail.DetailActivity
import com.whyaji.githubuser.ui.user.UserAdapter
import com.whyaji.githubuser.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private lateinit var adapter: UserAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle(R.string.favorite)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        favoriteViewModel.getFavoriteUser()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.setItems(list)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> true
        }
    }

    private fun mapList(users: List<FavoriteUser>): List<ItemsItem> {
        val listUser = ArrayList<ItemsItem>()
        for (user in users) {
            val userMapped = ItemsItem(
                login = user.login,
                id = user.id,
                avatarUrl = user.avatar_url
            )
            listUser.add(userMapped)
        }
        return listUser.toList()
    }
}