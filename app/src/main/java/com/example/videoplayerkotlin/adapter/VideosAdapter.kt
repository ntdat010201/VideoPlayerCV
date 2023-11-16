package com.example.videoplayerkotlin.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayerkotlin.activity.VideoPlayer
import com.example.videoplayerkotlin.databinding.FilesViewBinding
import com.example.videoplayerkotlin.dialog.DialogMenuVideo
import com.example.videoplayerkotlin.model.VideoModel
import com.example.videoplayerkotlin.utils.FileUtils

class VideosAdapter(
    var videoFolder: ArrayList<VideoModel>,
    private var context: Context,


    ) : RecyclerView.Adapter<VideosAdapter.MyHolder>() {

    var onClickListener : ((Int) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(
            FilesViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return videoFolder.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Glide.with(context).load(videoFolder[position].path).into(holder.binding.thumbnail)

        holder.binding.videoTitle.text = videoFolder[position].title
        holder.binding.videoDuration.text =
            FileUtils.formatDuration(videoFolder[position].duration.toInt())
        holder.binding.videoSize.text = FileUtils.formatSize(videoFolder[position].size.toLong())
        holder.binding.videoQuality.text = videoFolder[position].resolution + "p"

        holder.binding.videoMenu.setOnClickListener {
            val dial = DialogMenuVideo(context, position, videoFolder, this)
            dial.onClickDialog()
        }

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(holder.adapterPosition)
            val intent = Intent(context,VideoPlayer::class.java)
            context.startActivity(intent)

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateSearchList(searchList: java.util.ArrayList<VideoModel>) {
        videoFolder = arrayListOf()
        videoFolder.addAll(searchList)
        notifyDataSetChanged()
    }

    inner class MyHolder(var binding: FilesViewBinding) : RecyclerView.ViewHolder(binding.root)
}