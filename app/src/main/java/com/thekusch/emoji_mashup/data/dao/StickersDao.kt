package com.thekusch.emoji_mashup.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.thekusch.emoji_mashup.models.StickersEntity

@Dao
interface StickersDao {
    @Insert
    fun addedSticker(stickersEntity:StickersEntity)

    @Delete
    fun deleteSticker(stickersEntity: StickersEntity)

    @Query("SELECT * FROM stickers INNER JOIN sticker_package ON stickers.relatedPackageID=sticker_package.ID AND stickers.relatedPackageID=:packID")
    fun getPacksStickers(packID:Int):List<StickersEntity>
}