package com.medium.music.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.medium.music.MyApplication
import com.medium.music.R
import com.medium.music.activity.MainActivity
import com.medium.music.activity.PlayMusicActivity
import com.medium.music.adapter.SongAdapter
import com.medium.music.constant.Constant
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.FragmentAllSongsBinding
import com.medium.music.listener.IOnClickSongItemListener
import com.medium.music.model.Song
import com.medium.music.service.MusicService

class AllSongsFragment : Fragment() {

    private var mFragmentAllSongsBinding: FragmentAllSongsBinding? = null
    private var mListSong: MutableList<Song>? = null
    private var mSongAdapter: SongAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentAllSongsBinding = FragmentAllSongsBinding.inflate(inflater, container, false)
        initUi()
        initListener()
        getListAllSongs()
        return mFragmentAllSongsBinding?.root
    }

    private fun initUi() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mFragmentAllSongsBinding?.rcvData?.layoutManager = linearLayoutManager
        mListSong = ArrayList()
        mSongAdapter = SongAdapter(mListSong, object : IOnClickSongItemListener {
            override fun onClickItemSong(song: Song) {
                goToSongDetail(song)
            }

            override fun onClickFavoriteSong(song: Song, favorite: Boolean) {
                GlobalFunction.onClickFavoriteSong(activity, song, favorite)
            }
        })
        mFragmentAllSongsBinding?.rcvData?.adapter = mSongAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListAllSongs() {
        if (activity == null) return
        MyApplication[activity].getSongsDatabaseReference()
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    resetListData()
                    for (dataSnapshot in snapshot.children) {
                        val song = dataSnapshot.getValue(Song::class.java) ?: return
                        mListSong!!.add(0, song)
                    }
                    if (mSongAdapter != null) mSongAdapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    GlobalFunction.showToastMessage(
                        activity,
                        getString(R.string.msg_get_date_error)
                    )
                }
            })
    }

    private fun resetListData() {
        if (mListSong == null) {
            mListSong = ArrayList()
        } else {
            mListSong!!.clear()
        }
    }

    private fun goToSongDetail(song: Song) {
        MusicService.clearListSongPlaying()
        MusicService.mListSongPlaying!!.add(song)
        MusicService.isPlaying = false
        GlobalFunction.startMusicService(activity, Constant.PLAY, 0)
        GlobalFunction.startActivity(activity, PlayMusicActivity::class.java)
    }

    private fun initListener() {
        val activity = activity as MainActivity?
        if (activity?.activityMainBinding == null) {
            return
        }
        activity.activityMainBinding!!.header.layoutPlayAll.setOnClickListener {
            if (mListSong == null || mListSong!!.isEmpty()) return@setOnClickListener
            MusicService.clearListSongPlaying()
            MusicService.mListSongPlaying!!.addAll(mListSong!!)
            MusicService.isPlaying = false
            GlobalFunction.startMusicService(getActivity(), Constant.PLAY, 0)
            GlobalFunction.startActivity(getActivity(), PlayMusicActivity::class.java)
        }
    }
}