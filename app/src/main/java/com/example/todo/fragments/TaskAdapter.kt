package com.example.todo.fragments

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.db.model.Task
import com.example.todo.db.model.TaskStatus

class TaskAdapter(private val done:(task:Task)->Unit,
                  private val archive:(task:Task)->Unit,
                  private val delete:(task: Task)->Unit

) :RecyclerView.Adapter<TaskAdapter.NewTaskViewHolder>(){


    private var tasksList:List<Task> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(tasksList: List<Task>){
        this.tasksList=tasksList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewTaskViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view:View=inflater.inflate(R.layout.task_item,parent,false)
        return NewTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewTaskViewHolder, position: Int) {
        val task=tasksList[position]
        holder.bind(task)

    }

    override fun getItemCount(): Int {
        return tasksList.size
    }


    inner class NewTaskViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val taskTime:TextView=view.findViewById(R.id.taskTime)
        private val taskTitle:TextView=view.findViewById(R.id.taskTitle)
        private val taskDate:TextView=view.findViewById(R.id.taskDate)
        private val doneBtn:ImageView=view.findViewById(R.id.doneBtn)
        private val archiveBtn:ImageView=view.findViewById(R.id.archiveBtn)

        fun bind(task:Task){
            taskTime.text=task.time
            taskTitle.text=task.title
            taskDate.text=task.date
            doneBtn.setOnClickListener {
            done(task)
            }
            archiveBtn.setOnClickListener {
            archive(task)
            }

            when(task.status){
                TaskStatus.DONE->{
                    doneBtn.visibility=View.INVISIBLE
                    archiveBtn.visibility=View.INVISIBLE
                }
                TaskStatus.ARCHIVE->archiveBtn.visibility=View.INVISIBLE
            }



        }

    }

    fun deleteTask(index:Int){

        delete(tasksList[index])
    }
}