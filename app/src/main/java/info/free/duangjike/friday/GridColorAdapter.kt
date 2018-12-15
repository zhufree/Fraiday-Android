package info.free.duangjike.friday

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.view.LayoutInflater
import info.free.duangjike.R
import info.free.duangjike.ThemeUtil
import kotlinx.android.synthetic.main.item_color.view.*


class GridColorAdapter(private val colorList: Array<String>, private val mContext: Context) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ColorPickViewHolder
        val color = Color.parseColor(colorList[position])
        if (convertView == null) {
            val newConvertView = LayoutInflater.from(mContext).inflate(R.layout.item_color, null)
            newConvertView.background = ThemeUtil.customShape(color, color, 0, ThemeUtil.dip2px(15))
//            holder = ColorPickViewHolder()
//            /*得到各个控件的对象*/
//            holder.llHolder = newConvertView.ll_color_container
//            newConvertView.tag = holder//绑定ViewHolder对象
//            holder.llHolder.background = ThemeUtil.customShape(color, color, 0, ThemeUtil.dip2px(15))
            return newConvertView
        } else {
//            holder = convertView.tag as ColorPickViewHolder//取出ViewHolder对象
            convertView.background = ThemeUtil.customShape(color, color, 0, ThemeUtil.dip2px(15))
            return convertView
        }

    }

    override fun getItem(position: Int): Any {
        return colorList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return colorList.size
    }

    class ColorPickViewHolder {
        lateinit var llHolder: LinearLayout
    }
}