package id.depok.posyandu.utils.helper

import android.app.Activity
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import id.depok.posyandu.utils.DialogGenerator
import id.depok.posyandu.utils.extension.clearErrorMessage
import timber.log.Timber
import java.util.HashSet

object AuthHelper {

    fun specifyErrorMessage(context: Activity, e: Exception?) {
        e?.let {
            context.runOnUiThread {
                DialogGenerator.showErrorDialog(
                    context = context,
                    errorText = it.message.toString().clearErrorMessage(),
                    cancelAble = true,
                    textButton = "OK"
                ) {

                }
                FirebaseCrashlytics.getInstance().recordException(e)
                Timber.e(it.message.toString())
            }
        }
    }

    /*fun specifyErrorMessage(context: Context, e: Exception?) {
        e?.let {
            when {
                authProblem(it.message.toString()) -> {
                    DialogGenerator.showErrorDialog(
                        context = context,
                        errorText = if (it.message.toString().clearErrorMessage()
                                .contains("session_expired")
                        ) {
                            "Sesi akun anda telah habis klik Log Out untuk login kembali"
                        } else {
                            it.message.toString().clearErrorMessage()
                        },
                        cancelAble = false,
                        textButton = "Log Out"
                    ) {

                    }
                }
                conflictProblem(it.message.toString()) -> {
                    DialogGenerator.showErrorDialog(
                        context = context,
                        errorText = "Photo yang Sama telah diupload",
                        cancelAble = true,
                        textButton = "OK"
                    ) {
                    }
                }
                specialProblem(it.message.toString()) -> {
                    DialogGenerator.showErrorDialog(
                        context = context,
                        errorText = it.message.toString().clearErrorMessage(),
                        cancelAble = true,
                        textButton = "OK"
                    ) {
                    }
                }
                else -> {
                    DialogGenerator.showErrorDialog(
                        context = context,
                        errorText = it.message.toString().clearErrorMessage(),
                        cancelAble = true,
                        textButton = "OK"
                    ) {

                    }
                }
            }
            FirebaseCrashlytics.getInstance().recordException(e)
            Timber.e(it.message.toString())
        }
    }*/


}