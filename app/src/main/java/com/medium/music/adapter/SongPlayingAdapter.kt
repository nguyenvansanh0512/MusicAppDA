package com.medium.music.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medium.music.R
import com.medium.music.adapter.SongPlayingAdapter.SongPlayingViewHolder
import com.medium.music.databinding.ItemSongPlayingBinding
import com.medium.music.listener.IOnClickSongPlayingItemListener
import com.medium.music.model.Song
import com.medium.music.utils.GlideUtils

class SongPlayingAdapter(
    private val mListSongs: List<Song>?,
    private val iOnClickSongPlayingItemListener: IOnClickSongPlayingItemListener
) : RecyclerView.Adapter<SongPlayingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongPlayingViewHolder {
        val itemSongPlayingBinding =
            ItemSongPlayingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongPlayingViewHolder(itemSongPlayingBinding)
    }

    override fun onBindViewHolder(holder: SongPlayingViewHolder, position: Int) {
        val song = mListSongs!![position]
        if (song.isPlaying) {
            holder.mItemSongPlayingBinding.layoutItem.setBackgroundResource(R.color.background_bottom)
            holder.mItemSongPlayingBinding.imgPlaying.visibility = View.VISIBLE
        } else {
            holder.mItemSongPlayingBinding.layoutItem.setBackgroundResource(R.color.white)
            holder.mItemSongPlayingBinding.imgPlaying.visibility = View.GONE
        }
        GlideUtils.loadUrl(song.image, holder.mItemSongPlayingBinding.imgSong)
        holder.mItemSongPlayingBinding.tvSongName.text = song.title
        holder.mItemSongPlayingBinding.tvArtist.text = song.artist
        holder.mItemSongPlayingBinding.layoutItem.setOnClickListener {
            iOnClickSongPlayingItemListener.onClickItemSongPlaying(
                holder.adapterPosition
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSongs?.size ?: 0
    }

    class SongPlayingViewHolder(val mItemSongPlayingBinding: ItemSongPlayingBinding) :
        RecyclerView.ViewHolder(
            mItemSongPlayingBinding.root
        )
}