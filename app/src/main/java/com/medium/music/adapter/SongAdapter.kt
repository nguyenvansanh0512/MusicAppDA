package com.medium.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medium.music.R
import com.medium.music.adapter.SongAdapter.SongViewHolder
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.ItemSongBinding
import com.medium.music.listener.IOnClickSongItemListener
import com.medium.music.model.Song
import com.medium.music.utils.GlideUtils

class SongAdapter(
    private val mListSongs: List<Song>?,
    val iOnClickSongItemListener: IOnClickSongItemListener
) : RecyclerView.Adapter<SongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemSongBinding =
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(itemSongBinding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = mListSongs!![position]
        GlideUtils.loadUrl(song.image, holder.mItemSongBinding.imgSong)
        holder.mItemSongBinding.tvSongName.text = song.title
        holder.mItemSongBinding.tvArtist.text = song.artist
        val strListen = if (song.count > 1) "listens" else "listen"
        val strCountListen = song.count.toString() + " " + strListen
        holder.mItemSongBinding.tvCountListen.text = strCountListen
        val isFavorite = GlobalFunction.isFavoriteSong(song)
        if (isFavorite) {
            holder.mItemSongBinding.imgFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.mItemSongBinding.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
        holder.mItemSongBinding.layoutItem.setOnClickListener {
            iOnClickSongItemListener.onClickItemSong(
                song
            )
        }
        holder.mItemSongBinding.imgFavorite.setOnClickListener {
            iOnClickSongItemListener.onClickFavoriteSong(
                song,
                !isFavorite
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSongs?.size ?: 0
    }

    class SongViewHolder(val mItemSongBinding: ItemSongBinding) : RecyclerView.ViewHolder(
        mItemSongBinding.root
    )
}