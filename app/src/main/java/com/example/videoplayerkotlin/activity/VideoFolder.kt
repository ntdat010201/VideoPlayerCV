package com.example.videoplayerkotlin.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.videoplayerkotlin.R
import com.example.videoplayerkotlin.adapter.VideosAdapter
import com.example.videoplayerkotlin.databinding.ActivityVideoFolderBinding
import com.example.videoplayerkotlin.manager.VideoManager
import com.example.videoplayerkotlin.model.VideoModel
import com.example.videoplayerkotlin.utils.Const.MY_SORT_PREF
import java.util.*

open class VideoFolder : AppCompatActivity(), SearchView.OnQueryTextListener{
    private lateinit var binding: ActivityVideoFolderBinding

    private lateinit var name: String
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var videoManager : VideoManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoFolderBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        videoManager = VideoManager.getInstance()

        name = intent.getStringExtra("folderName").toString()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_back))
        val index: Int = name.lastIndexOf("/")
        val onlyFolderName: String = name.substring(index + 1)
        binding.toolbar.title = onlyFolderName

        loadVideos()

    }

    private fun initView() {
        positionCallback()
    }

    private fun initListener() {}

    private fun loadVideos() {
        videoManager.videos = getAllVideoFromFolder(this, name)

        if (videoManager.videos.size > 0) {
            videosAdapter = VideosAdapter(videoManager.videos, this)
            binding.videoRecyclerview.setHasFixedSize(true)
            binding.videoRecyclerview.setItemViewCacheSize(20)
            binding.videoRecyclerview.isDrawingCacheEnabled = true
            binding.videoRecyclerview.isNestedScrollingEnabled = false

            binding.videoRecyclerview.adapter = videosAdapter

        } else {
            Toast.makeText(this, "can't find any video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllVideoFromFolder(context: Context, name: String): ArrayList<VideoModel> {
        val preferences: SharedPreferences = getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE)
        val sort = preferences.getString("sorting", "sortByDate")
        var order: String? = null

        when (sort) {
            "sortByDate" -> {
                order = MediaStore.Video.Media.DATE_ADDED + " DESC "
            }
            "sortByName" -> {
                order = MediaStore.Video.VideoColumns.DISPLAY_NAME
            }
            "sortBySize" -> {
                order = MediaStore.Video.VideoColumns.SIZE
            }
        }

        val list: ArrayList<VideoModel> = arrayListOf()
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.RESOLUTION,
        )

        val selection = MediaStore.Video.Media.DATA + " like ? "
        val selectionArgs: Array<String> = arrayOf("%$name%")

        val cursor: Cursor? =
            context.contentResolver.query(uri, projection, selection, selectionArgs, order)

        if (cursor != null) {
            while (cursor.moveToNext()) {

                val id: String = cursor.getString(0)
                val path: String = cursor.getString(1)
                val title: String = cursor.getString(2)
                val size: String = cursor.getString(3)
                val resolution: String = cursor.getString(4)
                val duration: String = cursor.getString(5)
                val disName: String = cursor.getString(6)
                val bucket_display_name: String = cursor.getString(7)
                val width_height: String = cursor.getString(8)

                val files = VideoModel(
                    id, path, title, size, resolution, duration, disName, width_height
                )
                if (name.endsWith(bucket_display_name)) {
                    list.add(files)
                }
            }
            cursor.close()
        }
        return list
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        val menuItem: MenuItem = menu.findItem(R.id.search)
        val searchView: SearchView = menuItem.actionView as SearchView
        val ivClose: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        ivClose.setColorFilter(
            ContextCompat.getColor(applicationContext, R.color.white),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        searchView.queryHint = "Search file name"
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val input: String = newText.lowercase(Locale.getDefault())
        val searchList: ArrayList<VideoModel> = arrayListOf()
        for (model: VideoModel in videoManager.videos) {
            if (model.title.lowercase(Locale.getDefault()).contains(input)) {
                searchList.add(model)
            }
        }
        videosAdapter.updateSearchList(searchList)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val editor: SharedPreferences.Editor =
            getSharedPreferences(MY_SORT_PREF, MODE_PRIVATE).edit()
        when (item.itemId) {
            R.id.sort_by_date -> {
                editor.putString("sorting", "sortByDate")
                editor.apply()
                this.recreate()
            }
            R.id.sort_by_name -> {
                editor.putString("sorting", "sortByName")
                editor.apply()
                this.recreate()
            }
            R.id.sort_by_size -> {
                editor.putString("sorting", "sortBySize")
                editor.apply()
                this.recreate()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun positionCallback(){
        videosAdapter.onClickListener ={
            videoManager.position = it
        }
    }

}
