package com.thekusch.emoji_mashup.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "stickers",
    foreignKeys = [ForeignKey(entity = StickerPackageEntity::class,
        parentColumns = ["ID"],
        childColumns = ["relatedPackageID"],onDelete = ForeignKey.CASCADE)])
data class StickersEntity(
    @PrimaryKey(autoGenerate = true)
    var ID:Int=0,
    var fileName:String,
    var relatedPackageID:Int
)