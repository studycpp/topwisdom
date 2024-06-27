package xuganquan.app.topwisdom

import android.content.Context
import android.database.sqlite.SQLiteDatabase



object MySQLite {

    private var db:SQLiteDatabase?=null
    private var dbIsValid=false


    fun OpenDB():Boolean{
        if(!dbIsValid) {
            db = MyApplication.appContext.openOrCreateDatabase( MyApplication.appDBPath, Context.MODE_PRIVATE, null)
            dbIsValid=db!!.isOpen
        }
        return dbIsValid
    }


    data class returnBLOB(var ret: Boolean=false, var dataBOLB: ByteArray)
    fun readOneBLOBbyIdStr(IdStr:String): returnBLOB {
        OpenDB()
        val sql= "SELECT media FROM common WHERE \"id\" = '$IdStr' ;"
        val rRB= returnBLOB(false, byteArrayOf() )
        try {
            val cursor= db?.rawQuery(sql,null)
            if(cursor?.count!!>0){
                cursor.moveToNext() //!!!!!!!!!!!NEXT
                if (!cursor.isNull(0))
                    rRB.dataBOLB = cursor.getBlob(0)
                rRB.ret = true
            }
            cursor.close()
        } catch (_: Exception) {
        }
        return rRB
    }

    data class returnRowRecord(var ret: Boolean=false, var data:DC_Record)
    fun readOneRowRecordbyIdStr(idStr:String): returnRowRecord {
        OpenDB()
        val sql= "SELECT * FROM main WHERE \"id\" = '$idStr';"
        val rRR=returnRowRecord(false, DC_Record())
        try {
            val cursor= db?.rawQuery(sql,null)
            if(cursor?.count!!>0){
                cursor.moveToNext() //!!!!!!!!!!!NEXT
                for (i in 1 ..7){
                    if (!cursor.isNull(i)) {   //重要 否则报错
                        rRR.data.infos.add(cursor.getString(i))
                    }else {
                        rRR.data.infos.add("null")
                    }
                }
                for (i in 8 ..19){
                    if (!cursor.isNull(i)) {  //重要 否则报错
                        rRR.data.dataBOLBs.add(cursor.getBlob(i))
                    }else{
                        rRR.data.dataBOLBs.add(byteArrayOf())
                    }
                }
                rRR.ret=true
            }
            cursor.close()
        } catch (_: Exception) {
        }
        return rRR
    }

    data class returnFastInfo(var ret: Boolean=false, var data: DC_FastInfo)
    fun readFastInfo(): returnFastInfo {
        OpenDB()
        val sql= "SELECT id,title FROM main ORDER BY number;"
        val rCS=returnFastInfo(false,DC_FastInfo())
        try {
            val cursor= db?.rawQuery(sql,null)
            if(cursor?.count!!>0){
                for (i in 1..cursor.count) {
                    cursor.moveToNext() //!!!!!!!!!!!NEXT
                    if (!cursor.isNull(0)) {   //重要 否则报错
                        rCS.data.ids.add(cursor.getString(0))
                        if (!cursor.isNull(1))   //重要 否则报错
                            rCS.data.titles.add(cursor.getString(1))
                        else
                            rCS.data.titles.add("null")
                    }
                }
                rCS.ret=true
            }
            cursor.close()
        } catch (_: Exception) {
        }
        return rCS
    }


    fun closeDB() {
        dbIsValid=false
        db?.close()
    }
}
















