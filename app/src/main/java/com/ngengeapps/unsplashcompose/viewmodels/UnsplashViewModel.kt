package com.ngengeapps.unsplashcompose.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ngengeapps.unsplashcompose.models.Photo
import com.ngengeapps.unsplashcompose.networking.UnsplashAPI
import com.ngengeapps.unsplashcompose.repo.UnSplashPagingSource
import kotlinx.coroutines.flow.Flow

class UnsplashPageData(val topic:String){
    val photos:Flow<PagingData<Photo>> = Pager(PagingConfig(pageSize = 20)){
        UnSplashPagingSource(UnsplashAPI.create(), topic)
    }.flow

}
