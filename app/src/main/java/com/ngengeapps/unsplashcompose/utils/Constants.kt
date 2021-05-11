package com.ngengeapps.unsplashcompose.utils

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ngengeapps.unsplashcompose.models.Photo
import com.ngengeapps.unsplashcompose.networking.UnsplashAPI
import com.ngengeapps.unsplashcompose.repo.UnSplashPagingSource
import kotlinx.coroutines.flow.Flow

object Constants {
    val UNSPLASH_BASE_URL = "https://unsplash.com/napi/topics/"


    val pages = listOf("food-drink","wallpapers","nature","people",
        "architecture","fashion","film","technology","travel", "health","history",
        "interiors","current-events","business-work","street-photography")
    val mainPopUp = listOf("Brands","Blog","Topics","Collections",
        "Community","History","Made with Unsplash","API/Developers",
        "Official Apps","We're hiring")

    fun getUnsplashPageFlow(topic:String): Flow<PagingData<Photo>> = Pager(PagingConfig(pageSize = 20)){
        UnSplashPagingSource(UnsplashAPI.create(), topic)
    }.flow
}