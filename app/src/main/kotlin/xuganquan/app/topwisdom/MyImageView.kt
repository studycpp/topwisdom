package xuganquan.app.topwisdom

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageView.ScaleType
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Math.abs
import java.lang.Math.sqrt
import java.util.*
import kotlin.collections.ArrayList


class XGQImageView : AppCompatImageView {

    public  var  enableMove=true
    public  var  enableScale=false


    private var mode = 0
    private val MODE_NONE = -1
    private val MODE_TRANSLATION = 0
    private val MODE_SCALE = 1

    private var move_oldx = 0f
    private var move_oldy = 0f

    private var move_newx = 0f
    private var move_newy = 0f

    private var scale_oldx1 = 0f
    private var scale_oldx2 = 0f
    private var scale_oldy1= 0f
    private var scale_oldy2= 0f

    private var scale_newx1= 0f
    private var scale_newx2= 0f
    private var scale_newy1= 0f
    private var scale_newy2= 0f



    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )


    override fun performClick(): Boolean {
        return super.performClick()
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                move_oldx=event.x
                move_oldy=event.y
            }
            MotionEvent.ACTION_MOVE -> {
                if(event.pointerCount==1){
                    mode = MODE_TRANSLATION
                    move_newx=event.x
                    move_newy=event.y
                    if (enableMove)
                         move()
                }else{
                    mode = MODE_SCALE
                    newXY(event)
                    if (enableScale)
                          move()
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mode = MODE_SCALE
                oldXY(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
               // saveXY()
            }
             MotionEvent.ACTION_UP -> {}
        }
        return super.onTouchEvent(event)
    }

    private fun move() {
        when (mode) {
            MODE_TRANSLATION -> {
                val dx = (move_newx- move_oldx).toInt()
                val dy = (move_newy- move_oldy).toInt()
                val l = (left + dx)
                //在父控件中的右边位置 = 在左边的位置 + 自己的宽
                val r = l + width
                val t = (top + dy)
                val b = t + height
                //当水平或者垂直滑动距离大于10,才算拖动事件
                if (abs(dy) > 10 || abs(dx) > 10) {
                    this.layout(l, t, r, b)
                }

            }
            MODE_SCALE -> {
                //缩放
                if(abs(scale_oldx2 - scale_oldx1)>0.0001){
                    val xs=(scale_newx2-scale_newx1)/(scale_oldx2 - scale_oldx1)
                    if ((xs in 0.8f..4f)){
                        scaleX=xs
                    }
                }

                if(abs(scale_oldy2 - scale_oldy1)>0.0001) {
                    val ys = (scale_newy2 - scale_newy1) / (scale_oldy2 - scale_oldy1)
                    if ((ys in 0.8f..4f)) {
                        scaleY = ys
                    }
                }

            }
        }
        mode=MODE_NONE
    }


    fun oldXY(event: MotionEvent)  {
        scale_oldx1 = event.getX(0)
        scale_oldx2 = event.getX(event.pointerCount -1)
        scale_oldy1 = event.getY(0)
        scale_oldy2  = event.getY(event.pointerCount -1)
    }
    fun newXY(event: MotionEvent)  {
        scale_newx1 = event.getX(0)
        scale_newx2 = event.getX(event.pointerCount -1)
        scale_newy1 = event.getY(0)
        scale_newy2  = event.getY(event.pointerCount -1)
    }


}

