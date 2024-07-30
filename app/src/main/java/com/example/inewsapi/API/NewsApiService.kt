package com.example.inewsapi.API

import android.os.Parcel
import android.os.Parcelable
import com.example.inewsapi.model.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService() : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    @GET("everything")
    fun getArticles(
        @Query("q") query: String,
        @Query("from") from: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String
    ): Call<ArticleResponse>

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsApiService> {
        override fun createFromParcel(parcel: Parcel): NewsApiService {
            return NewsApiService(parcel)
        }

        override fun newArray(size: Int): Array<NewsApiService?> {
            return arrayOfNulls(size)
        }
    }
}