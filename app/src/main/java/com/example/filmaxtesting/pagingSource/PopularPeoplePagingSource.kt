package com.example.filmaxtesting.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.filmaxtesting.apiService.ApiService
import com.example.filmaxtesting.dataClasses.popularPeople.Person
import java.util.*

class PopularPeoplePagingSource(val apiService: ApiService):PagingSource<Int,Person>() {

    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        return  try {
            val currentPage=params.key?:1
            val response=apiService.getPopularPeople(page=currentPage)
            val responseData= mutableListOf<Person>()
            val data=response.body()?.results?: Collections.emptyList()
            responseData.addAll(data)

            LoadResult.Page(
                data=responseData,
                prevKey = if(currentPage==1) null else -1,
                nextKey =currentPage.plus(1)
            )

        }catch (e:Exception) {
            LoadResult.Error(e)
        }
    }
}