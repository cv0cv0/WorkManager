package me.gr.workmanager.worker

import android.content.Context
import android.graphics.Bitmap
import androidx.renderscript.Allocation
import androidx.renderscript.Element
import androidx.renderscript.RenderScript
import androidx.renderscript.ScriptIntrinsicBlur
import androidx.work.WorkerParameters

class BlurEffectFilterWorker(context: Context, params: WorkerParameters) : BaseFilterWorker(context, params) {

    override fun applyFilter(input: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, input.config)
        val rs: RenderScript = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
        val inAlloc = Allocation.createFromBitmap(rs, input)
        val outAlloc = Allocation.createTyped(rs, inAlloc.type)
        val intrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        intrinsic.setRadius(25f)
        intrinsic.setInput(inAlloc)
        intrinsic.forEach(outAlloc)
        outAlloc.copyTo(output)
        rs.finish()
        return output
    }
}