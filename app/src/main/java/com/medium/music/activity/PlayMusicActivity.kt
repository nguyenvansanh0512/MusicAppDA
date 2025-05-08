package com.medium.music.activity

import android.os.Bundle
import android.view.View
import com.medium.music.R
import com.medium.music.adapter.MusicViewPagerAdapter
import com.medium.music.databinding.ActivityPlayMusicBinding

class PlayMusicActivity : BaseActivity() {

    private var mActivityPlayMusicBinding: ActivityPlayMusicBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityPlayMusicBinding = ActivityPlayMusicBinding.inflate(
            layoutInflater
        )
        setContentView(mActivityPlayMusicBinding!!.root)
        initToolbar()
        initUI()
    }

    private fun initToolbar() {
        mActivityPlayMusicBinding!!.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white)
        mActivityPlayMusicBinding!!.toolbar.tvTitle.setText(R.string.music_player)
        mActivityPlayMusicBinding!!.toolbar.layoutPlayAll.visibility = View.GONE
        mActivityPlayMusicBinding!!.toolbar.imgLeft.setOnClickListener { onBackPressed() }
    }

    private fun initUI() {
        val musicViewPagerAdapter = MusicViewPagerAdapter(this)
        mActivityPlayMusicBinding!!.viewpager2.adapter = musicViewPagerAdapter
        mActivityPlayMusicBinding!!.indicator3.setViewPager(mActivityPlayMusicBinding!!.viewpager2)
        mActivityPlayMusicBinding!!.viewpager2.currentItem = 1
    }
}