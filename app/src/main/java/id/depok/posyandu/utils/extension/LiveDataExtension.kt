package id.depok.posyandu.utils.extension

import androidx.lifecycle.MutableLiveData

fun <T : Any?> MutableLiveData<T>.defaultValue(initialValue: T) = apply { setValue(initialValue) }
