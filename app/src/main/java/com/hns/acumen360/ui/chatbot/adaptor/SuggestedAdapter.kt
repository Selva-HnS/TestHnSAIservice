package com.hns.acumen360.ui.chatbot.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.hns.acumen360.R
import com.hns.acumen360.data.local.db.entity.SuggestedQuestionsDB
import com.hns.acumen360.ui.login.model.MenuAction

class SuggestedAdapter(listItem: MutableList<SuggestedQuestionsDB>, listener: getOptions) :
    RecyclerView.Adapter<SuggestedAdapter.ViewHolder>() {
    lateinit var mContext: Context
     var listItems: MutableList<SuggestedQuestionsDB> = listItem
     var listeners: getOptions = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val listItem: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_suggested, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = listItems[position]
        holder.tvContent.text=dataItem.title
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tvContent.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }*/

        holder.itemView.setOnClickListener {
            //listeners.onRead(dataItem.action,dataItem.displayName)
        }

    }

    override fun getItemCount(): Int {
        return listItems.size
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var ibClose: AppCompatTextView
         var tvContent: AppCompatTextView

        init {
            ibClose = itemView.findViewById(R.id.ib_close)
            tvContent = itemView.findViewById(R.id.tv_content)
        }
    }

    interface getOptions {
        fun onRead(data: String,displayName:String)
    }
}
