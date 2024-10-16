package com.guntur.storyapps.view.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.response.UploadResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val repos: UserRepository) : ViewModel() {

    companion object{
        private const val TAG = "uploadViewModel"
    }

    private val _uploadUser = MutableLiveData <UploadResponse>()
    val uploadUser : LiveData<UploadResponse> = _uploadUser

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    fun uploadStory(file: MultipartBody.Part, description: RequestBody, lat: Double? = null, lon: Double? = null) {
        viewModelScope.launch {
            _loading.value = true
            val response = repos.uploadImage(file, description, lat, lon)
            response.enqueue(object: Callback<UploadResponse>{
                override fun onResponse(
                    call: Call<UploadResponse>,
                    response: Response<UploadResponse>
                ) {
                    if (response.isSuccessful){
                        _loading.value = false
                        _uploadUser.value = response.body()
                        Log.d(TAG, "onSuccess: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                    _loading.value = false
                    Log.e(TAG, "onError: ${t.message}")
                }
            })
        }
    }
}