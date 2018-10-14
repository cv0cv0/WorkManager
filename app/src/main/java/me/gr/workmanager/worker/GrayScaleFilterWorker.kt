package me.gr.workmanager.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript
import androidx.work.WorkerParameters
import me.gr.workmanager.ScriptC_grayscale

class GrayScaleFilterWorker(context: Context, params: WorkerParameters) : BaseFilterWorker(context, params) {

    override fun applyFilter(input: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, input.config)
        val rs: RenderScript = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
        val inAlloc = Allocation.createFromBitmap(rs, input)
        val outAlloc = Allocation.createTyped(rs, inAlloc.type)
        val grayscale = ScriptC_grayscale(rs)
        grayscale._script = grayscale
        grayscale._width = input.width.toLong()
        grayscale._height = input.height.toLong()
        grayscale._in = inAlloc
        grayscale._out = outAlloc
        grayscale.invoke_filter()
        outAlloc.copyTo(output)
        rs.finish()
        return output
    }
}