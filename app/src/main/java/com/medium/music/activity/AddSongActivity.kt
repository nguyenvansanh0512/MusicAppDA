package com.medium.music.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.medium.music.MyApplication
import com.medium.music.R
import com.medium.music.constant.Constant
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.ActivityAddSongBinding
import com.medium.music.model.Song
import com.medium.music.utils.StringUtil

class AddSongActivity : BaseActivity() {

    private var mActivityAddSongBinding: ActivityAddSongBinding? = null
    private var isUpdate = false
    private var mSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityAddSongBinding = ActivityAddSongBinding.inflate(
            layoutInflater
        )
        setContentView(mActivityAddSongBinding!!.root)
        getDataIntent()
        initToolbar()
        initView()
        mActivityAddSongBinding!!.btnAddOrEdit.setOnClickListener { addOrEditFood() }
    }

    private fun getDataIntent() {
        val bundleReceived = intent.extras
        if (bundleReceived != null) {
            isUpdate = true
            mSong = bundleReceived[Constant.KEY_INTENT_SONG_OBJECT] as Song?
        }
    }

    private fun initToolbar() {
        mActivityAddSongBinding!!.toolbar.imgLeft.setImageResource(R.drawable.ic_back_white)
        mActivityAddSongBinding!!.toolbar.tvTitle.setText(R.string.label_add_song)
        mActivityAddSongBinding!!.toolbar.layoutPlayAll.visibility = View.GONE
        mActivityAddSongBinding!!.toolbar.imgLeft.setOnClickListener { onBackPressed() }
    }

    private fun initView() {
        if (isUpdate) {
            mActivityAddSongBinding!!.toolbar.tvTitle.text = getString(R.string.label_update_song)
            mActivityAddSongBinding!!.btnAddOrEdit.text = getString(R.string.action_edit)
            mActivityAddSongBinding!!.edtName.setText(mSong?.title)
            mActivityAddSongBinding!!.edtArtist.setText(mSong?.artist)
            mActivityAddSongBinding!!.edtImage.setText(mSong?.image)
            mActivityAddSongBinding!!.edtLink.setText(mSong?.url)
            mActivityAddSongBinding!!.chbFeatured.isChecked = mSong!!.isFeatured
            mActivityAddSongBinding!!.chbLatest.isChecked = mSong!!.isLatest
        } else {
            mActivityAddSongBinding!!.toolbar.tvTitle.text = getString(R.string.label_add_song)
            mActivityAddSongBinding!!.btnAddOrEdit.text = getString(R.string.action_add)
        }
    }

    private fun addOrEditFood() {
        val strName = mActivityAddSongBinding!!.edtName.text.toString().trim { it <= ' ' }
        val strArtist = mActivityAddSongBinding!!.edtArtist.text.toString().trim { it <= ' ' }
        val strImage = mActivityAddSongBinding!!.edtImage.text.toString().trim { it <= ' ' }
        val strLink = mActivityAddSongBinding!!.edtLink.text.toString().trim { it <= ' ' }
        val isFeatured = mActivityAddSongBinding!!.chbFeatured.isChecked
        val isLatest = mActivityAddSongBinding!!.chbLatest.isChecked
        if (StringUtil.isEmpty(strName)) {
            Toast.makeText(this, getString(R.string.msg_name_song_require), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (StringUtil.isEmpty(strArtist)) {
            Toast.makeText(this, getString(R.string.msg_artist_song_require), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (StringUtil.isEmpty(strImage)) {
            Toast.makeText(this, getString(R.string.msg_image_song_require), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (StringUtil.isEmpty(strLink)) {
            Toast.makeText(this, getString(R.string.msg_link_song_require), Toast.LENGTH_SHORT)
                .show()
            return
        }

        // Update song
        if (isUpdate) {
            showProgressDialog(true)
            val map: MutableMap<String, Any> = HashMap()
            map["title"] = strName
            map["artist"] = strArtist
            map["image"] = strImage
            map["url"] = strLink
            map["featured"] = isFeatured
            map["latest"] = isLatest
            MyApplication[this].getSongsDatabaseReference()
                ?.child(mSong?.id.toString())?.updateChildren(
                    map
                ) { _: DatabaseError?, _: DatabaseReference? ->
                    showProgressDialog(false)
                    Toast.makeText(
                        this@AddSongActivity,
                        getString(R.string.msg_edit_song_success), Toast.LENGTH_SHORT
                    ).show()
                    GlobalFunction.hideSoftKeyboard(this)
                }
            return
        }

        // Add song
        showProgressDialog(true)
        val songId = System.currentTimeMillis()
        val song = Song(songId, strName, strArtist, strImage, strLink, isFeatured, isLatest)
        MyApplication[this].getSongsDatabaseReference()
            ?.child(songId.toString())?.setValue(
                song
            ) { _: DatabaseError?, _: DatabaseReference? ->
                showProgressDialog(false)
                mActivityAddSongBinding!!.edtName.setText("")
                mActivityAddSongBinding!!.edtArtist.setText("")
                mActivityAddSongBinding!!.edtImage.setText("")
                mActivityAddSongBinding!!.edtLink.setText("")
                mActivityAddSongBinding!!.chbFeatured.isChecked = false
                mActivityAddSongBinding!!.chbLatest.isChecked = false
                GlobalFunction.hideSoftKeyboard(this)
                Toast.makeText(
                    this,
                    getString(R.string.msg_add_song_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}