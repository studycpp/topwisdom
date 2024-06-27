package xuganquan.app.topwisdom

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


data class selectItemData(var bmpIndex:Int=0, var str: String)
object MySelectlistClass {
    val  MyItemList:MutableList<selectItemData> =mutableListOf()
    val  numberCountArray= intArrayOf(0,0,0)
}

//适配器
class SelectItemRecyclerViewAdapter: RecyclerView.Adapter<SelectItemRecyclerViewAdapter.ViewHolder>() {

    var mOnItemClickListener: (( itemIndex:Int ) -> Unit)?=null
    val donated= DonateAndGameUtil.checkFast()

    //重复利用的某条目的View内存
    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        var iconInfo: ImageView = view.findViewById(R.id.idSelectItemIconInfo)
        var textInfo: TextView = view.findViewById(R.id.isSelectItemTextInfo)
        var splitInfo : TextView=view.findViewById(R.id.isSelectItemSplit)
    }

    //负责承载每个子项的布局。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_select_item, parent, false)
        val viewHolder = ViewHolder(view as View)
        return viewHolder
    }


    //负责将每个子项holder绑定数据。
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = MySelectlistClass.MyItemList[position]
        when(item.bmpIndex){
            MyConst.C_game_undo->holder.iconInfo.setImageResource(R.drawable.id_svg_prompt)
            MyConst.C_game_right->holder.iconInfo.setImageResource(R.drawable.id_svg_right)
            MyConst.C_game_wrong->holder.iconInfo.setImageResource(R.drawable.id_svg_wrong)
            else->holder.iconInfo.setImageResource(R.drawable.id_svg_prompt)
        }

        holder.textInfo.text = item.str

        if(1==holder.absoluteAdapterPosition and 1)
            holder.splitInfo.setBackgroundColor(Color.GREEN)
        else
            holder.splitInfo.setBackgroundColor(Color.BLUE)

        //viewHolder.itemView.isClickable=true
        holder.textInfo.setTypeface(null,Typeface.NORMAL)
        if (donated) {
            holder.textInfo.setTypeface(null,Typeface.NORMAL)
        }else{
            holder.itemView.isClickable=false
            if ((position+1)>MyConfig.achieveNumber){
                holder.textInfo.setTypeface(null,Typeface.ITALIC)
            }
        }
        holder.itemView.setOnClickListener { _ ->
            holder.itemView.setBackgroundColor(Color.GREEN)
            mOnItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount(): Int = MySelectlistClass.MyItemList.size



}