package id.depok.posyandu.utils.extension

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.*


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun EditText.bindTo(string: MutableLiveData<String>) {
    string.value = this.text.toString()
    this.addTextChangedListener {
        string.value = it.toString()
    }
}


fun Spinner.getData() : List<String>{
    val list : MutableList<String> = mutableListOf()
    repeat(this.adapter.count){
        list.add(adapter.getItem(it).toString())
    }
    return list
}

fun Spinner.setPositionValue(data : String) {
  this.setSelection(this.getData().indexOf(data))
}





fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun dpToPx(dp: Int, context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)
internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

fun CheckBox.makeChecked(){
    isChecked = true
}

fun CheckBox.makeUnChecked(){
    isChecked = false
}


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeVisibleWithAnimation(){
    visibility = View.VISIBLE
    this.startFadedAnimation(1)
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeDisable() {
    alpha = 0.3f
    isEnabled = false

}

fun View.makeEnable() {
    alpha = 1f
    isEnabled = true
}

fun Array<View>.makeAllGone() {
    forEach {
        it.makeGone()
    }
}


fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F,
    topRight: Float = 0F,
    bottomRight: Float = 0F,
    bottomLeft: Float = 0F,
) {
    cornerRadii = arrayOf(topLeft,
        topLeft,
        topRight,
        topRight,
        bottomRight,
        bottomRight,
        bottomLeft,
        bottomLeft).toFloatArray()
}


fun View.startFadedAnimation(repeat : Int, duration : Long = 1000){
    val anim = AlphaAnimation(1.0f, 0.0f)
    anim.duration = duration
    anim.repeatCount = repeat
    anim.repeatMode = Animation.REVERSE
    this.startAnimation(anim)
}

fun validateSpinnerAddress(
    frameLayout: TextInputLayout,
    frameEditText: TextInputEditText,
    province: Spinner,
    city: Spinner,
    district: Spinner,
    subDistrict: Spinner,
    postalCode: Spinner,
): Boolean {
    val address = frameEditText.text.toString()
    if (address.startsWith(" ")) {
        frameLayout.error = "Hindari penggunaan spasi awal"
        return false
    } else {
        frameLayout.isErrorEnabled = false
    }
    if (province.selectedItemPosition == 0)
        return false
    if (city.selectedItemPosition == 0)
        return false
    if (district.selectedItemPosition == 0)
        return false
    if (subDistrict.selectedItemPosition == 0)
        return false
    if (postalCode.selectedItemPosition == 0)
        return false
    return true
}

fun validateAddress(
    frameLayout: TextInputLayout,
    frameEditText: TextInputEditText,
    province: EditText,
    city: EditText,
    district: EditText,
    subDistrict: EditText,
    postalCode:EditText,
): Boolean {
    val address = frameEditText.text.toString()
    if (address.startsWith(" ")) {
        frameLayout.error = "Hindari penggunaan spasi awal"
        return false
    } else {
        frameLayout.isErrorEnabled = false
    }
    if (province.text.toString().isEmpty())
        return false
    if (city.text.toString().isEmpty())
        return false
    if (district.text.toString().isEmpty())
        return false
    if (subDistrict.text.toString().isEmpty())
        return false
    if (postalCode.text.toString().isEmpty())
        return false
    return true
}

fun validateDomicile(domicile: EditText): Boolean {
    if (domicile.text.isEmpty())
        return false
    return true
}