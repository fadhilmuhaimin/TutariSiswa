package com.pajaga.utils.system

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.pajaga.utils.other.showToast
import java.io.File
import java.io.IOException


class DocumentUtils(val activity: ComponentActivity) {

    val targetPdf = "/sdcard/page.pdf"

    fun createBitmapFromLayout(v: View, width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPdf(bitmap: Bitmap) {
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = pdfDocument.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()

        canvas.drawPaint(paint)

        val bitmapNew: Bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

        canvas.drawBitmap(bitmapNew, 0.toFloat(), 0.toFloat(), null)

        pdfDocument.finishPage(page)

        val file = File(targetPdf)



        try {
            createFile(Uri.fromFile(file))
//            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: IOException) {
            e.printStackTrace()

            showToast(activity, "something wrong try again $e")

            // after close document
            pdfDocument.close()
            showToast(activity, "succesfull download")
            openPdf()
        }
    }

    val CREATE_FILE = 1

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFile(pickerInitialUri: Uri) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "invoice.pdf")

            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker before your app creates the document.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        activity.startActivityForResult(intent, CREATE_FILE)
    }

    fun openPdf() {
        val file = File(targetPdf)

        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            try {
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showToast(activity, "No application for pdf view")
            }
        }
    }

}
