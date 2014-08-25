package org.imeds.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImedDateFormat {

	private static String IMES_DATE_FORMAT = "yyyy-MM-dd";
	
	//private static SimpleDateFormat sdf;
	
	public ImedDateFormat() {
		//sdf = new SimpleDateFormat(PMS_DATE_FORMAT);
	}
	
    /**
     * A convenient API to format date into string.
     */
    public String format(Date d) {
    	if (d==null) return null;
    	SimpleDateFormat sdf = new SimpleDateFormat(IMES_DATE_FORMAT);
        return sdf.format(d);
    }

    /**
     * A convenient API to parse date string.
     */
    public Date parse(String source) throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat(IMES_DATE_FORMAT);
        return sdf.parse(source);
    }
}

