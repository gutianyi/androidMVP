package top.horsttop.optimizedkt.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_recycler.view.*
import top.horsttop.optimizedkt.R

/**
 * Created by horsttop on 2018/5/23.
 */
 class RecyclerAdapter(var strs:ArrayList<String>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent,false))
    }

    override fun getItemCount(): Int = strs.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(strs[position])
    }


    inner class RecyclerViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        fun bind(str:String){
            with(itemView){
                txt_title.text = str
            }
        }
    }
}