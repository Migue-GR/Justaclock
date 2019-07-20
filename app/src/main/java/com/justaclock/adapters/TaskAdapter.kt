package com.justaclock.adapters;

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.justaclock.R
import kotlinx.android.synthetic.main.fragment_task_timer.*
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import java.lang.Exception

data class Task(var name: String, var time: String, var preview: Boolean)

class TaskAdapter(private val tasks: ArrayList<Task>, private val context: Context, private val txtv_tasks_empty: TextView): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false))
    }

    companion object {
        private val TAG: String = TaskAdapter::class.java.simpleName
    }

    fun getTasks(): ArrayList<Task>{
        return tasks
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    private fun getRippleEffect(pressedColor: Int, backgroundDrawable: Drawable): RippleDrawable {
        return RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null)
    }

    private fun getPressedState(pressedColor: Int): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task = tasks[position]

        if(task.preview){
            holder.cyt_bg_task_item.background = getRippleEffect(
                    context.resources.getColor(R.color.colorAccent, null),
                    context.resources.getDrawable(R.drawable.bg_puntued_task_yellow, null)
            )

        }else{
            if ((position + 1) % 2 == 0) {
                holder.cyt_bg_task_item.background = getRippleEffect(
                        context.resources.getColor(R.color.colorAccent, null),
                        context.resources.getDrawable(R.drawable.gradient_blue, null)
                )

            } else {
                holder.cyt_bg_task_item.background = getRippleEffect(
                        context.resources.getColor(R.color.colorAccent, null),
                        context.resources.getDrawable(R.drawable.gradient_yelllow, null)
                )
            }
        }

        if(task.name.isEmpty()){
            holder.txtv_task.text = "Unknown"

        }else{
            holder.txtv_task.text = task.name
        }

        holder.txtv_time.text = task.time

        holder.imgv_ic_delete_task.setOnClickListener{
            deleteItem(position)
        }

        holder.cyt_bg_task_item.setOnLongClickListener {
            deleteItem(position)

            return@setOnLongClickListener true
        }
    }

    private fun deleteItem(position: Int) {
        Handler().postDelayed({
            try {
                tasks.removeAt(position)
                notifyItemRemoved(position)

                Handler().postDelayed({
                    notifyDataSetChanged()

                    if(tasks.size == 0){
                        txtv_tasks_empty.visibility = View.VISIBLE

                    }else{
                        txtv_tasks_empty.visibility = View.GONE
                    }
                }, 500)

//                    Toast.makeText(context, "Deleted: $position", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }, 400)
    }
}

class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val txtv_task           = view.txtv_task
    val txtv_time           = view.txtv_time
    val cyt_bg_task_item    = view.cyt_bg_task_item
    val imgv_ic_delete_task = view.imgv_ic_delete_task
}