package com.dts.base

import kotlinx.serialization.Serializable


class clsClasses {


    data class clsCliente (
        var codigo_cliente : Int = 0,
        var nombre : String = "",
        var telefono : String = "",
        var direccion : String = "",
    )

    data class clsClientecontacto(
        var codigo_cliente_contacto : Int = 0,
        var codigo_cliente : String = "",
        var nombre : String = "",
        var telefono : String = "",
        var correo : String = "",
        var direccion : String = "",
        var Empresa : Int = 0,
    )

    data class clsClientedir(
        var codigo_direccion : Int = 0,
        var codigo_cliente : Int = 0,
        var direccion : String = "",
        var telefono : String = "",
        var referencia : String = "",
    )

    data class clsEstado(
        var codigo_ticket_estado : Int = 0,
        var nombre : String = "",
    )

    data class clsExistencia (
        var codigo : Int = 0,
        var nombre : String = "",
        var cant : Double = 0.0,
    )

    data class clsProdprecio (
        var codigo_precio : Int = 0,
        var codigo_producto : Int = 0,
        var nivel : Int = 0,
        var precio : Int = 0,
        var unidadmedida : String = "",
    )

    data class clsProducto (
        var codigo_producto : Int = 0,
        var desclarga : String = "",
        var codigo_tipo : String = "",
    )

    data class clsUsuario (
        var id : Int = 0,
        var nombre : String = "",
        var pin :  Int = 0,
        var rol : String = "",
    )

    data class clsTiposervicios (
        var codigo_tipo_departamento : Int = 0,
        var codigo_ticket_departamento : Int = 0,
        var nombre : String = "",
    )



    // ----------------------------------------------------------

    data class clsLocItem(
        var id: Int = 0,
        var fecha: Long = 0L,
        var longit: Double = 0.0,
        var latit: Double = 0.0,
        var bandera: Long = 0L,
    )

    data class clsParam (
        var codigo : Int = 0,
        var empresa : Int = 0,
        var id : Int = 0,
        var userid : Int = 0,
        var nombre : String = "",
        var valor : String = "",
    )

    // 0-idempresa, 1-idusuario, 2-Nombre Empresa, 3-modo,4-rol,
    // 5-pais, 6-idsucursal, 7-moneda
    data class clsSavepos (
        var id : Int = 0,
        var valor : String = "",
    )






    // -----------------------------------

    data class clsEnvioimagen (
        var id : String = "",
        var tipo : Int = 0,
    )

    data class clsEstadoorden (
        var id : Int = 0,
        var nombre : String = "",
    )

    data class d_menuitem(
        val mid: Int = 0,
        val nombre: String
    )



    data class clsCoordItem(
        var id: Int = 0,
        var longit: Double = 0.0,
        var latit: Double = 0.0,
        var velocidad: Double = 0.0,
        var rumbo: Double = 0.0,
        var precision: Double = 0.0,
    )

    data class clsCoordUlt(
        var id: Int = 0,
        var fecha: Long = 0L,
        var longit: Double = 0.0,
        var latit: Double = 0.0,
    )

    data class clsOrdenenccap (
        var idorden : Int = 0,
        var anulada : Int = 0,
        var activa : Int = 0,
        var cerrada : Int = 0,
        var firmausuario : String = "",
        var firmacliente : String = "",
        var latit : Double = 0.0,
        var longit : Double = 0.0,
        var fechaini : Long = 0L,
        var fechafin : Long = 0L,
        var nota : String = "",
        var recibido : Int = 0,
    )

    data class clsOrdencliente (
        var id : Int = 0,
        var nombre : String = "",
        var dir : String = "",
        var tel : String = "",
    )

    data class clsOrdencont (
        var id : Int = 0,
        var idcliente : Int = 0,
        var nombre : String = "",
        var dir : String = "",
        var tel : String = "",
    )

    data class clsOrdendet (
        var id : Int = 0,
        var idorden : Int = 0,
        var idproducto : Int = 0,
        var descripcion : String = "",
        var realizado : Int = 0,
        var cant : Double = 0.0,
        var activo : Int = 0,
    )

    data class clsOrdendir (
        var id : Int = 0,
        var idcliente : Int = 0,
        var referencia : String = "",
        var dir : String = "",
        var zona : String = "",
        var tel : String = "",
    )

    data class clsOrdenenc (
        var idorden : Int = 0,
        var numero : String = "",
        var fecha : Long = 0L,
        var idusuario : Int = 0,
        var idestado : Int = 0,
        var idtipo : Int = 0,
        var idclicontact : Int = 0,
        var iddir : Int = 0,
        var idcliente : Int = 0,
        var descripcion : String = "",
        var fecha_cierre : Long = 0L,
        var hora_ini : Long = 0L,
        var hora_fin : Long = 0L,
    )

    data class clsOrdenfoto (
        var id : Int = 0,
        var idorden : Int = 0,
        var nombre : String = "",
        var nota : String = "",
        var statcom : Int = 0,
    )

    data class clsOrdenlist (
        var idorden : Int = 0,
        var tarea : String = "",
        var cliente : String = "",
        var fecha : String = "",
        var estado : String = "",
        var idestado : Int = 0,
    )



    data class clsSyntaxlog (
        var id : Int = 0,
        var fecha : Long = 0L,
        var cmd : String = "",
    )

    data class clsTimeItem(
        var hora: Int= 0,
        var min: Int = 0,
        var longit: Double = 0.0,
        var latit: Double = 0.0,
        var nombre: String = ""
    )

    data class clsTipoorden (
        var id : Int = 0,
        var nombre : String = "",
    )

    data class clsUpdcmd (
        var id : Int = 0,
        var cmd : String = "",
    )

    data class clsUpdsave (
        var id : Int = 0,
        var cmd : String = "",
    )



    data class clsUsuarioMarker (
        var id : Int = 0,
        var nombre : String = "",
        var longit: Double = 0.0,
        var latit: Double = 0.0,
        var chue : Float =0.0F
    )

    data class clsUpdate (
        var SQL : String = ""
    )

    //region AppBase

    data class clsMenu (
        var id:Int = 0,
        var nombre: String? = null
    )

    data class exListDlgItem(
        var idresource: Int,
        var codigo: String ,
        var text: String ,
        var text2: String
    )

    //endregion

}