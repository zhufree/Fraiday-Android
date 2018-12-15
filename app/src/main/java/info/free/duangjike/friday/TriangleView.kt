package info.free.duangjike.friday

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import info.free.duangjike.R

/**
 * Created by zhufree on 2018/12/12.
 *
 */

class TriangleView : View {

    private val TOP = 0
    private val BOTTOM = 1
    private val RIGHT = 2
    private val LEFT = 3
    private val DEFAULT_WIDTH = 30
    private val DEFAULT_HEIGHT = 18
    private val DEFAULT_COLOR = R.color.jikeWhite
    private var mPaint = Paint()
    private var mWidth = 0
    private var mHeight = 0
    private var mDirection = TOP
    private var mPath = Path()

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TriangleView, 0, 0)
        mPaint.color = typedArray.getColor(R.styleable.TriangleView_trv_color, ContextCompat.getColor(getContext(), DEFAULT_COLOR))
        mDirection = typedArray.getInt(R.styleable.TriangleView_trv_direction, mDirection)
        typedArray.recycle()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (mWidth == 0 || widthMode != MeasureSpec.EXACTLY) {
            mWidth = DEFAULT_WIDTH
        }
        if (mHeight == 0 || heightMode != MeasureSpec.EXACTLY) {
            mHeight = DEFAULT_HEIGHT
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (mDirection) {
            TOP -> {
                mPath.moveTo(0f, mHeight.toFloat())
                mPath.lineTo(mWidth.toFloat(), mHeight.toFloat())
                mPath.lineTo(mWidth.toFloat() / 2, 0f)
            }
            BOTTOM -> {
                mPath.moveTo(0f, 0f)
                mPath.lineTo(mWidth.toFloat() / 2, mHeight.toFloat())
                mPath.lineTo(mWidth.toFloat(), 0f)
            }
            RIGHT -> {
                mPath.moveTo(0f, 0f)
                mPath.lineTo(0f, mHeight.toFloat())
                mPath.lineTo(mWidth.toFloat(), mHeight.toFloat() / 2)
            }
            LEFT -> {
                mPath.moveTo(0f, mHeight.toFloat() / 2)
                mPath.lineTo(mWidth.toFloat(), mHeight.toFloat())
                mPath.lineTo(mWidth.toFloat(), 0f)
            }
        }

        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }

    fun setColor(color: Int) {
        mPaint.color = color
        invalidate()
    }
}
