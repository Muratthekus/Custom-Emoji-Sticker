package com.thekusch.emoji_mashup.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatDialogFragment
import com.thekusch.emoji_mashup.R
import java.lang.ClassCastException

class DisplayPackStickerDialog(private var packageName:String):AppCompatDialogFragment() {

    private lateinit var uploadClickListener:UploadStickerClickListener
    var gridLayout: GridLayout? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.show_pack_stickers,null)

        gridLayout = view.findViewById(R.id.wrapperOfSticker)

        builder.setView(view)
            .setTitle(packageName)
            .setNegativeButton("Close", DialogInterface.OnClickListener(){
                    dialogInterface: DialogInterface, i: Int ->

            })
            .setPositiveButton("Upload", DialogInterface.OnClickListener(){
                    dialogInterface: DialogInterface, i: Int ->
                    uploadClickListener.uploadPackage(packageName)
            })

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            uploadClickListener = context as UploadStickerClickListener
        }catch (e:ClassCastException){
            throw ClassCastException("Error when dialog button click listener initialized")
        }
    }
    interface UploadStickerClickListener{
        fun uploadPackage(packageName: String)
    }
}