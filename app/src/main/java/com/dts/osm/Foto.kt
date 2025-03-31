package com.dts.osm


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.FileOutputStream

class Foto : PBase() {

    var img1: ImageView? = null

    var fbstorage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    var photoPath=""
    var idorden=0
    var idfoto=""

    val TAKE_PHOTO_REQUEST=1

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_foto)

            super.initbase(savedInstanceState)

            img1 = findViewById(R.id.imageView11)

            idorden=gl?.idorden!!

            fbstorage = FirebaseStorage.getInstance()
            storageReference = fbstorage?.getReference()

            launchCamera()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,  data: Intent?) {
        if (resultCode == Activity.RESULT_OK  && requestCode == TAKE_PHOTO_REQUEST) {
            processCapturedPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            finish()
        }
    }

    //region Events

    fun doExit(view : View) {
        finish()
    }

    //endregion

    //region Main

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(packageManager) != null) {
            photoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    private fun processCapturedPhoto() {

        try {
            val cursor = contentResolver.query(
                Uri.parse(photoPath),
                Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},null, null, null)
            cursor?.moveToFirst()
            val photoPath = cursor?.getString(0)
            cursor?.close()

            val ffile = File(photoPath)
            val fname = ffile.name
            val dname = ffile.parentFile.absoluteFile.toString()
            idfoto=""+idorden+"_"+fname
            var nfname = dname+"/"+idfoto

            val options = BitmapFactory.Options()
            options.inScaled = false;
            val bMap = BitmapFactory.decodeFile(photoPath,options)

            val exif = ExifInterface(ffile.absolutePath)
            var or=exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

            var rotdeg=orientationToDegrees(or)
            var sbmp=scaleBitmap(bMap,bMap.width.toFloat()/3,bMap.height.toFloat()/3,)
            var nbmp=rotateBitmap(sbmp!!,rotdeg.toFloat())

            saveBitmapAsJpg(nbmp,nfname)

            gl?.idfoto=idfoto
            ffile.delete()

            finish()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    fun orientationToDegrees(orientation: Int): Int {
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }

    fun rotateBitmap(original: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply {postRotate(degrees)}
        return Bitmap.createBitmap(original,0,0,original.width,original.height,matrix,true)
    }

    fun scaleBitmap(bmps: Bitmap?, newWidth: Float, newHeight: Float): Bitmap? {
        if (bmps == null) return null
        val width = bmps.width
        val height = bmps.height
        val matrix = Matrix()

        matrix.postScale(newWidth / width, newHeight / height)
        return Bitmap.createBitmap(bmps,0,0,bmps.width,bmps.height,matrix,true)
    }

    fun saveBitmapAsJpg(bitmap: Bitmap, filePath: String) {
        val file = File(filePath)
        var outputStream: FileOutputStream? = null

        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.flush()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        } finally {
            try {
                outputStream?.close()
            } catch (e: Exception) {
                msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }
        }
    }

    //endregion

    //region Dialogs


    //endregion

    //region Aux

    fun uploadFile() {
        try {
            var ifile= File("/storage/emulated/0/Pictures/abcd.jpg")
            var ufile = Uri.fromFile(ifile)
            val riversRef = storageReference?.child("fotos/${ufile.lastPathSegment}")
            var uploadTask = riversRef?.putFile(ufile)

            uploadTask?.addOnFailureListener {
                toastlong("fail")
            }?.addOnSuccessListener { taskSnapshot ->
                toastlong("ok")
                finish()
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Activity Events

    //endregion

}