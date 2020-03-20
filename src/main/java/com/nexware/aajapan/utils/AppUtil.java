/**
 *
 */
package com.nexware.aajapan.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.nexware.aajapan.core.Constants;

/**
 * The Class AppUtil.
 *
 * @author Karthik Selvaraj
 */
public class AppUtil {
	protected static final List<String> DATE_FORMATS = Arrays.asList("yyyy/MM", "yyyy");

	/**
	 * Checks if is collection empty.
	 *
	 * @param collection the collection
	 * @return true, if is collection empty
	 */
	private static boolean isCollectionEmpty(Collection<?> collection) {
		return (collection == null) || collection.isEmpty();
	}

	/**
	 * Checks if is object empty.
	 *
	 * @param object the object
	 * @return true, if is object empty
	 */
	public static boolean isObjectEmpty(Object object) {
		if (object == null) {
			return true;
		} else if (object instanceof String) {
			if (((String) object).trim().length() == 0) {
				return true;
			}
		} else if (object instanceof Collection) {
			return isCollectionEmpty((Collection<?>) object);
		}
		return false;
	}

	/**
	 * Gets required value if null.
	 *
	 * @param value
	 * @param return
	 *
	 * @return value based on condition
	 */
	public static <T> T ifNull(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	/**
	 * Gets required value if null or empty.
	 *
	 * @param value
	 * @param return
	 *
	 * @return value based on condition
	 */
	public static <T> T ifNullOrEmpty(T value, T defaultValue) {
		return isObjectEmpty(value) ? defaultValue : value;
	}

	/**
	 * Gets the bean to json string.
	 *
	 * @param beanClasses the bean classes
	 * @return the bean to json string
	 */
	public static String getBeanToJsonString(Object... beanClasses) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final Object beanClass : beanClasses) {
			stringBuilder.append(getBeanToJsonString(beanClass));
			stringBuilder.append(", ");
		}
		return stringBuilder.toString();
	}

	/**
	 * Concatenate.
	 *
	 * @param listOfItems the list of items
	 * @param separator   the separator
	 * @return the string
	 */
	public String concatenate(List<String> listOfItems, String separator) {
		final StringBuilder sb = new StringBuilder();
		final Iterator<String> stit = listOfItems.iterator();

		while (stit.hasNext()) {
			sb.append(stit.next());
			if (stit.hasNext()) {
				sb.append(separator);
			}
		}

		return sb.toString();
	}

	public int getNumberOfColumns(HttpServletRequest request, Pattern p) {
		final Enumeration<String> params = request.getParameterNames();
		int count = 0;
		while (params.hasMoreElements()) {
			if (p.matcher(params.nextElement()).matches()) {
				count++;
			}
		}
		return count;
	}

	public static Date addDays(Date date, int days) {
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	public static Date atStartOfDay(Date date) {
		final LocalDateTime localDateTime = dateToLocalDateTime(date);
		final LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		return localDateTimeToDate(startOfDay);
	}

	public static Date atEndOfDay(Date date) {
		final LocalDateTime localDateTime = dateToLocalDateTime(date);
		final LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		return localDateTimeToDate(endOfDay);
	}

	private static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	private static Date localDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date dateFromUTC(Date date) {
		return new Date(date.getTime() + Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
	}

	public static Date dateToUTC(Date date) {
		return new Date(date.getTime() - Calendar.getInstance().getTimeZone().getOffset(date.getTime()));
	}

	public static Date parseDate(String dateString) {
		for (final String formatString : DATE_FORMATS) {
			try {
				return new SimpleDateFormat(formatString).parse(dateString);
			} catch (final ParseException e) {
				e.getMessage();
			}
		}

		return null;
	}

	public static Date parseDate(String dateString, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(dateString);
		} catch (final ParseException e) {
			e.getMessage();
		}
		return null;
	}

	public static Date appendTime(Date dateWithoutCurrentTime) throws ParseException {
		final Date todayDateWithCurrentTime = new Date();
		final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		final String formattedDate = dateFormat.format(dateWithoutCurrentTime);
		final DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		final String formattedTime = timeFormat.format(todayDateWithCurrentTime);
		return new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(formattedDate + " " + formattedTime);
	}

	public static Date startDateOfMonth(Date date) {
		final Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		// add -1 month to current month
		// aCalendar.add(Calendar.MONTH, -1);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);
		return atStartOfDay(aCalendar.getTime());
	}

	public static Date endDateOfMonth(Date date) {
		final Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		// add -1 month to current month
		// aCalendar.add(Calendar.MONTH, -1);
		// set actual maximum date of previous month
		aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return atEndOfDay(aCalendar.getTime());
	}

	public static Date[] getPreviousMonthStartEndDate(Date date) {
		final Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -1);
		// set DATE to 1, so first date of previous month
		aCalendar.set(Calendar.DATE, 1);
		final Date firstDateOfPreviousMonth = atStartOfDay(aCalendar.getTime());
		// set actual maximum date of previous month
		aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		// read it
		final Date lastDateOfPreviousMonth = atEndOfDay(aCalendar.getTime());
		return new Date[] { firstDateOfPreviousMonth, lastDateOfPreviousMonth };

	}

	public static Date addMonths(int month) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month);

		return cal.getTime();
	}

	public static Date addMonths(Date date, int month) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}

	public static Date subtractDays(int days) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, days);

		return cal.getTime();
	}

	public static Date[] getLastFinancialYearDates(Date date) {
		final Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		Date endDateOfLastFinancialYear = null;
		Date startDateOfLastFinancialYear = null;
		if (aCalendar.get(Calendar.MONTH) <= 2) {
			// March 31 before year as end
			aCalendar.add(Calendar.YEAR, -1);
			final Calendar endDate = new GregorianCalendar(aCalendar.get(Calendar.YEAR), 2, 31);
			endDateOfLastFinancialYear = atEndOfDay(endDate.getTime());
			// April 01 before two year from start as starting year
			aCalendar.add(Calendar.YEAR, -1);
			final Calendar startDate = new GregorianCalendar(aCalendar.get(Calendar.YEAR), 3, 01);
			startDateOfLastFinancialYear = atStartOfDay(startDate.getTime());
		} else {
			// March 31 current year as end
			final Calendar endDate = new GregorianCalendar(aCalendar.get(Calendar.YEAR), 2, 31);
			endDateOfLastFinancialYear = atEndOfDay(endDate.getTime());
			// April 01 before year as start
			aCalendar.add(Calendar.YEAR, -1);
			final Calendar startDate = new GregorianCalendar(aCalendar.get(Calendar.YEAR), 3, 01);
			startDateOfLastFinancialYear = atStartOfDay(startDate.getTime());
		}

		return new Date[] { startDateOfLastFinancialYear, endDateOfLastFinancialYear };
	}

	public static Date[] getCurrentFinancialYearDates(Date date) {
		final Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(date);
		Date startDateOfCurrentFinancialYear = null;
		Date endDateOfCurrentFinancialYear = null;
		if (aCalendar.get(Calendar.MONTH) <= 2) { // if less less than or equal to march
			// Current date & time as end
			endDateOfCurrentFinancialYear = atEndOfDay(aCalendar.getTime());
			// April 01 before year as start
			aCalendar.add(Calendar.YEAR, -1);
			final Calendar startDate = new GregorianCalendar(aCalendar.get(Calendar.YEAR), 3, 01);
			startDateOfCurrentFinancialYear = atStartOfDay(startDate.getTime());
		} else {
			// Current date & time as end
			endDateOfCurrentFinancialYear = atEndOfDay(aCalendar.getTime());
			// April 01 current year as start
			final Calendar startDate = new GregorianCalendar(aCalendar.get(Calendar.YEAR), 3, 01);
			startDateOfCurrentFinancialYear = atStartOfDay(startDate.getTime());
		}
		return new Date[] { startDateOfCurrentFinancialYear, endDateOfCurrentFinancialYear };
	}

	public static void skip(Iterator<?> iterator, int num) {
		for (int i = 0; i < num; i++) {
			iterator.next();
		}
	}

	public static <T> List<T> addEmptyObject(List<T> list, int size) {
		for (int i = 0; i < size; i++) {
			list.add(null);
		}
		return list;
	}

	public static List<Date> getArrayOfMonthsBetweenDates(Date start, Date end) {

		Calendar beginCalendar = Calendar.getInstance();
		Calendar finishCalendar = Calendar.getInstance();

		beginCalendar.setTime(start);
		finishCalendar.setTime(end);
		List<Date> dateArray = new ArrayList<Date>();
		dateArray.add(beginCalendar.getTime());
		while (beginCalendar.before(finishCalendar)) {
			// add one month to date per loop
			beginCalendar.add(Calendar.MONTH, 1);
			dateArray.add(beginCalendar.getTime());
		}
		return dateArray;

	}

	public static Integer getDeptIdByDept(String dept) {
		Integer access = -1;
		switch (dept) {
		case "ACCOUNTS":
			access = Constants.ROLE_ID_ACCOUNTS;
			break;
		case "SHIPPING":
			access = Constants.ROLE_ID_SHIPPING;
			break;
		case "DOCUMENTS":
			access = Constants.ROLE_ID_DOCUMENTS;
			break;
		case "SALES":
			access = Constants.ROLE_ID_SALES;
			break;
		case "ADMIN":
			access = Constants.ROLE_ID_ADMIN;
			break;
		default:
			break;
		}
		return access;
	}

}
