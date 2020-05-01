package com.thekusch.emoji_mashup.viewmodel.repository

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import com.thekusch.emoji_mashup.data.MashupDatabase
import com.thekusch.emoji_mashup.data.dao.StickersDao
import com.thekusch.emoji_mashup.models.StickersEntity
import java.io.File
import java.io.FileInputStream

class StickersRepository(application: Application) {
    private var stickersDao : StickersDao
    private var db = MashupDatabase.getAppDB(application)
    init {
        stickersDao = db!!.StickersDao()
    }
    fun addedSticker(stickersEntity: StickersEntity) = AddedStickerAsync(stickersDao).execute(stickersEntity)
    fun deleteSticker(stickersEntity: StickersEntity)=DeleteStickerAsync(stickersDao).execute(stickersEntity)
    fun getPacksSticker(packID:Int):List<StickersEntity>{
        return GetPackStickerAsync(stickersDao).execute(packID).get()
    }
    fun loadSavedStickerBitmap(sticker:String,directory: String):Bitmap{
        return LoadSavedStickerBitmapAsync(sticker,directory).execute().get()
    }
    companion object{
        private class LoadSavedStickerBitmapAsync(private var sticker:String,private var directory: String):AsyncTask<Void,Void, Bitmap>(){
            override fun doInBackground(vararg params: Void?): Bitmap {

                val file = File(directory, sticker)
                return BitmapFactory.decodeStream(FileInputStream(file))
            }

        }
        private class AddedStickerAsync(private var stickersDao: StickersDao):AsyncTask<StickersEntity,Void,Void>(){
            override fun doInBackground(vararg params: StickersEntity?): Void? {
                stickersDao.addedSticker(params[0]!!)
                return null
            }
        }
        private class DeleteStickerAsync(private var stickersDao: StickersDao):AsyncTask<StickersEntity,Void,Void>(){
            override fun doInBackground(vararg params: StickersEntity?): Void? {
                stickersDao.deleteSticker(params[0]!!)
                return null
            }
        }
        private class GetPackStickerAsync(private var stickersDao: StickersDao):AsyncTask<Int,Void,List<StickersEntity>>(){
            override fun doInBackground(vararg params: Int?): List<StickersEntity> {
                return stickersDao.getPacksStickers(params[0]!!)
            }

        }
    }
}