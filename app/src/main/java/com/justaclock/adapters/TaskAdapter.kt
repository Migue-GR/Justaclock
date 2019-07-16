package com.justaclock.adapters;

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.justaclock.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import java.lang.Exception

data class Task(var name: String, val time: String)

class TaskAdapter(private val tasks: ArrayList<Task>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task = tasks[position]

        if ((position + 1) % 2 == 0) {
            holder.cyt_bg_task_item.setBackgroundResource(R.drawable.gradient_blue)
//            holder.cyt_bg_task_item.setBackgroundResource(R.drawable.gradient_yelllow)

        } else {
//            holder.cyt_bg_task_item.setBackgroundResource(R.drawable.gradient_yelllow)
            holder.cyt_bg_task_item.setBackgroundResource(R.drawable.gradient_blue)
        }

        if(task.name.isEmpty()){
            holder.txtv_task.text = "Unknown"

        }else{
            holder.txtv_task.text = task.name
        }

        holder.txtv_time.text = task.time

        holder.txtv_task.setOnClickListener{
            try {
                tasks.removeAt(position)
                notifyItemRemoved(position)

                Handler().postDelayed({
                    notifyDataSetChanged()
                }, 500)
                Toast.makeText(context, "Deleted: $position", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
    }
}

class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val txtv_task        = view.txtv_task
    val txtv_time        = view.txtv_time
    val cyt_bg_task_item = view.cyt_bg_task_item
}