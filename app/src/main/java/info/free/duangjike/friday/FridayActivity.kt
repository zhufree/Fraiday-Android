package info.free.duangjike.friday

import android.app.Activity
import android.app.AlertDialog
import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_LOCK
import android.app.WallpaperManager.FLAG_SYSTEM
import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.os.storage.StorageManager
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.view.Gravity.CENTER
import android.view.View.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.documentfile.provider.DocumentFile
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import info.free.duangjike.R
import info.free.duangjike.ThemeUtil
import info.free.duangjike.Util
import kotlinx.android.synthetic.main.activity_friday.*
import kotlinx.android.synthetic.main.layout_dialog_input.view.*
import kotlinx.android.synthetic.main.layout_dialog_pick_color.view.*
import org.jetbrains.anko.*
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*


class FridayActivity : AppCompatActivity() {

    private val shuSongType = 0
    private val kaiType = 1
    private val fangSongType = 2
    private val heiType = 3

    private val bubbleType = 0
    private val bgType = 1
    private val textType = 2

    private val wallType = 0

    private val cnType = 0
    private val enType = 1

    private var white: Int = -1
    private var blue: Int = -1
    private var yellow: Int = -1
    private var black: Int = -1
    private val today = getInstance(Locale.CHINA)
    private val fridayLink = "jike://page.jk/topic/565ac9dd4b715411006b5ecd"
    private val downJikeLink = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ruguoapp.jike&ckey=CK1411402428437"
    private val jikePackageName = "com.ruguoapp.jike"

    private val requestSavePic = 101
    private val requestSharePic = 102
    private val requestDeletePic = 103

