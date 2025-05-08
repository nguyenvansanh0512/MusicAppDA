package com.medium.music.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.auth.FirebaseAuth
import com.medium.music.R
import com.medium.music.constant.Constant
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.ActivityMainBinding
import com.medium.music.fragment.*
import com.medium.music.model.Song
import com.medium.music.model.User
import com.medium.music.prefs.DataStoreManager
import com.medium.music.service.MusicService
import com.medium.music.utils.GlideUtils

@SuppressLint("NonConstantResourceId")
class MainActivity : BaseActivity(), View.OnClickListener {

    private var mTypeScreen = TYPE_HOME
    var activityMainBinding: ActivityMainBinding? = null
        private set
    private var mAction = 0
    private val mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mAction = intent.getIntExtra(Constant.MUSIC_ACTION, 0)
            handleMusicAction()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(activityMainBinding!!.root)
        checkNotificationPermission()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mBroadcastReceiver,
            IntentFilter(Constant.CHANGE_LISTENER)
        )
        initUi()
        openHomeScreen()
        initListener()
        displayLayoutBottom()
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }

    private fun initUi() {
        if (DataStoreManager.user?.isAdmin == true) {
            activityMainBinding!!.menuLeft.layoutListSong.visibility = View.GONE
        } else {
            activityMainBinding!!.menuLeft.layoutListSong.visibility = View.VISIBLE
        }
        displayUserInformation()
    }

    private fun displayUserInformation() {
        val user: User? = DataStoreManager.user
        activityMainBinding!!.menuLeft.tvUserEmail.text = user?.email
    }

    private fun initToolbar(title: String) {
        activityMainBinding!!.header.imgLeft.setImageResource(R.drawable.ic_menu_left)
        activityMainBinding!!.header.tvTitle.text = title
    }

    private fun initListener() {
        activityMainBinding!!.header.imgLeft.setOnClickListener(this)
        activityMainBinding!!.header.layoutPlayAll.setOnClickListener(this)
        activityMainBinding!!.menuLeft.layoutClose.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuHome.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuAllSongs.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuFeaturedSongs.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuPopularSongs.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuNewSongs.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuFavoriteSongs.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuFeedback.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuContact.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuChangePassword.setOnClickListener(this)
        activityMainBinding!!.menuLeft.tvMenuSignOut.setOnClickListener(this)
        activityMainBinding!!.layoutBottom.imgPrevious.setOnClickListener(this)
        activityMainBinding!!.layoutBottom.imgPlay.setOnClickListener(this)
        activityMainBinding!!.layoutBottom.imgNext.setOnClickListener(this)
        activityMainBinding!!.layoutBottom.imgClose.setOnClickListener(this)
        activityMainBinding!!.layoutBottom.layoutText.setOnClickListener(this)
        activityMainBinding!!.layoutBottom.imgSong.setOnClickListener(this)
    }

    private fun openHomeScreen() {
        if (DataStoreManager.user?.isAdmin == true) {
            replaceFragment(AdminHomeFragment())
        } else {
            replaceFragment(HomeFragment())
        }
        mTypeScreen = TYPE_HOME
        initToolbar(getString(R.string.app_name))
        displayLayoutPlayAll()
    }

    fun openPopularSongsScreen() {
        replaceFragment(PopularSongsFragment())
        mTypeScreen = TYPE_POPULAR_SONGS
        initToolbar(getString(R.string.menu_popular_songs))
        displayLayoutPlayAll()
    }

    fun openNewSongsScreen() {
        replaceFragment(NewSongsFragment())
        mTypeScreen = TYPE_NEW_SONGS
        initToolbar(getString(R.string.menu_new_songs))
        displayLayoutPlayAll()
    }

    private fun openFavoriteSongsScreen() {
        replaceFragment(FavoriteFragment())
        mTypeScreen = TYPE_FAVORITE_SONGS
        initToolbar(getString(R.string.menu_favorite_songs))
        displayLayoutPlayAll()
    }

    private fun openFeedbackScreen() {
        if (DataStoreManager.user?.isAdmin == true) {
            replaceFragment(AdminFeedbackFragment())
        } else {
            replaceFragment(FeedbackFragment())
        }
        mTypeScreen = TYPE_FEEDBACK
        initToolbar(getString(R.string.menu_feedback))
        displayLayoutPlayAll()
    }

    private fun openContactScreen() {
        replaceFragment(ContactFragment())
        mTypeScreen = TYPE_CONTACT
        initToolbar(getString(R.string.menu_contact))
        displayLayoutPlayAll()
    }

    private fun openChangePasswordScreen() {
        replaceFragment(ChangePasswordFragment())
        mTypeScreen = TYPE_CHANGE_PASSWORD
        initToolbar(getString(R.string.menu_change_password))
        displayLayoutPlayAll()
    }

    private fun onClickSignOut() {
        FirebaseAuth.getInstance().signOut()
        DataStoreManager.user = null
        // Stop service when user sign out
        clickOnCloseButton()
        GlobalFunction.startActivity(this, SignInActivity::class.java)
        finishAffinity()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.layout_close -> activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
            R.id.img_left -> activityMainBinding!!.drawerLayout.openDrawer(GravityCompat.START)
            R.id.tv_menu_home -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openHomeScreen()
            }
            R.id.tv_menu_all_songs -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                replaceFragment(AllSongsFragment())
                mTypeScreen = TYPE_ALL_SONGS
                initToolbar(getString(R.string.menu_all_songs))
                displayLayoutPlayAll()
            }
            R.id.tv_menu_featured_songs -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                replaceFragment(FeaturedSongsFragment())
                mTypeScreen = TYPE_FEATURED_SONGS
                initToolbar(getString(R.string.menu_featured_songs))
                displayLayoutPlayAll()
            }
            R.id.tv_menu_popular_songs -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openPopularSongsScreen()
            }
            R.id.tv_menu_new_songs -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openNewSongsScreen()
            }
            R.id.tv_menu_favorite_songs -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openFavoriteSongsScreen()
            }
            R.id.tv_menu_feedback -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openFeedbackScreen()
            }
            R.id.tv_menu_contact -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openContactScreen()
            }
            R.id.tv_menu_change_password -> {
                activityMainBinding!!.drawerLayout.closeDrawer(GravityCompat.START)
                openChangePasswordScreen()
            }
            R.id.tv_menu_sign_out -> onClickSignOut()
            R.id.img_previous -> clickOnPrevButton()
            R.id.img_play -> clickOnPlayButton()
            R.id.img_next -> clickOnNextButton()
            R.id.img_close -> clickOnCloseButton()
            R.id.layout_text, R.id.img_song -> openPlayMusicActivity()
        }
    }

    private fun replaceFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment!!).commitAllowingStateLoss()
    }

    private fun showConfirmExitApp() {
        MaterialDialog.Builder(this)
            .title(getString(R.string.app_name))
            .content(getString(R.string.msg_exit_app))
            .positiveText(getString(R.string.action_ok))
            .onPositive { _: MaterialDialog?, _: DialogAction? -> finish() }
            .negativeText(getString(R.string.action_cancel))
            .cancelable(false)
            .show()
    }

    private fun displayLayoutPlayAll() {
        when (mTypeScreen) {
            TYPE_ALL_SONGS, TYPE_FEATURED_SONGS, TYPE_POPULAR_SONGS, TYPE_NEW_SONGS, TYPE_FAVORITE_SONGS -> activityMainBinding!!.header.layoutPlayAll.visibility =
                View.VISIBLE
            else -> activityMainBinding!!.header.layoutPlayAll.visibility = View.GONE
        }
    }

    private fun displayLayoutBottom() {
        if (MusicService.mPlayer == null) {
            activityMainBinding!!.layoutBottom.layoutItem.visibility = View.GONE
            return
        }
        activityMainBinding!!.layoutBottom.layoutItem.visibility = View.VISIBLE
        showInforSong()
        showStatusButtonPlay()
    }

    private fun handleMusicAction() {
        if (Constant.CANNEL_NOTIFICATION == mAction) {
            activityMainBinding!!.layoutBottom.layoutItem.visibility = View.GONE
            return
        }
        activityMainBinding!!.layoutBottom.layoutItem.visibility = View.VISIBLE
        showInforSong()
        showStatusButtonPlay()
    }

    private fun showInforSong() {
        if (MusicService.mListSongPlaying == null || MusicService.mListSongPlaying!!.isEmpty()) {
            return
        }
        val currentSong: Song =
            MusicService.mListSongPlaying!![MusicService.mSongPosition]
        activityMainBinding!!.layoutBottom.tvSongName.text = currentSong.title
        activityMainBinding!!.layoutBottom.tvArtist.text = currentSong.artist
        GlideUtils.loadUrl(currentSong.image, activityMainBinding!!.layoutBottom.imgSong)
    }

    private fun showStatusButtonPlay() {
        if (MusicService.isPlaying) {
            activityMainBinding!!.layoutBottom.imgPlay.setImageResource(R.drawable.ic_pause_black)
        } else {
            activityMainBinding!!.layoutBottom.imgPlay.setImageResource(R.drawable.ic_play_black)
        }
    }

    private fun clickOnPrevButton() {
        GlobalFunction.startMusicService(
            this,
            Constant.PREVIOUS,
            MusicService.mSongPosition
        )
    }

    private fun clickOnNextButton() {
        GlobalFunction.startMusicService(this, Constant.NEXT, MusicService.mSongPosition)
    }

    private fun clickOnPlayButton() {
        if (MusicService.isPlaying) {
            GlobalFunction.startMusicService(
                this,
                Constant.PAUSE,
                MusicService.mSongPosition
            )
        } else {
            GlobalFunction.startMusicService(
                this,
                Constant.RESUME,
                MusicService.mSongPosition
            )
        }
    }

    private fun clickOnCloseButton() {
        GlobalFunction.startMusicService(
            this,
            Constant.CANNEL_NOTIFICATION,
            MusicService.mSongPosition
        )
    }

    private fun openPlayMusicActivity() {
        GlobalFunction.startActivity(this, PlayMusicActivity::class.java)
    }

    override fun onBackPressed() {
        showConfirmExitApp()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    companion object {
        const val TYPE_HOME = 1
        const val TYPE_ALL_SONGS = 2
        const val TYPE_FEATURED_SONGS = 3
        const val TYPE_POPULAR_SONGS = 4
        const val TYPE_NEW_SONGS = 5
        const val TYPE_FAVORITE_SONGS = 6
        const val TYPE_FEEDBACK = 7
        const val TYPE_CONTACT = 8
        const val TYPE_CHANGE_PASSWORD = 9
    }
}