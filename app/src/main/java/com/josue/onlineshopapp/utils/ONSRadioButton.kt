package com.josue.onlineshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class ONSRadioButton(context: Context, attributeSet: AttributeSet):AppCompatRadioButton(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        //getting the font file to apply in the text
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}