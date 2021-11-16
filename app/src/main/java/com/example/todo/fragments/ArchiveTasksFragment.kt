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
import com.example.todo.R
import com.example.todo.SwipeToDeleteCallBack
import com.example.todo.databinding.FragmentArchiveBinding
import com.example.todo.db.model.Task
import com.example.todo.db.model.TaskStatus

class ArchiveTasksFragment: Fragment() {

   private lateinit var binding: FragmentArchiveBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var viewModel:TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentArchiveBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(requireActivity())[TaskViewModel::class.java]
        taskAdapter=TaskAdapter(::doneTask,::archiveTask,::deleteTask)
        binding.archiveTasksRV.layoutManager= LinearLayoutManager(requireContext())
        binding.archiveTasksRV.adapter=taskAdapter
        binding.archiveTasksRV.addItemDecoration(
            DividerItemDecoration(requireContext(),
                LinearLayout.VERTICAL)
        )

        val swipeDelete=object : SwipeToDeleteCallBack(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                taskAdapter.deleteTask(viewHolder.adapterPosition)

            }

        }

        val touchHelper= ItemTouchHelper(swipeDelete)
        touchHelper.attachToRecyclerView(binding.archiveTasksRV)




        viewModel.getAllTasks()
        viewModel.archiveTasksLiveData.observe(viewLifecycleOwner){
            if(it!=null){
                taskAdapter.setList(it)
                if (it.isEmpty()){
                    binding.archiveTasksRV.visibility=View.INVISIBLE
                    binding.noTasks.visibility=View.VISIBLE
                }else{
                    binding.archiveTasksRV.visibility=View.VISIBLE
                    binding.noTasks.visibility=View.GONE
                }

            }

        }
    }

    private fun doneTask(task: Task){
        task.status=TaskStatus.DONE
        viewModel.addTask(task)
    }

    private fun archiveTask(task: Task){

    }
    private fun deleteTask(task: Task){

        viewModel.deleteTask(task)
    }
}