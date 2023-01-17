package id.depok.posyandu.utils.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> AppCompatActivity.launchActivityForResult(
    options: Bundle? = null,
    requestCode: Int,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

fun AppCompatActivity.createResultLauncher(
    resultOk: () -> Unit,
    resultFailed: () -> Unit
): ActivityResultLauncher<Intent> {
    val resultExternalPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                resultOk()
            } else {
                resultFailed()
            }
        }
    return resultExternalPermission
}

fun Fragment.createResultLauncher(
    resultOk: () -> Unit,
    resultFailed: () -> Unit
): ActivityResultLauncher<Intent> {
    val resultExternalPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                resultOk()
            } else {
                resultFailed()
            }
        }
    return resultExternalPermission
}


inline fun AppCompatActivity.grantingExternalStoragePermission(
    resultExternalPermission: ActivityResultLauncher<Intent>,
    permissionCode: Int,
    actionGranted: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            actionGranted()
        } else {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", packageName))
                resultExternalPermission.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                resultExternalPermission.launch(intent)
            }
        }
    } else {
        val result: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val result1: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            actionGranted()
        } else {
            val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, permissionCode)
            } else {
                actionGranted()
            }
        }
    }
}


inline fun AppCompatActivity.checkStoragePermission(
    actionGranted: () -> Unit,
    actionDenied: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            actionGranted()
        } else {
            actionDenied()
        }
    } else {
        val result: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val result1: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            actionGranted()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                actionDenied()
            } else {
                actionGranted()
            }
        }
    }
}


inline fun AppCompatActivity.grantingCameraPermission(
    resultExternalPermission: ActivityResultLauncher<Intent>,
    requestCode: Int,
    actionGranted: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            actionGranted()
        } else {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", packageName))
                resultExternalPermission.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                resultExternalPermission.launch(intent)
            }
        }
    } else {
        val result: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val result1: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            actionGranted()
        } else {
            val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, requestCode)
            } else {
                actionGranted()
            }
        }
    }
}

inline fun Fragment.grantingPermission(
    launcher: ActivityResultLauncher<Intent>,
    permissionCode: Int,
    actionGranted: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            actionGranted()
        } else {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data =
                    Uri.parse(String.format("package:%s", this.requireActivity().packageName))
                launcher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                launcher.launch(intent)
            }
        }
    } else {
        val result: Int =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        val result1: Int =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED) {
            actionGranted()
        } else {
            val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().requestPermissions(
                    permission,
                    permissionCode
                )
            } else {
                actionGranted()
            }

        }
    }
}


inline fun <reified T : Any> AppCompatActivity.launchActivityForResult(
    launcher: ActivityResultLauncher<Intent>,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    launcher.launch(intent)
}

inline fun <reified T : Any> Fragment.launchActivityForResult(
    launcher: ActivityResultLauncher<Intent>,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(requireContext())
    intent.init()
    launcher.launch(intent)
}


inline fun <reified T : Any> Fragment.launchActivityForResult(
    options: Bundle? = null,
    requestCode: Int,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(requireContext())
    intent.init()
    startActivityForResult(intent, requestCode, options)
}


inline fun <reified T : Any> Context.launchActivityClearPrevious(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent, options)
}


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/*
fun Context.snackBar(view: View, message: String) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    val sbView = snackBar.view
    val tv = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    tv.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
    tv.textAlignment = TEXT_ALIGNMENT_CENTER
    sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
    snackBar.show()
}
*/

