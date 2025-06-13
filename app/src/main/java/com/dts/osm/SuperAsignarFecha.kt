package com.dts.osm

import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dts.base.clsClasses
import com.dts.classes.RecyclerItemClickListener
import com.dts.classes.clsUsuarioObj
import com.dts.classes.extChkListDlg
import com.dts.classes.extListDlg
import com.dts.ladapt.LA_SuperFechaAdapter
import com.dts.webservice.wsCommit
import com.dts.webservice.wsOpenDT
import java.time.Duration
import java.time.LocalTime

class SuperAsignarFecha : PBase() {

    var recview : RecyclerView? = null
    var lbltit : TextView? = null
    var lblusers : TextView? = null
    var lbldate : TextView? = null
    var lblhour : TextView? = null
    var lblmin : TextView? = null
    var lbldur : TextView? = null
    var imgprev : ImageView? = null
    var imgnext : ImageView? = null
    var pbar : ProgressBar? = null

    var UsuarioObj: clsUsuarioObj? = null

    var adapter: LA_SuperFechaAdapter? = null

    var wso: wsOpenDT? = null
    var wscom : wsCommit? = null

    var items = ArrayList<clsClasses.clsOrdenSupHora>()
    var users = ArrayList<clsClasses.clsCheckDlg>()
    var item = clsClasses.clsOrdenSupHora()

    var idorden = 0
    var afecha = 0L
    var hoy = 0L
    var ahora = 0
    var amin = 0
    var adur = 0.0

