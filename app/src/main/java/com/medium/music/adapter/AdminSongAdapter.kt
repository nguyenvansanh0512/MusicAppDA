package com.medium.music.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.medium.music.adapter.AdminSongAdapter.AdminSongViewHolder
import com.medium.music.databinding.ItemAdminSongBinding
import com.medium.music.listener.IOnManagerSongListener
import com.medium.music.model.Song
import com.medium.music.utils.GlideUtils

class AdminSongAdapter(
    private val mListSongs: List<Song>?,
    private val iOnManagerSongListener: IOnManagerSongListener
) : RecyclerView.Adapter<AdminSongViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminSongViewHolder {
        val itemAdminSongBinding = ItemAdminSongBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminSongViewHolder(itemAdminSongBinding)
    }

    override fun onBindViewHolder(holder: AdminSongViewHolder, position: Int) {
        val song = mListSongs!![position]
        GlideUtils.loadUrl(song.image, holder.mItemAdminSongBinding.imgSong)
        holder.mItemAdminSongBinding.tvName.text = song.title
        holder.mItemAdminSongBinding.tvArtist.text = song.artist
        if (song.isFeatured) {
            holder.mItemAdminSongBinding.tvFeatured.text = LABEL_YES
        } else {
            holder.mItemAdminSongBinding.tvFeatured.text = LABEL_NO
        }
        if (song.isLatest) {
            holder.mItemAdminSongBinding.tvLatest.text = LABEL_YES
        } else {
            holder.mItemAdminSongBinding.tvLatest.text = LABEL_NO
        }
        holder.mItemAdminSongBinding.imgEdit.setOnClickListener {
            iOnManagerSongListener.onClickUpdateSong(
                song
            )
        }
        holder.mItemAdminSongBinding.imgDelete.setOnClickListener {
            iOnManagerSongListener.onClickDeleteSong(
                song
            )
        }
    }

    override fun getItemCount(): Int {
        return mListSongs?.size ?: 0
    }

    class AdminSongViewHolder(val mItemAdminSongBinding: ItemAdminSongBinding) :
        RecyclerView.ViewHolder(
            mItemAdminSongBinding.root
        )

    companion object {
        const val LABEL_YES = "Yes"
        const val LABEL_NO = "No"
    }
}