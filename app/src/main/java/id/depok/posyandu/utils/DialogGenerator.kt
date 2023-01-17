package id.depok.posyandu.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.google.firebase.crashlytics.FirebaseCrashlytics
import id.depok.posyandu.BuildConfig
import id.depok.posyandu.R
import id.depok.posyandu.utils.extension.makeGone
import kotlinx.android.synthetic.main.dialog_confirmation.view.*
import kotlinx.android.synthetic.main.dialog_confirmation.view.btnOk
import kotlinx.android.synthetic.main.dialog_confirmation.view.lblMessage
import kotlinx.android.synthetic.main.dialog_confirmation.view.lblTitle
import kotlinx.android.synthetic.main.dialog_preview_image.view.*
import kotlinx.android.synthetic.main.dialog_yes_no_confirmation.view.*
import kotlinx.android.synthetic.main.layout_posyandu_data.*
import timber.log.Timber

object DialogGenerator {


    fun showErrorDialog(
        context: Context,
        errorText: String,
        cancelAble: Boolean,
        textButton: String = "OK",
        listener: () -> Unit = {},
    ) {
        generateConfirmationDialog(
            context,
            "Terjadi Kesalahan",
            errorText,
            textButton,
            cancelAble
        ) {
            it.dismiss()
            listener()
        }
    }

    fun generatePreviewImageDialog(
        context: Context,
        urlImg : String
    ) {
        try {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_preview_image, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

            dialog.setContentView(inflate)

            Glide.with(context).load(BuildConfig.BASE_IMG_URL + urlImg).into(inflate.imgView)

            inflate.btnCloseDialog.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Timber.e(e)
        }
    }

    fun generatePreviewImageDialog(
        context: Context,
        uriImg : Uri
    ) {
        try {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_preview_image, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

            dialog.setContentView(inflate)

            Glide.with(context).load(uriImg).into(inflate.imgView)

            inflate.btnCloseDialog.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.show()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Timber.e(e)
        }
    }

    fun generateConfirmationDialog(
        context: Context, title: String, message: String, textButton: String = "OK",
        cancelAble: Boolean = false,
        onDialogClicked: (Dialog) -> Unit,
    ) {
        try {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_confirmation, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

            dialog.setContentView(inflate)
            inflate.btnOk.text = textButton

            if (title.isEmpty()) {
                inflate.lblTitle.makeGone()
            } else {
                inflate.lblTitle.text = title
            }

            inflate.lblMessage.text = message

            inflate.btnOk.setOnClickListener {
                onDialogClicked(dialog)
                dialog.dismiss()
            }

            dialog.setCancelable(cancelAble)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            dialog.show()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Timber.e(e)
        }
    }

    fun generateYesNoConfirmationDialog(
        context: Context, title: String, message: String, textYesButton: String = "OK", textNoButton: String = "Cancel",
        cancelAble: Boolean = false,
        onDialogClicked: (Boolean) -> Unit,
    ) {
        try {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_yes_no_confirmation, null)
            dialog.window?.attributes?.windowAnimations = R.style.dialog_animation

            dialog.setContentView(inflate)
            inflate.btnYes.text = textYesButton
            inflate.btnCancel.text = textNoButton

            if (title.isEmpty()) {
                inflate.lblTitle.makeGone()
            } else {
                inflate.lblTitle.text = title
            }

            inflate.lblMessage.text = message

            inflate.btnYes.setOnClickListener {
                onDialogClicked(true)
                dialog.dismiss()
            }

            inflate.btnCancel.setOnClickListener {
                onDialogClicked(false)
                dialog.dismiss()
            }

            dialog.setCancelable(cancelAble)
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            dialog.show()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            Timber.e(e)
        }
    }
}