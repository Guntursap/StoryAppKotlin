package com.guntur.storyapps.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.guntur.storyapps.data.pref.UserModel
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repos: UserRepository) : ViewModel() {

    companion object{
        private const val TAG = "loginViewModel"
    }
    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> = _loginResponse

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    fun saveSession(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = repos.saveLogin(email, password)
                saveUser(
                    UserModel(
                        response.loginResult?.userId!!,
                        response.loginResult.name!!,
                        email,
                        response.loginResult.token!!,
                        true
                    )
                )
                _loading.value = false
                _loginResponse.value = response
                Log.d(TAG, "onSuccess : ${response.message}" )
            }catch (e: HttpException){
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _loading.value = false
                _loginResponse.value = errorBody
                Log.d(TAG, "onError : $errorMessage")
            }
        }
    }
    private fun saveUser(user: UserModel){
        viewModelScope.launch {
            repos.saveUser(user)
        }
    }
}