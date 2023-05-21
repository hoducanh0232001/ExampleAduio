package com.example.mymusic

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.mymusic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var broadCastInternet: BroadCastInternet? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        broadCastInternet = BroadCastInternet()

        binding.btnPlay.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                Intent(this, MyService::class.java).apply {
                    putExtra(MyService.ACTION,"START")
                }
            )
        }
        binding.btnPlayUrl.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                Intent(this, MyService::class.java).apply {
                    putExtra(MyService.ACTION,"STARTURL")
                }
            )
        }
        @Suppress("DEPRECATION")
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(broadCastInternet, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        ContextCompat.startForegroundService(
            this,
            Intent(this, MyService::class.java).apply {
                putExtra(MyService.ACTION,"STOP")
            }
        )
        unregisterReceiver(broadCastInternet)
    }
}