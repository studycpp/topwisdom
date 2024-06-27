package xuganquan.app.topwisdom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import xuganquan.app.topwisdom.databinding.MylayoutInputSelectBinding

fun interface IOnMyInputSelectChange{
    fun onMyInputSelectChange(value: Int)
}

class MyInputSelectView: ConstraintLayout {
    private  var binding: MylayoutInputSelectBinding


    private var mValueInt:Int= 1
    private var mlistener:IOnMyInputSelectChange?=null

    constructor(context: Context):super(context){
        binding=MylayoutInputSelectBinding.inflate(LayoutInflater.from(context),this,true)
        setUpEvent()
    }

    private fun hideAll(){
        binding.idInputSelect8.visibility= View.GONE
        binding.idInputSelect7.visibility= View.GONE
        binding.idInputSelect6.visibility= View.GONE
        binding.idInputSelect5.visibility= View.GONE
        binding.idInputSelect4.visibility= View.GONE
        binding.idInputSelect3.visibility= View.GONE
        binding.idInputSelect2.visibility= View.GONE
        binding.idInputSelect1.visibility= View.GONE
    }

    fun buildSelectUI(strLists:List<String>){
        hideAll()
        val size=strLists.size
        if((size<1) ||(size>8)){
            return
        }
        for(i in  0 until size){
            when(i){
                0->{
                    binding.idInputSelect1.visibility=View.VISIBLE
                    binding.idInputSelect1.text=strLists[i]
                }
                1->{
                    binding.idInputSelect2.visibility=View.VISIBLE
                    binding.idInputSelect2.text=strLists[i]
                }
                2->{
                    binding.idInputSelect3.visibility=View.VISIBLE
                    binding.idInputSelect3.text=strLists[i]
                }
                3->{
                    binding.idInputSelect4.visibility=View.VISIBLE
                    binding.idInputSelect4.text=strLists[i]
                }
                4->{
                    binding.idInputSelect5.visibility=View.VISIBLE
                    binding.idInputSelect5.text=strLists[i]
                }
                5->{
                    binding.idInputSelect6.visibility=View.VISIBLE
                    binding.idInputSelect6.text=strLists[i]
                }
                6->{
                    binding.idInputSelect7.visibility=View.VISIBLE
                    binding.idInputSelect7.text=strLists[i]
                }
                7->{
                    binding.idInputSelect8.visibility=View.VISIBLE
                    binding.idInputSelect8.text=strLists[i]
                }
            }
        }

    }

    // 4. 处理事件
    private fun setUpEvent() {

        binding.idInputSelect1.setOnClickListener {
            updateValue(1)
        }

        binding.idInputSelect2.setOnClickListener {
            updateValue(2)
        }
        binding.idInputSelect3.setOnClickListener {
            updateValue(3)
        }
        binding.idInputSelect4.setOnClickListener {
            updateValue(4)
        }
        binding.idInputSelect5.setOnClickListener {
            updateValue(5)
        }
        binding.idInputSelect6.setOnClickListener {
            updateValue(6)
        }
        binding.idInputSelect7.setOnClickListener {
            updateValue(7)
        }

        binding.idInputSelect8.setOnClickListener {
            updateValue(8)
        }

    }

    fun setOnMyInputSelectListener(listener: IOnMyInputSelectChange) {
        this.mlistener = listener
    }

    private fun updateValue(value:Int) {
        mlistener?.onMyInputSelectChange(value)
    }


}