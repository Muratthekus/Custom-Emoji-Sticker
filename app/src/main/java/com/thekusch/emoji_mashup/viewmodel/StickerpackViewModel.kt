package com.thekusch.emoji_mashup.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.thekusch.emoji_mashup.models.StickerPackageEntity
import com.thekusch.emoji_mashup.viewmodel.repository.StickerpackRepository

class StickerpackViewModel(application: Application):AndroidViewModel(application){
    private var repository = StickerpackRepository(application)
    private var allPackage = repository.getAllPack()

    fun createPackage(stickerPackageEntity: StickerPackageEntity){
        repository.createPackage(stickerPackageEntity)
    }
    fun getPackageID(packName:String):Int{
        return repository.getPackageID(packName)
    }

    fun getAllPackNames():List<String>{
        return repository.getAllPackNames()
    }

    fun deletePackage(stickerPackageEntity: StickerPackageEntity){
        repository.deletePackage(stickerPackageEntity)
    }
    fun deleteAllPackage(){
        repository.deleteAllPackages()
    }
    fun getAllPackage():LiveData<List<StickerPackageEntity>>{
        return allPackage
    }
}