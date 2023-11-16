package com.example.videoplayerkotlin.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.videoplayerkotlin.activity.MainActivity
import com.example.videoplayerkotlin.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val REQUEST_CODE_PERMISSION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
            ,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        initData()
        initView()
        initListener()

    }

    private fun initData() {
        permission()
    }
    private fun initView() {}
    private fun initListener() {}

    private fun nextActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun permission(){
        if (ContextCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_CODE_PERMISSION)
        }else{
            nextActivity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"permission granted", Toast.LENGTH_SHORT).show()
                nextActivity()
            } else{
                Toast.makeText(this,"denied", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_CODE_PERMISSION)
            }
        }
    }

}