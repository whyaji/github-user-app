package com.whyaji.githubuser.ui.detail

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ToggleButton
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginEnd
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.whyaji.githubuser.R
import com.whyaji.githubuser.databinding.ActivityDetailBinding
import com.whyaji.githubuser.viewmodel.DetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle(R.string.detail)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        toggleLoading(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val bundle = Bundle()

        if (username != null) {
            detailViewModel.setDetail(username)
            bundle.putString(EXTRA_USERNAME, username)
        }

        detailViewModel.getDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvRepository.text = it.publicRepos.toString()
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .into(ivUser)
                    if (i > 1) toggleLoading(false)
                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.apply {
            vpFollow.adapter = sectionPagerAdapter
            TabLayoutMediator(tlFollow, vpFollow) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)

        val toggleFavorite = menu.findItem(R.id.toggle_favorite).actionView as ToggleButton
        toggleFavorite.background =
            ResourcesCompat.getDrawable(resources, R.drawable.favorite_toggle, null)
        toggleFavorite.layoutParams =
            LinearLayout.LayoutParams(100, 100)
        toggleFavorite.textOff = ""
        toggleFavorite.textOn = ""

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        var isCheck = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        toggleFavorite.isChecked = true
                        isCheck = true
                    } else {
                        toggleFavorite.isChecked = false
                        isCheck = false
                    }
                }
            }
        }

        toggleFavorite.setOnClickListener {
            isCheck = !isCheck
            if (isCheck) {
                if (username != null) {
                    if (avatarUrl != null) {
                        detailViewModel.addToFavorite(username, id, avatarUrl)
                    }
                }
            } else {
                detailViewModel.removeFromFavorite(id)
            }
            toggleFavorite.isChecked = isCheck
        }
        return true
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

    private fun toggleLoading(load: Boolean) {
        i++
        binding.apply {
            val bindingDefault = listOf<View>(
                ivUser,
                tvName,
                tvUsername,
                tvFollowers,
                tvFollowing,
                tvRepository,
            )

            val bindingProgress = listOf<View>(
                pbIv,
                pbTvName,
                pbTvUsername,
                pbTvFollowers,
                pbTvFollowing,
                pbTvRepository
            )

            for (bd in bindingDefault) {
                if (load) {
                    bd.visibility = View.GONE
                } else {
                    bd.visibility = View.VISIBLE
                }
            }

            for (bd in bindingProgress) {
                if (!load) {
                    bd.visibility = View.GONE
                } else {
                    bd.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"

        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}