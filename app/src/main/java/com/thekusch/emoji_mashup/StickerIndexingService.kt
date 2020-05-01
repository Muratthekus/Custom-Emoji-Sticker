package com.thekusch.emoji_mashup

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.google.firebase.appindexing.FirebaseAppIndex
import java.io.File


class StickerIndexingService:JobIntentService() {

    companion object{
        const val UNIQUE_JOB_ID = 11881546
        fun enqueueWork(context: Context) {
            enqueueWork(context, StickerIndexingService::class.java, UNIQUE_JOB_ID, Intent())
        }
    }
    override fun onHandleWork(intent: Intent) {
        UploadSticker.setStickers(applicationContext, FirebaseAppIndex.getInstance())
    }
}