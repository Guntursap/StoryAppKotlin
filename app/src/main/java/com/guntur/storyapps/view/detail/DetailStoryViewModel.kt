package com.guntur.storyapps.view.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.response.DetailResponse
import com.guntur.storyapps.data.response.Story
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val repos: UserRepository): ViewModel() {

    private val _detailResponse = MutableLiveData<Story>()
    val detailStory: LiveData<Story> = _detailResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun getAllDetailStory(id: String){
        _loading.value = true
        viewModelScope.launch {
            val response = repos.getDetailStory(id)
            response.enqueue(object : Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    _loading.value = false
                    if (response.isSuccessful) {
                        _detailResponse.value = response.body()?.story as Story
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _loading.value = false
                    Log.e(TAG, "error: ${t.message}")
                }

            })
        }
    }
    companion object{
        private const val TAG = "detailStoryViewModel"
    }

}