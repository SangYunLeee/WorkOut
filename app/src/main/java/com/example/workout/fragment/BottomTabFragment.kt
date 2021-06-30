package com.example.workout.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.workout.BodyCheckMainActivity
import com.example.workout.R
import com.example.workout.WorkOutDetailListActivity
import com.example.workout.WorkOutMainActivity


class BottomTabFragment : Fragment(R.layout.fragment_bottm_tab) {
    lateinit var m_tab_btn1 : Button
    lateinit var m_tab_btn2 : Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        m_tab_btn1 = view.findViewById<Button>(R.id.fragment_tab_btn_1)
        m_tab_btn2 = view.findViewById<Button>(R.id.fragment_tab_btn_2)

        m_tab_btn1.setOnClickListener {
            val purposeActivity = WorkOutMainActivity::class.java
            if (activity?.javaClass == purposeActivity)
                return@setOnClickListener
            val intent = Intent(activity, purposeActivity)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        m_tab_btn2.setOnClickListener {
            val purposeActivity = BodyCheckMainActivity::class.java
            if (activity?.javaClass == purposeActivity)
                return@setOnClickListener
            val intent = Intent(activity, purposeActivity)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}