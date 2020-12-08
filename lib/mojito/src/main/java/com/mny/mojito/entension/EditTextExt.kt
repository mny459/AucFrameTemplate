package com.mny.mojito.entension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 *@author mny on 2019-10-20.
 *        Email：mny9@outlook.com
 *        Desc:
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}