package com.thekusch.emoji_mashup.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.thekusch.emoji_mashup.models.StickerPackageEntity

@Dao
interface StickerPackageDao {
    @Insert
    fun createPackage(stickerPack:StickerPackageEntity)

    @Delete
    fun deletePackage(stickerPack: StickerPackageEntity)

    @Query("SELECT * FROM sticker_package")
    fun getAllPackage():LiveData<List<StickerPackageEntity>>

    @Query("SELECT packageName FROM sticker_package")
    fun getAllPackageName():List<String  >

    @Query("SELECT ID FROM sticker_package WHERE packageName=:packName")
    fun getPackageID(packName:String):Int

    @Query("DELETE FROM sticker_package")
    fun deleteAllPackage()
}