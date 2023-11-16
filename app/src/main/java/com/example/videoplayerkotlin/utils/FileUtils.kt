package com.example.videoplayerkotlin.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.videoplayerkotlin.R
import com.example.videoplayerkotlin.adapter.VideosAdapter
import com.example.videoplayerkotlin.model.VideoModel
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.DecimalFormat

object FileUtils {

    fun shareFile(videoFolder: ArrayList<VideoModel>, position: Int, context: Context) {
        val uri: Uri = Uri.parse(videoFolder[position].path)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "video/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(Intent.createChooser(intent, "share"))
        Toast.makeText(context, "loading...", Toast.LENGTH_SHORT).show()
    }

    fun renameFiles(
        view: View,
        videoFolder: ArrayList<VideoModel>,
        position: Int,
        context: Context
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.rename_layout)

        val editText: EditText = dialog.findViewById(R.id.rename_edt)
        val cancel: Button = dialog.findViewById(R.id.cancel_rename_bt)
        val renameBtn: Button = dialog.findViewById(R.id.rename_bt)

        val renameFile = File(videoFolder[position].path)
        var nameText: String = renameFile.name
        nameText = nameText.substring(0, nameText.lastIndexOf("."))
        editText.setText(nameText)
        editText.clearFocus()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        renameBtn.setOnClickListener {
            val onlyPath: String = renameFile.parentFile!!.absolutePath
            var ext: String = renameFile.absolutePath
            ext = ext.substring(ext.lastIndexOf("."))
            val newPath = onlyPath + "/" + editText.text + ext
            val newFile = File(newPath)
            val rename: Boolean = renameFile.renameTo(newFile)
            if (rename) {
                context.applicationContext.contentResolver.delete(
                    MediaStore.Files.getContentUri("external"),
                    MediaStore.MediaColumns.DATA + "=?",
                    arrayOf(renameFile.absolutePath)
                )
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = Uri.fromFile(newFile)
                context.applicationContext.sendBroadcast(intent)
                Snackbar.make(view, "rename success", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view, "rename failed", Snackbar.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    fun deleteFile(
        view: View,
        videoFolder: ArrayList<VideoModel>,
        position: Int,
        context: Context, adapter: VideosAdapter
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("delete").setMessage(videoFolder[position].title)
            .setNegativeButton("cancel") { _, _ ->
                //
            }.setPositiveButton("ok") { _, _ ->
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoFolder[position].id.toLong()
                )
                val file = File(videoFolder[position].path)
                val deleted: Boolean = file.delete()
                if (deleted) {
                    context.applicationContext.contentResolver.delete(contentUri, null, null)
                    videoFolder.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, videoFolder.size)
                    Snackbar.make(view, "file deleted success", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(view, "file deleted fail", Snackbar.LENGTH_SHORT).show()
                }
            }.show()
    }

    @SuppressLint("SetTextI18n")
    fun showProperties(
        videoFolder: ArrayList<VideoModel>,
        position: Int,
        context: Context
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.file_properties_layout)
        val name: String = videoFolder[position].title
        val path: String = videoFolder[position].path
        val size: String = formatSize(videoFolder[position].size.toLong())
        val duration: String = formatDuration(videoFolder[position].duration.toInt())
        val resolution: String = videoFolder[position].resolution

        val tit: TextView = dialog.findViewById(R.id.pro_title)
        val st: TextView = dialog.findViewById(R.id.pro_storage)
        val siz: TextView = dialog.findViewById(R.id.pro_size)
        val dur: TextView = dialog.findViewById(R.id.pro_duration)
        val res: TextView = dialog.findViewById(R.id.pro_resolution)

        tit.text = name
        st.text = path
        siz.text = size
        dur.text = duration
        res.text = resolution + "p"

        dialog.show()
    }



fun formatSize(byres : Long) : String{
    val hrSize: String

    val k: Double = byres / 1024.0
    val m: Double = byres / 1024.0 / 1024.0
    val g: Double = byres / 1024.0 / 1024.0 / 1024.0
    val t: Double = byres / 1024.0 / 1024.0 / 1024.0 / 1024.0

    //the format
    val dec = DecimalFormat("0.00")
    hrSize = if (t > 1) {
        dec.format(t) + " TB"
    } else if (g > 1) {
        dec.format(g) + " GB"
    } else if (m > 1) {
        dec.format(m) + " MB"
    } else if (k > 1) {
        dec.format(m) + " KB"
    } else {
        dec.format(m) + " Bytes"
    }
    return hrSize
}

    fun formatDuration(duration: Int) : String {
        val time: String
        val hrs: Int = duration / (1000 * 60 * 60)
        val min: Int = duration % (1000 * 60 * 60) / (1000 * 60)
        val secs: Int = duration % (1000 * 60 * 60) % (1000 * 60 * 60) % (1000 * 60) / 1000
        time = if (hrs < 1) {
            String.format("%02d:%02d", min, secs)
        } else {
            String.format("%1d:%02d:%02d", hrs, min, secs)
        }
        return time
    }


}