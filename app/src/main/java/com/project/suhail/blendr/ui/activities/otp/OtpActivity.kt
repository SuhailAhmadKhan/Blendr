package com.project.suhail.blendr.ui.activities.otp

import android.content.Intent
import android.os.Bundle
import com.project.suhail.blendr.databinding.ActivityOtpBinding
import com.project.suhail.blendr.ui.activities.setup_profile.SetupProfileActivity
import com.project.suhail.blendr.utils.BaseActivity
import com.project.suhail.blendr.utils.Constants
import com.project.suhail.blendr.utils.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : BaseActivity() {

    private val mBinding by lazy { ActivityOtpBinding.inflate(layoutInflater) }
    private var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        showProgressDialog("Sending OTP...")

        supportActionBar?.hide()

        val phoneNumber = intent.getStringExtra(Constants.KEY_PHONE_NUMBER)

        mBinding.tvVerifyOtpTitle.text = "Verify $phoneNumber"

        val phoneAuthOptions = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

                override fun onVerificationFailed(p0: FirebaseException) {}

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    verificationId = p0
                    hideProgressDialog()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)

        mBinding.otpView.setOtpCompletionListener { otp ->
            val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otp)

            firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, SetupProfileActivity::class.java).also {
                            finishAffinity()
                        })
                    } else {
                        showToast("Failed")
                    }
                }
        }

    }

}