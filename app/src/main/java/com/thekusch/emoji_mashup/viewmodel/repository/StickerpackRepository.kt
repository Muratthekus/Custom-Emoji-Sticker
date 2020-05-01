package com.thekusch.emoji_mashup.viewmodel.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.thekusch.emoji_mashup.data.MashupDatabase
import com.thekusch.emoji_mashup.data.dao.StickerPackageDao
import com.thekusch.emoji_mashup.data.dao.StickersDao
import com.thekusch.emoji_mashup.models.StickerPackageEntity

class StickerpackRepository(application: Application) {
    private var stickerdao: StickerPackageDao
    private var stickerpack_list:LiveData<List<StickerPackageEntity>>
    private var db = MashupDatabase.getAppDB(application)
    init {
        stickerdao = db!!.StickerPackageDao()
        stickerpack_list=stickerdao.getAllPackage()
    }
    fun createPackage(stickerPackageEntity: StickerPackageEntity){
        CreatePackAsync(stickerdao).execute(stickerPackageEntity)
    }
    fun getPackageID(packName:String):Int{
        return GetPackageIdAsync(stickerdao).execute(packName).get()
    }

    fun deletePackage(stickerPackageEntity: StickerPackageEntity){
        DeletePackAsync(stickerdao).execute(stickerPackageEntity)
    }
    fun deleteAllPackages(){
        DeleteAllPackAsync(stickerdao).execute()
    }
    fun getAllPack():LiveData<List<StickerPackageEntity>>{
        return stickerpack_list
    }
    fun getAllPackNames():List<String>{
        return GetAllPackNames(stickerdao).execute().get()
    }
    companion object{
        private class CreatePackAsync(private var stickerPackageDao: StickerPackageDao):AsyncTask<StickerPackageEntity,Void,Void>(){
            override fun doInBackground(vararg params: StickerPackageEntity?): Void? {
                stickerPackageDao.createPackage(params[0]!!)
                return null
            }
        }
        private class DeletePackAsync(private var stickerPackageDao: StickerPackageDao):AsyncTask<StickerPackageEntity,Void,Void>(){
            override fun doInBackground(vararg params: StickerPackageEntity?): Void? {
                stickerPackageDao.deletePackage(params[0]!!)
                return null
            }
        }
        private class DeleteAllPackAsync(private var stickerPackageDao: StickerPackageDao):AsyncTask<Void,Void,Void>(){
            override fun doInBackground(vararg params: Void?): Void? {
                stickerPackageDao.deleteAllPackage()
                return null
            }

        }
        private class GetPackageIdAsync(private var stickerPackageDao: StickerPackageDao):AsyncTask<String,Void,Int>(){
            override fun doInBackground(vararg params: String?): Int {
                return stickerPackageDao.getPackageID(params[0]!!)
            }

        }
        private class GetAllPackNames(private var stickerPackageDao: StickerPackageDao):AsyncTask<Void,Void,List<String>>(){
            override fun doInBackground(vararg params: Void?): List<String>? {
                return stickerPackageDao.getAllPackageName()
            }

        }
    }
}