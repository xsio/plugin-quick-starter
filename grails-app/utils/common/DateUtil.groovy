package common

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by zdg on 2019/7/31 下午5:23.
 */
class DateUtil {

    static formatStrings = [
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS"
    ]

    static Date parse(String dateString) {
        for (formatString in formatStrings) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString)
            } catch (ParseException e) {
            }
        }
    }

    static def parseToUTC(String timeString) { //传进去UTC时间  -->解析出对应到本地时间
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss")
            df.setTimeZone(new SimpleTimeZone(0, "UTC"))
            return df.parse(timeString)
        } catch (Exception e) {

        }
    }

    static def getCurrentDateStr(String timeZone = "UTC") {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df.setTimeZone(new SimpleTimeZone(0, timeZone))
        return df.format(new Date())
    }

    static String format(Date date) {
        return date.format("yyyy-MM-dd")
    }

    static def DateToMonthString() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM")
        Calendar before1 = Calendar.getInstance();
        before1.add(Calendar.DAY_OF_MONTH, -1)
        return fmt.format(before1.getTime()).toString()
    }

    static def getYesterday() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Calendar before1 = Calendar.getInstance();
        before1.add(Calendar.DAY_OF_MONTH, -1)
        return fmt.format(before1.getTime()).toString()
    }
    static def getToday() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Calendar before1 = Calendar.getInstance();
//        before1.add(Calendar.DAY_OF_MONTH, -1)
        return fmt.format(before1.getTime()).toString()
    }

    static def getFourDaysAgo() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        Calendar before1 = Calendar.getInstance();
        before1.add(Calendar.DAY_OF_MONTH, -4)
        return fmt.format(before1.getTime()).toString()
    }

    static def timeStampAddRandom() {
        return new Date().getTime() + String.valueOf(new Random().nextInt(100))
    }


}
