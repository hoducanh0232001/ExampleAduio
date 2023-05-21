package com.example.mymusic

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService: Service() {


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    private var mediaPlayer: MediaPlayer?= null
    private var mediaPlayerUrl: MediaPlayer?= null
    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "$this::onCreate")
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
       // return super.onStartCommand(intent, flags, startId)
        Log.e(TAG, "$this::onStartCommand: intent= $intent, flags= $flags, startId: $startId")

        if (intent?.getBooleanExtra("STOP_AUDIO",false) == true){
            stopSelf()
            stopMedia()
            stopForegroundInternal()
        }
        when(intent?.getStringExtra(ACTION)){
            "START"-> {
                startForeGroundInternal()
                playMedia()
                Log.e(TAG, "START: " )
            }
            "STARTURL"-> {
                startForeGroundInternal()
                playMediaUrl()
            }
            "STOP"->{
                stopMedia()
                stopForegroundInternal()
            }
            "STOPURL"->{
            stopMediaUrl()
            stopForegroundInternal()
        }
        }

        startForeGroundInternal()
        return START_NOT_STICKY
    }
    private fun stopMedia(){
        mediaPlayer?.run {
            stop()
            release()
        }
        mediaPlayer = null
    }
    private fun playMediaUrl(){
        val url = "https://www.youtube.com/watch?v=AvDiAlQOsGE"
        mediaPlayer.apply {
            mediaPlayerUrl?.setDataSource(url)
            mediaPlayerUrl?.prepareAsync()
            mediaPlayerUrl?.setOnPreparedListener {
                mediaPlayerUrl?.start()
            }
        }
    }
    private fun stopMediaUrl(){
        mediaPlayerUrl?.run {
            stop()
            release()
        }
        mediaPlayer = null
    }

    private fun playMedia() {
        mediaPlayer?.run {
            if (!isPlaying){
                seekTo(0)
                start()
            }
            return
        }
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.aloha,
        ).apply {
            isLooping = true
            setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
        }
    }
    private fun startForeGroundInternal(){
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this,getString(R.string.my_channel_id))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("DucAnh Day")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notification = builder.build()
        startForeground(
            NOTI_ID,
            notification
        )

    }
    private fun stopForegroundInternal(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
    private fun createNotificationChannel(){
        val name: String = getString(R.string.my_channel)
        val descriptionText: String = getString(R.string.my_channel_descripton)
        val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            getString(R.string.my_channel_id),
        name,
        importance).apply {
            description = descriptionText
        }
        //register the channel with the system
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "$this::onDestroy: " )
    }
     companion object{
        private const val TAG = "MyService"
        private const val NOTI_ID = 1
        const val ACTION = "action"
    }
}