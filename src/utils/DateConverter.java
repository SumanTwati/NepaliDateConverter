package utils;


import exceptions.DateRangeNotSupported;
import exceptions.EmptyDateException;
import exceptions.InvalidBsDayOfMonthException;
import exceptions.InvalidDateFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DateConverter {
    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);
    private final char separator;
    private static final String DEFAULT_FORMAT = Constants.DEFAULT_DATE_FORMAT;
    private final String format;

    private static final int[] adBase = {1944, 1, 1, 7};
    private static final int[] bsBase = {2000, 1, 1, 4};
    private static final int[] adToBs = {2000, 9, 16, 6};
    private static final int[] bsToAd = {1943, 4, 13, 3};

    private static final int[][] npDates =
            {{2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3},
                    {3, 3, 3, 4, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3},
                    {3, 3, 3, 4, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3}, {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 3, 4, 2, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3}, {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3}, {2, 4, 3, 4, 3, 3, 1, 2, 2, 1, 1, 3},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3}, {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 2, 2},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3}, {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 2, 2},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3}, {3, 3, 3, 4, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3}, {3, 3, 3, 4, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 3, 4, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {2, 4, 3, 4, 3, 3, 1, 2, 1, 2, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 1, 3}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 3, 4, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {3, 3, 3, 4, 3, 3, 1, 2, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 1, 2, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 3},
                    {3, 3, 3, 4, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 2}, {3, 4, 3, 4, 3, 2, 2, 2, 1, 2, 1, 3},
                    {3, 3, 3, 4, 3, 3, 2, 1, 2, 1, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 1, 2, 1, 2, 2},
                    {3, 4, 3, 4, 3, 2, 2, 2, 1, 1, 2, 2}, {3, 3, 4, 4, 3, 2, 2, 2, 1, 2, 2, 2},
                    {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 2, 2}, {3, 3, 4, 3, 3, 2, 2, 2, 1, 2, 2, 2},
                    {3, 3, 4, 3, 3, 2, 2, 2, 1, 2, 2, 2}, {3, 4, 3, 4, 2, 3, 2, 2, 1, 2, 2, 2},
                    {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 2, 2}, {3, 3, 4, 3, 3, 3, 2, 2, 1, 2, 2, 2},
                    {2, 3, 4, 4, 2, 3, 2, 2, 1, 2, 2, 2}, {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 2, 2},
                    {2, 4, 3, 4, 3, 2, 2, 2, 1, 2, 2, 2}};

    public DateConverter() {
        this(DEFAULT_FORMAT, Constants.DEFAULT_DATE_SEPARATOR);
    }

    public DateConverter(String format, char separator) {
        this.format = format;
        this.separator = separator;
    }

    public String toBS(LocalDateTime adDate) {
        return toBS(adDate.getYear(), adDate.getMonthValue(), adDate.getDayOfMonth());
    }

    private String toBS(int y, int m, int d) {
        if (m <= 0) {
            m = 1;
        } else if (m >= 12) {
            m = 12;
        }

        if (validateADDate(y)) {

            int enDays = d, year, month, day;
            for (year = adBase[0], month = adBase[1]; ; month++) {
                if (y == year && m == month)
                    break;
                if (month > 12) {
                    month = 1;
                    year += 1;
                    if (y == year && m == month)
                        break;
                }

                enDays = num(String.valueOf(enDays)) + enMonDays(year, month);
            }

            for (year = adToBs[0], month = adToBs[1], day = adToBs[2]; ; enDays--, day++) {
                if (day > npMonDays(year, month)) {
                    day = 1;
                    month = num(String.valueOf(month)) + 1;
                }
                if (month > 12) {
                    month = 1;
                    year = num(String.valueOf(year)) + 1;
                }
                if (enDays == 0)
                    break;
            }

            return String.valueOf(year) + this.separator + pad(month) + this.separator + pad(day);
        } else {
            throw new InvalidDateFormatException("Invalid date.");
        }
    }

    private boolean validateBsDate(int bsYear, int bsMonth, int bsDayOfMonth) {
        if (bsYear < bsBase[0]) {
            throw new DateRangeNotSupported("Bikram Sambat is earlier than supported date");
        } else if (bsYear > 2090) {
            throw new DateRangeNotSupported("Bikram Sambat is later than supported date");
        } else {
            logger.debug("Debug: converter supports  year {} ", bsYear);
            if (bsMonth >= 1 && bsMonth <= 12) {
                logger.debug("debug: month between 1 and 12");
                int dayOfMonth = npMonDays(bsYear, bsMonth);
                logger.debug("Debug:total days in month {} ", dayOfMonth);
                if (bsDayOfMonth <= dayOfMonth) {
                    return true;
                } else {
                    String message = String.format(
                            "Invalid day of month  %d for year  %d  and month  %d. It should be %d ",
                            bsDayOfMonth, bsYear, bsMonth, dayOfMonth);
                    logger.warn(message);
                    throw new InvalidBsDayOfMonthException(message);
                }
            }
        }
        return false;
    }

    private boolean validateADDate(int adYear) {
        if (adYear < adBase[0]) {
            throw new DateRangeNotSupported("AD is earlier than supported date");
        } else if (adYear > 2090) {
            throw new DateRangeNotSupported("AD is later than supported date");
        } else {
            return true;
        }
    }

    private boolean matchFormat(String bsDate) {
        if (format.equals(DEFAULT_FORMAT)) {
            logger.debug("Date format want to test is {} real text is {}", format, bsDate);
            Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            return p.matcher(bsDate).matches();
        } else {
            logger.debug("Date format is {}", format);
            return Pattern.matches("\\d{2}-\\d{2}-\\d{4}", bsDate);
        }
    }

    private void isDate(String date) {
        if (date.isBlank()) {
            throw new EmptyDateException("Empty date.");
        } else if (!matchFormat(date)) {
            throw new InvalidDateFormatException(
                    "Incorrect date format  " + format + " date provided was " + date);
        } else {
            // with a space before, zero or one time
            String time =
                    "(\\s(([01]?\\d)|(2[0123]))[:](([012345]\\d)|(60))" + "[:](([012345]\\d)|(60)))?";

            // no check for leap years
            // and 31.02.2006 will also be correct
            String day = "(([12]\\d)|(3[012])|(0?[1-9]))"; // 01 up to 31
            String month = "((1[012])|(0\\d))"; // 01 up to 12
            String year = "\\d{4}";

            // define here all date format
            ArrayList<String> patterns = new ArrayList<>();
            patterns.add(day + "[-.]" + month + "[-.]" + year + time);
            patterns.add(year + "-" + month + "-" + day + time);
            patterns.add(year + "/" + month + "/" + day + time);
            // here you can add more date formats if you want

            // check dates
            for (String objPa : patterns) {
                Pattern p = Pattern.compile(objPa);
                if (p.matcher(date).matches())
                    return;
            }
            throw new InvalidDateFormatException("Invalid BS date format.");
        }
    }

    public LocalDateTime toAD(String bsDate) {
        int bsYear;
        int bsMonth;
        int bsDayOfMonth;

        isDate(bsDate);

        if (separator == Character.MIN_VALUE) {
            bsDayOfMonth = Integer.parseInt(bsDate.substring(2, 4));
            bsMonth = Integer.parseInt(bsDate.substring(0, 2));
            bsYear = Integer.parseInt(bsDate.substring(4));
        } else {
            String[] bsDates = bsDate.split(String.valueOf(separator));
            bsYear = Integer.parseInt(bsDates[0]);
            bsMonth = Integer.parseInt(bsDates[1]);
            bsDayOfMonth = Integer.parseInt(bsDates[2]);
        }

        if (validateBsDate(bsYear, bsMonth, bsDayOfMonth)) {
            return toAD(bsYear, bsMonth, bsDayOfMonth);
        } else {
            throw new IllegalStateException("Invalid BS date");
        }
    }

    private LocalDateTime toAD(int y, int m, int d) {
        int npDays = d, year, month, day;

        for (year = bsBase[0], month = bsBase[1]; ; month++) {
            if (y == year && m == month)
                break;
            if (month > 12) {
                month = 1;
                year = num(String.valueOf(year)) + 1;
                if (y == year && m == month)
                    break;
            }
            npDays = num(String.valueOf(npDays)) + npMonDays(year, month);
        }

        for (year = bsToAd[0], month = bsToAd[1], day = bsToAd[2]; ; npDays--, day++) {
            if (day > enMonDays(year, month)) {
                day = 1;
                month = num(String.valueOf(month)) + 1;
            }
            if (month > 12) {
                month = 1;
                year = num(String.valueOf(year)) + 1;
            }
            if (npDays == 0)
                break;
        }
        return LocalDateTime.of(year, month, day, 0, 0);
    }

    private int num(String n) {
        return Integer.parseInt(n, 10);
    }

    public String pad(int n) {
        if (n < 10) {
            return "0" + n;
        }

        return n + "";
    }

    private int enMonDays(int y, int m) {
        int[] max = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (m < 1 || m > 12)
            return 0;
        if (m == 2 && isLeap(y))
            return 29;

        return max[m - 1];
    }

    public int npMonDays(int y, int m) {
        return 28 + npDates[y - 2000][m - 1];
    }

    private boolean isLeap(int y) {
        return (y % 100 != 0 || y % 400 == 0) && y % 4 == 0;
    }
}
