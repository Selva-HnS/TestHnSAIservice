package com.hns.acumen360.ui.chatbot.adaptor

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.method.KeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hns.acumen360.R
import com.hns.acumen360.data.remote.stars.threads.MessageItem
import java.util.Date


class ThreadChildAdapter(
    listItem: MutableList<MessageItem>,
    val childCallBackListener: ChildCallBackListener,
    val context: Context
) :
    RecyclerView.Adapter<ThreadChildAdapter.ViewHolder>() {
    lateinit var mContext: Context
    var listItems: MutableList<MessageItem> = listItem


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val listItem: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_thread_adapter_child_layout, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = listItems[position]
        holder.tvContent.setText(dataItem.heading)
        holder.tvContent.requestFocus() // Auto-focus on EditText
        holder.view.setOnClickListener {
            childCallBackListener.childClickListener(dataItem.threadId!!)
        }
        if (dataItem.isEdit) {

            //holder.tvContent.setKeyListener(holder.tvContent.getTag() as KeyListener)  //if need

            holder.tvContent.isEnabled = true
            holder.tvContent.isFocusable = true
            holder.view.visibility = View.GONE
            holder.btnMore.visibility = View.VISIBLE
            holder.btnMore.text = "\uf00c"
            holder.btnMore.setTextColor(ContextCompat.getColor(mContext, R.color.acumen360_blue))
            holder.parentLayout.setBackgroundResource(R.drawable.edit_text_thread_background)
            holder.btnMore.setBackgroundResource(R.drawable.button_thread_background)
            Log.i("onMenuItemClick", "  End: " + Date())
        } else {
            holder.tvContent.tag = holder.tvContent.keyListener
            holder.tvContent.keyListener = null

            holder.tvContent.isEnabled = false
            holder.tvContent.isFocusable = false
            holder.view.visibility = View.VISIBLE
            holder.btnMore.visibility = View.VISIBLE
            holder.btnMore.text = "\uf141"
            holder.btnMore.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray))
            holder.parentLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            holder.btnMore.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))

        }

        if (dataItem.isEdit) {
            holder.tvContent.setOnEditorActionListener { v, actionId, event ->
                v.requestFocus()
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (holder.tvContent.text.toString().isNullOrEmpty()) {
                        holder.tvContent.setText(dataItem.heading ?: "")
                    }
                    childCallBackListener?.updateThread(
                        dataItem?.threadId ?: "",
                        holder.tvContent.text.toString()
                    )
                    // Optionally, hide the keyboard
                    v.clearFocus()
                    val inputMethodManager =
                        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(holder.tvContent.windowToken, 0)
                    true
                } else {
                    false
                }
            }


        }


        holder.btnMore.setOnClickListener {
            if (dataItem.isEdit) {
                if (holder.tvContent.text.toString().isNullOrEmpty()) {
                    holder.tvContent.setText(dataItem.heading ?: "")
                }
                dataItem.isEdit = false
                childCallBackListener?.updateThread(
                    dataItem?.threadId ?: "",
                    holder.tvContent.text.toString()
                )
                notifyItemChanged(position)
            } else {
                resetRecords()
                childCallBackListener.more(
                    dataItem.threadId!!,
                    it,
                    position,
                    dataItem,
                    holder.tvContent
                )
            }

        }

        /*holder.tvContent.setOnFocusChangeListener { _, hasFocus ->
            Log.i("ThreadChildAdapter", "hasFocus: $hasFocus")
            if (hasFocus) {
                holder.itemView.scrollIntoView()
            }
        }*/
    }


    fun resetRecords() {
        for ((allPosition, item) in listItems.withIndex()) {
            if (item.isEdit) {
                item.isEdit = false
                notifyItemChanged(allPosition)
                break
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvContent: AppCompatEditText
        var btnMore: AppCompatButton
        var view: View
        var parentLayout: ConstraintLayout

        init {
            tvContent = itemView.findViewById(R.id.item_text_view)
            btnMore = itemView.findViewById(R.id.btn_more)
            view = itemView.findViewById(R.id.view_)
            parentLayout = itemView.findViewById(R.id.ll_parent)
        }
    }

    public interface ChildCallBackListener {
        fun childClickListener(threadID: String)
        fun more(
            threadID: String,
            view: View,
            position: Int,
            dataItem: MessageItem,
            tvContent: AppCompatEditText
        )

        fun updateThread(threadID: String, heading: String)
    }


    private fun View.scrollIntoView() {
        val rect = Rect()
        this.getDrawingRect(rect)
        this.requestRectangleOnScreen(rect, true)
    }


}