    lateinit var itime : LocalTime
    lateinit var ftime : LocalTime

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_super_asignar_fecha)

            super.initbase(savedInstanceState)

            recview = findViewById(R.id.recview)
            recview?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
            lbltit = findViewById(R.id.textView14);lbltit?.text=gl?.gstr
            lblusers = findViewById(R.id.textView41);lblusers?.text=""
            lbldate = findViewById(R.id.textView44);
            lblhour = findViewById(R.id.textView45);
            lblmin = findViewById(R.id.textView46);
            lbldur = findViewById(R.id.textView47);
            imgprev = findViewById(R.id.imageView46);
            imgnext = findViewById(R.id.imageView47);
            pbar = findViewById(R.id.progressBar6)

            idorden=gl?.idorden!!

            UsuarioObj = clsUsuarioObj(this, Con!!, db!!)

            wso = wsOpenDT(gl?.wsurl)
            wscom = wsCommit(gl?.wsurl)

            Inicio()
            setHandlers()

        } catch (e:Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+". "+e.message)
        }
    }

    //region Events

    fun doApply(view: View) {
        try {
            if (tieneTecnicos()) {
                if (validaHorarios()) msgask(0,"¿Guardar horario?")
            } else {
                msgbox("No está asignado ninguno tecnico");return
            }
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun doUsers(view: View) {
        listaTecnicos()
    }

    fun doPrev(view: View) {
        if (afecha==hoy) {
            return
        } else {
            afecha=du?.addDays(afecha,-1)!!
            lbldate?.text=du?.sfechash(afecha)+ " "+ du?.dayweeksp(afecha)
        }
    }

    fun doNext(view: View) {
        afecha=du?.addDays(afecha,1)!!
        lbldate?.text=du?.sfechash(afecha)+ " "+ du?.dayweeksp(afecha)
    }

    fun doHour(view: View) {
        showHourMenu()
    }

    fun doMin(view: View) {
        showMinMenu()
    }

    fun doDur(view: View) {
        showDurMenu()
    }

    fun doExit(view: View) {
        finish()
    }

    fun setHandlers() {
        try {

            recview?.addOnItemTouchListener(
                RecyclerItemClickListener(this, recview!!,
                    object : RecyclerItemClickListener.OnItemClickListener {

                        override fun onItemClick(view: View, position: Int) {
                            item = items[position]

                            //val context: Context = view?.getContext()!!
                            //val intent = Intent(context, SuperAsignarFecha::class.java)
                            //context.startActivity(intent)

                        }

                        override fun onItemLongClick(view: View?, position: Int) { }
                    })
            )

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

    }

    //endregion

    //region Main

    fun listItems() {
        try {
            adapter = LA_SuperFechaAdapter(items)
            recview?.adapter = adapter
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun Inicio() {

        afecha = du?.actDate!!
        hoy = afecha
        ahora = du?.gethour(du?.actDateTime!!)!!+1
        amin = 0
        adur = 1.0

        lbldate?.text=du?.sfechash(afecha)+ " "+ du?.dayweeksp(afecha)
        lblhour?.text=if (ahora>9) ""+ahora else "0"+ahora
        lblmin?.text=if (amin>9) ""+amin else "0"+amin
        lbldur?.text="1"

        listaHoras()
        llenaTecnicos()
    }

    fun listaHoras() {
        try {

            pbar?.isVisible=true
            items.clear()

            var fs=du?.univfechash(afecha)

            sql = "SELECT dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO, dbo.D_ORDEN_SERVICIO_ENC.NUMERO, dbo.Users.Nombre, dbo.P_CLIENTE.NOMBRE AS CLIENTE,  " +
                    "dbo.P_TIPO_ORDEN_SERVICIO.NOMBRE AS TIPO, dbo.AndrDate(dbo.D_ORDEN_SERVICIO_ENC.FECHA_SERVICIO) AS FECHA, " +
                    "dbo.AndrHour(dbo.D_ORDEN_SERVICIO_ENC.HORA_SERVICIO_INI) AS HORAINI, dbo.AndrHour(dbo.D_ORDEN_SERVICIO_ENC.HORA_SERVICIO_FIN) AS HORAFIN, dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_USUARIO  " +
                    "FROM dbo.D_ORDEN_SERVICIO_ENC " +
                    "INNER JOIN dbo.D_ORDEN_SERVICIO_USUARIO ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_ORDEN_SERVICIO = dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_ORDEN_SERVICIO " +
                    "INNER JOIN dbo.Users ON dbo.D_ORDEN_SERVICIO_USUARIO.CODIGO_USUARIO = dbo.Users.UserId " +
                    "INNER JOIN dbo.P_CLIENTE ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_CLIENTE = dbo.P_CLIENTE.CODIGO_CLIENTE " +
                    "INNER JOIN dbo.P_TIPO_ORDEN_SERVICIO ON dbo.D_ORDEN_SERVICIO_ENC.CODIGO_TIPO_ORDEN_SERVICIO = dbo.P_TIPO_ORDEN_SERVICIO.CODIGO_TIPO_ORDEN_SERVICIO " +
                    "WHERE (dbo.D_ORDEN_SERVICIO_ENC.CODIGO_EMPRESA = "+gl?.idemp+") " +
                    "AND (dbo.D_ORDEN_SERVICIO_ENC.FECHA_SERVICIO >='"+fs+"') " +
                    "ORDER BY dbo.D_ORDEN_SERVICIO_ENC.FECHA_SERVICIO, dbo.D_ORDEN_SERVICIO_ENC.HORA_SERVICIO_INI, dbo.Users.Nombre"

            wso!!.execute(sql) { cbListaHoras() }

        } catch (e: Exception) {
            pbar?.isVisible=false
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cbListaHoras() {
        val dt: Cursor?
        var ff = 0L
        var fini = 0L
        var ffin = 0L

        try {
            if (wso!!.errflag) throw java.lang.Exception(wso!!.error)

            dt = wso!!.openDTCursor

            var rn=dt?.count!!
            if (rn>0) {

                dt?.moveToFirst()

                for (i in 0 until rn) {
                    item=clsClasses.clsOrdenSupHora()

                    item.CODIGO_ORDEN_SERVICIO = dt?.getInt(0)!!
                    item.NUMERO = dt?.getString(1)!!
                    item.USUARIO = dt?.getInt(8)!!
                    item.NOMBRE = dt?.getString(2)!!
                    item.CLIENTE = dt?.getString(3)!!
                    item.TIPO = dt?.getString(4)!!

                    ff = dt?.getLong(5)!!
                    fini = dt?.getLong(6)!!
                    ffin = dt?.getLong(7)!!

                    item.FECHA = ff
                    item.FECHAINI = ff+fini
                    item.FECHAFIN = ff+ffin
                    item.HORAINI = fini
                    item.HORAFIN = ffin
                    item.SFECHAINI  = du?.sfechash(item.FECHAINI)+" "+du?.dayweeksp(item.FECHAINI)+" - "+du?.shora(item.FECHAINI)
                    item.SFECHAFIN  = du?.shora(item.FECHAFIN)!!

                    items.add(item)

                    if (i<rn-1)  dt?.moveToNext()
                }
            }

            listItems()
        } catch (e: java.lang.Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
        pbar?.isVisible=false
    }

    fun validaHorarios() : Boolean {
        try {
            val dur=adur*60;val idur=dur.toLong()
            itime = LocalTime.of(ahora, amin)
            ftime = itime.plus( Duration.ofMinutes(idur))

            var citas = ArrayList<clsClasses.clsOrdenSupHora>()
            for (itm in items) {
                if (itm.FECHA==afecha) citas.add(itm)
            }

            val hval=clsValidaHorario(this,afecha,itime,ftime,users,citas)

            if (hval.validate()) {
                return true
            } else {
                msgbox(hval.errors,"Conflicto ")
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }

        return false
    }

    fun guardaAsignacion() {
        var sf  =""
        var sti = ""
        var stf = ""

        try {
            sf  = du?.univfechash(afecha)!!
            sti = timestr(itime)
            stf = timestr(ftime)

            sql = ""

            sql+="UPDATE D_ORDEN_SERVICIO_ENC SET " +
                    "CODIGO_ESTADO_ORDEN_SERVICIO=3, " +
                    "FECHA_SERVICIO='"+sf+"', " +
                    "HORA_SERVICIO_INI='"+sti+"', " +
                    "HORA_SERVICIO_FIN='"+stf+"' " +
                    "WHERE CODIGO_ORDEN_SERVICIO="+idorden+  ";"

            sql+="DELETE FROM D_ORDEN_SERVICIO_USUARIO WHERE (CODIGO_ORDEN_SERVICIO="+idorden+") ;"

            for (itm in users) {
                if (itm.check==1) {
                    sql += "INSERT INTO D_ORDEN_SERVICIO_USUARIO " +
                            "(CODIGO_ORDEN_SERVICIO, CODIGO_USUARIO, CODIGO_USUARIO_ASIGNO, USUARIO_AGR)  " +
                            "VALUES (" + idorden + "," + itm.id + "," + gl?.iduser + "," + gl?.iduser + "); "
                }
            }

            wscom!!.execute(sql, { cbuardaAsignacion() })

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun cbuardaAsignacion() {
        try {
            if (wscom?.errflag!!) throw java.lang.Exception()

            gl?.close_assign=true
            finish()

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    //endregion

    //region Valida horarios

    class clsValidaHorario {
        public var errors = ""

        var citas = ArrayList<clsCita>()
        var tecs = ArrayList<clsClasses.clsCheckDlg>()

        var hini = 0
        var hfin = 0
        var teccn = 0
        var flag = true
        var tname = ""

        constructor(owner : PBase, fecha : Long, horaini : LocalTime, horafin : LocalTime, usr : ArrayList<clsClasses.clsCheckDlg>, horas : ArrayList<clsClasses.clsOrdenSupHora>) {
            var cita : clsCita
            var hora = 0
            var min = 0
            var hmi = 0
            var hmf = 0

            try {

                for (itm in usr) {
                    if (itm.check==1) tecs.add(itm)
                }

                for (itm in horas) {
                    if (itm.FECHA==fecha) {

                        hora=owner?.du?.gethour(itm.FECHAINI)!!
                        min=owner?.du?.getmin(itm.FECHAINI)!!
                        hmi=100*hora+min

                        hora=owner?.du?.gethour(itm.FECHAFIN)!!
                        min=owner?.du?.getmin(itm.FECHAFIN)!!
                        hmf=100*hora+min

                        cita=clsCita(itm.USUARIO,hmi,hmf)
                        citas.add(cita)
                    }
                }

                hini = horaini.hour*100 + horaini.minute
                hfin = horafin.hour*100 + horafin.minute

                teccn = tecs.size
            } catch (e: Exception) {
                throw java.lang.Exception(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
            }
        }

        fun validate() : Boolean {
            errors = ""

            for (itm in tecs) {
                tname=itm.nombre!!
                validatec(itm.id)
            }

            return flag
        }

        fun validatec(idtec : Int ) {
            var hmi = 0
            var hmf = 0

            for (itm in citas) {
                if (itm.usuario==idtec) {
                    hmi=itm.hini;hmf=itm.hfin

                    if (hmi<hini && hmf>hini) {
                        flag=false
                        errors+=tname+" "+timestr(hmi)+" - "+timestr(hmf) +"\n"
                        return
                    }

                    if (hmi<hfin && hmf>hfin) {
                        flag=false
                        errors+=tname+" "+timestr(hmi)+" - "+timestr(hmf) +"\n"
                        return
                    }

                }
            }
        }

        data class clsCita (
            var usuario:Int = 0,
            var hini: Int =0,
            var hfin: Int =0
        )

        private fun timestr(tm : Int) : String{
            var hh=tm / 100
            var mm=tm % 100
            return ""+hh+":"+if (mm>9) ""+mm else "0"+mm
        }
    }

    //endregion

    //region Dialogs

    fun dialogswitch() {
        try {
            when (gl?.dialogid) {
                0 -> { guardaAsignacion() }
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun listaTecnicos() {
        try {
            val listdlg = extChkListDlg(0)

            listdlg.buildDialog(this, "Tecnico","Aplicar")

            listdlg.setWidth(-1)
            listdlg.setBottomCenterPosition()

            for (itm in users!!) {
                listdlg.addData(itm.id,itm.nombre!!,itm.check==1)
            }

            listdlg.clickListener= Runnable {
                var sidx=listdlg.selidx

                if (listdlg.getChecked(sidx)) {
                    users.get(sidx).check=1
                } else {
                    users.get(sidx).check=0
                }
            }

            listdlg.setOnRightClick { v: View? ->
                listdlg.dismiss()
            }

            listdlg.setOnLeftClick { v: View? ->
                nombresTecnicos()
                listdlg.dismiss()}
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun showHourMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@SuperAsignarFecha, "Hora")
            listdlg.setLines(8)
            listdlg.setWidth(-1)
            listdlg.setBottomCenterPosition()

            listdlg.addData( 8, " 8 ")
            listdlg.addData( 9,"  9 ")
            listdlg.addData(10," 10 ")
            listdlg.addData(11," 11 ")
            listdlg.addData(12," 12 ")
            listdlg.addData(13," 13 ")
            listdlg.addData(14," 14 ")
            listdlg.addData(15," 15 ")
            listdlg.addData(16," 16 ")
            listdlg.addData(17," 17 ")
            listdlg.addData(18," 18 ")

            listdlg.clickListener= Runnable {
                ahora=listdlg.selcodint
                lblhour?.text=if (ahora>9) ""+ahora else "0"+ahora
            }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showMinMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@SuperAsignarFecha, "Minutos")
            listdlg.setLines(4)
            listdlg.setWidth(-1)
            listdlg.setBottomCenterPosition()

            listdlg.addData( 0," 00 ")
            listdlg.addData(15," 15 ")
            listdlg.addData(30," 30 ")
            listdlg.addData(45," 45 ")

            listdlg.clickListener= Runnable {
                amin=listdlg.selcodint
                lblmin?.text=if (amin>9) ""+amin else "0"+amin
            }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    fun showDurMenu() {
        try {
            val listdlg = extListDlg();

            listdlg.buildDialog(this@SuperAsignarFecha, "Duración")
            listdlg.setLines(8)
            listdlg.setWidth(-1)
            listdlg.setBottomCenterPosition()

            listdlg.addData( 5," 0.5 ")
            listdlg.addData(10," 1")
            listdlg.addData(15," 1.5 ")
            listdlg.addData(20," 2 ")
            listdlg.addData(25," 2.5 ")
            listdlg.addData(30," 3")
            listdlg.addData(40," 4 ")
            listdlg.addData(50," 5 ")
            listdlg.addData(60," 6 ")
            listdlg.addData(70," 7 ")
            listdlg.addData(80," 8 ")

            listdlg.clickListener= Runnable {
                var idur=listdlg.selcodint
                var ddur= idur.toDouble()
                adur=ddur/10
                lbldur?.text=listdlg.getText(listdlg.selidx)
            }

            listdlg.setOnLeftClick { v: View? -> listdlg.dismiss() }
            listdlg.show()
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

    //region Aux

    fun llenaTecnicos() {
        try {
            users.clear()
            UsuarioObj?.fill("WHERE (rol='TEC') ORDER BY nombre")

            for (itm in UsuarioObj?.items!!) {
                var user=clsClasses.clsCheckDlg(itm.id,itm.nombre,0)
                users?.add(user)
            }
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message)
        }
    }

    fun nombresTecnicos() {
        var un=""
        try {
            for (itm in users!!) {
               if (itm.check==1) un+=itm.nombre+", "
            }
            un = if (un.length >= 2) un.dropLast(2) else ""
        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name+" . "+e.message);un=""
        }
        lblusers?.text=un
    }

    fun tieneTecnicos() : Boolean {
        var tc=0
        for (itm in users!!) {
            if (itm.check==1) tc++
        }
        return tc>0
    }

    fun timestr(tm : LocalTime) : String{
        var hh=tm.hour
        var mm=tm.minute
        return ""+hh+":"+if (mm>9) ""+mm else "0"+mm + ":00"
    }

    //endregion

    //region Activity Events

     override fun onResume() {
        try {
            super.onResume()
            gl?.dialogr = Runnable { dialogswitch() }

            UsuarioObj!!.reconnect(Con!!, db!!)

        } catch (e: Exception) {
            msgbox(object : Any() {}.javaClass.enclosingMethod.name + " . " + e.message)
        }
    }

    //endregion

}