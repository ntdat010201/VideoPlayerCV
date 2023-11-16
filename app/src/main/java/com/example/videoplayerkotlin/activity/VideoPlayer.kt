package com.example.videoplayerkotlin.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.example.videoplayerkotlin.R
import com.example.videoplayerkotlin.databinding.ActivityVideoPlayerBinding
import com.example.videoplayerkotlin.manager.VideoManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

class VideoPlayer : AppCompatActivity() {
    private lateinit var binding: ActivityVideoPlayerBinding

    private lateinit var videoManager: VideoManager
    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    private fun initData() {
        videoManager = VideoManager.getInstance()
    }

    private fun initView() {
        startVideo()
    }

    private fun initListener() {
    }

    private fun startVideo() {
        val path: String = videoManager.videos[videoManager.position!!].path
        player = SimpleExoPlayer.Builder(this).build()
        binding.playerView.player = player
        val mediaItem = MediaItem.fromUri(path)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }


}