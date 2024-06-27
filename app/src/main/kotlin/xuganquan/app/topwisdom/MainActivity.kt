package xuganquan.app.topwisdom

import android.R.*
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import xuganquan.app.topwisdom.databinding.ActivityMainBinding


interface IOnMainNotify{

    fun OnMainNotify(srcFragment:String, action:String, arg1:Int=0, arg2:String="")
}

class MainActivity : AppCompatActivity(),IOnMainNotify {

    private lateinit var binding: ActivityMainBinding

    //////////////

    //private  var mLastFragmentName:String=MyConst.C_fragment_game
    private  var mCurrentFragmentName:String=MyConst.C_fragment_game
    //music
    private var musicPlayer=MyMusicSoundsPlayer()
    //sound
    private var soundPlayer=MyMusicSoundsPlayer()

    //////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //data////////////////////////////

        //ui////////////////////////////
        /*
        var returnbolb = MySQLite.readOneBLOBbyIdStr("id_background")
        if (returnbolb.ret) {
            val buf = ByteArrayInputStream(returnbolb.dataBOLB)
            val bmp = BitmapFactory.decodeStream(buf)
            binding.idMainAllView.setImageBitmap(bmp)
        }
        */

     //设置事件
        fun playHoverSound(){
             if (MyConfig.soundSwitch)
                 soundPlayer.play("id_sound_hover",false)
        }
        //选项
        binding.idMainOption.setOnClickListener {
            playHoverSound()
            if (MyConst.C_fragment_option!=mCurrentFragmentName) {
                binding.idMainTitle.text = this.getText(R.string.str_option_title)
                binding.idMainBody.text=this.getText(R.string.str_option_info)
                changeFragment(MyConst.C_fragment_option)
            }else{
                    playGame()
            }

        }
        //任意选择对话框
        binding.idMainSelect.setOnClickListener {
            playHoverSound()
            if (MyConst.C_fragment_select!=mCurrentFragmentName) {
                binding.idMainTitle.text = this.getText(R.string.str_select_title)
                binding.idMainBody.text=this.getText(R.string.str_select_info)
                changeFragment(MyConst.C_fragment_select)
            }else{
                    playGame()
            }
        }


        //前一题
        binding.idMainPrev.setOnClickListener {
            playHoverSound()
            MyConfig.requireNumber -= 1
            playGame()

        }

        //后一题
        binding.idMainNext.setOnClickListener {
            playHoverSound()
            MyConfig.requireNumber += 1
            playGame()
        }



