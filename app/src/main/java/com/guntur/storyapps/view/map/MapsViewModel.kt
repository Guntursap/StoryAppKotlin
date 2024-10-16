package com.guntur.storyapps.view.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.response.StoryResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repos: UserRepository): ViewModel() {

    private val _mapResponse = MutableLiveData<StoryResponse>()
    val mapResponse: LiveData<StoryResponse> = _mapResponse

    fun getStoryLocation(){
        viewModelScope.launch {
            try {
                val response = repos.getStoryLocation()
                Log.d(TAG, "onSuccess: ${response.message}")
                _mapResponse.value = response
            } catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }
    companion object{
        private const val TAG = "MapsViewModel"
    }
}