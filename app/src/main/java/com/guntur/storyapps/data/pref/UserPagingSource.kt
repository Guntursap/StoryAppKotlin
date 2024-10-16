package com.guntur.storyapps.data.pref

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.guntur.storyapps.data.response.ListStoryItem
import com.guntur.storyapps.data.retrofit.ApiService
import retrofit2.HttpException


class UserPagingSource(private val apiService: ApiService): PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(position, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position -1,
                nextKey = if (responseData.listStory.isEmpty()) null else position + 1
            )
        } catch (e: HttpException){
            Log.e(TAG, "Error paging: ${e.localizedMessage}")
            return LoadResult.Error(e)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val TAG = "UserPagingSource"
    }
}