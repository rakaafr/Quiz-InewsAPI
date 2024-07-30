package com.example.inewsapi

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.api.NewsApiService
import com.example.newsapp.adapter.ArticleAdapter
import com.example.newsapp.model.ArticleResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var newsApiService: NewsApiService
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Retrofit
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")F
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()

        newsApiService = retrofit.create(NewsApiService::class.java)

        // Initialize RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(emptyList())
        recyclerView.adapter = articleAdapter

        fetchArticles()
    }

    private fun fetchArticles() {
        progressBar.visibility = View.VISIBLE
        newsApiService.getArticles(
            query = "tesla",
            from = "2024-06-30",
            sortBy = "publishedAt",
            apiKey = "8747b72eb04441e285278705f3895545"
        ).enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val articleResponse = response.body()
                    articleResponse?.let {
                        articleAdapter = ArticleAdapter(it.articles)
                        recyclerView.adapter = articleAdapter
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to get articles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}