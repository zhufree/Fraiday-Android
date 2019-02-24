package info.free.duangjike.friday

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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import info.free.duangjike.R
import info.free.duangjike.ThemeUtil
import info.free.duangjike.Util
import info.free.duangjike.Util.clearOldPicture
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_friday.*
import kotlinx.android.synthetic.main.layout_dialog_input.view.*
import kotlinx.android.synthetic.main.layout_dialog_pick_color.view.*
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
    private val today = Calendar.getInstance(Locale.CHINA)
    private val fridayLink = "jike://page.jk/topic/565ac9dd4b715411006b5ecd"
    private val downJikeLink = "http://a.app.qq.com/o/simple.jsp?pkgname=com.ruguoapp.jike&ckey=CK1411402428437"
    private val jikePackageName = "com.ruguoapp.jike"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, flag)
        setContentView(R.layout.activity_friday)
        white = resources.getColor(R.color.jikeWhite)
        blue = resources.getColor(R.color.jikeBlue)
        yellow = resources.getColor(R.color.jikeYellow)
        black = resources.getColor(R.color.jikeBlack)
        setDate()
        refreshTheme()
        setEvent()
        clearOldPicture()
    }

    private fun setEvent() {
        tv_face_type_shu_song?.setOnClickListener {
            switchTypeFace(shuSongType)
        }
        tv_face_type_fang_song?.setOnClickListener {
            switchTypeFace(fangSongType)
        }
        tv_face_type_hei?.setOnClickListener { switchTypeFace(heiType) }
        tv_face_type_kai?.setOnClickListener { switchTypeFace(kaiType) }

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
            Flowable.create<Bitmap>({ emitter ->
                val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                today.time = Date()
                val canvas = Canvas(bitmap)
                cl_picture_container?.draw(canvas)
                emitter.onNext(bitmap)
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")
                        Util.saveBitmapFile(it, format.format(today.time))
                        Toast.makeText(this, "Ojbk！", LENGTH_SHORT).show()
                    }
        }

        tv_share?.setOnClickListener {
            Flowable.create<Bitmap>({ emitter ->
                today.time = Date()
                val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                val canvas = Canvas(bitmap)
                cl_picture_container?.draw(canvas)
                emitter.onNext(bitmap)
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        val format = SimpleDateFormat("yyyy-MM-dd-hh:mm:ss")
                        val shareIntent = Intent(ACTION_SEND)
                        shareIntent.type = "image/*"
                        val uri = Uri.fromFile(Util.saveBitmapFile(it, format.format(today.time)))
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                        this.startActivity(Intent.createChooser(shareIntent, "分享到..."))
                    }
        }

        tv_switch_en?.setOnClickListener {
            switchLanguage(enType)
        }
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
            } else {
                cl_controller?.visibility = GONE
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
    }

    private fun setBitmap(type: Int) {
        try {
            val wpm = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
            Flowable.create<Bitmap>({ emitter ->
                val bitmap = Bitmap.createBitmap(cl_picture_container.width, cl_picture_container.height,
                        Bitmap.Config.RGB_565)
                //使用Canvas，调用自定义view控件的onDraw方法，绘制图片
                val canvas = Canvas(bitmap)
                cl_picture_container?.draw(canvas)
                emitter.onNext(bitmap)
                emitter.onComplete()
            }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        wpm.setBitmap(it, null, true, if (type == wallType) FLAG_SYSTEM
                        else FLAG_LOCK)
                        Toast.makeText(this, "Ojbk！", LENGTH_SHORT).show()
                    }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showInputDialog(type: Int) {
        val inputView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_input, null)

        val titleString = when (type) {
            bubbleType -> "气泡"
            bgType -> "背景"
            textType -> "文字"
            else -> "气泡"
        }
        val colorDialog = AlertDialog.Builder(this)
                .setTitle("自定义${titleString}颜色")
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
                Toast.makeText(this, "请输入正确的颜色值", LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshTheme() {
        cl_picture_container?.setBackgroundColor(FridayPreference.getBgColor())
        changeBubbleColor(FridayPreference.getBubbleColor())
        changeTextColor(FridayPreference.getTextColor())
        tv_color_name?.text = FridayPreference.getColorName()
        switchTypeFace(FridayPreference.getFontType())
        tv_color_name?.paint?.isFakeBoldText = true
        tv_is_friday?.paint?.isFakeBoldText = true
        tv_question?.paint?.isFakeBoldText = true

        tv_face_type_kai.post {
            tv_face_type_kai?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_face_type_kai.height.toFloat() / 2)
        }
        tv_face_type_hei.post {
            tv_face_type_hei?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_face_type_hei.height.toFloat() / 2)
        }
        tv_face_type_fang_song.post {
            tv_face_type_fang_song?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_face_type_fang_song.height.toFloat() / 2)
        }
        tv_face_type_shu_song.post {
            tv_face_type_shu_song?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_face_type_shu_song.height.toFloat() / 2)
        }
        tv_size_full.post {
            tv_size_full?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_size_full.height.toFloat() / 2)
        }
        tv_size_square.post {
            tv_size_square?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_size_square.height.toFloat() / 2)
        }
        tv_set_wallpaper.post {
            tv_set_wallpaper?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_set_wallpaper.height.toFloat() / 2)
        }
        tv_share.post {
            tv_share?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_share.height.toFloat() / 2)
        }
        tv_save.post {
            tv_save?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_save.height.toFloat() / 2)
        }
        tv_go_friday.post {
            tv_go_friday?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_go_friday.height.toFloat() / 2)
        }
        tv_switch_en?.post {
            tv_switch_en?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_switch_en.height.toFloat() / 2)
        }
        tv_switch_cn?.post {
            tv_switch_cn?.background = ThemeUtil.customShape(blue, blue, 0,
                    tv_switch_en.height.toFloat() / 2)
        }
        v_bubble_color_white.background = ThemeUtil.customShape(white, white, 0, ThemeUtil.dip2px(10))
        v_bubble_color_yellow.background = ThemeUtil.customShape(yellow, yellow, 0, ThemeUtil.dip2px(10))
        v_bubble_color_blue.background = ThemeUtil.customShape(blue, blue, 0, ThemeUtil.dip2px(10))
        v_bg_color_white.background = ThemeUtil.customShape(white, white, 0, ThemeUtil.dip2px(10))
        v_bg_color_yellow.background = ThemeUtil.customShape(yellow, yellow, 0, ThemeUtil.dip2px(10))
        v_bg_color_blue.background = ThemeUtil.customShape(blue, blue, 0, ThemeUtil.dip2px(10))
        v_text_color_white.background = ThemeUtil.customShape(white, white, 0, ThemeUtil.dip2px(10))
        v_text_color_black.background = ThemeUtil.customShape(black, black, 0, ThemeUtil.dip2px(10))
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

    private fun switchTypeFace(fontType: Int) {
        FridayPreference.setFontType(fontType)
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

    private fun switchLanguage(langType: Int) {
        tv_question?.text = if (langType == enType) "Is today Friday?" else "今天是周五吗？"
        if (today.get(DAY_OF_WEEK) == 6) {
            tv_is_friday.text = if (langType == enType) "YES!" else "是"
        } else {
            tv_is_friday.text =  if (langType == enType) "NO" else "是"
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
            bubbleType -> "气泡"
            bgType -> "背景"
            textType -> "字体"
            else -> "气泡"
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
                .setTitle("更多${titleString}颜色")
                .setView(inputView)
                .setPositiveButton("OK") { _, _ -> }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()
        colorDialog.show()
    }
}
