package info.free.duangjike.friday

import android.Manifest
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
import androidx.core.content.FileProvider
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import info.free.duangjike.R
import info.free.duangjike.ThemeUtil
import info.free.duangjike.Util
import info.free.duangjike.Util.clearOldPicture
import info.free.duangjike.friday.Constants.REQUEST_PERMISSION
import kotlinx.android.synthetic.main.activity_friday.*
import kotlinx.android.synthetic.main.layout_dialog_input.view.*
import kotlinx.android.synthetic.main.layout_dialog_pick_color.view.*
import org.jetbrains.anko.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, flag)
        setContentView(R.layout.activity_friday)
        white = resources.getColor(R.color.jikeWhite)
        blue = resources.getColor(R.color.jikeBlue)
        yellow = resources.getColor(R.color.jikeYellow)
        black = resources.getColor(R.color.jikeBlack)
        methodRequiresPermission()
        initData()
        setDate()
        refreshTheme()
        switchLanguage(FridayPreference.getLang())
        setEvent()
        clearOldPicture()
    }

    @AfterPermissionGranted(REQUEST_PERMISSION)
    private fun methodRequiresPermission() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    this, getString(R.string.request_permission),
                    REQUEST_PERMISSION, *perms
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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
                val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                today.time = Date()
                val canvas = Canvas(bitmap)
                cl_picture_container?.draw(canvas)
                val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss", Locale.CHINA)
                Util.saveBitmapFile(bitmap, format.format(today.time))
                uiThread {
                    toast("Ojbk！")
                }
            }

        }

        tv_share?.setOnClickListener {
            doAsync {
                val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                today.time = Date()
                val canvas = Canvas(bitmap)
                cl_picture_container?.draw(canvas)
                val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss", Locale.CHINA)
                val file = Util.saveBitmapFile(bitmap, format.format(today.time))
                val uri = FileProvider.getUriForFile(this@FridayActivity, applicationContext.packageName + ".provider", file)
                uiThread {
                    val shareIntent = Intent(ACTION_SEND)
                    shareIntent.type = "image/*"
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    it.startActivity(Intent.createChooser(shareIntent, "分享到..."))
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

        setBg(tv_face_type_kai)
        setBg(tv_face_type_hei)
        setBg(tv_face_type_fang_song)
        setBg(tv_face_type_shu_song)
        setBg(tv_size_full)
        setBg(tv_size_square)
        setBg(tv_set_wallpaper)
        setBg(tv_share)
        setBg(tv_save)
        setBg(tv_go_friday)
        setBg(tv_switch_en)
        setBg(tv_switch_cn)
        setBg(ib_copyright)
        setBg(ib_donate)

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
        tv_triangle.setColor(color)
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
        tv_color_name.setTextColor(color)
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
            tv_is_friday.text = if (langType == enType) "YES!" else "是"
        } else {
            tv_is_friday.text = if (langType == enType) "NO" else "不是"
        }
        if (langType == enType) {
            sv_font_en_container.visibility = VISIBLE
            sv_font_cn_container.visibility = GONE
        } else {
            sv_font_en_container.visibility = GONE
            sv_font_cn_container.visibility = VISIBLE
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
            tv_is_friday.text = "是"
        } else {
            tv_is_friday.text = "不是"
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
        val colorDialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.more_some_color, titleString))
                .setView(inputView)
                .setPositiveButton("OK") { _, _ -> }
                .setNeutralButton("选择图片") { _, _ ->
                    openImagePicker()
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()
        colorDialog.show()
    }

    private var imageList: ArrayList<Image> = ArrayList()
    private var pathList: ArrayList<String> = ArrayList()

    fun openImagePicker() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            imageList = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES)
            try {
                var fitSize = false
                alert("如需修改请重新选择图片", "是否拉伸图片填满屏幕？") {
                    yesButton { iv_img_bg.scaleType = ImageView.ScaleType.FIT_XY }
                    noButton { iv_img_bg.scaleType = ImageView.ScaleType.CENTER_CROP }
                }.show()
                if (imageList.size > 0) {
                    val imgPath = imageList[0].path
                    val bitmap = BitmapFactory.decodeFile(imageList[0].path)
                    iv_img_bg.setImageBitmap(bitmap)
                    cl_picture_container.setBackgroundColor(Color.TRANSPARENT)
                }
                imageList.clear()
            } catch (e: Exception) {
                Log.e("Exception", e.message, e)
            }
        }
    }
}
