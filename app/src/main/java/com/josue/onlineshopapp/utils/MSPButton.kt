package com.josue.onlineshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MSPButton(context: Context, attrs: AttributeSet): AppCompatButton(context,attrs) {

    init{
        //call func to apply the font to the components
        applyFont()
    }

    private fun applyFont() {
        //getting the font file to apply in the text
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}