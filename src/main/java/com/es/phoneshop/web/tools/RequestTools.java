package com.es.phoneshop.web.tools;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class RequestTools {
    public static Integer parseIntegerUsingLocale(HttpServletRequest request, String number) throws ParseException {
        NumberFormat decimalFormat = DecimalFormat.getInstance(request.getLocale());
        int value = decimalFormat.parse(number).intValue();
        if (value > 0) {
            return value;
        }
        else {
            throw new ParseException("value below 0", 0);
        }
    }
}
