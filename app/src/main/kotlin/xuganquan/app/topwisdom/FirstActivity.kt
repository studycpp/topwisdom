package xuganquan.app.topwisdom

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import xuganquan.app.topwisdom.databinding.ActivityFirstBinding


class FirstActivity : AppCompatActivity() {

    private lateinit var  binding :ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //data////////////////////////////

        runBlocking{
            ////test
           // MyConfig.achieveNumber=10
            //MyConfig["achieveNumber"]=10
            ////test
            ///////////////////
            supervisorScope{//CoroutineScope
                launch {
                    MyConfig.nowTime=TimeUtil.getTime(true)
                }
                launch {
                    //准备资源 最重要数据库
                    val dbStream=assets.open( MyConst.C_dbName)

                    ////test
                    //MyFileUtil.saveStreamToFile(dbStream, MyApplication.appDBPath)
                    //dbStream.close()
                    ////test

                    if(MyFileUtil.fileIsExists(MyApplication.appDBPath)!= withContext(
                            Dispatchers.IO
                        ) {
                            dbStream.available()
                        }.toLong()) {
                        MyFileUtil.saveStreamToFile(dbStream, MyApplication.appDBPath)
                    }
                    withContext(Dispatchers.IO) {
                        dbStream.close()
                    }
                    //可否打开
                    if(!MySQLite.OpenDB()) {
                        binding.idFirstStart.isEnabled = false
                        finish()
                        System.exit(0)
                    }
                    //user记录扩容
                    val rCR = MySQLite.readFastInfo()
                    if (rCR.ret) {
                        MyConfig.allNumber = rCR.data.ids.size
                        MyConfig.allIdTitle.ids = rCR.data.ids.toMutableList()
                        MyConfig.allIdTitle.titles = rCR.data.titles.toMutableList()
                        //深度复制
                        //allIdTitle.ids.add(rCR.data.ids[i])
                        //allIdTitle.titles.add(rCR.data.titles[i])
                    }
                }


            }

        }

        //配置文件读取
        MyConfig.loadAllConfig()
        if(MyConfig.achieveNumber>0){
            MyConfig.requireNumber=MyConfig.nowNumber
        }else{
            MyConfig.requireNumber=1
        }
        //first run
        if (MyConfig.firstRun < 1) {
            MyConfig.firstRun = 2
            MyConfig.saveAllConfig()  //第一次保存配置
            binding.idFirstStart.text = getText(R.string.str_start)

        } else {
            binding.idFirstStart.text = getText(R.string.str_resume)

        }

        /*背景
        val returnblob=MySQLite.readOneBLOBbyIdStr("id_background")
        if(returnblob.ret) {
            val imageStream = ByteArrayInputStream(returnblob.dataBOLB)
            val  bitmap = BitmapFactory.decodeStream(imageStream)
            binding.idFirstBackground.alpha = 0.5F
            binding.idFirstBackground.setImageBitmap(bitmap)
        }
        */

        //设置事件
        binding.idFirstSwitch.setOnCheckedChangeListener{
            //buttonView, isChecked ->
                _, isChecked ->
            MyConfig.musicSwitch = isChecked
            MyConfig["musicSwitch"] =isChecked //写入配置
        }

        //绑定事件
        binding.idFirstPrivacy.setOnClickListener{
            val abuidler=AlertDialog.Builder(this)
            abuidler.apply {
                setCancelable(true)
                setTitle(R.string.str_privacy_label)
                setMessage(R.string.str_privacy_info)
                val adiag=create()
                abuidler.setPositiveButton(
                    R.string.str_yes,  {
                            _, _->//(DialogInterface dialog, int which)
                        adiag.dismiss()
                    }
                )
            }
            abuidler.show()

        }

        //绑定事件
        binding.apply{
            binding.idFirstStart.setOnClickListener{
                startActivity(Intent(this@FirstActivity, MainActivity::class.java))
                //finish()
            }

        }


    }

    override fun onResume() {
        super.onResume()
        //音乐开关
        binding.idFirstSwitch.isChecked=MyConfig.musicSwitch
    }

}