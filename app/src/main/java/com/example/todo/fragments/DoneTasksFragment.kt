package com.example.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.SwipeToDeleteCallBack
import com.example.todo.databinding.FragmentDoneBinding
import com.example.todo.db.model.Task
import com.example.todo.db.model.TaskStatus

class DoneTasksFragment :Fragment(){

    private lateinit var binding: FragmentDoneBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel:TaskViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(requireActivity())[TaskViewModel::class.java]
        taskAdapter=TaskAdapter(::doneTask,::archiveTask,::deleteTask)
        binding.doneTasksRv.layoutManager= LinearLayoutManager(requireContext())
        binding.doneTasksRv.adapter=taskAdapter
        binding.doneTasksRv.addItemDecoration(
            DividerItemDecoration(requireContext(),
                LinearLayout.VERTICAL)
        )

        val swipeDelete=object : SwipeToDeleteCallBack(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                taskAdapter.deleteTask(viewHolder.adapterPosition)

            }

        }

        val touchHelper= ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(binding.doneTasksRv)


        viewModel.getAllTasks()
        viewModel.doneTasksLiveData.observe(viewLifecycleOwner){
            if(it!=null){
                taskAdapter.setList(it)
                if (it.isEmpty()){
                    binding.doneTasksRv.visibility=View.INVISIBLE
                    binding.noTasks.visibility=View.VISIBLE
                }else{
                    binding.doneTasksRv.visibility=View.VISIBLE
                    binding.noTasks.visibility=View.GONE
                }

            }

        }
    }
    private fun doneTask(task: Task){
    }

    private fun archiveTask(task: Task){
        task.status= TaskStatus.ARCHIVE
        viewModel.addTask(task)
    }
    private fun deleteTask(task: Task){

        viewModel.deleteTask(task)
    }


}