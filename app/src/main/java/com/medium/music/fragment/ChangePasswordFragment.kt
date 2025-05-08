package com.medium.music.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.medium.music.R
import com.medium.music.activity.MainActivity
import com.medium.music.databinding.FragmentChangePasswordBinding
import com.medium.music.model.User
import com.medium.music.prefs.DataStoreManager
import com.medium.music.utils.StringUtil

class ChangePasswordFragment : Fragment() {

    private var mFragmentChangePasswordBinding: FragmentChangePasswordBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFragmentChangePasswordBinding = FragmentChangePasswordBinding.inflate(
            inflater,
            container, false
        )
        initListener()
        return mFragmentChangePasswordBinding?.root
    }

    private fun initListener() {
        mFragmentChangePasswordBinding?.btnChangePassword
            ?.setOnClickListener { onClickValidateChangePassword() }
    }

    private fun onClickValidateChangePassword() {
        if (activity == null) return
        val strOldPassword =
            mFragmentChangePasswordBinding?.edtOldPassword?.text.toString().trim { it <= ' ' }
        val strNewPassword =
            mFragmentChangePasswordBinding?.edtNewPassword?.text.toString().trim { it <= ' ' }
        val strConfirmPassword =
            mFragmentChangePasswordBinding?.edtConfirmPassword?.text.toString().trim { it <= ' ' }
        if (StringUtil.isEmpty(strOldPassword)) {
            Toast.makeText(
                activity,
                getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT
            ).show()
        } else if (StringUtil.isEmpty(strNewPassword)) {
            Toast.makeText(
                activity,
                getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT
            ).show()
        } else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(
                activity,
                getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT
            ).show()
        } else if (DataStoreManager.user?.password != strOldPassword) {
            Toast.makeText(
                activity,
                getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT
            ).show()
        } else if (strNewPassword != strConfirmPassword) {
            Toast.makeText(
                activity,
                getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT
            ).show()
        } else if (strOldPassword == strNewPassword) {
            Toast.makeText(
                activity,
                getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT
            ).show()
        } else {
            changePassword(strNewPassword)
        }
    }

    private fun changePassword(newPassword: String) {
        if (activity == null) return
        val mainActivity = activity as MainActivity?
        mainActivity!!.showProgressDialog(true)
        val user = FirebaseAuth.getInstance().currentUser ?: return
        user.updatePassword(newPassword)
            .addOnCompleteListener { task: Task<Void?> ->
                mainActivity.showProgressDialog(false)
                if (task.isSuccessful) {
                    Toast.makeText(
                        mainActivity,
                        getString(R.string.msg_change_password_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    val userLogin: User? = DataStoreManager.user
                    userLogin?.password = newPassword
                    DataStoreManager.user = userLogin
                    mFragmentChangePasswordBinding?.edtOldPassword?.setText("")
                    mFragmentChangePasswordBinding?.edtNewPassword?.setText("")
                    mFragmentChangePasswordBinding?.edtConfirmPassword?.setText("")
                }
            }
    }
}