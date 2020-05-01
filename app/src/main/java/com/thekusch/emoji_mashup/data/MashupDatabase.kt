package com.thekusch.emoji_mashup.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thekusch.emoji_mashup.data.dao.StickerPackageDao
import com.thekusch.emoji_mashup.data.dao.StickersDao
import com.thekusch.emoji_mashup.models.StickerPackageEntity
import com.thekusch.emoji_mashup.models.StickersEntity

@Database(entities = [StickerPackageEntity::class,StickersEntity::class],version = 2)
abstract class MashupDatabase:RoomDatabase() {
    abstract fun StickerPackageDao() : StickerPackageDao
    abstract fun StickersDao() : StickersDao

    companion object{
        private var INSTANCE : MashupDatabase ?= null
        fun getAppDB(context: Context):MashupDatabase?{
            if(INSTANCE == null){
                synchronized(MashupDatabase::class){
                    INSTANCE = Room.databaseBuilder(context,MashupDatabase::class.java,"appDB")
                        .fallbackToDestructiveMigration().build()

                }
            }
            return INSTANCE
        }
    }
}