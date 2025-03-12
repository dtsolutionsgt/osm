package com.dts.mant

import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import com.dts.ladapt.ListAdaptTablas
import com.dts.ladapt.ListAdaptTablas2
import com.dts.osm.PBase
import com.dts.osm.R

class Tablas : PBase() {

    var grid: GridView? = null
    var dgrid: GridView? = null
    var spin: Spinner? = null
    var spinf: Spinner? = null
    var pbar: ProgressBar? = null
    var txt1: EditText? = null

    val spinlist = ArrayList<String>()
    val values = ArrayList<String>()
    val dvalues = ArrayList<String>()
    var adapter: ListAdaptTablas? = null
    var dadapter: ListAdaptTablas2? = null

    var cw = 0
    var scod: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_tablas)

            super.initbase(savedInstanceState)

            grid = findViewById(R.id.gridview1) as GridView
            dgrid = findViewById(R.id.gridview2) as GridView
            spin = findViewById(R.id.spinner) as Spinner
            pbar = findViewById(R.id.progressBar3) as ProgressBar; pbar?.visibility= View.INVISIBLE
            txt1 = findViewById(R.id.editText1) as EditText

            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            cw = ((displayMetrics.widthPixels - 22) / 5) - 1

            setHandlers()

            fillSpinner()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //region Events

    fun doClear(view: View?) {
        try {
            txt1!!.setText("")
            txt1!!.requestFocus()
        } catch (e: java.lang.Exception) {
        }
    }

    private fun setHandlers() {
        try {
            spin!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    val spinlabel: TextView
                    try {
                        spinlabel = parentView.getChildAt(0) as TextView
                        spinlabel.setTextColor(Color.BLACK)
                        spinlabel.setPadding(5, 0, 0, 0)
                        spinlabel.textSize = 18f
                        spinlabel.setTypeface(spinlabel.typeface, Typeface.BOLD)
                        scod = spinlist[position]
                        if (!scod.equals(" ", ignoreCase = true)) {
                            txt1!!.setText("")
                            processTable()
                        }
                    } catch (e: java.lang.Exception) {
                        mu!!.msgbox(e.message)
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    return
                }
            }
            grid!!.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    try {
                        val lvObj = grid!!.getItemAtPosition(position)
                        val item = lvObj as String
                        adapter!!.setSelectedIndex(position)
                        toast(item)
                    } catch (e: java.lang.Exception) {
                        mu!!.msgbox(e.message)
                    }
                }
            dgrid!!.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    try {
                        val lvObj = dgrid!!.getItemAtPosition(position)
                        val item = lvObj as String
                        dadapter!!.setSelectedIndex(position)
                        toast(item)
                    } catch (e: java.lang.Exception) {
                        mu!!.msgbox(e.message)
                    }
                }
            dgrid!!.onItemLongClickListener =
                AdapterView.OnItemLongClickListener { parent, view, position, id ->
                    try {
                        val lvObj = dgrid!!.getItemAtPosition(position)
                        val item = lvObj as String
                        adapter!!.setSelectedIndex(position)
                        msgbox(item)
                    } catch (e: java.lang.Exception) {
                        mu!!.msgbox(e.message)
                    }
                    true
                }
            txt1!!.setOnKeyListener(View.OnKeyListener { arg0, arg1, arg2 ->
                if (arg2.action == KeyEvent.ACTION_DOWN) {
                    when (arg1) {
                        KeyEvent.KEYCODE_ENTER -> {
                            processTable()
                            return@OnKeyListener true
                        }
                    }
                }
                false
            })
        } catch (e: java.lang.Exception) {
            mu!!.msgbox(e.message)
        }
    }

    //endregion

    //region Main

    // Main
    private fun processTable() {
        try {
            pbar!!.visibility = View.VISIBLE

            val mmtimer = Handler()
            val mmrunner = Runnable {
                pbar!!.visibility = View.VISIBLE
                values.clear()
                dvalues.clear()
                adapter = ListAdaptTablas(this@Tablas, values)
                grid!!.adapter = adapter
                dadapter = ListAdaptTablas2(this@Tablas, dvalues)
                dgrid!!.adapter = dadapter
            }

            mmtimer.postDelayed(mmrunner, 50)
            val mtimer = Handler()
            val mrunner = Runnable { showData(scod!!) }
            mtimer.postDelayed(mrunner, 1000)

        } catch (e: java.lang.Exception) {
            mu!!.msgbox(e.message)
        }
    }

    private fun showData(tn: String) {
        val PRG: Cursor
        val dt: Cursor
        var n: String
        val flt: String
        var ss = ""
        var cc = 1
        var j: Int

        try {
            ss = "SELECT "
            sql = "PRAGMA table_info('$tn')"
            PRG = db!!.rawQuery(sql, null)
            cc = PRG.count
            PRG.moveToFirst()
            j = 0
            while (!PRG.isAfterLast) {
                var ci=PRG.getColumnIndex("name")
                n = PRG.getString(ci)
                // t=PRG.getString(PRG.getColumnIndex("type"));// INTEGER , TEXT , REAL
                values.add(n)
                ss = ss + n
                if (j < cc - 1) ss = "$ss,"
                PRG.moveToNext()
                j++
            }
            ss = "$ss FROM $tn"
            flt = txt1!!.text.toString()
            if (!mu!!.emptystr(flt)) ss = "$ss WHERE $flt"
        } catch (e: java.lang.Exception) {
            mu!!.msgbox(e.message)
        }

        val layoutParams = grid!!.layoutParams
        layoutParams.width = (cw * cc) + 25
        grid!!.layoutParams = layoutParams
        grid!!.columnWidth = cw
        grid!!.stretchMode = GridView.NO_STRETCH
        grid!!.numColumns = cc

        adapter = ListAdaptTablas(this, values)
        grid!!.adapter = adapter

        val dlayoutParams = dgrid!!.layoutParams
        dlayoutParams.width = (cw * cc) + 25
        dgrid!!.layoutParams = dlayoutParams
        dgrid!!.columnWidth = cw
        dgrid!!.stretchMode = GridView.NO_STRETCH
        dgrid!!.numColumns = cc

        try {
            dt = Con!!.OpenDT(ss)
            if (dt.count == 0) {
                pbar!!.visibility = View.INVISIBLE; return
            }
            dt.moveToFirst()
            while (!dt.isAfterLast) {
                for (i in 0 until cc) {
                    ss = try {
                        dt.getString(i)
                    } catch (e: java.lang.Exception) {
                        "?"
                    }
                    try {
                        if (ss.length > 100) ss = ss.substring(0, 99) + " ... "
                    } catch (e: java.lang.Exception) {
                        ss = "?"
                    }
                    dvalues.add(ss)
                }
                dt.moveToNext()
            }
        } catch (e: java.lang.Exception) {
            mu!!.msgbox(e.message)
        }

        dadapter = ListAdaptTablas2(this, dvalues)
        dgrid!!.adapter = dadapter
        pbar!!.visibility = View.INVISIBLE
    }


    //endregion

    //region Dialogs


    //endregion

    //region Aux

    private fun fillSpinner() {
        val DT: Cursor

        spinlist.clear();spinlist.add(" ")

        try {
            sql =
                "SELECT name FROM sqlite_master WHERE type='table' AND name<>'android_metadata' order by name"
            DT = Con!!.OpenDT(sql)
            DT.moveToFirst()
            while (!DT.isAfterLast) {
                spinlist.add(DT.getString(0))
                DT.moveToNext()
            }
        } catch (e: java.lang.Exception) {
            mu!!.msgbox(e.message)
        }
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinlist)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin!!.adapter = dataAdapter
    }

    //endregion

    //region Activity Events


    //endregion

}