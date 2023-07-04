package com.project.suhail.blendr.ui.activities.phone_number

import android.content.Intent
import android.os.Bundle
import com.project.suhail.blendr.databinding.ActivityPhoneNumberBinding
import com.project.suhail.blendr.ui.activities.home.HomeActivity
import com.project.suhail.blendr.ui.activities.otp.OtpActivity
import com.project.suhail.blendr.utils.BaseActivity
import com.project.suhail.blendr.utils.Constants

class PhoneNumberActivity : BaseActivity() {

    private val mBinding by lazy { ActivityPhoneNumberBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        if (firebaseAuth.uid != null) {
            startActivity(Intent(this, HomeActivity::class.java)).apply {
                finish()
            }
        }

        supportActionBar?.hide()
        mBinding.etPhoneNumber.requestFocus()

        mBinding.btnContinue.setOnClickListener {
            Intent(this, OtpActivity::class.java).apply {
                putExtra(Constants.KEY_PHONE_NUMBER, mBinding.etPhoneNumber.text.trim().toString())
                startActivity(this)
            }
        }

    }

}