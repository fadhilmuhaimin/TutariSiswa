package com.pajaga.utils.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.autodhil.tutarisiswa.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import com.pajaga.utils.other.Constant

//object CustomAlertDialog {
//
//    var getInputTxt = ""
//    var getInputDropdown = ""
//
//    fun getView(context: Context): View? {
//        val customAlertDialogView = LayoutInflater.from(context)
//            .inflate(R.layout.examples_custom_alert_dialog, null, false)
//
//        val examplesInputTxt = customAlertDialogView.findViewById<TextInputEditText>(R.id.examplesInputTxt)
//        val examplesInputDropdown = customAlertDialogView.findViewById<TextInputLayout>(R.id.examplesInputDropdown)
//        val examplesDropdown = (examplesInputDropdown.editText as? AutoCompleteTextView)
//
//        val adapter = ArrayAdapter(context, R.layout.examples_custom_list_dropdown, Constant.exampleListDropwdownText)
//
//        examplesDropdown?.setAdapter(adapter)
//
//        getInputTxt = examplesInputTxt.text.toString()
//
//        examplesDropdown?.setOnItemClickListener { adapterView, view, i, l ->
//            val getItem = adapterView.getItemAtPosition(i)
//            getInputDropdown = (getItem as String?).toString()
//        }
//        return customAlertDialogView
//    }
//}