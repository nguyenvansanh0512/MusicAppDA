package com.medium.music.listener

import com.medium.music.model.Song

interface IOnClickSongItemListener {
    fun onClickItemSong(song: Song)
    fun onClickFavoriteSong(song: Song, favorite: Boolean)
}