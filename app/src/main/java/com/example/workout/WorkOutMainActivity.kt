package com.example.workout

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.workout.DataType.RecordOfDay
import com.example.workout.DataType.WO_Record
import com.example.workout.helper.Helper
import com.google.gson.Gson
import java.time.LocalDate
import java.util.*


class WorkOutMainActivity : AppCompatActivity() {

    lateinit var m_today_record : RecordOfDay
    lateinit var m_today_workout : Vector<WO_Record>
    lateinit var m_focusedItem : WO_Record

    var m_input_number : Int = 0
    var m_focus_toptab : Int = 0


    // VIEW ITEM
    lateinit var m_toptab_1 : TextView
    lateinit var m_toptab_2 : TextView
    lateinit var m_toptab_3 : TextView

    lateinit var m_avg : TextView
    lateinit var m_sum : TextView
    lateinit var m_count : TextView

    lateinit var m_add_10 : TextView
    lateinit var m_add_5 : TextView
    lateinit var m_add_1 : TextView

    lateinit var m_input_text : TextView

    lateinit var m_add_btn : TextView
    lateinit var m_fix_btn : TextView

    fun getTodayRecord() : RecordOfDay {
        val mPrefs = getSharedPreferences("record", MODE_PRIVATE)
        val json_retrive = mPrefs.getString("all_record", "")
        val obj: RecordOfDay = Gson().fromJson(json_retrive, RecordOfDay::class.java)
        return obj
    }

    fun saveTodayRecord() {
        val mPrefs = getSharedPreferences("record", MODE_PRIVATE)
        val prefsEditor  = mPrefs.edit()
        val gson = Gson()
        val json_saving = gson.toJson(m_today_record)
        prefsEditor.putString("all_record", json_saving)
        prefsEditor.commit()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewItemBinding()
        setInitProperty()
        setListener()
        updateViewItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveTodayRecord()
    }

    fun updateFocusedRecord() {
        m_focusedItem = m_today_workout[m_focus_toptab]
    }

    fun initProperty_TodayRecord() {
        var todayRecord = getTodayRecord()

        if (todayRecord.IsEmpty()) {
            m_today_record = RecordOfDay(LocalDate.now(), Vector<WO_Record>())
            var listOfType : MutableList<String> = mutableListOf("턱걸이", "푸시업", "달라기")
            for (item in listOfType) {
                m_today_record.listOfRecord.add(WO_Record(item, 0, 0))
            }
        }
        else {
            m_today_record = todayRecord
        }
        m_today_workout = m_today_record.listOfRecord
    }


    fun setInitProperty() {
        initProperty_TodayRecord()
        updateFocusedRecord()
    }

    fun setViewItemBinding() {
        m_toptab_1 = findViewById<TextView>(R.id.toptab_1)
        m_toptab_2 = findViewById<TextView>(R.id.toptab_2)
        m_toptab_3 = findViewById<TextView>(R.id.toptab_3)

        m_avg = findViewById<TextView>(R.id.avg)
        m_sum = findViewById<TextView>(R.id.sum)
        m_count = findViewById<TextView>(R.id.count)

        m_add_1 = findViewById<TextView>(R.id.add_1)
        m_add_5 = findViewById<TextView>(R.id.add_5)
        m_add_10 = findViewById<TextView>(R.id.add_10)

        m_input_text = findViewById<TextView>(R.id.enter_number)

        m_add_btn = findViewById<TextView>(R.id.add_btn)
        m_fix_btn = findViewById<TextView>(R.id.fix_btn)
    }

    fun setListener() {

        val focus_color = ContextCompat.getColor(this, R.color._light_green)
        val normal_color = ContextCompat.getColor(this, R.color.beige)

    // TOP_TAB
        m_toptab_1.setOnClickListener { button ->
            m_focus_toptab = 0
            updateFocusedRecord()
            button.setBackgroundColor(focus_color)
            m_toptab_2.setBackgroundColor(normal_color)
            m_toptab_3.setBackgroundColor(normal_color)
            updateViewItems()
        }

        m_toptab_2.setOnClickListener { button ->
            m_focus_toptab = 1
            updateFocusedRecord()
            button.setBackgroundColor(focus_color)
            m_toptab_1.setBackgroundColor(normal_color)
            m_toptab_3.setBackgroundColor(normal_color)
            updateViewItems()
        }

        m_toptab_3.setOnClickListener{ button->
            m_focus_toptab = 2
            updateFocusedRecord()
            button.setBackgroundColor(focus_color)
            m_toptab_1.setBackgroundColor(normal_color)
            m_toptab_2.setBackgroundColor(normal_color)
            updateViewItems()
        }
    // ADD NUMBER
        m_add_1.setOnClickListener {
            m_input_number += 1
            m_input_text.setText(m_input_number.toString())
        }

        m_add_5.setOnClickListener {
            m_input_number += 5
            m_input_text.setText(m_input_number.toString())
        }

        m_add_10.setOnClickListener {
            m_input_number += 10
            m_input_text.setText(m_input_number.toString())
        }

    // ADD OR FIX
        m_add_btn.setOnClickListener {
            if (0 >= m_input_number)
                return@setOnClickListener
            m_focusedItem.apply {
                cnt++
                sum += m_input_number
            }
            m_input_number = 0
            updateViewItems()
        }
    }

    fun updateViewItems () {
        var sum     : Int    = 0
        var avg     : Double = 0.0
        var cnt     : Int    = 0

        // 포커스된 아이템에 대한 정보를 가져온다.
        sum = m_focusedItem.sum
        cnt = m_focusedItem.cnt
        avg = m_focusedItem.sum.toDouble() / m_focusedItem.cnt

        // 평균값을 소수점 첫째자리로 변경
        if (avg.isNaN())
            avg = 0.0
        avg = Helper.roundOffDecimal(avg)

        // View 를 업데이트한다.
        m_sum.setText(sum.toString())
        m_count.setText(cnt.toString())
        m_avg.setText( avg.toString() )
        m_input_text.setText(m_input_number.toString())
    }
}