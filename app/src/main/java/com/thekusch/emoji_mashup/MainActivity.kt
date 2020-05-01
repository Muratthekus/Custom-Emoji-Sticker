package com.thekusch.emoji_mashup

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.media.Image
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.appindexing.FirebaseAppIndex
import com.google.firebase.appindexing.builders.Indexables
import com.google.firebase.appindexing.builders.StickerBuilder
import com.google.firebase.appindexing.builders.StickerPackBuilder
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.thekusch.emoji_mashup.enum.SelectedButtonEnum
import com.thekusch.emoji_mashup.firebase.UploadFile
import com.thekusch.emoji_mashup.models.StickerPackageEntity
import com.thekusch.emoji_mashup.models.StickersEntity
import com.thekusch.emoji_mashup.ui.adapter.Stickerpack_adapter
import com.thekusch.emoji_mashup.ui.dialog.AddStickerDialog
import com.thekusch.emoji_mashup.ui.dialog.CreatePackDialog
import com.thekusch.emoji_mashup.ui.dialog.DisplayPackStickerDialog
import com.thekusch.emoji_mashup.viewmodel.StickerpackViewModel
import com.thekusch.emoji_mashup.viewmodel.StickersViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),
    CreatePackDialog.DialogClickListener,
    Stickerpack_adapter.ViewClickListener,
    Stickerpack_adapter.ButtonListener,
    View.OnClickListener,
    AddStickerDialog.EmojiBodyPartsButtonClickListener,
    AddStickerDialog.AddStickerDialogListener,
    DisplayPackStickerDialog.UploadStickerClickListener{

    private lateinit var button:Button

    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: Stickerpack_adapter

    //Viewmodels
    private lateinit var stickerPackViewModel : StickerpackViewModel
    private lateinit var stickersViewModel : StickersViewModel

    //Dialogs
    private lateinit var addStickerDialog:AddStickerDialog
    private lateinit var displayStickerDialog:DisplayPackStickerDialog

    //Boolean
    var isResultInitialized = false

    //We must keep the selected images bitmaps
    private var headBitmap : Bitmap? = null
    private var mouthBitmap : Bitmap? = null
    private var eyesBitmap : Bitmap? = null
    private var eyebrowBitmap : Bitmap? = null
    private var additionalBitmap : Bitmap? = null

    private lateinit var selectedPackageName:String
    private var selectedPackageStickerCount:Int=-1
    private var linearLayoutList = ArrayList<LinearLayout>()

    private val createSticker = CreateSticker(this)
    private val showPackageSticker = ShowPackageSticker(this)

    //threads
    private val handler = Handler()
    private val checkPackListChangeRunnable = Runnable { checkPackageList() }
    private val waitInitialize = Runnable{ showPackageSticker.waitUntilGridLayoutInitialize(selectedPackageName)}
    private lateinit var waitUntilFectchUrlFinish:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.createPackButton)
        button.setOnClickListener(this)
        //adapter & recyclerview initialization
        recyclerview = findViewById(R.id.recylerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        adapter = Stickerpack_adapter()
        recyclerview.adapter = adapter
        adapter.setButtonListener(this)
        adapter.setViewClickListener(this)

        stickerPackViewModel = ViewModelProviders.of(this).get(StickerpackViewModel::class.java)
        stickersViewModel = ViewModelProviders.of(this).get(StickersViewModel::class.java)

        handler.postDelayed(checkPackListChangeRunnable,1000)

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val cw = ContextWrapper(applicationContext)
                val directory = cw.getDir(adapter.getItemAt(viewHolder.adapterPosition).packageName,Context.MODE_PRIVATE)
                directory.deleteRecursively()
                stickerPackViewModel.deletePackage(adapter.getItemAt(viewHolder.adapterPosition))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerview)
    }


    private fun checkPackageList(){
        stickerPackViewModel.getAllPackage().observe(this, Observer {
            adapter.setList(it)
        })
    }

    //DisplayPackStickerDialog's upload button click listener ? okay
    override fun uploadPackage(packageName: String) {
        val packID = stickerPackViewModel.getPackageID(packageName)
        val list = stickersViewModel.getPackStickers(packID)

        StaticMember.packName=packageName
        //StaticMember.files=files

        StaticMember.uri = ArrayList(list.size)
        StaticMember.context=applicationContext
        val storageRef = FirebaseStorage.getInstance("").reference
        var bool = false
        for(file in list){
            val item = storageRef.child("/${file.fileName}.png")
            item.downloadUrl.addOnCompleteListener{
                StaticMember.uri.add(it.result.toString())
                bool = true
            }
        }
        waitUntilFectchUrlFinish = Runnable {
            if(bool)
                StickerIndexingService.enqueueWork(this)
        }
        handler.postDelayed(waitUntilFectchUrlFinish,1000);

    }

    //Open all stickers inside the package
    override fun onViewClickListener(stickerPackageEntity: StickerPackageEntity) {
        displayStickerDialog = DisplayPackStickerDialog(stickerPackageEntity.packageName)
        displayStickerDialog.show(supportFragmentManager,"Upload Sticker")
        selectedPackageName=stickerPackageEntity.packageName
        showPackageSticker.waitUntilGridLayoutInitialize(selectedPackageName)

    }

    //Open create sticker screen
    override fun onButtonListener(stickerPackageEntity: StickerPackageEntity) {
        addStickerDialog = AddStickerDialog(stickerPackageEntity.packageName)
        addStickerDialog.show(supportFragmentManager,"Add Sticker")
        isResultInitialized=false
    }

    //Create package button listener
    override fun onClick(v: View?) {
        if(v!!.id == R.id.createPackButton){
            val createPackDialog = CreatePackDialog()
            createPackDialog.show(supportFragmentManager,"Create Package")
        }
    }

    //Save sticker package file into database and create package file
    override fun onDialogButtonClick(packagename: String, creatorname: String) {
        val isUniqueName = stickerPackViewModel.getAllPackNames().any {
            packagename == it
        }
        if(isUniqueName){
            Toast.makeText(this,"there cannot be two package with the same name",Toast.LENGTH_LONG).show()
        }
        else{
            val stickerPack = StickerPackageEntity(packageName = packagename,creatorName = creatorname )
            stickerPackViewModel.createPackage(stickerPack)
            createStickerPackFile(packagename)
        }
    }

    private fun createStickerPackFile(fileName:String){
        val cw = ContextWrapper(applicationContext)
        val directory = cw.getDir(fileName,Context.MODE_PRIVATE)

        if(!directory.exists())
            directory.mkdir()
    }

    //Save sticker into database and create png file into package folder
    override fun onAddStickerDialogListener(packageFolderName:String) {
        val packID = stickerPackViewModel.getPackageID(packageName)
        val list = stickersViewModel.getPackStickers(packID)
        if(list.size<=30){
            val cw = ContextWrapper(applicationContext)
            val directory = cw.getDir(packageFolderName,Context.MODE_PRIVATE)
            val fileName = UUID.randomUUID().toString()
            val path = File(directory, fileName)
            var fos : FileOutputStream? = null
            try {
                fos = FileOutputStream(path)
                val bitmap = createSticker.createSingleImageFromMultipleImages(headBitmap,0,0,mouthBitmap,eyesBitmap,eyebrowBitmap,additionalBitmap)
                UploadFile(bitmap,fileName).upload()
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fos)
            }catch (e:Exception){
                Log.e("Emoji Save Error", e.printStackTrace().toString())
            }finally {
                try {
                    fos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
            val packId = stickerPackViewModel.getPackageID(packageFolderName)
            val stickerEntity = StickersEntity(fileName = fileName,relatedPackageID = packId)
            stickersViewModel.addedSticler(stickerEntity)
        }
        else{
            Toast.makeText(applicationContext,"Can not be added more than 30 sticker to a package",Toast.LENGTH_LONG).show()
        }

    }

    // Create Sticker Dialog Body Part's Button
    override fun onEmojiBodyPartsButtonClickListener(selectedButton: SelectedButtonEnum) {
        val list = createSticker.listOfBodyPartImages("emojis/${selectedButton.partsName}")
        createSticker.displayBodyParts(selectedButton.partsName,list)
    }

    inner class ShowPackageSticker(var context: Context){
        private fun getStickerList(packageName: String){
            val packId = stickerPackViewModel.getPackageID(packageName)
            val stickerList = stickersViewModel.getPackStickers(packId)

            loadStickerBitmap(stickerList,packageName)
        }

        //Fetch stickers from device
        private fun loadStickerBitmap(stickerList:List<StickersEntity>,packageName: String){
            val cw = ContextWrapper(applicationContext)
            selectedPackageStickerCount=-1
            linearLayoutList.clear()
            val directory = cw.getDir(packageName,Context.MODE_PRIVATE)
            try{
                for(i in stickerList.indices){
                    val bitmap = stickersViewModel.getStickerBitmap(stickerList[i].fileName,directory.absolutePath)
                    showPackStickers(bitmap,i)
                }
                for(layout in linearLayoutList){
                    displayStickerDialog.gridLayout!!.addView(layout)
                }
            }catch (e:FileNotFoundException){
                throw FileNotFoundException("There is no file named $packageName")
            }
        }
        //For each 3 sticker, one linearlayout will be create
        private fun createLinearLayoutForGridColumn():LinearLayout{
            val item = LinearLayout(context)
            item.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            item.orientation = LinearLayout.HORIZONTAL
            return item
        }

        //put sticker into linear layout and put this layout into linear layout lsit
        private fun showPackStickers(bitmap: Bitmap,currentSticker:Int){
            //For every three stickers we will create linear layout
            if(currentSticker%3==0){
                val linearLayout : LinearLayout = createLinearLayoutForGridColumn()
                linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.setPadding(4,0,4,0)
                linearLayoutList.add(linearLayout)
                selectedPackageStickerCount++
            }
            val item = ImageView(context)
            item.layoutParams = LinearLayout.LayoutParams(0,bitmap.height,1f)
            item.setImageBitmap(bitmap)
            item.isClickable = true
            item.scaleType=ImageView.ScaleType.CENTER_INSIDE

            linearLayoutList[selectedPackageStickerCount].addView(item)
        }

        //We have to wait until gridlayout initialize finish, because all sticker will be display
        //inside this grid layout
        fun waitUntilGridLayoutInitialize(packageName: String){
            if(displayStickerDialog.gridLayout!=null){
                handler.removeCallbacks(waitInitialize)
                getStickerList(packageName)
            }
            else{
                handler.postDelayed(waitInitialize,100)
            }
        }
    }
    inner class CreateSticker(var context: Context){
        fun listOfBodyPartImages(subFolderName:String):Array<String>{
            val list : Array<String>? = assets.list(subFolderName)
            return list!!
        }
        fun displayBodyParts(subFolderName: String,subFolderFilesName:Array<String>){

            if(!isResultInitialized){
                eyesBitmap=null
                mouthBitmap= null
                eyebrowBitmap=null
                additionalBitmap=null
                initializeResultImage()
                isResultInitialized=true
            }

            addStickerDialog.linearLayout.removeAllViews()
            for(fileName in subFolderFilesName){
                addStickerDialog.linearLayout.addView(createDynamicImageView(subFolderName,fileName))
            }
        }
        private fun initializeResultImage(bodyPartFileName:String = "1.png"){
            val assetManager : AssetManager = assets

            val inputStream = assetManager.open("emojis/head/${bodyPartFileName}")

            val bitmap = createSingleImageFromMultipleImages(BitmapFactory.decodeStream(inputStream),0,0)
            headBitmap=bitmap
            addStickerDialog.resultImage.setImageBitmap(bitmap)
        }
        private fun changeResultImage(inputStream: InputStream){
            val mergedBitmaps = createSingleImageFromMultipleImages(headBitmap,0,0,mouthBitmap,eyesBitmap,eyebrowBitmap,additionalBitmap)
            addStickerDialog.resultImage.setImageBitmap(mergedBitmaps)
        }
        //Change result images bitmap according to user selection
        private fun createDynamicImageView(subFolderName: String,fileName:String):ImageView{
            val item = ImageView(context)
            item.layoutParams = LinearLayout.LayoutParams(150,LinearLayout.LayoutParams.MATCH_PARENT)

            val assetManager : AssetManager = assets
            val inputStream = assetManager.open("emojis/${subFolderName}/$fileName")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            item.setImageBitmap(bitmap)
            item.isClickable=true
            item.setOnClickListener{
                when(subFolderName){
                    "head" -> headBitmap = bitmap

                    "eyes" ->{
                        eyesBitmap = if(fileName!="0.png")
                            bitmap
                        else
                            null
                    }

                    "mouth" ->{
                        mouthBitmap = if(fileName!="0.png")
                            bitmap
                        else
                            null
                    }

                    "eyebrow" -> {
                        eyebrowBitmap = if(fileName!="0.png")
                            bitmap
                        else
                            null
                    }

                    "additional" -> {
                        additionalBitmap = if(fileName!="0.png")
                            bitmap
                        else
                            null
                    }
                }
                changeResultImage(inputStream)

            }
            return item
        }

        fun createSingleImageFromMultipleImages(head:Bitmap?,height:Int, width:Int,
                                                        vararg images:Bitmap?):Bitmap{

            val resultBitmap:Bitmap = if(height == 0 || width == 0){
                Bitmap.createBitmap(512,512,head!!.config)
            } else{
                Bitmap.createBitmap(512,512,head!!.config)
            }

            val canvas = Canvas(resultBitmap)
            canvas.drawBitmap(head,0f,0f,null)
            for(i in images){
                if(i!=null)
                    canvas.drawBitmap(i,1f,1f,null)
            }
            return resultBitmap
        }
    }

}


