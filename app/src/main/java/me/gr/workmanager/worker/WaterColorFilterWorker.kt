package me.gr.workmanager.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.RenderScript
import androidx.work.WorkerParameters
import me.gr.workmanager.ScriptC_waterColorEffect

class WaterColorFilterWorker(context: Context, params: WorkerParameters) : BaseFilterWorker(context, params) {

    override fun applyFilter(input: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, input.config)
        val rs = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
        val inAlloc = Allocation.createFromBitmap(rs, input)
        val outAlloc = Allocation.createTyped(rs, inAlloc.type)
        val oilFilterEffect = ScriptC_waterColorEffect(rs)
        oilFilterEffect._script = oilFilterEffect
        oilFilterEffect._width = input.width.toLong()
        oilFilterEffect._height = input.height.toLong()
        oilFilterEffect._in = inAlloc
        oilFilterEffect._out = outAlloc
        oilFilterEffect.invoke_filter()
        outAlloc.copyTo(output)
        rs.finish()
        return output
    }
}