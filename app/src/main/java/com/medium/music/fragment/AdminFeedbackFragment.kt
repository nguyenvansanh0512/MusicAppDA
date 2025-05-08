package com.medium.music.fragment

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
import com.medium.music.adapter.AdminFeedbackAdapter
import com.medium.music.databinding.FragmentAdminFeedbackBinding
import com.medium.music.model.Feedback

class AdminFeedbackFragment : Fragment() {

    private var mFragmentAdminFeedbackBinding: FragmentAdminFeedbackBinding? = null
    private var mListFeedback: MutableList<Feedback>? = null
    private var mFeedbackAdapter: AdminFeedbackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentAdminFeedbackBinding =
            FragmentAdminFeedbackBinding.inflate(inflater, container, false)
        initView()
        getListFeedback()
        return mFragmentAdminFeedbackBinding?.root
    }

    private fun initView() {
        if (activity == null) return
        val linearLayoutManager = LinearLayoutManager(activity)
        mFragmentAdminFeedbackBinding?.rcvFeedback?.layoutManager = linearLayoutManager
    }

    private fun getListFeedback() {
        if (activity == null) return
        MyApplication[activity].getFeedbackDatabaseReference()
            ?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (mListFeedback != null) {
                        mListFeedback!!.clear()
                    } else {
                        mListFeedback = ArrayList()
                    }
                    for (dataSnapshot in snapshot.children) {
                        val feedback = dataSnapshot.getValue(Feedback::class.java)
                        if (feedback != null) {
                            mListFeedback!!.add(0, feedback)
                        }
                    }
                    mFeedbackAdapter = AdminFeedbackAdapter(mListFeedback)
                    mFragmentAdminFeedbackBinding?.rcvFeedback?.adapter = mFeedbackAdapter
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}