package me.gr.workmanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_filter.*
import me.gr.workmanager.R
import me.gr.workmanager.common.IMGUR_CLIENT_ID
import me.gr.workmanager.common.KEY_IMAGE_URI
import me.gr.workmanager.util.ImageOperation
import me.gr.workmanager.viewmodel.FilterViewModel

class FilterActivity : AppCompatActivity() {
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(FilterViewModel::class.java)
    }
    private val imageUri: Uri? by lazy(LazyThreadSafetyMode.NONE) {
        intent.getStringExtra(KEY_IMAGE_URI)?.let { Uri.parse(it) }
    }
    private var outputUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        upload_radio.isEnabled = IMGUR_CLIENT_ID.isNotEmpty()
        Glide.with(this).load(imageUri).into(imageView)

        go_button.setOnClickListener {
            viewModel.apply(ImageOperation(
                    imageUri = imageUri,
                    applyWaterColor = watercolor_checkbox.isChecked,
                    applyGrayScale = grayScale_checkbox.isChecked,
                    applyBlur = blur_checkbox.isChecked,
                    applySave = save_radio.isChecked,
                    applyUpload = upload_radio.isChecked))
        }

        output_button.setOnClickListener { _ ->
            if (outputUri != null) {
                val intent = Intent(Intent.ACTION_VIEW, outputUri)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }

        cancel_button.setOnClickListener { viewModel.cancel() }

        viewModel.outputStatus.observe(this, Observer { statusList ->
            if (statusList == null || statusList.isEmpty()) return@Observer

            val status = statusList[0]
            val finished = status.state.isFinished
            if (!finished) {
                progress_horizontal.visibility = View.VISIBLE
                cancel_button.visibility = View.VISIBLE
                go_button.visibility = View.GONE
                output_button.visibility = View.GONE
            } else {
                progress_horizontal.visibility = View.GONE
                cancel_button.visibility = View.GONE
                go_button.visibility = View.VISIBLE

                status.outputData.getString(KEY_IMAGE_URI)
                        ?.let {
                            outputUri = Uri.parse(it)
                            output_button.visibility = View.VISIBLE
                        }
            }
        })
    }
}