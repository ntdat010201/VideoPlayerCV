package com.example.videoplayerkotlin.activity

import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import com.example.videoplayerkotlin.adapter.FolderAdapter
import com.example.videoplayerkotlin.databinding.ActivityMainBinding
import com.example.videoplayerkotlin.model.VideoModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var folderList : ArrayList<String>
    private lateinit var videoList : ArrayList<VideoModel>
    private lateinit var folderAdapter : FolderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        folderList = arrayListOf()
        videoList = arrayListOf()

        showRecyclerView()

    }

    private fun initView() {}

    private fun initListener() {

    }

    private fun fetchAllVideos(context : Context): ArrayList<VideoModel> {
        val videoModels: ArrayList<VideoModel> = arrayListOf()

        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC "

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.RESOLUTION,
        )
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, orderBy)

        if (cursor != null) {
            while (cursor.moveToNext()) {

                val id: String = cursor.getString(0)
                val path: String = cursor.getString(1)
                val title: String = cursor.getString(2)
                val size: String = cursor.getString(3)
                val resolution: String = cursor.getString(4)
                val duration: String = cursor.getString(5)
                val disName: String = cursor.getString(6)
                val width_height: String = cursor.getString(7)

                val videoFiles = VideoModel(id, path, title, size, resolution, duration, disName, width_height)

                val slashFirstIndex: Int = path.lastIndexOf("/")
                val subString: String = path.substring(0, slashFirstIndex)

                if (!folderList.contains(subString)) {
                    folderList.add(subString)
                }
                videoModels.add(videoFiles)
            }
            cursor.close()
        }
        return videoModels
    }

    private fun showRecyclerView(){
        videoList = fetchAllVideos(this)
        if (folderList != null && folderList.size > 0 && videoList != null){
            folderAdapter = FolderAdapter(folderList,videoList,this)
            binding.folderRecyclerview.adapter = folderAdapter
        } else{
            Toast.makeText(this,"can't find any video folder", Toast.LENGTH_SHORT).show()
        }
    }

}