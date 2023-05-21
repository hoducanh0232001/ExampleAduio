package com.example.mymusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import java.util.logging.Handler

@Suppress("DEPRECATION")
class BroadCastInternet : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val connectManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectManager.activeNetworkInfo
        if (networkInfo?.isConnected == true){
            Toast.makeText(context,"Internet Connected",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Internet Disconnect",Toast.LENGTH_SHORT).show()
            Thread.sleep(3_000)
            Toast.makeText(context,"Audio Close",Toast.LENGTH_SHORT).show()
            stopMyService(context)

        }
    }
    private fun stopMyService(context: Context?){
        val serviceIntent = Intent(context, MyService::class.java)
        serviceIntent.putExtra("STOP_AUDIO", true)
        context?.stopService(serviceIntent)
        context?.startService(serviceIntent)
    }

}