package xuganquan.app.topwisdom

import android.media.MediaPlayer
import java.lang.Thread.sleep


class MyMusicSoundsPlayer {
    private var mplayer:MediaPlayer?
    private var source:String=""

    constructor(){
        mplayer=MediaPlayer()
    }

    fun play(name:String,loop:Boolean){

        mplayer?.apply {
            if((source == name) && isPlaying()) {
                return
            }
            stop()
            reset()
        }

        source=name
        val filename=MyApplication.appCachePath+ source
        if (-1L==MyFileUtil.fileIsExists(filename)) {
            val mr=MySQLite.readOneBLOBbyIdStr(source)
            if(mr.ret) { //大文件无法读取
                MyFileUtil.saveByteArrayToFile(mr.dataBOLB, filename)
            }else
                return
        }

        try {
            mplayer?.apply {
                //reset() //must reset
                setDataSource(filename)
                isLooping=loop
                setVolume(0.3F,0.5F)
                prepareAsync()
                setOnPreparedListener {
                        start()
                }
            }
        }catch (_:Exception) {
            close()
            mplayer=MediaPlayer()
        }
    }

    fun stop(){
        mplayer?.stop()
    }

    fun close()
    {
        mplayer?.apply {
            stop()
            release()
        }
        mplayer=null
    }


}