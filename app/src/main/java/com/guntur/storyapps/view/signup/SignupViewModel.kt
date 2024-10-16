package com.guntur.storyapps.view.signup


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repos: UserRepository): ViewModel() {

    companion object{
        private const val TAG = "signupViewModel"
    }

    private val _signupUser = MutableLiveData<RegisterResponse>()
    val signupUser : LiveData<RegisterResponse> = _signupUser

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    fun signup(name: String, email: String, password: String){
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repos.createSession(name, email, password)
                _loading.value = false
                _signupUser.value = response
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val errorMessage = errorBody.message
                _loading.value = false
                _signupUser.value = errorBody
                Log.d(TAG, "onError : $errorMessage")
            }
        }
    }
}