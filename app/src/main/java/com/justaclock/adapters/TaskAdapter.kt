package com.justaclock.adapters;

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.justaclock.R
import kotlinx.android.synthetic.main.recycler_view_item.view.*

data class Task(var name: String, val time: String)

class TaskAdapter(private val tasks: ArrayList<Task>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task: Task = tasks[position]
        holder.txtv_task.text = task.name
        holder.txtv_time.text = task.time

        holder.txtv_task.setOnLongClickListener{
            notifyItemRemoved(position)
            tasks.remove(task)

            true
        }
    }
}

class ViewHolder (view: View): RecyclerView.ViewHolder(view) {
    val txtv_task = view.txtv_task
    val txtv_time = view.txtv_time
}
