package com.dts.fbase

import com.dts.base.clsClasses
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class fbServicio (troot: String?, month: String?, folder: String?) : fbBase(troot) {

    var litem: clsClasses.clsFbServicio? = null
    var items = ArrayList<clsClasses.clsFbServicio>()

    var exist=-1

    override var path=root+"/"+month+"/"+folder+"/"

    fun setItem(item: clsClasses.clsFbServicio?) {
        fdt = fdb.getReference(path+item?.id)
        fdt.setValue(item)
    }

    fun delItem(key: Int) {
        fdb.getReference(path+"/"+key).removeValue()
    }

    fun load(itemid: Int,rnCallback: Runnable) {
        var sitemid=""+itemid

        try {

            fdb.getReference(path).child(sitemid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        litem = snapshot.getValue(clsClasses.clsFbServicio::class.java)
                        exist=1
                    } else {
                        exist=0
                    }
                    callBack = rnCallback
                    runCallBack()
                }

                override fun onCancelled(error: DatabaseError) {
                    exist=-1
                    callBack = rnCallback
                    runCallBack()
                }
            })
        } catch (e: Exception) {
            exist=-1
            callBack = rnCallback
            runCallBack()
        }
    }

    fun listItems(rnCallback: Runnable) {
        try {
            items.clear()
            fdb.getReference(path).get().addOnCompleteListener(OnCompleteListener<DataSnapshot> { task ->
                if (task.isSuccessful) {
                    try {
                        items.clear()
                        val res = task.result

                        if (res.exists()) {
                            for (node in res.children) {

                                litem = clsClasses.clsFbServicio()

                                litem!!.id = node.child("id").getValue(Int::class.java)!!
                                litem!!.numero = ""+node.child("numero").getValue(String::class.java)!!
                                litem!!.cliente = ""+node.child("cliente").getValue(String::class.java)!!
                                litem!!.estado = node.child("estado").getValue(String::class.java)!!
                                litem!!.fin = node.child("fin").getValue(Long::class.java)!!
                                litem!!.inicio = node.child("inicio").getValue(Long::class.java)!!
                                litem!!.user = ""+node.child("user").getValue(String::class.java)!!
                                litem!!.tarea = ""+node.child("tarea").getValue(String::class.java)!!

                                items.add(litem!!)
                            }
                        }
                        errflag = false
                    } catch (e: Exception) {
                        value = "filterItems : "+e.toString()
                        errflag = true
                    }
                } else {
                    value = task.exception!!.message
                    errflag = true
                }
                callBack = rnCallback
                runCallBack()
            })
        } catch (e: Exception) {
            value = e.message
            errflag = true
        }
    }

}