package com.josue.onlineshopapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.josue.onlineshopapp.R

class MSPTextViewBold(context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {

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