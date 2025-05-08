package com.medium.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medium.music.adapter.BannerSongAdapter.BannerSongViewHolder
import com.medium.music.databinding.ItemBannerSongBinding
import com.medium.music.listener.IOnClickSongItemListener
import com.medium.music.model.Song
import com.medium.music.utils.GlideUtils

class BannerSongAdapter(
    private val mListSongs: List<Song>?,
    val iOnClickSongItemListener: IOnClickSongItemListener
) : RecyclerView.Adapter<BannerSongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerSongViewHolder {
        val itemBannerSongBinding =
            ItemBannerSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerSongViewHolder(itemBannerSongBinding)
    }

    override fun onBindViewHolder(holder: BannerSongViewHolder, position: Int) {
        val song = mListSongs!![position]
        GlideUtils.loadUrlBanner(song.image, holder.mItemBannerSongBinding.imageBanner)
        holder.mItemBannerSongBinding.layoutItem.setOnClickListener {
            iOnClickSongItemListener.onClickItemSong(
                song
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSongs?.size ?: 0
    }

    class BannerSongViewHolder(val mItemBannerSongBinding: ItemBannerSongBinding) :
        RecyclerView.ViewHolder(
            mItemBannerSongBinding.root
        )
}