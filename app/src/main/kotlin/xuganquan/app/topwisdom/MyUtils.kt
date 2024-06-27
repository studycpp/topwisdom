package xuganquan.app.topwisdom

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.lang.Math.abs
import java.net.URL
import java.text.SimpleDateFormat


data class DC_Record(
    var infos: MutableList<String> = mutableListOf(), var dataBOLBs:MutableList <ByteArray> =mutableListOf()
    )
data class DC_FastInfo(
    var ids: MutableList<String> = mutableListOf(),
    var titles: MutableList<String> = mutableListOf()
    )

object MyConfig {

    //用户数据
    var firstRun: Int = 0
    var nowNumber: Int = 1
    var achieveNumber: Int = 0
    var errorCount: Int = 0
    var errorAllowCount: Int = 3
    var lastWrongTime: Long = 0L
    var regCode: String = ""
    var musicSwitch = true
    var soundSwitch = true
    var userName: String = "userName"
    var userData: MutableMap<String, Int> = mutableMapOf()



    //////////////////////////////////////////////////////
    var currentGameId= "null"
    var allNumber: Int = 100
    var requireNumber: Int = 1
    var allIdTitle = DC_FastInfo()
    var theNowRecord = MySQLite.returnRowRecord(false, DC_Record())
    var nowTime: Long = -1L
    var theGameResult: Int = MyConst.C_game_wrong
    ///////////////////////////////////////////////////

    //基本路径


    ////////////////////////////
    private val mySharedPreference = MyApplication.appContext.getSharedPreferences(
        MyConst.C_configXmlName,
        AppCompatActivity.MODE_PRIVATE
    )


    fun saveAllConfig() {
         with(mySharedPreference.edit()) {
            putInt("firstRun", firstRun)
            putInt("nowNumber", nowNumber)
            putInt("achieveNumber", achieveNumber)
            putInt("errorCount", errorCount)
            putInt("errorAllowCount", errorAllowCount)
            putLong("lastWrongTime", lastWrongTime)
            putString("regCode", regCode)
            putBoolean("musicSwitch", musicSwitch)
            putBoolean("soundSwitch", soundSwitch)
            putString("userName", userName)

             //以数据库为准
            userData.forEach {
                putInt(it.key, it.value)
            }
            apply()
        }
    }

    fun loadAllConfig() {
        //加载配置
        firstRun = mySharedPreference.getInt("firstRun", 0)
        nowNumber = mySharedPreference.getInt("nowNumber", 1)
        achieveNumber = mySharedPreference.getInt("achieveNumber", 0)
        errorCount = mySharedPreference.getInt("errorCount", 0)
        errorAllowCount = mySharedPreference.getInt("errorAllowCount", 3)
        lastWrongTime = mySharedPreference.getLong("lastWrongTime", 0)
        regCode = mySharedPreference.getString("regCode", "null")!!
        musicSwitch = mySharedPreference.getBoolean("musicSwitch", true)
        soundSwitch = mySharedPreference.getBoolean("soundSwitch", true)
        userName = mySharedPreference.getString("userName", "userName").toString()

        //以数据库为准
        ////MyConfig.theAllIdTitle
        for (akey in MyConfig.allIdTitle.ids) {
            userData[akey] = mySharedPreference.getInt(akey, MyConst.C_game_undo)
        }

    }

    /*
      inline operator fun <reified T: Comparable<T>> set(str: String, value: T ){
        when (T::class) {
            Int::class -> myEditor.putInt(str, value as? Int?:0)
            String::class -> myEditor.putString(str, value as? String?:null)
            Long::class -> myEditor.putLong(str, value as? Long?:0L)
            Boolean::class -> myEditor.putBoolean(str, value as? Boolean?:false)
            else-> myEditor.putString(str, value.toString())
        }
    }

   */

    operator fun <T: Comparable<T>> set(str: String, value: T )=with(mySharedPreference.edit()){
        //利用with函数定义临时的命名空间
        when (value) {
            is Int -> putInt(str, value)
            is String-> putString(str, value)
            is Long-> putLong(str, value)
            is Boolean -> putBoolean(str, value)
            else-> putString(str, value.toString())
        }.apply()
    }
}

