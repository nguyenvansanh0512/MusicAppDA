package com.medium.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.medium.music.MyApplication
import com.medium.music.R
import com.medium.music.activity.MainActivity
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.FragmentFeedbackBinding
import com.medium.music.model.Feedback
import com.medium.music.prefs.DataStoreManager
import com.medium.music.utils.StringUtil

class FeedbackFragment : Fragment() {

    private var mFragmentFeedbackBinding: FragmentFeedbackBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentFeedbackBinding = FragmentFeedbackBinding.inflate(inflater, container, false)
        mFragmentFeedbackBinding?.edtEmail?.setText(DataStoreManager.user?.email)
        mFragmentFeedbackBinding?.tvSendFeedback?.setOnClickListener { onClickSendFeedback() }
        return mFragmentFeedbackBinding?.root
    }

    private fun onClickSendFeedback() {
        if (activity == null) {
            return
        }
        val activity = activity as MainActivity?
        val strName = mFragmentFeedbackBinding?.edtName?.text.toString()
        val strPhone = mFragmentFeedbackBinding?.edtPhone?.text.toString()
        val strEmail = mFragmentFeedbackBinding?.edtEmail?.text.toString()
        val strComment = mFragmentFeedbackBinding?.edtComment?.text.toString()
        if (StringUtil.isEmpty(strName)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.name_require))
        } else if (StringUtil.isEmpty(strComment)) {
            GlobalFunction.showToastMessage(activity, getString(R.string.comment_require))
        } else {
            activity!!.showProgressDialog(true)
            val feedback = Feedback(strName, strPhone, strEmail, strComment)
            MyApplication[getActivity()].getFeedbackDatabaseReference()
                ?.child(System.currentTimeMillis().toString())
                ?.setValue(
                    feedback
                ) { _: DatabaseError?, _: DatabaseReference? ->
                    activity.showProgressDialog(false)
                    sendFeedbackSuccess()
                }
        }
    }

    private fun sendFeedbackSuccess() {
        GlobalFunction.hideSoftKeyboard(activity)
        GlobalFunction.showToastMessage(activity, getString(R.string.msg_send_feedback_success))
        mFragmentFeedbackBinding?.edtName?.setText("")
        mFragmentFeedbackBinding?.edtPhone?.setText("")
        mFragmentFeedbackBinding?.edtComment?.setText("")
    }
}