package id.depok.posyandu.utils.extension

import android.util.Patterns
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.regex.Matcher
import java.util.regex.Pattern


const val blockCharacters = "'/~*#^|$%&!\"/"

fun String.isEmailValid(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this)
        .matches() && !this.contains("@coway") /*&& checkEmailPattern(this)*/
}

fun String.isEmailValidLogin(): Boolean {
    return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


fun String.isKtpValid(birthdate: String): Boolean {
    if (length == 16) {
        val date = substring(6, 12)

        try { // returns Any
            val birthDay = birthdate.substring(0, 2).toInt()
            val birthMonth = birthdate.substring(2, 4).toInt()
            val birthYear = birthdate.substring(4, 6).toInt()

            val day = date.substring(0, 2).toInt()
            val month = date.substring(2, 4).toInt()
            val year = date.substring(4, 6).toInt()

            if (birthDay == day || birthDay + 40 == day) {
                if (birthMonth == month && birthYear == year) {
                    return true
                }
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }
    return false
}

fun String.cleanSlash(): String {
    return this.replace("\"", "").replace("\"", "")
}

fun String.cleanHtml(): String {
    return this.replace("<br>", "\n").replace("\\u003cdiv\\u003e", "")
        .replace("\\u003c/div\\u003e", "")
}

fun String.cleanForeign(): String {
    return this.replace("[^a-zA-Z0-9]".toRegex(), "")
}


fun String.containSpecialCharacter(): Boolean {
    blockCharacters.forEach { b ->
        this.forEach {
            if (it == b) {
                return true
            }
        }
    }
    return false
}


fun String?.maskRestrictString(): String {
    if (this != null) {
        return if (this.length > 8) {
            val s = StringBuilder()
            this.forEachIndexed { index, c ->
                if (index > 3 && index < this.length - 4) {
                    s.append('*')
                } else {
                    s.append(c)
                }
            }
            s.toString()
        } else {
            this
        }
    } else {
        return ""
    }
}

fun String?.maskPhoneString(): String {
    if (this != null) {
        return if (this.length > 8) {
            val s = StringBuilder()
            this.forEachIndexed { index, c ->
                if (index > 4 && index < this.length - 4) {
                    s.append('*')
                } else {
                    s.append(c)
                }
            }
            s.toString()
        } else {
            this
        }
    } else {
        return ""
    }
}


fun String?.maskCreditCardString(): String {
    if (this != null) {
        return if (this.length > 8) {
            val s = StringBuilder()
            this.forEachIndexed { index, c ->
                if (index < this.length - 4) {
                    s.append('*')
                } else {
                    s.append(c)
                }
            }
            s.toString()
        } else {
            this
        }
    } else {
        return ""
    }
}


fun String?.maskRestrictPassportString(): String {
    if (this != null) {
        return if (this.length > 4) {
            val s = StringBuilder()
            this.forEachIndexed { index, c ->
                if (index > 1 && index < this.length - 2) {
                    s.append('*')
                } else {
                    s.append(c)
                }
            }
            s.toString()
        } else {
            this
        }
    } else {
        return ""
    }
}

fun String?.maskEmail(): String {
    return this?.replace(Regex("(^[^@]{3}|(?!^)\\G)[^@]"), "$1*") ?: ""
}

fun String.cleanPhone(): String {
    return this.replace("(+62)", "0")
        .replace("(62)", "0")
        .trim()
}

fun String.isValidPhone(): Boolean {
    return Pattern.matches(Patterns.PHONE.pattern(), this) && this.length <= 13
}

fun String.isKtpValid(): Boolean {
    if (length == 16) {
        val date = substring(6, 12)

        try { // returns Any
            val day = date.substring(0, 2).toInt()
            val month = date.substring(2, 4).toInt()
            if (day < 1 || day > 31) {
                if (day > 71 || day < 41) {
                    return false
                }
            }

            if (month < 1 || month > 12) {
                return false
            }
        } catch (e: NumberFormatException) {
            return false
        }

    }

    return true
}

fun String.isNumber(): Boolean {
    if (length == 0) {
        return false
    }
    this.forEach { c ->
        if (c < '0' || c > '9') {
            return false
        }
    }
    return true
}

fun String.isDecimalNumber(): Boolean {
    try {
        this.toDouble()
    } catch (e: Exception) {
        return false
    }
    return true
}



fun String.clearErrorMessage(): String {
    var result = this
    result = result.replace("_server_messages", "")
    result = result.replace("message", "")
    result = result.replace("\"{", "")
    result = result.replace("}\"", "")
    result = result.replace("\\", "")
    result = result.replace("\\\"", "")
    result = result.replace("<b>", "")
    result = result.replace("</b>", "")
    result = result.replace("_error_", "")
    result = result.replace("{", "")
    result = result.replace("}", "")
    result = result.replace(":", "")
    result = result.replace("[", "")
    result = result.replace("]", "")
    result = result.replace("\"", "")
    return result
}

/*fun getMessageError(serverMessage: String): String {
    var result = serverMessage
    result = result.replace("\"{", "{")
    result = result.replace("}\"", "}")
    result = result.replace("\\", "")

    val errorMessage: List<ErrorMessages> =
        Gson().fromJson(result, object : TypeToken<List<ErrorMessages>>() {}.type)
    return errorMessage[0].errorMessage
}*/

fun String.getIdFromYoutube(): String? {
    val pattern =
        "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|watch\\?v%3D|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*"

    val compiledPattern = Pattern.compile(pattern)
    val matcher: Matcher = compiledPattern.matcher(this)

    return if (matcher.find()) {
        matcher.group()
    } else {
        null
    }
}

fun String.eliminateSlash(): String {
    return this.replace("\\", "")
}


fun checkEmailPattern(email: String): Boolean {
    val pattern = "/^\\w+([\\.-]?\\w+)@\\w+([\\.-]?\\w+)(\\.\\w{2,3})+\$/"

    val compiledPattern = Pattern.compile(pattern)

    return compiledPattern.matcher(email).matches()
}

fun String.toRequestBody(data : MediaType?) : RequestBody {
    return RequestBody.create(MediaType.parse(data.toString()), this)
}

fun String?.toMediaTypeOrNull() : MediaType? {
    return if(this.isNullOrEmpty()){
        null
    } else {
        MediaType.parse("multipart/form-data")
    }
}