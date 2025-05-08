package com.medium.music.activity

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.medium.music.R
import com.medium.music.constant.Constant
import com.medium.music.constant.GlobalFunction
import com.medium.music.databinding.ActivitySignUpBinding
import com.medium.music.model.User
import com.medium.music.prefs.DataStoreManager
import com.medium.music.utils.StringUtil

class SignUpActivity : BaseActivity() {

    private var mActivitySignUpBinding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivitySignUpBinding = ActivitySignUpBinding.inflate(
            layoutInflater
        )
        setContentView(mActivitySignUpBinding!!.root)
        mActivitySignUpBinding!!.rdbUser.isChecked = true
        mActivitySignUpBinding!!.imgBack.setOnClickListener { onBackPressed() }
        mActivitySignUpBinding!!.layoutSignIn.setOnClickListener { finish() }
        mActivitySignUpBinding!!.btnSignUp.setOnClickListener { onClickValidateSignUp() }
    }

    private fun onClickValidateSignUp() {
        val strEmail = mActivitySignUpBinding!!.edtEmail.text.toString().trim { it <= ' ' }
        val strPassword = mActivitySignUpBinding!!.edtPassword.text.toString().trim { it <= ' ' }
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.msg_email_require),
                Toast.LENGTH_SHORT
            ).show()
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.msg_password_require),
                Toast.LENGTH_SHORT
            ).show()
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(
                this@SignUpActivity,
                getString(R.string.msg_email_invalid),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (mActivitySignUpBinding!!.rdbAdmin.isChecked) {
                if (!strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                    Toast.makeText(
                        this@SignUpActivity,
                        getString(R.string.msg_email_invalid_admin),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    signUpUser(strEmail, strPassword)
                }
                return
            }
            if (strEmail.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.msg_email_invalid_user),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                signUpUser(strEmail, strPassword)
            }
        }
    }

    private fun signUpUser(email: String, password: String) {
        showProgressDialog(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task: Task<AuthResult?> ->
                showProgressDialog(false)
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val userObject = User(user.email, password)
                        if (user.email != null && user.email!!.contains(Constant.ADMIN_EMAIL_FORMAT)) {
                            userObject.isAdmin = true
                        }
                        DataStoreManager.user = userObject
                        GlobalFunction.startActivity(this@SignUpActivity, MainActivity::class.java)
                        finishAffinity()
                    }
                } else {
                    Toast.makeText(
                        this@SignUpActivity, getString(R.string.msg_sign_up_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}