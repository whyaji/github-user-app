package com.whyaji.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.whyaji.githubuser.api.ApiConfig
import com.whyaji.githubuser.data.local.SettingPreferences
import com.whyaji.githubuser.data.response.GithubResponse
import com.whyaji.githubuser.data.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {
    private val _searchResults = MutableLiveData<List<ItemsItem>>()
    val searchResult: MutableLiveData<List<ItemsItem>> = _searchResults

    private val _searchError = MutableLiveData<Throwable>()

    fun getThemeSettings() = preferences.getThemeSetting().asLiveData()

    fun searchUsers(query: String) {
        val client = ApiConfig.getApiService().searchUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                if (response.isSuccessful) {
                    _searchResults.postValue(response.body()?.items!!)
                } else {
                    _searchError.value = Throwable("Failed to search users: ${response.code()} ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _searchError.value = t
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    class Factory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(pref) as T
        }
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}