object DonateAndGameUtil{
    fun checkFast(): Boolean {
        var ret=false
        if (MyConfig.regCode.length>=32){  //32位订单号
            val tempCode=MyConfig.regCode.substring(0..7).toInt()
            if(tempCode in 20230401 ..20501212)
                ret=true
        }
        return ret
    }


    fun checkUnDonaterEnable():Boolean{
        //错误数目未超过限制 首次3次 以后每2小时一次
        if(MyConfig.errorCount<MyConfig.errorAllowCount){
            return true
        }else{
            MyConfig.nowTime=TimeUtil.getTime(false)
            if(MyConfig.errorAllowCount>=3){//原来允许3次 改成1次
                MyConfig.errorAllowCount=1
                MyConfig["errorAllowCount"]=MyConfig.errorAllowCount
                MyConfig.errorCount=1  //调整为当前
                MyConfig["errorCount"]=MyConfig.errorCount
                return false//必须捐赠
            }else{
                if((MyConfig.nowTime-MyConfig.lastWrongTime)>MyConst.C_waitTime_millisecond) {
                    MyConfig.lastWrongTime=MyConfig.nowTime
                    MyConfig["lastWrongTime"]=MyConfig.lastWrongTime
                    MyConfig.errorCount=0
                    MyConfig["errorCount"]=MyConfig.errorCount
                    return true  //可以玩下一关
                }else {
                    return false//必须捐赠
                }
            }

        }
    }


    fun GetPlayType(number: Int):String{
        ///校正用户输入
        var realNumber=number
        if (number < 1)
            realNumber = MyConfig.allNumber
        if (number > MyConfig.allNumber)
            realNumber = 1

        /////////////////
        if (checkFast())  {
            //注册用户合理计算
            MyConfig.requireNumber=realNumber
            return MyConst.C_ret_pass
        }else{
            //非注册用户 在范围内可玩
            if(checkUnDonaterEnable()){
                if ( realNumber in 1..MyConfig.achieveNumber+1){
                    MyConfig.requireNumber=realNumber
                    return MyConst.C_ret_pass
                }else{
                    MyConfig.requireNumber = MyConfig.nowNumber
                    return MyConst.C_ret_must_donate_limit
                }
            }else{
                if ( realNumber in 1..MyConfig.achieveNumber){
                    MyConfig.requireNumber=realNumber
                    return MyConst.C_ret_pass
                }else{
                    MyConfig.requireNumber=MyConfig.achieveNumber
                    return MyConst.C_ret_must_donate_count_arrive
                }

            }
        }
    }

    fun getUnDonateInfo(showTime:Boolean):String{
        MyConfig.nowTime=TimeUtil.getTime(false)
        var info1=MyApplication.appContext.getString((R.string.str_wrong_ask_donate),
                                                    MyConfig.errorCount.toString()) //string.format
        val shouldTime=MyConfig.lastWrongTime+MyConst.C_waitTime_millisecond
        val waitTimeInterval=(shouldTime-MyConfig.nowTime)/1000
        val sdf=SimpleDateFormat("HH:mm:ss")
        val rightTimeStr= sdf.format(shouldTime)
        var info2=MyApplication.appContext.getString(R.string.str_waittime,
                                                     (waitTimeInterval/60).toString(),
                                                    waitTimeInterval.toString(),
                                                    rightTimeStr)
        if (showTime)
            return (info1+info2)
        else
            return  info1
    }

}

object screenUtil{
    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun generateRandomX(leftRange: Int = 0, rightRange: Int = getScreenWidth()): Int {
        return (leftRange..rightRange).random()
    }

    fun generateRandomY(leftRange: Int = 0, rightRange: Int = getScreenHeight()): Int {
        return (leftRange..rightRange).random()
    }
}

object MyFileUtil {

