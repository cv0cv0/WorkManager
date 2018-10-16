package me.gr.workmanager

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.activity_main.*
import me.gr.workmanager.common.KEY_IMAGE_URI
import me.gr.workmanager.data.randomImage
import me.gr.workmanager.ui.FilterActivity
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.indefiniteSnackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), AnkoLogger {
    companion object {
        private const val REQUEST_CODE_IMAGE = 100
        private const val REQUEST_CODE_PERMISSIONS = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        credits_text.text = HtmlCompat.fromHtml(getString(R.string.credits), HtmlCompat.FROM_HTML_MODE_COMPACT)
        credits_text.movementMethod = LinkMovementMethod.getInstance()

        selectImage_button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS)
            } else {
                showImagePick()
            }
        }

        selectStockImage_button.setOnClickListener {
            startActivity<FilterActivity>(KEY_IMAGE_URI to randomImage().toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                handleImageResult(data)
            } else {
                error("Unexpected Result code $resultCode")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePick()
            } else {
                credits_text.indefiniteSnackbar(R.string.set_permissions_in_settings)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun handleImageResult(data: Intent?) {
        val uri: Uri? = data?.clipData?.getItemAt(0)?.uri
        if (uri == null) {
            error("Invalid input image Uri.")
            return
        }
        startActivity<FilterActivity>(KEY_IMAGE_URI to uri.toString())
    }

    private fun showImagePick() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }
}