    var currentBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_friday)
        white = resources.getColor(R.color.jikeWhite)
        blue = resources.getColor(R.color.jikeBlue)
        yellow = resources.getColor(R.color.jikeYellow)
        black = resources.getColor(R.color.jikeBlack)
        initData()
        setDate()
        refreshTheme()
        switchLanguage(FridayPreference.getLang())
        setEvent()
        getImageDir(requestDeletePic)
    }


    private fun initData() {
        for (i in 0 until 11) {
            val tvFont = TextView(this)
            tvFont.text = i.toString()
            tvFont.setTextColor(white)
            tvFont.gravity = CENTER
            val dp4 = Util.dp2px(4)
            tvFont.setPadding(dp4, dp4, dp4, dp4)
            val tvParam = ConstraintLayout.LayoutParams(MATCH_CONSTRAINT, ViewGroup.LayoutParams.WRAP_CONTENT)
            tvParam.topToTop = 0
            tvParam.bottomToBottom = 0
            tvParam.startToStart = 0
            tvParam.dimensionRatio = "1:1"
            tvParam.marginStart = Util.dp2px(30 * i)
            setBg(tvFont)
            cl_font_en.addView(tvFont, tvParam)
            tvFont.setOnClickListener {
                switchEnTypeFace(enFontList[i])
            }
        }
    }

    private fun setEvent() {
        tv_face_type_shu_song?.setOnClickListener {
            switchCnTypeFace(shuSongType)
        }
        tv_face_type_fang_song?.setOnClickListener {
            switchCnTypeFace(fangSongType)
        }
        tv_face_type_hei?.setOnClickListener { switchCnTypeFace(heiType) }
        tv_face_type_kai?.setOnClickListener { switchCnTypeFace(kaiType) }

        tv_size_square?.setOnClickListener {
            val containerLp = cl_picture_container.layoutParams as ConstraintLayout.LayoutParams
            containerLp.height = 0
            containerLp.dimensionRatio = "1:1"
            cl_picture_container.layoutParams = containerLp
        }
        tv_size_full?.setOnClickListener {
            val containerLp = cl_picture_container.layoutParams as ConstraintLayout.LayoutParams
            containerLp.height = MATCH_PARENT
            containerLp.dimensionRatio = null
            cl_picture_container.layoutParams = containerLp
        }

        tv_go_friday?.setOnClickListener {
            //遍历所有应用
            val pkgInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val hasJike = pkgInfoList.filter { it.processName == jikePackageName }.isNotEmpty()
            val jikeIntent = Intent(ACTION_VIEW)
            jikeIntent.data = Uri.parse(if (hasJike) fridayLink else downJikeLink)
            startActivity(jikeIntent)
        }
        tv_set_wallpaper?.setOnClickListener { setBitmap(wallType) }

        tv_save?.setOnClickListener {
            doAsync {
                currentBitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                currentBitmap?.let {
                    val canvas = Canvas(it)
                    cl_picture_container?.draw(canvas)
                    getImageDir(requestSavePic)
                }
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
            }
        }

        tv_share?.setOnClickListener {
            doAsync {
                currentBitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                currentBitmap?.let {
                    val canvas = Canvas(it)
                    cl_picture_container?.draw(canvas)
                    getImageDir(requestSharePic)
                }
            }
        }

        tv_switch_en?.setOnClickListener { switchLanguage(enType) }
        tv_switch_cn?.setOnClickListener { switchLanguage(cnType) }

        v_bubble_color_blue?.setOnClickListener { changeBubbleColor(blue) }
        v_bubble_color_white?.setOnClickListener { changeBubbleColor(white) }
        v_bubble_color_yellow?.setOnClickListener { changeBubbleColor(yellow) }
        v_bg_color_blue?.setOnClickListener { changeBgColor(blue) }
        v_bg_color_white?.setOnClickListener { changeBgColor(white) }
        v_bg_color_yellow?.setOnClickListener { changeBgColor(yellow) }
        v_text_color_white?.setOnClickListener { changeTextColor(white) }
        v_text_color_black?.setOnClickListener { changeTextColor(black) }

        tv_custom_bubble_color.setOnClickListener { showInputDialog(bubbleType) }
        tv_custom_bg_color.setOnClickListener { showInputDialog(bgType) }
        tv_custom_text_color.setOnClickListener { showInputDialog(textType) }
        tv_more_bubble_color.setOnClickListener { showPickColorDialog(bubbleType) }
        tv_more_bg_color.setOnClickListener { showPickColorDialog(bgType) }
        tv_more_text_color.setOnClickListener { showPickColorDialog(textType) }

        cl_picture_container.setOnClickListener {
            if (cl_controller?.visibility == GONE) {
                cl_controller?.visibility = VISIBLE
                ib_copyright?.visibility = VISIBLE
                ib_donate?.visibility = VISIBLE
            } else {
                cl_controller?.visibility = GONE
                ib_copyright?.visibility = GONE
                ib_donate?.visibility = GONE
                tv_question?.visibility = VISIBLE
                tv_today?.visibility = VISIBLE
                tv_is_friday?.visibility = VISIBLE
                tv_color_name?.visibility = VISIBLE
                tv_triangle?.visibility = VISIBLE
            }
        }

        cl_picture_container?.setOnLongClickListener {
            val imgMenu = listOf("清除图片", "重新选择", "FIX_XY", "FIT_CENTER", "CENTER", "CENTER_CROP", "CENTER_INSIDE")
            selector("修改图片", imgMenu) { _, i ->
                when (i) {
                    0 -> {
                        imageList.clear()
                        iv_img_bg?.setImageBitmap(null)
                    }
                    1 -> openImagePicker()
                    2 -> iv_img_bg?.scaleType = ImageView.ScaleType.FIT_XY
                    3 -> iv_img_bg?.scaleType = ImageView.ScaleType.FIT_CENTER
                    4 -> iv_img_bg?.scaleType = ImageView.ScaleType.CENTER
                    5 -> iv_img_bg?.scaleType = ImageView.ScaleType.CENTER_CROP
                    6 -> iv_img_bg?.scaleType = ImageView.ScaleType.CENTER_INSIDE
                }
            }
            true
        }
        tv_question.setOnClickListener {
            tv_question?.visibility = if (tv_question?.visibility == GONE) VISIBLE else GONE
            tv_triangle?.visibility = tv_question?.visibility ?: GONE
        }
        tv_is_friday.setOnClickListener {
            tv_is_friday?.visibility = if (tv_is_friday?.visibility == GONE) VISIBLE else GONE
        }
        tv_today.setOnClickListener {
            tv_today?.visibility = if (tv_today?.visibility == GONE) VISIBLE else GONE
        }
        tv_color_name.setOnClickListener {
            tv_color_name?.visibility = if (tv_color_name?.visibility == GONE) VISIBLE else GONE
        }

        ib_donate?.setOnClickListener { startActivity(Intent(this, DonateActivity::class.java)) }
        ib_copyright?.setOnClickListener { startActivity(Intent(this, CopyrightActivity::class.java)) }
    }


    private fun getImageDir(code: Int) {
        val sm = getSystemService(StorageManager::class.java)
        val volume = sm?.primaryStorageVolume
        volume?.createAccessIntent(DIRECTORY_PICTURES)?.also {
            startActivityForResult(it, code)
        }
    }


    private fun setBitmap(type: Int) {
        try {
            val wpm = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
            doAsync {
                val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                val canvas = Canvas(bitmap)
                cl_picture_container?.draw(canvas)
                wpm.setBitmap(bitmap, null, true, if (type == wallType) FLAG_SYSTEM
                else FLAG_LOCK)
                uiThread {
                    toast("Ojbk！")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showInputDialog(type: Int) {
        val inputView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_input, null)

        val titleString = when (type) {
            bubbleType -> getString(R.string.bubble)
            bgType -> getString(R.string.background)
            textType -> getString(R.string.text)
            else -> getString(R.string.bubble)
        }
        val colorDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.custom_some_color, titleString))
                .setView(inputView)
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .create()
        colorDialog.show()
        colorDialog.getButton(BUTTON_POSITIVE).setOnClickListener {
            val colorString = inputView.et_color_input.text.toString()
            if (colorString.startsWith("#") && (colorString.length == 7 || colorString.length == 7)) {
                when (type) {
                    bubbleType -> changeBubbleColor(Color.parseColor(colorString))
                    bgType -> changeBgColor(Color.parseColor(colorString))
                    textType -> changeTextColor(Color.parseColor(colorString))
                    else -> changeBubbleColor(Color.parseColor(colorString))
                }
                colorDialog.dismiss()
            } else {
                Toast.makeText(this, R.string.right_color, LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshTheme() {
        cl_picture_container?.setBackgroundColor(FridayPreference.getBgColor())
        changeBubbleColor(FridayPreference.getBubbleColor())
        changeTextColor(FridayPreference.getTextColor())
        tv_color_name?.text = FridayPreference.getColorName()
        Log.i("friday", FridayPreference.getLang().toString())
        if (FridayPreference.getLang() == cnType) {
            switchCnTypeFace(FridayPreference.getCnFontType())
        } else {
            switchEnTypeFace(FridayPreference.getEnFontType())
        }
        tv_color_name?.paint?.isFakeBoldText = true
        tv_is_friday?.paint?.isFakeBoldText = true
        tv_question?.paint?.isFakeBoldText = true

        arrayOf(tv_face_type_kai, tv_face_type_hei, tv_face_type_fang_song, tv_face_type_shu_song,
                tv_size_full, tv_size_square, tv_set_wallpaper, tv_share, tv_save,
                tv_go_friday, tv_switch_en, tv_switch_cn, ib_copyright, ib_donate).forEach { setBg(it) }

        v_bubble_color_white.background = ThemeUtil.customShape(white, white, 0, ThemeUtil.dip2px(10))
        v_bubble_color_yellow.background = ThemeUtil.customShape(yellow, yellow, 0, ThemeUtil.dip2px(10))
        v_bubble_color_blue.background = ThemeUtil.customShape(blue, blue, 0, ThemeUtil.dip2px(10))
        v_bg_color_white.background = ThemeUtil.customShape(white, white, 0, ThemeUtil.dip2px(10))
        v_bg_color_yellow.background = ThemeUtil.customShape(yellow, yellow, 0, ThemeUtil.dip2px(10))
        v_bg_color_blue.background = ThemeUtil.customShape(blue, blue, 0, ThemeUtil.dip2px(10))
        v_text_color_white.background = ThemeUtil.customShape(white, white, 0, ThemeUtil.dip2px(10))
        v_text_color_black.background = ThemeUtil.customShape(black, black, 0, ThemeUtil.dip2px(10))


    }

    private fun setBg(view: View?) {
        view?.post {
            view.background = ThemeUtil.customShape(blue, blue, 0,
                    view.height.toFloat() / 2)
        }
    }

    private fun changeBubbleColor(color: Int) {
        FridayPreference.setBubbleColor(color)
        tv_question?.post {
            tv_question?.background = ThemeUtil.customShape(color, color, 0,
                    tv_question.height.toFloat() / 2)
        }
        tv_triangle?.setColor(color)
    }

    private fun changeBgColor(color: Int) {
        FridayPreference.setBgColor(color)
        cl_picture_container.setBackgroundColor(color)
        if (color == blue) {
            tv_color_name?.text = "即刻蓝"
        }
        if (color == yellow) {
            tv_color_name?.text = "即刻黄"
        }
        if (color == white) {
            tv_color_name?.text = "白"
        }
        if (color == black) {
            tv_color_name?.text = "黑"
        }
    }

    private fun changeTextColor(color: Int) {
        FridayPreference.setTextColor(color)
        tv_question?.setTextColor(color)
        tv_is_friday?.setTextColor(color)
        tv_today?.setTextColor(color)
        tv_color_name?.setTextColor(color)
    }

    private val enFontList = arrayOf("AmaticSC-Regular.ttf", "Courgette-Regular.ttf", "IndieFlower.ttf",
            "Kalam-Regular.ttf", "Laila-Regular.ttf", "Molle-Regular.ttf", "NovaFlat.ttf",
            "PermanentMarker-Regular.ttf", "PlayfairDisplay-Regular.ttf", "Teko-Regular.ttf",
            "YanoneKaffeesatz-Regular.ttf")

    private fun switchCnTypeFace(fontType: Int) {
        FridayPreference.setCnFontType(fontType)
        val typeface = when (fontType) {
            shuSongType -> Typeface.createFromAsset(assets, "FZSSJW.TTF")
            fangSongType -> Typeface.createFromAsset(assets, "FZFSJW.TTF")
            heiType -> Typeface.createFromAsset(assets, "FZHTJW.TTF")
            kaiType -> Typeface.createFromAsset(assets, "FZKTJW.TTF")
            else -> Typeface.createFromAsset(assets, "FZKTJW.TTF")
        }
        tv_is_friday?.typeface = typeface
        tv_question?.typeface = typeface
    }

    private fun switchEnTypeFace(fontName: String) {
        FridayPreference.setEnFontType(fontName)
        val typeface = Typeface.createFromAsset(assets, if (fontName.isEmpty()) "AmaticSC-Regular.ttf" else fontName)
        tv_is_friday?.typeface = typeface
        tv_question?.typeface = typeface
    }

    private fun switchLanguage(langType: Int) {
        Log.i("friday", "change lang = $langType")
        FridayPreference.setLang(langType)
        tv_question?.text = if (langType == enType) "Is today Friday?" else "今天是周五吗？"
        if (today.get(DAY_OF_WEEK) == 6) {
            tv_is_friday?.text = if (langType == enType) "YES!" else "是"
        } else {
            tv_is_friday?.text = if (langType == enType) "NO" else "不是"
        }
        if (langType == enType) {
            sv_font_en_container?.visibility = VISIBLE
            sv_font_cn_container?.visibility = GONE
        } else {
            sv_font_en_container?.visibility = GONE
            sv_font_cn_container?.visibility = VISIBLE
            setBg(tv_face_type_kai)
            setBg(tv_face_type_hei)
            setBg(tv_face_type_fang_song)
            setBg(tv_face_type_shu_song)
        }
    }

    private fun setDate() {
        tv_today?.text = "${getWeekDayString(today.get(DAY_OF_WEEK))} ${today.get(YEAR)}" +
                ".${today.get(MONTH) + 1}.${today.get(DAY_OF_MONTH)}"
        if (today.get(DAY_OF_WEEK) == 6) {
            tv_is_friday?.text = "是"
        } else {
            tv_is_friday?.text = "不是"
        }
    }

    private fun getWeekDayString(day: Int): String {
        return when (day) {
            1 -> "Sunday"
            2 -> "Monday"
            3 -> "Tuesday"
            4 -> "Wednesday"
            5 -> "Thursday"
            6 -> "Friday"
            7 -> "Saturday"
            else -> "Friday"
        }
    }

    private fun showPickColorDialog(type: Int) {
        val titleString = when (type) {
            bubbleType -> getString(R.string.bubble)
            bgType -> getString(R.string.background)
            textType -> getString(R.string.text)
            else -> getString(R.string.bubble)
        }
        val inputView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_pick_color, null)

        inputView.gv_pick_color.adapter = GridColorAdapter(Util.colorList, this)
        inputView.gv_pick_color.setOnItemClickListener { _, _, position, _ ->
            val colorString = Util.colorList[position]
            when (type) {
                bubbleType -> changeBubbleColor(Color.parseColor(colorString))
                bgType -> {
                    changeBgColor(Color.parseColor(colorString))
                    tv_color_name?.text = Util.colorNameList[position]
                    FridayPreference.setColorName(Util.colorNameList[position])
                }
                textType -> changeTextColor(Color.parseColor(colorString))
                else -> changeBubbleColor(Color.parseColor(colorString))
            }
        }
        val colorDialogBuilder = AlertDialog.Builder(this)
                .setTitle(getString(R.string.more_some_color, titleString))
                .setView(inputView)
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { _, _ -> }

        if (type == bgType) {
            colorDialogBuilder.setNeutralButton("选择图片") { _, _ ->
                openImagePicker()
            }
        }
        colorDialogBuilder
                .create()
                .show()
    }

    private var imageList: ArrayList<Image> = ArrayList()

    fun openImagePicker() {
        imageList.clear()
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#4A4E69")         //  Toolbar color
                .setStatusBarColor("#4A4E69")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#8C6070")     //  ProgressBar color
                .setBackgroundColor("#e0f7fa")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Pictures")           //  Folder title (works with FolderMode = true)
//                    .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("确定")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                .setMaxSize(1)                     //  Max images can be selected
                .setSavePath("cut_black")         //  Image capture folder name
                .setSelectedImages(imageList)          //  Selected images
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setRequestCode(Constants.PICK_PICTURE)                //  Set request code, default Config.RC_PICK_IMAGES
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start()
    }

    private fun saveFile(uri: Uri, next: (Uri) -> Unit) {
        contentResolver?.takePersistableUriPermission(uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val df = DocumentFile.fromTreeUri(this, uri)
        val fridayPicDir = df?.findFile("Friday") // 获取文件夹
        val format = SimpleDateFormat("yyyy-MM-dd-hh_mm_ss", Locale.CHINA)

        fridayPicDir?.createFile("image/jpg", "${format.format(today.time)}.jpg")?.also {
            doAsync {
                val pfd = contentResolver.openFileDescriptor(it.uri, "w")
                // 打开文件流，要注意是读取还是写入，r和w，使用w会重写覆盖原文件
                pfd?.let {p ->
                    val fileOutputStream = FileOutputStream(p.fileDescriptor)
                    currentBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.close()
                    next(it.uri)
                    uiThread {
                        toast("Ojbk！")
                    }
                }
            }
        }
    }

    private fun deleteFile(uri: Uri) {
        contentResolver?.takePersistableUriPermission(uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        val df = DocumentFile.fromTreeUri(this, uri)
        val fridayPicDir = df?.findFile("Friday") // 获取文件夹
        fridayPicDir?.listFiles()?.forEach {
            if (it.name?.contains("donation") == false) {
                if (Date().time - it.lastModified() > 3 * 24 * 3600 * 1000) {
                    it.delete()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        today.time = Date()
        data?.data?.let { uri ->
            if (requestCode == requestSavePic && resultCode == Activity.RESULT_OK) {
                saveFile(uri) {}
            } else if (requestCode == requestSharePic && resultCode == Activity.RESULT_OK) {
                saveFile(uri) {
                    val shareIntent = Intent(ACTION_SEND)
                    shareIntent.type = "image/*"
                    shareIntent.putExtra(Intent.EXTRA_STREAM, it)
                    startActivity(Intent.createChooser(shareIntent, "分享到..."))
                }
            } else if (requestCode == requestDeletePic && resultCode == Activity.RESULT_OK) {
                deleteFile(uri)
            }
            else if (resultCode == Activity.RESULT_OK) {
                imageList = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)
                try {
                    alert("长按背景空白处可修改图片", "是否确定选择该图片？") {
                        yesButton {
                            if (imageList.size > 0) {
                                val bitmap = BitmapFactory.decodeFile(imageList[0].path)
                                iv_img_bg.setImageBitmap(bitmap)
                            }
                        }
                        negativeButton("重新选择") {
                            openImagePicker()
                        }
                        neutralPressed("不选了") {}
                    }.show()

                } catch (e: Exception) {
                    Log.e("Exception", e.message, e)
                }
            }
        }
    }
}