    fun fileIsExists(strFile: String): Long {
        try {
            val f = File(strFile)
            if (!f.exists()) {
                return -1L
            }else{
                return  f.length()
            }

        } catch (e: Exception) {
            return -1L
        }
    }


    fun copyResToFileUsingStream(resID: Int, destStr: String) {
        val fis: InputStream = MyApplication.appContext.resources.openRawResource(resID)
        val fos: OutputStream = FileOutputStream(destStr)
        try {
            val buffer = ByteArray(1024)
            var length: Int
            while (fis.read(buffer).also { length = it } !=-1) {
                fos.write(buffer, 0, length)
            }
        } finally {
            fis.close()
            fos.close()
        }
    }


    fun saveByteArrayToFile(srcArray: ByteArray, destStr: String) {
        var fis:InputStream?=null
        var fos:OutputStream?=null
        try {
            fis=ByteArrayInputStream(srcArray)
            fos=FileOutputStream(destStr)
            val buffer = ByteArray(1024)
            var length: Int
            while (fis.read(buffer).also { length = it } !=-1) {
                fos.write(buffer, 0, length)
            }

        } finally {
            fis?.close()
            fos?.close()
        }
    }
    fun saveStreamToFile(srcStream: InputStream, destStr: String) {
        var fos: OutputStream?=null
        try {
            fos=FileOutputStream(destStr)
            val buffer = ByteArray(1024)
            var length: Int
            while (srcStream.read(buffer).also { length = it } !=-1) {
                fos.write(buffer, 0, length)
            }

        } finally {
            fos?.close()
        }
    }

    fun copyFileUsingStream(source: File, dest: File) {
        var fis: InputStream? = null
        var fos: OutputStream? = null
        try {
            fis = FileInputStream(source)
            fos = FileOutputStream(dest)
            val buffer = ByteArray(1024)
            var length: Int
            while (fis.read(buffer).also { length = it } > 0) {
                fos.write(buffer, 0, length)
            }
        } finally {
            fis?.close()
            fos?.close()
        }
    }



}

object TimeUtil
{
    fun getTime(needNetWork: Boolean):Long{
        var time2:Long=-1L

        var time1=System.currentTimeMillis()

        if(needNetWork) {
            try {
                    val url = URL("""https://www.alipay.com/""")
                    val conn = url.openConnection()
                    conn.connectTimeout = 2 * 1000
                    conn.connect()
                    time2 = conn.date

            } catch (e: Exception) {
                        time2 = System.currentTimeMillis()
            }
        }

        if (abs(time1-time2)<20*1000)
             time1=time2

        return time1
    }
}

/*
object VibrateUtils {

    //震动milliseconds毫秒
    fun vibrate(context: Context, milliseconds: Long) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
             val vib =vibratorManager.defaultVibrator
             val effect=VibrationEffect.createOneShot(milliseconds,VibrationEffect.DEFAULT_AMPLITUDE)
              vib.vibrate(effect)
        } else {
            val vib=context.getSystemService(VIBRATOR_SERVICE) as Vibrator
             vib.vibrate(milliseconds)
        }

    }


    /**
     * 以pattern[]方式震动
     * @param repeat -1 不重复  0一直震动
     */
    fun vibrate(context: Context, pattern: LongArray, repeat: Int) {

        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        vib.vibrate(pattern, repeat)
    }

//取消震动

    //取消震动
    fun virateCancle(context: Context) {
        val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        try {
            vib.cancel()
        } catch (e: Exception) {
            //e.printStackTrace()
        }
    }
}
*/

object NetUtil{
    fun whichNetwork(context:Context):Int {
        var whichNet = -1
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nc = cm.getNetworkCapabilities(cm.activeNetwork)
            when{
                nc == null->{
                    whichNet=-1   // typeName = "请打开网络"
                }
                nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->{
                    whichNet =1   // "当前使用WIFI网络"
                }
                nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->{
                    whichNet =2   // "当前使用移动网络"
                }
            }
        } catch (_:Exception ) {
        }

        return whichNet
    }
}