package com.example.wallpaper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bumptech.glide.Glide
import com.example.wallpaper.databinding.ActivityWallpaperDetailBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class WallpaperDetail : AppCompatActivity() {
    lateinit var binding: ActivityWallpaperDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val image = intent.getStringExtra("image").orEmpty()
        Glide.with(this).load(image).into(binding.wallpaperImage)
        binding.downloadBtn.setOnClickListener {
            lifecycleScope.launch {
                val bitmap = getBitmapFromURL(image)
                if (bitmap != null) {
                    saveImage(finalBitmap = bitmap)
                }
                Log.d("TAG", bitmap.toString())
            }
        }
        binding.shareBtn.setOnClickListener {
            lifecycleScope.launch {
                val bitmap = getBitmapFromURL(image)
                if (bitmap != null) {
                    val uri = getImageUriFromBitmap(bitmap = bitmap, context = this@WallpaperDetail)
                    shareImageUri(uri)
                }
                Log.d("TAG", bitmap.toString())
            }
        }
    }

    private suspend fun getBitmapFromURL(src: String?): Bitmap? {
        val loading = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(src)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun shareImageUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        startActivity(intent)
    }


    private fun saveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        ).toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val name = "Image-$n.jpg"
        val file = File(myDir, name)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(
            this, arrayOf(file.toString()), null
        ) { path, uri ->
            Log.i("ExternalStorage", "Scanned $path:")
            Log.i("ExternalStorage", "-> uri=$uri")
        }
        Toast.makeText(this@WallpaperDetail, "Wallpaper save to Gallery", Toast.LENGTH_SHORT).show()
    }
}