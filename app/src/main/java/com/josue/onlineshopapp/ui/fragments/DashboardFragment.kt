package com.josue.onlineshopapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.josue.onlineshopapp.R
import com.josue.onlineshopapp.ui.activities.SettingsActivity

class DashboardFragment : Fragment() {

    //fun to inflate the option menu in fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //option menu in fragment
        setHasOptionsMenu(true)
    }

    //fun for the views
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val textView: TextView = root.findViewById(R.id.text_dashboard)
        textView.text = "This is dashboard Fragment"

        return root
    }

    //fun to inflate the Dashboard menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //fun to handle the action items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id){

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)

    }



}