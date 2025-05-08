package com.medium.music.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.medium.music.constant.Constant
import com.medium.music.constant.GlobalFunction

class MusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.extras!!.getInt(Constant.MUSIC_ACTION)
        GlobalFunction.startMusicService(context, action, MusicService.mSongPosition)
    }
}