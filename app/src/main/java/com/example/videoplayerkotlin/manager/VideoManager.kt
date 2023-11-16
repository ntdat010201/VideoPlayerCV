package com.example.videoplayerkotlin.manager

import com.example.videoplayerkotlin.model.VideoModel

open class VideoManager {
    lateinit var videos: ArrayList<VideoModel>
    var position: Int? = null


    companion object{
          private var instance : VideoManager? = null
        fun getInstance() : VideoManager {
            if (instance == null) {
                instance = VideoManager()
            }
            return instance as VideoManager

        }
    }

}