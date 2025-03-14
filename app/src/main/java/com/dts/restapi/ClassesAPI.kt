package com.dts.restapi

import kotlinx.serialization.Serializable

class ClassesAPI {

    @Serializable
    data class clsAPIProdprecio (
        var type : String = "",
        var codigo_precio : Int = 0,
        var codigo_producto : Int = 0,
        var nivel : Int = 0,
        var precio : Int = 0,
        var unidadmedida : String = "",
    )

    @Serializable
    data class clsAPIProducto (
        var type : String = "",
        var CODIGO_PRODUCTO : Int = 0,
        var DESCLARGA : String = "",
        var CODIGO_TIPO : String = "",
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