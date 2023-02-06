package com.example.newsapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.api.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter

    companion object {
        const val API_KEY = "fbd73a7e00c843bfa71c55d16c10aad2"
        const val BASE_URL = "https://newsapi.org"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        val recyclerViewId = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerViewId.layoutManager = LinearLayoutManager(this)

        mAdapter = NewsListAdapter(this)
        recyclerViewId.adapter = mAdapter

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getBreakingNews()
            } catch (e: IOException) {
                Log.i(TAG, e.localizedMessage)
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.i(TAG, e.localizedMessage)
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!.articles
                val newsArray = ArrayList<Article>()

                for (i in responseBody.indices) {
                    newsArray.add(responseBody[i])
                }

                progressBar.visibility = View.INVISIBLE

                mAdapter.updateNews(newsArray)
            }
        }
    }

    override fun onItemClicked(item: Article) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}