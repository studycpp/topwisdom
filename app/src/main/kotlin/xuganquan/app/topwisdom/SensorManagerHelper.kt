package xuganquan.app.topwisdom

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorManagerHelper(private val context: Context,private val sensorType:Int) : SensorEventListener {
    ///////////////////////////////////////////
    public var isNotlimited=true
    // 传感器管理器
    private var sensorManager: SensorManager? = null
    // 传感器
    private var sensor: Sensor? = null
    // 重力感应监听器
    private var onSensorListener: IOnSensorListener? = null

    ///////////////////////////////////////////
    // 上次检测时间
    private var lastUpdateTime: Long = 0


    // 两次检测的时间间隔
    private val UPTATE_INTERVAL_TIME = 200
    // 手机上一个传感器数值
    private var sensorOldValues: Array<Float> = arrayOf(0f,0f,0f,0f)

    ///////////////////////////////////////////


    /**
     * 开始检测
     */
    fun start() {
        // 获得传感器管理器
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager != null) {

            sensor = sensorManager!!.getDefaultSensor(sensorType)
            // 注册
            if (sensor != null) {
                sensorManager!!.registerListener(
                    this, sensor,
                    SensorManager.SENSOR_DELAY_NORMAL   //SensorManager.SENSOR_DELAY_GAME
                )
            }else{
                isNotlimited=false
            }
        }else{
            isNotlimited=false
        }

    }

    /**
     * 停止检测
     */
    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    // 摇晃监听接口
    fun interface IOnSensorListener {
        fun onSensorChanged()
    }

    // 设置重力感应监听器
    fun setOnListener(listener: IOnSensorListener) {
        onSensorListener = listener
    }

    /**
     * 重力感应器感应获得变化数据
     * android.hardware.SensorEventListener#onSensorChanged(android.hardware
     * .SensorEvent)
     */
    override fun onSensorChanged(event: SensorEvent) {
        // 现在检测时间
        val currentUpdateTime = System.currentTimeMillis()
        // 两次检测的时间间隔
        val timeInterval = currentUpdateTime - lastUpdateTime
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME) return
        // 现在的时间变成last时间
        lastUpdateTime = currentUpdateTime

        // 获得变化值
        val deltaX = event.values[0] - sensorOldValues[0]
        val deltaY =event.values[1] - sensorOldValues[1]
        val deltaZ = event.values[2] - sensorOldValues[2]

        ///////
        if(Sensor.TYPE_ACCELEROMETER==sensorType) {
            if ((deltaX>20f)||(deltaY>20f)||(deltaZ>20f)) {
                onSensorListener?.onSensorChanged()
            }
        }
        if(Sensor.TYPE_GRAVITY==sensorType){
            if (event.values[1]<-6f) {  //zhongli加速度 -9.6
                onSensorListener?.onSensorChanged()
            }
        }

        // 将现在的
        sensorOldValues[0]  = event.values[0]
        sensorOldValues[1]  = event.values[1]
        sensorOldValues[2]  = event.values[2]

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) { }

}
