package com.example.videoplayerkotlin.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.videoplayerkotlin.R
import com.example.videoplayerkotlin.adapter.VideosAdapter
import com.example.videoplayerkotlin.model.VideoModel
import com.example.videoplayerkotlin.utils.FileUtils
import com.google.android.material.bottomsheet.BottomSheetDialog

class DialogMenuVideo(
    private val context: Context,
    private val position: Int,
    private val videoFolder: ArrayList<VideoModel>,
    private val adapter: VideosAdapter
) {

    fun onClickDialog() {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View =
            LayoutInflater.from(context).inflate(R.layout.file_menu, null)

        bottomSheetView.findViewById<View>(R.id.menu_down).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<View>(R.id.menu_share).setOnClickListener {
            bottomSheetDialog.dismiss()
            FileUtils.shareFile(videoFolder, position, context)
        }
        bottomSheetView.findViewById<View>(R.id.menu_rename).setOnClickListener {
            bottomSheetDialog.dismiss()
            FileUtils.renameFiles(View(context), videoFolder, position, context)
        }
        bottomSheetView.findViewById<View>(R.id.menu_delete).setOnClickListener {
            bottomSheetDialog.dismiss()
            FileUtils.deleteFile(View(context), videoFolder, position, context, adapter)
        }
        bottomSheetView.findViewById<View>(R.id.menu_properties).setOnClickListener {
            bottomSheetDialog.dismiss()
            FileUtils.showProperties(videoFolder, position, context)
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

}