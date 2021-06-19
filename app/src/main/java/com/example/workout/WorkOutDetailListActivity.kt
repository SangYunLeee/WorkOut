package com.example.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.example.workout.DataType.RecordOfDay
import com.example.workout.helper.Helper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WorkOutDetailListActivity : AppCompatActivity() {

    lateinit var m_allDayRecord : MutableMap<String, RecordOfDay>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_out_detail_list)

        var container = findViewById<LinearLayout>(R.id.workout_container)

        m_allDayRecord = getAllDayRecord() ?: mutableMapOf()
        for ( item_all in m_allDayRecord ) {
            var sum = 0
            // if item_all has sum number, then insert
            val list =  item_all.value.listOfRecord
            for(item in list)
                sum += item.sum
            if (sum == 0 && item_all.key != LocalDate.now().toString())
                continue
            // item_all insert
            var itemView =  layoutInflater.inflate(R.layout.list_item_workout_detail, null)
            var dateView =  itemView.findViewById<TextView>(R.id.workout_date)
            var tab1View =  itemView.findViewById<TextView>(R.id.workout_item_of_tap1)
            var tab2View =  itemView.findViewById<TextView>(R.id.workout_item_of_tap2)
            var tab3View =  itemView.findViewById<TextView>(R.id.workout_item_of_tap3)

            tab1View.setText(item_all.value.listOfRecord[0].sum.toString())
            tab2View.setText(item_all.value.listOfRecord[1].sum.toString())
            tab3View.setText(item_all.value.listOfRecord[2].sum.toString())
            var date  =  LocalDate.parse(item_all.value.date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            dateView.setText(""+date.monthValue + "-" + date.dayOfMonth)

            container.addView(itemView)

        }
    }

    fun getAllDayRecord() : MutableMap<String, RecordOfDay>? {
        return Helper.getFromSP("AllDayRecord", "record", this)
    }
}