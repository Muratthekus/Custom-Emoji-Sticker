package com.thekusch.emoji_mashup.ui.viewholder

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thekusch.emoji_mashup.R
import kotlinx.android.synthetic.main.package_list_item.view.*

class StickerpackViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var packageIcon : ImageView = itemView.findViewById(R.id.package_icon)
    var packageName : TextView = itemView.findViewById(R.id.package_name)
    var creatorName : TextView = itemView.findViewById(R.id.creator_name)
    var button : Button = itemView.findViewById(R.id.settings_button)
}