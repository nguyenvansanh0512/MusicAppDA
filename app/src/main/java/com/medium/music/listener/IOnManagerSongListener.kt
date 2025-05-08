package com.medium.music.listener

import com.medium.music.model.Song

interface IOnManagerSongListener {
    fun onClickUpdateSong(song: Song)
    fun onClickDeleteSong(song: Song)
}