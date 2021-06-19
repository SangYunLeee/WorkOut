package com.example.workout

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.workout.DataType.RecordOfDay
import com.example.workout.DataType.WO_Record
import com.example.workout.helper.Helper
import java.time.LocalDate
import java.util.*

// FUNCTION NAME DEFINITION
    // SP -> Local : pull*FromSP
    // SP <- Local : push*ToSP
    // View -> Local : localize*
    // View <- Local : updateView*

class WorkOutMainActivity : AppCompatActivity() {
    var m_all_record : MutableMap<String, RecordOfDay> = mutableMapOf<String, RecordOfDay>()
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
    lateinit var m_log_btn : TextView
    lateinit var m_delete_today_btn : TextView

    fun getTodayRecord() : RecordOfDay? {
        var allDayRecord : MutableMap<String, RecordOfDay>? = getAllDayRecord()
        var today = LocalDate.now().toString()
        return allDayRecord?.get(today)
    }

    fun getTodayRecordFromRunTime() : RecordOfDay? {
        var current = LocalDate.now().toString()
        return m_all_record.get(current) ?: null
    }

    fun getAllDayRecord() : MutableMap<String, RecordOfDay>? {
        return Helper.getFromSP("AllDayRecord", "record", this)
    }

    fun pushAllDayRecordToSP(){
        Helper.pushToSP(m_all_record, "AllDayRecord", "record", this)
    }

    fun localizeAllDayRecord(){
        var today = LocalDate.now().toString()
        m_all_record[today] = m_today_record
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewItemBinding()
        setInitProperty()
        setListener()
        updateViewItems()
    }

    override fun onResume() {
        super.onResume()
        //property update
        setResumeProperty()
        // View update
        updateViewItems()
        updateViewTopTab(m_focus_toptab)
    }

    fun rePointingFocusedRecord(focusedTab : Int = 0) {
        m_focus_toptab = focusedTab
        m_focusedItem = m_today_workout[focusedTab]
    }

    fun pullRecordFromSP() {
        m_all_record = getAllDayRecord()?: mutableMapOf()
        var todayRecord = getTodayRecordFromRunTime()
        if (todayRecord != null) {
            m_today_record = todayRecord
        }
        else {
            // Initialize today record
            m_today_record = RecordOfDay(LocalDate.now().toString(), Vector<WO_Record>())
            var listOfType : MutableList<String> = mutableListOf("턱걸이", "푸시업", "스쿼트")
            for (item in listOfType) {
                m_today_record.listOfRecord.add(WO_Record(item, 0, 0))
            }
        }
        // set reference for workout record
        m_today_workout = m_today_record.listOfRecord
    }

    fun setInitProperty() {
        pullRecordFromSP()
        rePointingFocusedRecord()
    }

    fun setResumeProperty() {
        pullRecordFromSP()
        rePointingFocusedRecord(m_focus_toptab)
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
        m_log_btn = findViewById<TextView>(R.id.log_btn)
        m_delete_today_btn = findViewById<TextView>(R.id.delete_today_workout)
    }

    fun setListener() {
    // TOP_TAB
        m_toptab_1.setOnClickListener {
            changeTopTab(0)
            updateViewItems()
        }

        m_toptab_2.setOnClickListener {
            changeTopTab(1)
            updateViewItems()
        }

        m_toptab_3.setOnClickListener{
            changeTopTab(2)
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

            localizeAllDayRecord()
            pushAllDayRecordToSP()
            updateViewItems()
        }

        m_fix_btn.setOnClickListener {
            // reset input text
            m_input_number = 0
            m_input_text.setText(m_input_number.toString())
        }

        m_log_btn.setOnClickListener {
            val intent = Intent(this, WorkOutDetailListActivity::class.java)
            startActivity(intent)
        }

        m_delete_today_btn.setOnClickListener {
            m_today_workout.clear()
            var listOfType : MutableList<String> = mutableListOf("턱걸이", "푸시업", "스쿼트")
            for (item in listOfType) {
                m_today_workout.add(WO_Record(item, 0, 0))
            }
            pushAllDayRecordToSP()
            rePointingFocusedRecord(m_focus_toptab)
            updateViewItems()
            updateViewTopTab(m_focus_toptab)
        }
    }

    fun updateViewItems () {
        // 포커스된 아이템에 대한 정보를 가져온다.
        var sum  = m_focusedItem.sum
        var cnt  = m_focusedItem.cnt
        var avg  = m_focusedItem.sum.toDouble() / m_focusedItem.cnt

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

    fun changeTopTab(focus : Int) {
        // change properties
        rePointingFocusedRecord(focus)
        // change view's items
        updateViewTopTab(focus)
    }

    fun updateViewTopTab(focus : Int = 0) {
        val focus_color = ContextCompat.getColor(this, R.color.basil_green_500)
        val normal_color = ContextCompat.getColor(this, R.color.basil_green_100)

        val focus_text_color = ContextCompat.getColor(this, R.color.white_smoke)
        val normal_text_color = ContextCompat.getColor(this, R.color.dim_gray)


        m_toptab_1.apply {
            setBackgroundColor(normal_color)
            setTextColor(normal_text_color)

        }
        m_toptab_2.apply {
            setBackgroundColor(normal_color)
            setTextColor(normal_text_color)
        }
        m_toptab_3.apply {
            setBackgroundColor(normal_color)
            setTextColor(normal_text_color)
        }
        var focusedTab : TextView? = when (focus) {
            0 -> m_toptab_1
            1 -> m_toptab_2
            2 -> m_toptab_3
            else -> null
        }
        focusedTab?.apply {
            setBackgroundColor(focus_color)
            setTextColor(focus_text_color)
        }
    }
}