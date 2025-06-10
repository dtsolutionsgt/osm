package com.dts.restapi

import kotlinx.serialization.Serializable

class ClassesAPI {

    @Serializable
    data class clsAPIClasificacion (
        var type : String = "",
        var CODIGO_CLASIFICACION : Int = 0,
        var DESCRIPCION : String = "",
        var ES_MATERIAL : Int = 0,
    )

    @Serializable
    data class clsAPICliente (
        var type : String = "",
        var Codigo_Cliente : Int = 0,
        var Nombre : String = "",
        var Telefono : String = "",
        var Direccion : String = "",
        var Nivel : Int = 0,
        var Nit : String = "",
    )

    @Serializable
    data class clsAPIClienteContacto(
        var type : String = "",
        var CODIGO_CLIENTE_CONTACTO : Int = 0,
        var CODIGO_CLIENTE : Int = 0,
        var NOMBRE : String = "",
        var TELEFONO : String = "",
        var CORREO : String = "",
        var DIRECCION : String = "",
        var Empresa : Int = 0,
    )

    @Serializable
    data class clsAPIClienteDir(
        var type : String = "",
        var CODIGO_DIRECCION : Int = 0,
        var DIRECCION : String = "",
        var TELEFONO : String = "",
        var REFERENCIA : String = "",
        var CODIGO_CLIENTE : Int = 0,
    )

    @Serializable
    data class clsAPICoord (
        var type : String = "",
        var COORX : Double = 0.0,
        var COORY : Double = 0.0
    )

    @Serializable
    data class clsAPIEstado(
        var type : String = "",
        var CODIGO_ESTADO : Int = 0,
        var NOMBRE : String = "",
    )

    @Serializable
    data class clsAPIOrdenDet (
        var type : String = "",
        var CODIGO_ORDEN_SERVICIO_DET : Int = 0,
        var CODIGO_ORDEN_SERVICIO : Int = 0,
        var CODIGO_PRODUCTO  : Int = 0,
        var DESCRIPCION : String = "",
        var REALIZADO  : Int = 0,
        var CANTIDAD : Double = 0.0,
        var ACTIVO  : Int = 0,
    )

    @Serializable
    data class clsAPIOrdenEnc (
        var type : String = "",
        var CODIGO_ORDEN_SERVICIO : Int = 0,
        var NUMERO : String = "",
        var FECHA : Long = 0L,
        var FECHA_CIERRE : Long = 0L,
        var HORA_SERVICIO_INI : Long = 0L,
        var HORA_SERVICIO_FIN : Long = 0L,
        var CODIGO_USUARIO_ASIGNADO : Int = 0,
        var CODIGO_ESTADO_ORDEN_SERVICIO : Int = 0,
        var CODIGO_TIPO_ORDEN_SERVICIO : Int = 0,
        var CODIGO_CLIENTE_CONTACTO : Int = 0,
        var CODIGO_DIRECCION : Int = 0,
        var CODIGO_CLIENTE : Int = 0,
        var DESCRIPCION : String = "",
    )

    @Serializable
    data class clsAPIOrdenEncSup (
        var type : String = "",
        var CODIGO_ORDEN_SERVICIO : Int = 0,
        var NUMERO : String = "",
        var NOMBRE : String = "",
        var USUARIO : String = "",
        var CODIGO_SUCURSAL : Int = 0,
        var NTIPO : String = "",
        var NESTADO : String = "",
        var ESTADO : Int = 0,
        var FECHAAGR : Long = 0L,
        var FECHASERV : Long = 0L,
        var PRIORIDAD : Int = 0,
    )

    @Serializable
    data class clsAPIProdprecio (
        var type : String = "",
        var CODIGO_PRECIO : Int = 0,
        var CODIGO_PRODUCTO : Int = 0,
        var NIVEL : Int = 0,
        var PRECIO : Double = 0.0,
        var UNIDADMEDIDA : String = "",
    )

    @Serializable
    data class clsAPIProducto (
        var type : String = "",
        var CODIGO_PRODUCTO : Int = 0,
        var DESCLARGA : String = "",
        var CODIGO_TIPO : String = "",
    )

    @Serializable
    data class clsAPIRazonFalla (
        var type : String = "",
        var CODIGO_RAZON_FALLA : Int = 0,
        var CODIGO_TIPO_ORDEN_SERVICIO : Int = 0,
        var CODIGO_CLASIFICACION : Int = 0,
        var DESCRIPCION : String = "",
    )

    @Serializable
    data class clsAPIRazonNoAtencion (
        var type : String = "",
        var CODIGO_RAZON_NOATENCION : Int = 0,
        var DESCRIPCION : String = "",
    )

    @Serializable
    data class clsAPISucursal (
        var type : String = "",
        var CODIGO_SUCURSAL : Int = 0,
        var DESCRIPCION : String = "",
        var DIRECCION : String = "",
        var FEL_FECHA_VENCE_CONTRATO : String = "",
    )

    @Serializable
    data class clsAPITipoServicio(
        var type : String = "",
        var CODIGO_TIPO : Int = 0,
        var NOMBRE : String = "",
    )

    @Serializable
    data class clsAPIUsuarioApp (
        var type : String = "",
        var Codigo_Usuario : Int = 0,
        var Id_app : String = "",
        var Nombre  : String = "",
        var Activo  : Boolean = true,
        var Rol  : String = "",
        var Pin : Int = 0
    )

}