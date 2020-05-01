package com.thekusch.emoji_mashup.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sticker_package")
data class StickerPackageEntity(
    @PrimaryKey(autoGenerate = true)
    var ID:Int=0,
    var packageName:String,
    var creatorName:String
)