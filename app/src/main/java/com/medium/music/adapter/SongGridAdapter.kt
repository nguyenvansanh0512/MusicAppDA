package com.medium.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medium.music.R
import com.medium.music.adapter.SongGridAdapter.SongGridViewHolder
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.ItemSongGridBinding
import com.medium.music.listener.IOnClickSongItemListener
import com.medium.music.model.Song
import com.medium.music.utils.GlideUtils

class SongGridAdapter(
    private val mListSongs: List<Song>?,
    val iOnClickSongItemListener: IOnClickSongItemListener
) : RecyclerView.Adapter<SongGridViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongGridViewHolder {
        val itemSongGridBinding =
            ItemSongGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongGridViewHolder(itemSongGridBinding)
    }

    override fun onBindViewHolder(holder: SongGridViewHolder, position: Int) {
        val song = mListSongs!![position]
        GlideUtils.loadUrl(song.image, holder.mItemSongGridBinding.imgSong)
        holder.mItemSongGridBinding.tvSongName.text = song.title
        holder.mItemSongGridBinding.tvArtist.text = song.artist
        val strListen = if (song.count > 1) "listens" else "listen"
        val strCountListen = song.count.toString() + " " + strListen
        holder.mItemSongGridBinding.tvCountListen.text = strCountListen
        val isFavorite = GlobalFunction.isFavoriteSong(song)
        if (isFavorite) {
            holder.mItemSongGridBinding.imgFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.mItemSongGridBinding.imgFavorite.setImageResource(R.drawable.ic_unfavorite)
        }
        holder.mItemSongGridBinding.layoutItem.setOnClickListener {
            iOnClickSongItemListener.onClickItemSong(
                song
            )
        }
        holder.mItemSongGridBinding.imgFavorite.setOnClickListener {
            iOnClickSongItemListener.onClickFavoriteSong(
                song,
                !isFavorite
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSongs?.size ?: 0
    }

    class SongGridViewHolder(val mItemSongGridBinding: ItemSongGridBinding) :
        RecyclerView.ViewHolder(
            mItemSongGridBinding.root
        )
}