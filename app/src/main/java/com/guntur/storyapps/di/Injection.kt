package com.guntur.storyapps.di

import android.content.Context
import com.guntur.storyapps.data.pref.UserPreference
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.pref.dataStore
import com.guntur.storyapps.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository = runBlocking {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getApiService(user.token)
        UserRepository.getInstance(pref, apiService)
    }
}