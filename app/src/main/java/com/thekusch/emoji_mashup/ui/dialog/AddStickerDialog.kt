package com.thekusch.emoji_mashup.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDialogFragment
import com.thekusch.emoji_mashup.R
import com.thekusch.emoji_mashup.enum.SelectedButtonEnum
import kotlinx.android.synthetic.main.package_list_item.*
import java.lang.ClassCastException

class AddStickerDialog(private var packageName:String):AppCompatDialogFragment(), View.OnClickListener {

    lateinit var resultImage:ImageView
    private lateinit var headButton:Button
    private lateinit var mouthButton:Button
    private lateinit var eyeButton:Button
    private lateinit var eyebrowButton:Button
    private lateinit var additionalButton:Button
    lateinit var linearLayout: LinearLayout

    private lateinit var dialogClickListener : AddStickerDialogListener
    private lateinit var buttonClickListener : EmojiBodyPartsButtonClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.addsticker_dialog,null)

        //initialize
        resultImage = view.findViewById(R.id.mashupEmojiHolder)

        headButton = view.findViewById(R.id.emojiHeadButton)
        headButton.setOnClickListener(this)

        mouthButton = view.findViewById(R.id.emojiMouthButton)
        mouthButton.setOnClickListener(this)

        eyeButton = view.findViewById(R.id.emojiEyeButton)
        eyeButton.setOnClickListener(this)

        eyebrowButton = view.findViewById(R.id.emojiEyebrowButton)
        eyebrowButton.setOnClickListener(this)

        additionalButton = view.findViewById(R.id.emojiAdditional)
        additionalButton.setOnClickListener(this)

        linearLayout = view.findViewById(R.id.emojiBodyPartsSection)

        builder.setView(view)
            .setTitle("Add Sticker")
            .setNegativeButton("Cancel", DialogInterface.OnClickListener(){
                    dialogInterface: DialogInterface, i: Int ->

            })
            .setPositiveButton("Okay", DialogInterface.OnClickListener(){
                    dialogInterface: DialogInterface, i: Int ->
                    dialogClickListener.onAddStickerDialogListener(packageName)
            })


        return builder.create()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.emojiHeadButton -> buttonClickListener.onEmojiBodyPartsButtonClickListener(SelectedButtonEnum.HEAD_BUTTON)

            R.id.emojiMouthButton -> buttonClickListener.onEmojiBodyPartsButtonClickListener(SelectedButtonEnum.MOUTH_BUTTON)

            R.id.emojiEyeButton -> buttonClickListener.onEmojiBodyPartsButtonClickListener(SelectedButtonEnum.EYE_BUTTON)

            R.id.emojiEyebrowButton -> buttonClickListener.onEmojiBodyPartsButtonClickListener(SelectedButtonEnum.EYEBROW_BUTTON)

            R.id.emojiAdditional -> buttonClickListener.onEmojiBodyPartsButtonClickListener(SelectedButtonEnum.ADDITIONAL)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogClickListener = context as AddStickerDialogListener
            buttonClickListener = context as EmojiBodyPartsButtonClickListener
        }catch (e:ClassCastException){
            throw ClassCastException("Error when dialog button click listener initialized")
        }
    }

    interface AddStickerDialogListener{
        fun onAddStickerDialogListener(packageFolderName:String)
    }

    interface EmojiBodyPartsButtonClickListener{
        fun onEmojiBodyPartsButtonClickListener(selectedButton:SelectedButtonEnum)
    }

}