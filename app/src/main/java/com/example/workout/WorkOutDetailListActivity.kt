package com.example.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.workout.DataType.RecordOfDay
import com.example.workout.helper.Helper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkOutDetailListActivity : AppCompatActivity() {

    private lateinit var _allDayRecord : MutableMap<String, RecordOfDay>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_out_detail_list)

        var container = findViewById<LinearLayout>(R.id.workout_container)

        _allDayRecord = getAllDayRecord() ?: mutableMapOf()
        for ( record in _allDayRecord ) {
            var sum = 0
            // if record has sum number, then insert
            val list =  record.value.listOfRecord
            for(item in list)
                sum += item.sum
            if (sum == 0 && record.key != LocalDate.now().toString())
                continue
            // record insert
            var itemView =  layoutInflater.inflate(R.layout.list_item_workout_detail, null)
            var dateView =  itemView.findViewById<TextView>(R.id.workout_date)
            var tab1View =  itemView.findViewById<TextView>(R.id.workout_item_of_tap1)
            var tab2View =  itemView.findViewById<TextView>(R.id.workout_item_of_tap2)
            var tab3View =  itemView.findViewById<TextView>(R.id.workout_item_of_tap3)
            var tab4View =  itemView.findViewById<ImageView>(R.id.workout_item_of_tap4)


            tab1View.text = record.value.listOfRecord[0].sum.toString()
            tab2View.text = record.value.listOfRecord[1].sum.toString()
            tab3View.text = record.value.listOfRecord[2].sum.toString()
            var date  =  LocalDate.parse(record.value.date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            dateView.text = ""+date.monthValue + "-" + date.dayOfMonth

            container.addView(itemView)

            tab4View.setOnClickListener {
                record.value.listOfRecord[0].sum = 0
                record.value.listOfRecord[0].cnt = 0
                record.value.listOfRecord[1].sum = 0
                record.value.listOfRecord[1].cnt = 0
                record.value.listOfRecord[2].sum = 0
                record.value.listOfRecord[2].cnt = 0
                tab1View.text = "0"
                tab2View.text = "0"
                tab3View.text = "0"
                pushAllDayRecordToSP(_allDayRecord)
            }
        }
    }

    fun getAllDayRecord() : MutableMap<String, RecordOfDay>? {
        return Helper.getFromSP("AllDayRecord", "record", this)
    }

    fun pushAllDayRecordToSP(record : MutableMap<String, RecordOfDay>){
        Helper.pushToSP(record, "AllDayRecord", "record", this)
    }
}