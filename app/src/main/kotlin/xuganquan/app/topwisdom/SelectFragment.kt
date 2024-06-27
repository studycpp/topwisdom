package xuganquan.app.topwisdom

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xuganquan.app.topwisdom.databinding.FragmentSelectBinding


class SelectFragment : Fragment()  {

    private val columnCount = 1

    private lateinit var binding: FragmentSelectBinding

    private var mListener:IOnMainNotify?=null

    val myadapter = SelectItemRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSelectBinding.inflate(inflater, container, false)

        (binding.idSelectView).apply {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }

            myadapter.mOnItemClickListener={
                itemIndex->
                ///(activity as MainActivity).OnNotify()直接干
                // Toast.makeText(this.context,"${itemIndex+1}",Toast.LENGTH_SHORT).show()
                mListener?.OnMainNotify(MyConst.C_fragment_select,MyConst.C_action_select_game,itemIndex+1,)
            }
            adapter=myadapter
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        //载入标题title和图像索引
        MySelectlistClass.MyItemList.clear()
        MySelectlistClass.numberCountArray.fill(0)
        var index:Int=0

        //以数据库读取为准 MyConfig.allIdTitle.ids
        for(key in MyConfig.userData.keys){
            val shid= selectItemData(0,"")  //必须new
            shid.bmpIndex=MyConfig.userData[key]?:0
            if (!(shid.bmpIndex in intArrayOf(MyConst.C_game_undo, MyConst.C_game_right, MyConst.C_game_wrong) )) {
                shid.bmpIndex=MyConst.C_game_undo
                MyConfig.userData[key]=MyConst.C_game_undo
                MyConfig[key]=MyConst.C_game_undo
            }
            MySelectlistClass.numberCountArray[shid.bmpIndex]+=1

            shid.str=(index+1).toString()+"."+MyConfig.allIdTitle.titles[index];  //先读取标题
            MySelectlistClass.MyItemList.add(shid)
            index++

        }//for end
        mListener?.OnMainNotify(MyConst.C_fragment_select,MyConst.C_action_refresh_info)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            mListener=context
    }

    override fun onDetach() {
        super.onDetach()
        MySelectlistClass.MyItemList.clear()
        mListener=null
    }

}