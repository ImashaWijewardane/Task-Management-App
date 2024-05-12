package com.example.taskmanager.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.models.ToDoEntity

class TodoAdapter(private val listener: TodoClickListener) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val todoList = ArrayList<ToDoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = todoList[position]
        holder.title.text = item.title
        holder.title.isSelected = true
        holder.note.text = item.note
        holder.date.text = item.date
        holder.date.isSelected = true
        holder.todoLayout.setOnClickListener {
            listener.onItemClicked(todoList[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = todoList.size

    fun updateList(newList: List<ToDoEntity>) {
        todoList.clear()
        todoList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoLayout: CardView = itemView.findViewById(R.id.card_layout)
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val note: TextView = itemView.findViewById(R.id.tv_note)
        val date: TextView = itemView.findViewById(R.id.tv_date)
    }

    interface TodoClickListener {
        fun onItemClicked(todo: ToDoEntity)
    }
}
