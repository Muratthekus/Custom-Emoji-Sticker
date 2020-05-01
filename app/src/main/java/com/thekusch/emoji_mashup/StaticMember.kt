package com.thekusch.emoji_mashup

import android.content.Context
import android.net.Uri
import java.io.File

class StaticMember {
    companion object{
        lateinit var packName:String
        lateinit var files : List<File>
        lateinit var context: Context
        var uri: MutableList<String> = ArrayList()
    }
}