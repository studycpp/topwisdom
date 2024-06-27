package xuganquan.app.topwisdom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import xuganquan.app.topwisdom.MyConst.C_Record_Index_result
import xuganquan.app.topwisdom.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var mListener:IOnMainNotify?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding=FragmentResultBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity)
            mListener=context
    }

    override fun onDetach() {
        super.onDetach()
        mListener=null
    }

    override fun onResume() {
        super.onResume()

        //错误
        if(MyConst.C_game_wrong==MyConfig.theGameResult) {
            binding.idResultImageView.setImageResource(R.drawable.id_svg_ret_wrong)
            binding.idResultTitleView.text=getText(R.string.str_wrong_title)
            if(DonateAndGameUtil.checkFast()){
                binding.idResultInfoView.text=getText(R.string.str_wrong_hasdonated)
            }else {
                binding.idResultInfoView.text=DonateAndGameUtil.getUnDonateInfo(false)
            }

            //捐赠
            binding.idResultButton1.text=getText(R.string.str_donate)
            binding.idResultButton1.setOnClickListener { _->
                mListener?.OnMainNotify(MyConst.C_fragment_result,MyConst.C_action_donate)
            }

            //重做
            binding.idResultButton2.text=getText(R.string.str_redo)
            binding.idResultButton2.setOnClickListener { _ ->
                mListener?.OnMainNotify(MyConst.C_fragment_result,MyConst.C_action_game_redo)
            }

        //正确
        }else{
            binding.idResultImageView.setImageResource(R.drawable.id_svg_ret_right)
            binding.idResultTitleView.text=getText(R.string.str_right_title)
            binding.idResultInfoView.text=MyConfig.theNowRecord.data.infos[MyConst.C_Record_Index_result]
            //分享
            binding.idResultButton1.text=getText(R.string.str_share)
            binding.idResultButton1.setOnClickListener {
                    _->
                mListener?.OnMainNotify(MyConst.C_fragment_result,MyConst.C_action_share)
            }
            //下一题
            binding.idResultButton2.text=getText(R.string.str_next)
            binding.idResultButton2.setOnClickListener { _->
                mListener?.OnMainNotify(MyConst.C_fragment_result,MyConst.C_action_select_game)
            }

        }
    }


}
