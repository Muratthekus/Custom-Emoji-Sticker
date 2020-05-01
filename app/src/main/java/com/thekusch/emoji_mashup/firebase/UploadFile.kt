package com.thekusch.emoji_mashup.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class UploadFile(private var bitmap: Bitmap,private var fileName:String) {
    private val storageRef = FirebaseStorage.getInstance("").reference
    private val imageRef = storageRef.child("/$fileName.png")
    private var baos : ByteArrayOutputStream = ByteArrayOutputStream()
    fun upload(){
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
        val data = baos.toByteArray()
        val uploadTask:UploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener {
            Log.d("UPLOAD","$it")
        }
        uploadTask.addOnCompleteListener{
            Log.d("UPLOAD","UPLOAD SUCCESFUL")
        }

    }
}