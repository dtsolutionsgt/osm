package com.dts.restapi

import kotlinx.serialization.Serializable

class ClassesAPI {

    @Serializable
    data class clsAPICliente (
        var type : String = "",
        var Codigo_Cliente : Int = 0,
        var Empresa : Int = 0,
        var Bloqueado : Boolean = true,
        var Telefono : String = "",
        var Direccion : String = "",
    )

    @Serializable
    data class clsAPIClienteContacto(
        var type : String = "",
        var CODIGO_CLIENTE_CONTACTO : Int = 0,
        var CODIGO_CLIENTE : String = "",
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
    data class clsAPIEstado(
        var type : String = "",
        var CODIGO_TICKET_ESTADO : Int = 0,
        var Nombre : String = "",
    )

    @Serializable
    data class clsAPIProdprecio (
        var type : String = "",
        var CODIGO_PRECIO : Int = 0,
        var CODIGO_PRODUCTO : Int = 0,
        var NIVEL : Int = 0,
        var PRECIO : Int = 0,
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
    data class clsAPITipoServicio(
        var type : String = "",
        var CODIGO_TIPO_SERVICIO_DEP : Int = 0,
        var CODIGO_TICKET_DEPARTAMENTO : Int = 0,
        var Nombre : String = "",
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