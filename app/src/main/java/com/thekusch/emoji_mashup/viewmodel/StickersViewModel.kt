package com.thekusch.emoji_mashup.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.thekusch.emoji_mashup.models.StickersEntity
import com.thekusch.emoji_mashup.viewmodel.repository.StickersRepository

class StickersViewModel(application: Application):AndroidViewModel(application) {
    private var repository = StickersRepository(application)

    fun addedSticler(stickersEntity: StickersEntity){
        repository.addedSticker(stickersEntity)
    }
    fun deleteSticker(stickersEntity: StickersEntity){
        repository.deleteSticker(stickersEntity)
    }
    fun getStickerBitmap(stickerName:String,directory:String):Bitmap{
        return repository.loadSavedStickerBitmap(stickerName,directory)
    }
    fun getPackStickers(packID:Int):List<StickersEntity>{
        return repository.getPacksSticker(packID)
    }
}