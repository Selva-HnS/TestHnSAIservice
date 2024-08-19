package com.hns.acumen360.ui.chatbot.adaptor


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hns.acumen360.R
import com.hns.acumen360.data.remote.stars.threads.MessageItem
import com.hns.acumen360.utils.common.CommonUtils

class ThreadAdapter(
    private val groupedItems: Map<String, List<MessageItem>>,
    private val context: Context, val parentCallBackListener: ParentCallBackListener
) :
    RecyclerView.Adapter<ThreadAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_thread_adapter_parent_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val category = groupedItems.keys.elementAt(position)
        val items = groupedItems.getValue(category)
        holder.bind(category, items, position)
    }

    override fun getItemCount(): Int = groupedItems.size

    var threadChildAdapter: ThreadChildAdapter? = null

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.category_text_view)
        private val itemTextView: TextView = itemView.findViewById(R.id.item_text_view)
        private val rvChild: RecyclerView = itemView.findViewById(R.id.rvChild)

        fun bind(category: String, items: List<MessageItem>, position: Int) {
            categoryTextView.text = category
            //itemTextView.text = items.joinToString("\n") { it.name!! }
            var listChild = mutableListOf<MessageItem>()
            for (item in items) {
                listChild.add(item)
            }

            var orderList = CommonUtils.orderChatHistoryThreads(listChild).toMutableList()
            rvChild.layoutManager = LinearLayoutManager(context)
            threadChildAdapter = ThreadChildAdapter(orderList, object :
                ThreadChildAdapter.ChildCallBackListener {
                override fun childClickListener(threadID: String) {
                    parentCallBackListener?.parentClickListener(threadID)
                }

                override fun more(
                    threadID: String,
                    view: View,
                    childPosition: Int,
                    dataItem: MessageItem,
                    tvContent: AppCompatEditText
                ) {

                    for (key in groupedItems.keys) {
                        val child = groupedItems.getValue(key)
                        for ((childPosition, item) in child.withIndex()) {
                            if (item.isEdit) {
                                item.isEdit = false
                                threadChildAdapter?.notifyDataSetChanged()
                            }
                        }
                    }
                    parentCallBackListener?.more(
                        threadID,
                        view,
                        position,
                        childPosition,
                        dataItem,
                        tvContent, threadChildAdapter
                    )
                }

                override fun updateThread(threadID: String, heading: String) {
                    parentCallBackListener?.updateThread(threadID, heading)
                }

            }, context)
            rvChild.adapter = threadChildAdapter
            Log.i("ChildSize", " bind: " + listChild.size)
        }
    }
}

fun resetRecords() {

}

public interface ParentCallBackListener {
    fun parentClickListener(threadID: String)
    fun more(
        threadID: String,
        view: View,
        position: Int,
        childPosition: Int,
        dataItem: MessageItem,
        tvContent: AppCompatEditText,
        threadChildAdapter: ThreadChildAdapter?,
    )

    fun updateThread(threadID: String, heading: String)
}


