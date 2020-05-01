package com.thekusch.emoji_mashup.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thekusch.emoji_mashup.R
import com.thekusch.emoji_mashup.models.StickerPackageEntity
import com.thekusch.emoji_mashup.ui.viewholder.StickerpackViewholder
import kotlin.random.Random


class Stickerpack_adapter: RecyclerView.Adapter<StickerpackViewholder>() {

    private lateinit var buttonListener:ButtonListener
    private lateinit var viewClickListener:ViewClickListener

    private var packageList:List<StickerPackageEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerpackViewholder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.package_list_item,parent,false)
        return StickerpackViewholder(itemView)
    }

    override fun getItemCount(): Int {
        return packageList.size
    }
    fun setList(list:List<StickerPackageEntity>){
        this.packageList=list
        notifyDataSetChanged()
    }
    fun getItemAt(pos:Int):StickerPackageEntity{
        return packageList[pos]
    }
    fun getPackage(pos:Int):StickerPackageEntity{
        return packageList[pos]
    }
    override fun onBindViewHolder(holder: StickerpackViewholder, position: Int) {
        val pack = packageList[position]
        val rnd = Random
        holder.packageIcon.setBackgroundColor(Color.argb(255,rnd.nextInt(256),rnd.nextInt(256),rnd.nextInt(256)))
        holder.packageName.text=pack.packageName
        holder.creatorName.text=pack.creatorName
        holder.button.setOnClickListener {
            buttonListener.onButtonListener(pack)
        }
        holder.itemView.setOnClickListener{
            viewClickListener.onViewClickListener(pack)
        }
    }
    interface ViewClickListener{
        fun onViewClickListener(stickerPackageEntity: StickerPackageEntity)
    }
    fun setViewClickListener(listener:ViewClickListener){this.viewClickListener=listener}

    interface ButtonListener{
        fun onButtonListener(stickerPackageEntity: StickerPackageEntity)
    }
    fun setButtonListener(listener:ButtonListener){this.buttonListener=listener}

    fun setIcon(){

    }
    interface SetPackIcon{
        fun onSetPackIcon(v:View)
    }
}