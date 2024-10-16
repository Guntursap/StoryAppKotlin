package com.guntur.storyapps.data.pref


import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.guntur.storyapps.data.response.DetailResponse
import com.guntur.storyapps.data.response.ListStoryItem
import com.guntur.storyapps.data.response.UploadResponse
import com.guntur.storyapps.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun createSession(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun saveLogin(email: String, password: String) =
        apiService.login(email, password)

    fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null)
    : Call<UploadResponse> {
        return apiService.uploadImage(file, description, lat, lon)
    }

    suspend fun saveUser(user: UserModel){
        userPreference.saveSession(user)
    }
    fun getSession(): LiveData<UserModel> {
        return userPreference.getSession().asLiveData()
    }

    fun getStory(): LiveData<PagingData<ListStoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            pagingSourceFactory = {
                UserPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun getStoryLocation() = apiService.getStoriesWithLocation()

    fun getDetailStory(id: String): Call<DetailResponse>{
        return apiService.getDetailStories(id)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun clearInstance() {
            instance = null
        }
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService)
            }.also { instance = it }
    }
}