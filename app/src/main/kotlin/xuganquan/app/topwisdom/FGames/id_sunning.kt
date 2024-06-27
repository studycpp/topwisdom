package xuganquan.app.topwisdom.FGames

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import xuganquan.app.topwisdom.*
import java.io.ByteArrayInputStream


fun GameFragment.id_sunning(){
    mLayout.visibility=View.INVISIBLE

    val C_NowGameId="id_sunning"

    //准备资源
    for (i in 0..2){
        val  imageStream = ByteArrayInputStream(MyConfig.theNowRecord.data.dataBOLBs[i])
        val  bitmap= Drawable.createFromStream(imageStream,i.toString())
        mImages.add(bitmap)
    }

    class TheGame {
        //left, top,  right , bottom  -1 mean no need
        val mViewMargins=arrayOf(
            arrayOf(0,50,0,0),
            arrayOf(0,50,0,0),
            arrayOf(0,70,100,0)
        )
        //width height FIT_XY
        val mViewSizes=arrayOf(
            arrayOf(0,mHeight*3/5),
            arrayOf(0,mHeight*3/5),
            arrayOf(300,150)
        )

        fun animate1() {
            // Animate the loading view to 0% opacity. After the animation ends,
            // set its visibility to GONE as an optimization step (it won't
            // participate in layout passes, etc.)
            view1.animate()
                .alpha(0f)
                .setDuration(2000L)
                .setListener(null)

            view2.apply {
                // Set the content view to 0% opacity but visible, so that it is visible
                // (but fully transparent) during the animation.
                alpha = 0f
                visibility = View.VISIBLE

                // Animate the content view to 100% opacity, and clear any animation
                // listener set on the view.
                animate()
                    .alpha(1f)
                    .setDuration(2100L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override  fun onAnimationEnd(animation: Animator) {
                            if (C_NowGameId!=MyConfig.currentGameId) {
                                return
                            }
                            NotifyResult(true)

                        }
                    })
            }
            view3.apply {
                animate()
                    .scaleX(0.1f)
                    .scaleY(0.1f)
                    .setDuration(1000L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override  fun onAnimationEnd(animation: Animator) {
                            visibility = View.INVISIBLE
                        }
                    })

            }

        }
        val view1 = ImageView(mLayout.context).apply {
                visibility = View.INVISIBLE
                id = mNewID++
                layoutParams = ConstraintLayout.LayoutParams(mWidth, mHeight).apply {

                    startToStart = mID
                    topToTop = mID
                    endToEnd = mID
                    //bottomToBottom = mID

                    leftMargin = mViewMargins[0][0]
                    topMargin = mViewMargins[0][1]
                    rightMargin = mViewMargins[0][2]
                    //bottomMargin=mViewMargins[0][3]

                    scaleType = ImageView.ScaleType.FIT_XY
                    width = mViewSizes[0][0]
                    height = mViewSizes[0][1]


                }
                setImageDrawable(mImages[0])
                mLayout.addView(this)
                visibility = View.VISIBLE
            }
        val view2 = ImageView(mLayout.context).apply {
                visibility = View.INVISIBLE
                id = mNewID++
                layoutParams = ConstraintLayout.LayoutParams(mWidth, mHeight).apply {

                    startToStart = mID
                    topToTop = mID
                    endToEnd = mID
                    //bottomToBottom = mID

                    leftMargin = mViewMargins[1][0]
                    topMargin = mViewMargins[1][1]
                    rightMargin = mViewMargins[1][2]
                    //bottomMargin=mViewMargins[1][3]

                    scaleType = ImageView.ScaleType.FIT_XY
                    width = mViewSizes[1][0]
                    height = mViewSizes[1][1]


                }
                setImageDrawable(mImages[1])
                mLayout.addView(this)
                // visibility=View.VISIBLE
            }
        val view3 = XGQImageView(mLayout.context).apply {
                visibility = View.INVISIBLE
                id = mNewID++
                layoutParams = ConstraintLayout.LayoutParams(mWidth, mHeight).apply {

                    // startToStart = mID
                    topToTop = view1.id
                    endToEnd = view1.id
                    //bottomToBottom = mID

                    // leftMargin  =mViewMargins[2][0]
                    topMargin = mViewMargins[2][1]
                    rightMargin = mViewMargins[2][2]
                    //bottomMargin=mViewMargins[2][3]

                    scaleType = ImageView.ScaleType.FIT_XY
                    width = mViewSizes[2][0]
                    height = mViewSizes[2][1]

                    alpha = 0.4F

                }
                setImageDrawable(mImages[2])
                mLayout.addView(this)
                enableMove = true
                enableScale = true
                visibility = View.VISIBLE
                setOnClickListener {
                    isEnabled=false
                    animate1()
                }
            }

    }

////////////////////////////////////////////////////////////////////
    val theGame=TheGame()
    mLayout.visibility=View.VISIBLE
}

