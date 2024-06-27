package xuganquan.app.topwisdom


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xuganquan.app.topwisdom.databinding.FragmentOptionBinding


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OptionFragment : Fragment() {

    private lateinit var binding: FragmentOptionBinding

    private var mListener:IOnMainNotify?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding=FragmentOptionBinding.inflate(inflater,container, false)

        //绑定事件 音乐
        binding.idOptionSwitchMusic.setOnCheckedChangeListener{
                _, isChecked ->
            MyConfig.musicSwitch = isChecked
            MyConfig["musicSwitch"] = isChecked
            mListener?.OnMainNotify(MyConst.C_fragment_option,MyConst.C_action_refresh_info)

        }
        //绑定事件 音效
        binding.idOptionSwitchSound.setOnCheckedChangeListener{
                _, isChecked ->
            MyConfig.soundSwitch = isChecked
            MyConfig["soundSwitch"] = isChecked
            mListener?.OnMainNotify(MyConst.C_fragment_option,MyConst.C_action_refresh_info)

        }
        //绑定事件 donate
        binding.idOptionDonate.setOnClickListener {

            mListener?.OnMainNotify(MyConst.C_fragment_option,MyConst.C_action_donate)

        }

        //绑定事件 share
        binding.idOptionShare.setOnClickListener {

            mListener?.OnMainNotify(MyConst.C_fragment_option,MyConst.C_action_share)

        }

        //绑定事件 donateInput
        binding.idOptionDonateInput.setOnClickListener {
                MyConfig.regCode = binding.idOptionDonateText.text.toString()
                MyConfig["regCode"] = MyConfig.regCode

            if (DonateAndGameUtil.checkFast()) {
                MyConfig.errorCount=0
                MyConfig["errorCount"]=0
                showDonateRight()
            }
            else
                showDonateError()
        }

        binding.idOptionDonateText.setOnClickListener {
            binding.idOptionDonateInput.isEnabled=true
        }

        return binding.root       //return inflater.inflate(R.layout.fragment_option,container, false )

    }

    private fun showDonateRight(){
        binding.idOptionDonateLabel.text=getText(R.string.str_donate_ok)
        binding.idOptionDonateText.hint= getText(R.string.str_donate_code)
        binding.idOptionDonateInput.isEnabled=false
    }

    private fun showDonateError() {
        binding.idOptionDonateLabel.text=getText(R.string.str_donate_no)
        binding.idOptionDonateText.hint=MyConfig.regCode
        binding.idOptionDonateInput.isEnabled=true
    }



    override fun onResume() {
        super.onResume()

        binding.idOptionSwitchMusic.isChecked=MyConfig.musicSwitch
        binding.idOptionSwitchSound.isChecked=MyConfig.soundSwitch

        binding.idOptionDonateText.isEnabled=true

        if (DonateAndGameUtil.checkFast()){
            showDonateRight()
        }else{
            showDonateError()
        }


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mListener=context
    }
    override fun onDetach() {
        super.onDetach()
        mListener=null
    }

}