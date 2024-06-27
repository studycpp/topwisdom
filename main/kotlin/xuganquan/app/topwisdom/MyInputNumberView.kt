package xuganquan.app.topwisdom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import xuganquan.app.topwisdom.databinding.MylayoutInputNumberBinding

fun interface IOnMyInputNumberChange{
    fun onMyInputNumberChange(str: String)
}

class MyInputNumberView: ConstraintLayout {
    private  var binding: MylayoutInputNumberBinding


    private var mValueStr:String= "0"
    private var mlistener:IOnMyInputNumberChange?=null

    constructor(context: Context):super(context){
        binding=MylayoutInputNumberBinding.inflate(LayoutInflater.from(context),this,true)
        binding.idInputNumberText.setText("")  //.text baocuo
        setUpEvent()
    }


    // 4. 处理事件
    private fun setUpEvent() {
        binding.idInputNumberText.addTextChangedListener( object: TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
               // listener?.onMyInputChange(mValueStr)
            }
        })

        binding.idInputNumberDec.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            var mValueInt=mValueStr.toIntOrNull()
            if(null==mValueInt)
                 mValueInt=0
            else
                mValueInt-=1
            mValueStr=mValueInt.toString()
            updateValueText()
        }
        binding.idInputNumberAdd.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            var mValueInt2=mValueStr.toIntOrNull()
            if(null==mValueInt2)
                mValueInt2=0
            else
                mValueInt2 += 1

            mValueStr=mValueInt2.toString()
            updateValueText()
        }
        binding.idInputNumberDel.setOnClickListener {
            mValueStr=""
            updateValueText()
        }

        binding.idInputNumberComfirm.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mlistener?.onMyInputNumberChange(mValueStr)
        }

        binding.idInputNumber1.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="1"
            updateValueText()
        }
        binding.idInputNumber2.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="2"
            updateValueText()
        }
        binding.idInputNumber3.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="3"
            updateValueText()
        }
        binding.idInputNumber4.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="4"
            updateValueText()
        }
        binding.idInputNumber5.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="5"
            updateValueText()
        }
        binding.idInputNumber6.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="6"
            updateValueText()
        }
        binding.idInputNumber7.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="7"
            updateValueText()
        }
        binding.idInputNumber8.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="8"
            updateValueText()
        }
        binding.idInputNumber9.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="9"
            updateValueText()
        }
        binding.idInputNumber0.setOnClickListener {
            mValueStr=binding.idInputNumberText.text.toString()
            mValueStr+="0"
            updateValueText()
        }

    }

    fun setOnMyInputListener(listener: IOnMyInputNumberChange) {
        this.mlistener = listener
    }

    private fun updateValueText() {
        binding.idInputNumberText.setText(mValueStr)  //.textbaocuo
    }


}