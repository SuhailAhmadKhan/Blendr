package com.project.suhail.blendr.ui.activities.view_image

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.project.suhail.blendr.databinding.ActivityViewImageBinding
import com.project.suhail.blendr.utils.BaseActivity
import com.project.suhail.blendr.utils.FirebaseUtils
import com.project.suhail.blendr.utils.GeneralFunctions

class ViewImageActivity : BaseActivity() {

    private val mBinding by lazy { ActivityViewImageBinding.inflate(layoutInflater) }
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        supportActionBar?.hide()

        imageUrl = intent.getStringExtra(FirebaseUtils.IMAGE)

        if (imageUrl == null)
            return

        GeneralFunctions.loadImage(this, imageUrl!!, mBinding.iv)

        val permissionContract = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultMap ->
            resultMap.entries.forEach { entry ->
                if (entry.value) {
                    GeneralFunctions.downloadImage(this, imageUrl!!)

                }
            }
        }

        mBinding.iv.setOnClickListener { ActivityCompat.finishAfterTransition(this) }

        mBinding.ivDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 29)
                GeneralFunctions.downloadImage(this, imageUrl!!)
            else
                permissionContract.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }

}