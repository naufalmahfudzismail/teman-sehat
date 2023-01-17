package id.depok.posyandu.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    val serverDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
    val serverDateFormatDefault = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val serverLongdateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.ENGLISH)
    val serverLongdateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val viewDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
    val viewSimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("id"))
    val viewDateFormatWithTime = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale("id"))
    val monthFormatter = SimpleDateFormat("yyyy-MM", Locale.ENGLISH)
    val monthFormatterId = SimpleDateFormat("MMMM yyyy", Locale("id"))
    val monthDayFormatterId = SimpleDateFormat("dd MMMM ", Locale("id"))
    val currentDate = Calendar.getInstance()

    private val viewTimeFormat = SimpleDateFormat("HH:mm", Locale("id"))
    private val serverTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
    val formatSalesOrder = SimpleDateFormat("dd MMMM yyyy", Locale("ID"))



    fun viewDate(date: String): String {
        return viewDateFormat.format(date)
    }

    fun viewDate(date: Date): String {
        return viewDateFormat.format(date)
    }

    fun today(): String {
        return serverDateFormat.format(Date(System.currentTimeMillis()))
    }

    fun lastYear(): String {
        return serverDateFormat.format(Date(System.currentTimeMillis()))
    }

    fun todayWithAdditionDay(addition : Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + addition)
        return serverDateFormat.format(calendar.time)
    }

    fun dateWithAddition(date : String, addition : Int, additionType : Int): String {
        val calendar = Calendar.getInstance().apply {
            serverDateFormat.parse(date)?.let {
                this.time = it
            }
        }
        calendar.set(additionType, calendar.get(additionType) + addition)
        return serverDateFormat.format(calendar.time)
    }

    fun todayWithTime(): String {
        return serverLongdateFormat.format(Date(System.currentTimeMillis()))
    }

    fun todayWithTimePlus(milliSecond: Long): String {
        return serverLongdateFormat.format(Date(System.currentTimeMillis() + milliSecond))
    }

    fun todayWithTimeNoZone(): String {
        return serverLongdateTimeFormat.format(Date(System.currentTimeMillis()))
    }

    fun todaySalesOrderFormat(): String {
        return viewDateFormat.format(Date(System.currentTimeMillis()))
    }

    fun formatDateFromServer(date: String?): String =
        try {
            viewDateFormat.format(serverDateFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatSimpleDateFromServer(date: String?): String =
        try {
            viewSimpleDateFormat.format(serverDateFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerMonthYear(date: String?): String =
        try {
            monthFormatterId.format(serverDateFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateWithMonthYearFromCalendar(date: Date): String =
        try {
            monthFormatterId.format(date)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTime(date: String?): String =
        try {
            viewDateFormatWithTime.format(serverLongdateFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTimeToDate(date: String?): String =
        try {
            viewDateFormat.format(serverLongdateFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTimeToDateTime(date: String?): String =
        try {
            viewDateFormatWithTime.format(serverLongdateFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTimeToLong(date: String?): Long =
        try {
            serverLongdateFormat.parse(date!!)!!.time
        } catch (e: Exception) {
            0L
        } catch (e: NullPointerException) {
            0L
        }

    fun formatDateFromServerWithSimpleTime(date: String?): String =
        try {
            viewTimeFormat.format(serverTimeFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTimeStamp(date: String?): String =
        try {
            viewDateFormat.format(serverLongdateTimeFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTimeStampToDate(date: String?): String =
        try {
            viewDateFormatWithTime.format(serverLongdateTimeFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateFromServerWithTimeStampToMonthYear(date: String?): String =
        try {
            monthFormatterId.format(serverLongdateTimeFormat.parse(date!!)!!)
        } catch (e: Exception) {
            "-"
        } catch (e: NullPointerException) {
            "-"
        }

    fun formatDateToStringServer(date: Date): String {
        return serverDateFormat.format(date)
    }


    fun newSchemeDatePass(): Boolean {
        val dateStart = serverDateFormat.parse("2021-06-30")
        val dateEnd = serverDateFormat.parse("2022-01-01")
        val dateNow = serverDateFormat.parse(today())
        dateNow?.let {
            return dateNow.after(dateStart) && dateNow.before(dateEnd)
        }
        throw Exception("Date Unconscious")
    }


    fun newSchemeDatePass(desireDate: String): Boolean {
        val dateStart = serverDateFormat.parse("2021-06-30")
        val dateEnd = serverDateFormat.parse("2022-01-01")
        val dateNow = serverDateFormat.parse(desireDate)
        dateNow?.let {
            return dateNow.after(dateStart) && dateNow.before(dateEnd)
        }
        throw Exception("Date Unconscious")
    }

    fun schemeMayDatePass(desireDate: String): Boolean {
        val dateStart = serverDateFormat.parse("2022-04-01")
        val dateEnd = serverDateFormat.parse("2022-06-01")
        val dateNow = serverDateFormat.parse(desireDate)
        dateNow?.let {
            Timber.e(
                "Check Date Match : ${dateNow.after(dateStart) && dateNow.before(dateEnd)}"
            )
            return dateNow.after(dateStart) && dateNow.before(dateEnd)
        }
        throw Exception("Date Unconscious")
    }

    fun contractNewSchemeDatePass(contractDate: String): Boolean {
        val datePassSixMonths = Calendar.getInstance().apply {
            this.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 7)
            this.set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        val dateMaxRange = Calendar.getInstance().apply {
            this.set(Calendar.MONTH, currentDate.get(Calendar.MONTH) - 5)
            this.set(Calendar.DAY_OF_MONTH, 1)
        }

        val dateContract = serverDateFormat.parse(contractDate)

        dateContract?.let {
            Timber.e((dateContract.after(datePassSixMonths.time) and dateContract.before(dateMaxRange.time)).toString())
            return dateContract.after(datePassSixMonths.time) and dateContract.before(dateMaxRange.time)
        }
        throw Exception("Date Unconscious")
    }

    fun isPassSettleMonth(settleDate: String): Boolean {
        try {
            val monthFormat = "yyyy-MM"
            val todayCalendar = Calendar.getInstance()
            val monthFormatter = SimpleDateFormat(monthFormat, Locale.ENGLISH)
            val dateSettle = serverDateFormat.parse(settleDate)

            dateSettle?.let {
                val monthSettle = monthFormatter.format(dateSettle)
                val thisMonth = monthFormatter.format(todayCalendar.time)
                return !monthSettle.equals(thisMonth)
            }

            return false

        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        }
    }

    fun isPassSettleMonthCommission(settleDate: String): Boolean {
        try {
            val monthFormat = "yyyy-MM"
            val todayCalendar = serverDateFormat.parse("2022-07-01")
            val monthFormatter = SimpleDateFormat(monthFormat, Locale.ENGLISH)
            val dateSettle = serverDateFormat.parse(settleDate)

            dateSettle?.let {
                val monthSettle = monthFormatter.format(dateSettle.time)
                val thisMonth = monthFormatter.format(todayCalendar?.time)
                return monthSettle >= thisMonth
            }

            return false

        } catch (e: Exception) {
            Timber.e(e)
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        }
    }
}