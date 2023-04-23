package com.whyaji.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.whyaji.githubuser.api.ApiConfig
import com.whyaji.githubuser.data.local.FavoriteUser
import com.whyaji.githubuser.data.local.FavoriteUserDao
import com.whyaji.githubuser.data.local.UserDatabase
import com.whyaji.githubuser.data.response.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<DetailUserResponse>()
    private val user: LiveData<DetailUserResponse> = _user

    private val _searchError = MutableLiveData<Throwable>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setDetail(username: String) {
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _user.postValue(response.body())
                } else {
                    _searchError.value =
                        Throwable("Failed to search users: ${response.code()} ${response.message()}")
                    Log.e(DetailViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _searchError.value = t
                Log.e(DetailViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}