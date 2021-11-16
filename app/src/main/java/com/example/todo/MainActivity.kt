package com.example.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.todo.databinding.ActivityMainBinding
import com.example.todo.db.model.Task
import com.example.todo.db.model.TaskStatus
import com.example.todo.fragments.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomSheet:BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetLayout:LinearLayout
    private lateinit var pickTimeLayout:TextInputLayout
    private lateinit var pickInput:TextInputEditText
    private lateinit var titleLayout: TextInputLayout
    private lateinit var titleInput:TextInputEditText
    private lateinit var pickDateLayout: TextInputLayout
    private lateinit var pickDateInput:TextInputEditText
    private lateinit var viewModel:TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navFragmentHost=supportFragmentManager.findFragmentById(R.id.app_nav_host) as NavHostFragment
        navController=navFragmentHost.navController
        binding.appBottomNav.setupWithNavController(navController)
        viewModel=ViewModelProvider(this)[TaskViewModel::class.java]
        // initBottomSheetViews
        initBottomSheetViews()
        setBehavior()

        binding.floatingActionButton.setOnClickListener {
            if (bottomSheet.state!=BottomSheetBehavior.STATE_EXPANDED){
                bottomSheet.state=BottomSheetBehavior.STATE_EXPANDED
            }else{
                if (validateInputs()){
                    val newTask=Task(
                        id=0,
                        title = titleInput.text.toString(),
                        time = pickInput.text.toString(),
                        date = pickDateInput.text.toString(),
                        status = TaskStatus.NEW
                        )
                    viewModel.addTask(newTask)
                    bottomSheet.state=BottomSheetBehavior.STATE_COLLAPSED
                }

            }

        }

        pickTime()
        pickDate()

    }



    private fun initBottomSheetViews(){
        bottomSheetLayout=findViewById(R.id.app_bottom_sheet)
        pickTimeLayout=findViewById(R.id.pickTimeLayout)
        pickInput=findViewById(R.id.pickInput)
        titleLayout=findViewById(R.id.pickTitleLayout)
        titleInput=findViewById(R.id.pickTitleInput)
        pickDateLayout=findViewById(R.id.pickDateLayout)
        pickDateInput=findViewById(R.id.dateInput)

    }


    private fun setBehavior(){
        bottomSheet= BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheet.setBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_EXPANDED->{
                        bottomSheetLayout.visibility=View.VISIBLE
                        binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_add_24)

                    }
                    BottomSheetBehavior.STATE_COLLAPSED->{
                        bottomSheetLayout.visibility=View.INVISIBLE
                        binding.floatingActionButton.setImageResource(R.drawable.ic_baseline_edit_24)
                        clearData()
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

    }


    private fun pickTime(){
        pickInput.setOnClickListener {
            val currentTime=Calendar.getInstance()
            val startHour=currentTime.get(Calendar.HOUR_OF_DAY)
            val startMinute=currentTime.get(Calendar.MINUTE)

            TimePickerDialog(this,TimePickerDialog.OnTimeSetListener{_,hourOfDay,minute->
                val time= Time(hourOfDay,minute,0)
                val timeFormat= SimpleDateFormat("h:mm a",Locale.UK)
                pickInput.setText(timeFormat.format(time))
            },startHour,startMinute,false).show()

        }
    }

    private fun pickDate(){
        pickDateInput.setOnClickListener {
            val calender= Calendar.getInstance()

            DatePickerDialog(this,DatePickerDialog.OnDateSetListener{_,year,month,day->
                calender.set(Calendar.YEAR,year)
                calender.set(Calendar.MONTH,month)
                calender.set(Calendar.DAY_OF_MONTH,day)
                val dateFormat=SimpleDateFormat("dd MMMM, yyyy",Locale.UK)
                pickDateInput.setText(dateFormat.format(calender.time))
            },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }



    private fun validateInputs():Boolean{
        var valid=true

        if (titleInput.text==null||titleInput.text!!.isEmpty()) {
            valid=false
            titleLayout.error="Pleas Enter The Title"
        }else{
            titleLayout.isErrorEnabled=false
        }
        if (pickInput.text==null||pickInput.text!!.isEmpty()) {
            valid=false
            pickTimeLayout.error="Pleas Enter The Time"
        }else{
            pickTimeLayout.isErrorEnabled=false
        }
        if (pickDateInput.text==null||pickDateInput.text!!.isEmpty()) {
            valid=false
            pickDateLayout.error="Pleas Enter The Date"
        }else{
            pickDateLayout.isErrorEnabled=false
        }

     return valid
    }



    private fun clearData(){
        pickInput.text?.clear()
        titleInput.text?.clear()
        pickDateInput.text?.clear()
        titleLayout.isErrorEnabled=false
        pickTimeLayout.isErrorEnabled=false
        pickDateLayout.isErrorEnabled=false
    }





}