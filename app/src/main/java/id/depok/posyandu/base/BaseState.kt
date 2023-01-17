package id.depok.posyandu.base

import java.lang.Exception

class BaseState<T>(
    var loading: Boolean = false,
    var error : Exception? = null,
    var data: T? = null,
    var count : Int? = null
)