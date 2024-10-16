package com.guntur.storyapps.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.guntur.storyapps.data.pref.UserModel
import com.guntur.storyapps.data.pref.UserRepository
import com.guntur.storyapps.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repos: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel>{
        return repos.getSession()
    }

    fun logout() {
        viewModelScope.launch {
            repos.logout()
        }
    }

    val getAllStories: LiveData<PagingData<ListStoryItem>> =
        repos.getStory().cachedIn(viewModelScope)

}