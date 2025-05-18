package com.medium.music.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.medium.music.R
import com.medium.music.adapter.ContactAdapter
import com.medium.music.constant.AboutUsConfig
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.FragmentContactBinding
import com.medium.music.model.Contact

class ContactFragment : Fragment() {

    private var mFragmentContactBinding: FragmentContactBinding? = null
    private var mContactAdapter: ContactAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentContactBinding = FragmentContactBinding.inflate(inflater, container, false)
        initUi()

        return mFragmentContactBinding?.root
    }

    private fun initUi() {
        mFragmentContactBinding?.tvAboutUsTitle?.text = AboutUsConfig.ABOUT_US_TITLE
        mFragmentContactBinding?.tvAboutUsContent?.text = AboutUsConfig.ABOUT_US_CONTENT

        mContactAdapter = ContactAdapter(activity, getListContact(), object : ContactAdapter.ICallPhone {
            override fun onClickCallPhone() {
                GlobalFunction.callPhoneNumber(activity)
            }
        })
        val layoutManager = GridLayoutManager(activity, 3)
        mFragmentContactBinding?.rcvData?.isNestedScrollingEnabled = false
        mFragmentContactBinding?.rcvData?.isFocusable = false
        mFragmentContactBinding?.rcvData?.layoutManager = layoutManager
        mFragmentContactBinding?.rcvData?.adapter = mContactAdapter
    }



    private fun getListContact(): List<Contact> {
        val contactArrayList: MutableList<Contact> = ArrayList()
        contactArrayList.add(Contact(Contact.FACEBOOK, R.drawable.ic_facebook))
        contactArrayList.add(Contact(Contact.HOTLINE, R.drawable.ic_hotline))
        contactArrayList.add(Contact(Contact.GMAIL, R.drawable.ic_gmail))
        contactArrayList.add(Contact(Contact.SKYPE, R.drawable.ic_skype))
        contactArrayList.add(Contact(Contact.YOUTUBE, R.drawable.ic_youtube))
        contactArrayList.add(Contact(Contact.ZALO, R.drawable.ic_zalo))
        return contactArrayList
    }

    override fun onDestroy() {
        super.onDestroy()
        mContactAdapter?.release()
    }
}