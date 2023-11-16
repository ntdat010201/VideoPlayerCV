package com.example.videoplayerkotlin.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayerkotlin.activity.VideoFolder
import com.example.videoplayerkotlin.databinding.FolderViewBinding
import com.example.videoplayerkotlin.model.VideoModel

class FolderAdapter(
    private var folderName: ArrayList<String>,
    private var videoModels: ArrayList<VideoModel>,
    private var context: Context

) : RecyclerView.Adapter<FolderAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FolderViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return folderName.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val index: Int = folderName[position].lastIndexOf("/")
        holder.binding.txtFolderName.text = folderName[position].substring(index + 1)
        holder.binding.videosCount.text = countVideos(folderName[position]).toString()

        holder.itemView.setOnClickListener {
           var intent = Intent(context, VideoFolder::class.java)
            intent.putExtra("folderName",folderName[position])
            context.startActivity(intent)
        }
    }

    inner class MyViewHolder(var binding: FolderViewBinding) : RecyclerView.ViewHolder(binding.root)

    private fun countVideos(folders: String): Int {
        var count = 0

        for (model: VideoModel in videoModels) {
            if (model.path.substring(0,model.path.lastIndexOf("/")).endsWith(folders)){
                count++
            }
        }
        return count
    }


}