        //提示
        binding.idMainTitle.setOnClickListener {
            playHoverSound()
            val builder = AlertDialog.Builder(this)
            when (mCurrentFragmentName) {
                MyConst.C_fragment_game, MyConst.C_fragment_result-> {
                    if (DonateAndGameUtil.checkFast())
                        builder.setMessage(MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_hint])
                    else
                        builder.setMessage(this.getText(R.string.str_please_donate))

                    val alert = builder.create()
                    alert.setCanceledOnTouchOutside(true)//随便点一下屏幕是否消失
                    alert.show()
                }
            }
        }

    }

    fun changeFragment(name: String) {
         val ft =supportFragmentManager.beginTransaction()
         var  newFragment:Fragment?
           when (name){
                MyConst.C_fragment_game->{
                    newFragment=GameFragment()
                }
                MyConst.C_fragment_option-> {
                    newFragment=OptionFragment()
                }
                MyConst.C_fragment_select->{
                    newFragment= SelectFragment()
                }
                MyConst.C_fragment_result->{
                     newFragment=ResultFragment()
                }
                else->return
        }
        mCurrentFragmentName=name
        ft.replace(R.id.idMainClientLayout,newFragment)
        ft.commit()
    }

    override fun onResume() {
        super.onResume()
        //上次题目
        runBlocking {
            launch {
                MyConfig.nowTime=TimeUtil.getTime(true)
                if(MyConfig.musicSwitch)
                    musicPlayer.play("id_music1",true)
            }
            launch {
                changeFragment(mCurrentFragmentName)
            }
        }


    }

    override fun onPause() {
        super.onPause()
        musicPlayer.stop()
        soundPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyConfig.saveAllConfig()
        MySQLite.closeDB()
        musicPlayer.close()
        soundPlayer.close()
    }

    fun playGame(){
        val ret=DonateAndGameUtil.GetPlayType(MyConfig.requireNumber)
        when(ret){
            MyConst.C_ret_pass->{
                changeFragment(MyConst.C_fragment_game)
            }else->{
                showWaitDialog(ret)
            }
        }
    }



    private fun showWaitDialog(style: String){

        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_donate, null)
        val textView1=dialogView.findViewById(R.id.idDialogDonateInfo) as TextView
         if(MyConst.C_ret_must_donate_limit==style)
             textView1.text=getText(R.string.str_wrong_must_donate)
          else
             textView1.text= DonateAndGameUtil.getUnDonateInfo(true)

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setView(dialogView)
        val buttonDonate=dialogView.findViewById(R.id.idDialogDonateDonate) as Button
        val buttonYes=dialogView.findViewById(R.id.idDialogDonateYes) as Button
        val alert = builder.create()
        alert.show()

        buttonYes.setOnClickListener {
            alert.dismiss()
        }

        buttonDonate.setOnClickListener {
            alert.dismiss()
            runBlocking {
                try {
                    donate()
                }catch(_:Exception){
                }
            }
        }

    }

    private fun donate() {
        //alipays://platformapi/startapp?appId=09999988&actionType=toAccount&goBack=NO&amount=金额(单位：元)&userId=用户ID&memo=转账备注
        MyConfig.nowTime=TimeUtil.getTime(true)
        val uri: Uri = Uri.parse(MyConst.C_alipay+"""捐赠最高智慧app_"""+MyConfig.nowTime.toString())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }



    private fun share() {
        val aintent = Intent(Intent.ACTION_SEND)
        aintent.type = "text/plain"
        aintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.str_share_title))
        aintent.putExtra(Intent.EXTRA_TEXT, getString(R.string.str_share_info))

        startActivity(Intent.createChooser(aintent, getString(R.string.str_share_title)))

    }

    @SuppressLint("SetTextI18n")
    override fun OnMainNotify(srcFragment:String, action:String, arg1:Int, arg2:String){
        when (srcFragment) {
            //选项
            MyConst.C_fragment_option -> {
                when(action){
                    MyConst.C_action_refresh_info -> {
                        if(MyConfig.musicSwitch)
                            musicPlayer.play("id_music1",true)
                        else
                            musicPlayer.stop()

                        if(MyConfig.soundSwitch)
                            soundPlayer.play("id_sound_hover",false)
                        else
                            soundPlayer.stop()
                    }
                    MyConst.C_action_donate-> donate()
                    MyConst.C_action_share-> share()
                }

            }
            //选题目
            MyConst.C_fragment_select -> {
                when(action){
                    MyConst.C_action_refresh_info->{
                        binding.idMainBody.text = String.format(getText(R.string.str_select_info).toString(),
                            MyConfig.allNumber.toString(),
                            MySelectlistClass.numberCountArray[0].toString() ,
                            MySelectlistClass.numberCountArray[1].toString(),
                            MySelectlistClass.numberCountArray[2].toString()
                        )
                    }
                    MyConst.C_action_select_game->{
                        MyConfig.requireNumber=arg1
                        playGame()
                    }
                }
            }
            //游戏开始和结束
            MyConst.C_fragment_game -> {
                when(action){
                    MyConst.C_action_refresh_info ->{
                        binding.idMainTitle.text =MyConfig.nowNumber.toString()+ "."+MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_title]
                        binding.idMainBody.text = MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_body]

                    }
                    MyConst.C_action_result->{
                        val id=MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_id]
                        //错误
                        if (MyConst.C_game_wrong==arg1){
                            if (MyConfig.soundSwitch)
                                soundPlayer.play("id_sound_wrong",false)
                            MyConfig.userData[id]=MyConst.C_game_wrong
                            MyConfig[id]=MyConst.C_game_wrong
                            if(!DonateAndGameUtil.checkFast()){
                                MyConfig.errorCount++
                                MyConfig["errorCount"]=MyConfig.errorCount
                                MyConfig.lastWrongTime=MyConfig.nowTime
                                MyConfig["lastWrongTime"]=MyConfig.lastWrongTime
                            }
                        }else{//正确
                            if(MyConfig.nowNumber>MyConfig.achieveNumber) {
                                MyConfig.achieveNumber = MyConfig.nowNumber
                                MyConfig["achieveNumber"] = MyConfig.achieveNumber
                            }
                            if (MyConfig.soundSwitch)
                                soundPlayer.play("id_sound_right",false)
                            MyConfig.userData[id]=MyConst.C_game_right
                            MyConfig[id]=MyConst.C_game_right
                        }
                        changeFragment(MyConst.C_fragment_result)
                    }
                }
            }

           //结果 选择下一步
            MyConst.C_fragment_result -> {
                when(action){
                    MyConst.C_action_game_redo ->{
                        MyConfig.requireNumber=MyConfig.nowNumber
                        playGame()
                    }
                    MyConst.C_action_donate ->{
                        donate()
                        changeFragment(MyConst.C_fragment_option)
                    }
                    MyConst.C_action_share ->share()
                    MyConst.C_action_select_game ->{
                        MyConfig.requireNumber++
                        playGame()
                    }
                }
            }

            else -> return
        }
    }




}

//显示ui



