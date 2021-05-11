package com.ngengeapps.unsplashcompose.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ngengeapps.unsplashcompose.models.Photo
import com.ngengeapps.unsplashcompose.networking.UnsplashAPI
import retrofit2.HttpException
import java.io.IOException

class UnSplashPagingSource(val unsplashAPI: UnsplashAPI,
                           val topic:String
):PagingSource<Int,Photo>() {
    private  val TAG = "UnSplashPagingSource"
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val nextPage = params.key?:1
            val response = unsplashAPI.getPhotos(topic = topic,page = nextPage,perPage = 20)
            Log.d(TAG, "load: $response")
            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextPage.plus(1)
            )
        } catch (e:IOException) {
            Log.e(TAG, "load: ioExc occurred ${e.message}", )
            LoadResult.Error(e)
        }
        catch (exc:HttpException) {
            Log.e(TAG, "load: httpExc", )
            LoadResult.Error(exc)
        } catch (exc:Exception) {
            LoadResult.Error(exc)
        }


    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {  anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}