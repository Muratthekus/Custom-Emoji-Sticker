package com.thekusch.emoji_mashup.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.thekusch.emoji_mashup.R
import java.lang.ClassCastException

class CreatePackDialog: AppCompatDialogFragment() {
    private lateinit var packageName:EditText
    private lateinit var creatorName:EditText

    private lateinit var listener : DialogClickListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.createpackdialog,null)

        packageName = view.findViewById(R.id.packagename_edittext)
        creatorName = view.findViewById(R.id.creatorname_edittext)

        builder.setView(view)
            .setTitle("Create Package")
            .setNegativeButton("Cancel",DialogInterface.OnClickListener(){
                    dialogInterface: DialogInterface, i: Int ->

            })
            .setPositiveButton("Okay",DialogInterface.OnClickListener(){
                    dialogInterface: DialogInterface, i: Int ->
                    listener.onDialogButtonClick(packageName.text.toString(),creatorName.text.toString())
            })

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogClickListener
        }catch (e:ClassCastException){
            throw ClassCastException("Error when dialog button click listener initialized")
        }
    }
    interface DialogClickListener{
        fun onDialogButtonClick(packagename:String, creatorname:String){
        }
    }
}