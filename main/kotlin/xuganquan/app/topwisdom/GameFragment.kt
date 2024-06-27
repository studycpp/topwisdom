package xuganquan.app.topwisdom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import xuganquan.app.topwisdom.FGames.*
import xuganquan.app.topwisdom.databinding.FragmentGameBinding


class GameFragment : Fragment(){

    lateinit var  binding:FragmentGameBinding

    lateinit var vto: ViewTreeObserver

    var mHide=true
    val mImages:MutableList<Drawable? > = mutableListOf()
    var mID=0
    var mWidth:Int=0
    var mHeight:Int=0
    lateinit var mLayout:ConstraintLayout
    var mNewID=90000
    lateinit var mListener:IOnMainNotify


    fun NotifyResult(result:Boolean){
        if(result)
            MyConfig.theGameResult=MyConst.C_game_right
        else
            MyConfig.theGameResult=MyConst.C_game_wrong
        mListener.OnMainNotify(MyConst.C_fragment_game,MyConst.C_action_result,MyConfig.theGameResult)
    }
    fun refresh(){
        MyConfig.currentGameId=MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_id]
        mNewID=90000
        mLayout.removeAllViews() //移除所有视图和属性
        mListener.OnMainNotify(MyConst.C_fragment_game,MyConst.C_action_refresh_info)
        mImages.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentGameBinding.inflate ( inflater, container, false)
        mID=binding.root.id
        //为了得到大小 加监视器
        vto= binding.idGameLayout.getViewTreeObserver()
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.idGameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                mLayout = binding.idGameLayout
                mWidth  = binding.idGameLayout.width
                mHeight = binding.idGameLayout.height
                loadGame()
               }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MyConfig.currentGameId="null"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity){
            mListener=context
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onResume() {
        super.onResume()
        /*        arguments?.let {
        param1 = it.getInt(MyConst.C_bundle_game_number)
        }*/
        MyConfig.theNowRecord=MySQLite.readOneRowRecordbyIdStr(MyConfig.allIdTitle.ids[MyConfig.requireNumber-1])
        if (!MyConfig.theNowRecord.ret) {
            return
        }

    }

    fun mAlertError(){
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(true)
            setTitle(R.string.str_error)
            setIcon(R.drawable.id_svg_ret_wrong)
            setMessage(R.string.str_redo)
            val alert = builder.create()
            setPositiveButton(
                R.string.str_yes,  {
                        _, _->//(DialogInterface dialog, int which)
                    alert.dismiss()
                }
            )
        }
        builder.show()
    }


    fun mTipMessage(str:String){
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setCancelable(true)
            setMessage(str)
            val alert = builder.create()
            alert.setCanceledOnTouchOutside(true)
            setPositiveButton(
                R.string.str_yes,  {
                        _, _->//(DialogInterface dialog, int which)
                    alert.dismiss()
                }
            )
        }
        builder.show()
    }
/////////////////////////////////////////////////////////////

    fun mAnimate10(view:View){
        view.apply {
            animate()
                .scaleX(0.2f)
                .scaleY(0.2f)
                .setDuration(1000L)
                .setListener(object : AnimatorListenerAdapter() {
                    override  fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                    }
                })

        }
    }

    fun mAnimate11(view:View) {
        view.apply {
            animate()
                .rotation(360F)
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(500L)
                .setListener(object : AnimatorListenerAdapter() {
                    override  fun onAnimationEnd(animation: Animator) {
                        visibility = View.INVISIBLE

                    }
                })

        }
    }


/////////////////////////////////////////////////////////////
    fun loadGame(){
        MyConfig.nowNumber=MyConfig.requireNumber
        MyConfig["nowNumber"]=MyConfig.nowNumber

        refresh()
        ////////////////////////////////////////////////////////
        when(MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_id]){
            "id_sunning"             ->     id_sunning()
            "id_click_coins"         ->     id_click_coins()
            "id_menu"                ->     id_menu()
            "id_menu_rabbit"         ->     id_menu_rabbit()
            "id_doge"                ->     id_doge()
            "id_nine_palace_grids"   ->     id_nine_palace_grids()
            "id_unlock_pattern"      ->     id_unlock_pattern()
            "id_fun_cat"             ->     id_fun_cat()
            "id_catch_fish"          ->     id_catch_fish()
            "id_ai_area"             ->     id_ai_area()
            "id_fable_bamboo"        ->     id_fable_bamboo()
            "id_msg_crypt"           ->     id_msg_crypt()
            "id_farm"                ->     id_farm()
            "id_china_tea"           ->     id_china_tea()
            "id_football_game"       ->     id_football_game()
            "id_three_button"        ->     id_three_button()
            "id_observe_traffic"     ->     id_observe_traffic()
            "id_establish_equality"  ->     id_establish_equality()
            "id_steal_case"          ->     id_steal_case()
            "id_revelation"          ->     id_revelation()
            else->id_sunning()
        }

    }



}//end ///////////

/*
准备资源
初始状态
监控输入
判断和保存状态
*/




