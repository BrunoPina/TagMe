/*
 * Created on 03/02/2004
 *
 */
package br.com.tagme.commons.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

/**
 * @author gualberto
 *
 */
public class TimeUtils {
	private static Map<Integer, int[]>	weekendJump;
	private static long					zeroDate;
	public static final int				MINUTES_PER_DAY				= 1440;
	public static final int				SECONDS_PER_MINUTE			= 60;
	public static final int				MILLISECONDS_PER_DAY		= 86400000;
	public static final int				MILLISECONDS_PER_SECONDS	= 1000;
	public static final int				MILLISECONDS_PER_MINUTE		= 1000 * 60;
	public static final int				MILLISECONDS_PER_HOUR		= 1000 * 60 * 60;

	static {
		weekendJump = new HashMap<Integer, int[]>();
		weekendJump.put(new Integer(Calendar.SATURDAY), new int[] { -1, 2 });
		weekendJump.put(new Integer(Calendar.SUNDAY), new int[] { -2, 1 });

		try {
			SimpleDateFormat dtFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			zeroDate = dtFormat.parse("30/12/1899 00:00:00").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static long add(long timestamp, int amount, int field) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp);
		calendar.add(field, amount);

		return calendar.getTimeInMillis();
	}

	public static long set(long timestamp, int value, int field) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp);
		calendar.set(field, value);

		return calendar.getTimeInMillis();
	}

	public static long addWorkingDays(long time, int days) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);

		for (int i = 0; i < days;) {
			c.add(Calendar.DAY_OF_MONTH, 1);

			int day = c.get(Calendar.DAY_OF_WEEK);

			if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
				i++;
			}
		}

		return c.getTimeInMillis();
	}

	public static long clear(long timestamp, int field) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp);
		calendar.clear(field);

		return calendar.getTimeInMillis();
	}

	public static Timestamp bigDecimal2Timestamp(BigDecimal time) throws Exception {
		String strOrigem = time.toString();
		strOrigem = StringUtils.padl(strOrigem, 6, '0');

		StringBuffer strHora = new StringBuffer();
		strHora.append(strOrigem.substring(0, 2));
		strHora.append(":");
		strHora.append(strOrigem.substring(2, 4));
		strHora.append(":");
		strHora.append(strOrigem.substring(4, 6));

		return toTimestamp(strHora.toString(), "HH:mm:ss");
	}

	public static Timestamp buildPrintableTimestamp(long time, final String format) {
		// O TimeStamp criado aqui terá como resposta ao toString()
		// uma data de acordo com o formato escolhido.
		return new Timestamp(time) {
			public String toString() {
				SimpleDateFormat dfm = new SimpleDateFormat(format);
				return dfm.format(this);
			}
		};
	}

	/**
	 * Recebe uma data ou data/hora em formato String e converte em um Timestamp. O String deve estar neste formato: d[d]/m[m]/y[yy[yyy][yyyy]] H[HH][:[M[MM][:S[SS]]] Note que os valores dos campos podem ser imcompletos.
	 *
	 * @param text
	 * @return
	 */
	public static Timestamp buildTimestamp(String text) {
		text = StringUtils.getEmptyAsNull(text);

		if (text == null) {
			return null;
		}

		int dateSeparatorCount = StringUtils.countChars(text, '/');

		Calendar c = new GregorianCalendar();
		int currentMillenium = (c.get(Calendar.YEAR) / 100) * 100;
		c.clear();

		int dateTimeSeparator = text.indexOf(' ');

		if (dateTimeSeparator == -1) {
			dateTimeSeparator = text.length();
		}

		if (dateSeparatorCount != 0) {
			if (dateSeparatorCount != 2) {
				throw new IllegalArgumentException("Data inválida: " + text);
			}

			String date = text.substring(0, dateTimeSeparator).trim();

			if (StringUtils.countChars(date, '/') != 2) {
				throw new IllegalArgumentException("Data inválida: " + text);
			}

			// Essa matriz possui o field do Calendar a ser setado e um valor de ajuste que este field deverá sofrer.
			// no caso do mês esse valor é -1 pois o mês é ZERO-BASED (janeiro é 0) no Calendar :-(
			int[][] dateComponents = { { Calendar.DATE, 0 }, { Calendar.MONTH, -1 }, { Calendar.YEAR, 0 } };
			int componentIndex = 0;

			StringTokenizer tokenizer = new StringTokenizer(date, "/");

			while (tokenizer.hasMoreTokens()) {
				int val = Integer.parseInt(tokenizer.nextToken().trim());
				c.set(dateComponents[componentIndex][0], val + dateComponents[componentIndex++][1]);
			}

			if (c.get(Calendar.YEAR) < 1000) {
				c.add(Calendar.YEAR, currentMillenium);
			}

			String time = (dateTimeSeparator < text.length()) ? text.substring(dateTimeSeparator).trim() : "";

			if (time.length() > 0) {
				int timeSeparatorCount = StringUtils.countChars(time, ':');

				if (timeSeparatorCount == 0) {
					time = insertTimeSeparator(time);
				}

				if (timeSeparatorCount > 2) {
					throw new IllegalArgumentException("Hora inválida: " + text);
				}

				tokenizer = new StringTokenizer(time, ":");

				int[] timeComponents = { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND };
				componentIndex = 0;

				while (tokenizer.hasMoreTokens()) {
					c.set(timeComponents[componentIndex++], Integer.parseInt(tokenizer.nextToken().trim()));
				}
			}
		} else {
			int timeSeparatorCount = StringUtils.countChars(text, ':');

			if (timeSeparatorCount > 2) {
				throw new IllegalArgumentException("Hora inválida: " + text);
			}

			StringTokenizer tokenizer = new StringTokenizer(text, ":");

			int[] timeComponents = { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND };
			int componentIndex = 0;

			while (tokenizer.hasMoreTokens()) {
				c.set(timeComponents[componentIndex++], Integer.parseInt(tokenizer.nextToken().trim()));
			}
		}

		return new Timestamp(c.getTimeInMillis());
	}

	public static long clearDate(long datetime) {
		long date = clearTime(datetime);

		return datetime - date;
	}

	public static Timestamp clearTime(Timestamp timestamp) {
		return new Timestamp(clearTime(timestamp.getTime()));
	}

	public static long clearTime(long timestamp) {
		GregorianCalendar calendar = new GregorianCalendar();

		calendar.setTimeInMillis(timestamp);
		clearTime(calendar);

		return calendar.getTime().getTime();
	}

	public static void clearTime(Calendar calendar) {
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		calendar.clear(Calendar.AM_PM);
	}

	public static int compareDates(Calendar c1, Calendar c2) {
		Calendar menorDataPossivel = new GregorianCalendar();
		// 01/01/1600 para GregorianCalendar
		menorDataPossivel.set(1600, 01, 01);
		if(c1.getTimeInMillis() < menorDataPossivel.getTimeInMillis()){
			c1.setTimeInMillis(menorDataPossivel.getTimeInMillis());
		}else if(c1.get(Calendar.YEAR) > 99999){
			c1.set(Calendar.YEAR, 99999);
		}
		
		if(c2.getTimeInMillis() < menorDataPossivel.getTimeInMillis()){
			c2.setTimeInMillis(menorDataPossivel.getTimeInMillis());
		}else if(c2.get(Calendar.YEAR) > 99999){
			c2.set(Calendar.YEAR, 99999);
		}
		
		long l1 = (c1.get(Calendar.YEAR) * 10000) + (c1.get(Calendar.MONTH) * 100) + c1.get(Calendar.DAY_OF_MONTH);
		long l2 = (c2.get(Calendar.YEAR) * 10000) + (c2.get(Calendar.MONTH) * 100) + c2.get(Calendar.DAY_OF_MONTH);

		return (int) (l1 - l2);
	}

	public static int compareOnlyDates(Timestamp t1, Timestamp t2) {
		if (t1 == null) {
			if (t2 == null) {
				return 0;
			}

			return -1;
		} else if (t2 == null) {
			return 1;
		}
		
		int year = 0;
		Calendar c = new GregorianCalendar();
		Calendar menorDataPossivel = new GregorianCalendar();
		// 01/01/1600 para GregorianCalendar
		menorDataPossivel.set(1600, 01, 01);
		
		c.setTimeInMillis(t1.getTime());
		/*
		 * Validações inseridas para dar suporte a parâmetros setados de maneiras irreais por usuarios. 
		 * Por ex.: Dias de antecedencia = 5000000.
		 */
		//valida menor e maior datas possíveis
		if(c.getTimeInMillis() < menorDataPossivel.getTimeInMillis()){
			c.setTimeInMillis(menorDataPossivel.getTimeInMillis());
		}else if(c.get(Calendar.YEAR) > 99999){
			c.set(Calendar.YEAR, 99999);
		}
		
		menorDataPossivel.setTimeInMillis(menorDataPossivel.getTimeInMillis());
		
		year = c.get(Calendar.YEAR); 
		long l1 = (year * 10000) + (c.get(Calendar.MONTH) * 100) + c.get(Calendar.DAY_OF_MONTH);

		c.setTimeInMillis(t2.getTime());
		if(c.getTimeInMillis() < menorDataPossivel.getTimeInMillis()){
			c.setTimeInMillis(menorDataPossivel.getTimeInMillis());
		}else if(c.get(Calendar.YEAR) > 99999){
			c.set(Calendar.YEAR, 99999);
		}

		year = c.get(Calendar.YEAR);
		long l2 = (year * 10000) + (c.get(Calendar.MONTH) * 100) + c.get(Calendar.DAY_OF_MONTH);

		return (int) (l1 - l2);
	}

	public static Timestamp concatDateAndTime(Timestamp date, Timestamp time) throws Exception {
		SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");

		return toTimestamp(dfDate.format(date).concat(" ").concat(dfTime.format(time)), "dd/MM/yyyy HH:mm:ss");
	}

	public static String mesext(Timestamp date) {

		return new java.text.SimpleDateFormat("MMMMMMMMMM").format(date);
	}

	public static String dataPorExtenso(Object data) {
		if (data == null) {
			return "";
		}

		return new java.text.SimpleDateFormat("dd 'de' MMMMMMMMMM 'de' yyyy").format(data);
	}

	public static Integer dayOfWeek(Timestamp data) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data.getTime());

		return new Integer(cal.get(Calendar.DAY_OF_WEEK));
	}

	public static String formataDDMMYY(Object data) {
		SimpleDateFormat frmData = new SimpleDateFormat("dd/MM/yy");

		return ((data != null) ? frmData.format(data) : "  /  /  ");
	}

	public static String formataDDMMYYYY(Object data) {
		SimpleDateFormat frmData = new SimpleDateFormat("dd/MM/yyyy");

		return ((data != null) ? frmData.format(data) : "  /  /    ");
	}

	public static long getDayEnd(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.HOUR, c.getActualMaximum(Calendar.HOUR));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getActualMaximum(Calendar.MILLISECOND));
		c.set(Calendar.AM_PM, c.getActualMaximum(Calendar.AM_PM));

		return c.getTimeInMillis();
	}

	public static long getDayStart(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.HOUR, c.getActualMinimum(Calendar.HOUR));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
		c.set(Calendar.AM_PM, c.getActualMinimum(Calendar.AM_PM));

		return c.getTimeInMillis();
	}

	public static int getMaximum(Timestamp time, int field) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time.getTime());
		return c.getActualMaximum(field);
	}

	public static int getDifference(Timestamp t1, Timestamp t2) {
		return getDifference(t1, t2, true);
	}

	public static int getDifference(Timestamp t1, Timestamp t2, boolean unsigned) {
		long l1 = t1.getTime();
		long l2 = t2.getTime();
		int result = (int) ((l1 / MILLISECONDS_PER_DAY) - (l2 / MILLISECONDS_PER_DAY));

		if ((result < 0) && unsigned) {
			result *= -1;
		}

		return result;
	}

	public static long getDifferenceInMinutes(Timestamp t1, Timestamp t2, boolean unsigned) {
		long l1 = t1.getTime();
		long l2 = t2.getTime();
		int result = (int) (timestamp2Minutes(l1) - timestamp2Minutes(l2));

		if ((result < 0) && unsigned) {
			result *= -1;
		}

		return result;
	}

	public static long getDifferenceInMinutes(Timestamp t1, Timestamp t2) {
		return getDifferenceInMinutes(t1, t2, true);
	}

	public static long getDifferenceInMonths(Timestamp t1, Timestamp t2) {
		Calendar c1 = new GregorianCalendar();
		Calendar c2 = new GregorianCalendar();

		c1.setTime(t1);
		c2.setTime(t2);

		c1.setTimeInMillis(TimeUtils.clearTime(c1.getTimeInMillis()));
		c2.setTimeInMillis(TimeUtils.clearTime(c2.getTimeInMillis()));

		c1.set(Calendar.DAY_OF_MONTH, 1);
		c2.set(Calendar.DAY_OF_MONTH, 1);

		int meses = 0;

		while (c1.getTimeInMillis() != c2.getTimeInMillis()) {
			meses++;

			if (c1.compareTo(c2) < 0) {
				c1.add(Calendar.MONTH, 1);
			} else {
				c1.add(Calendar.MONTH, -1);
			}
		}

		return meses;
	}
	
	public static long getDifferenceInHour(Timestamp t1, Timestamp t2) {
		return getDifferenceInHour(t1, t2, true);
	}

	public static long getDifferenceInHour(Timestamp t1, Timestamp t2, boolean unsigned) {
		long l1 = t1.getTime();
		long l2 = t2.getTime();
		int result = (int) (timestamp2Hour(l1) - timestamp2Hour(l2));

		if ((result < 0) && unsigned) {
			result *= -1;
		}

		return result;
	}
	
	public static long timestamp2Hour(long timestamp) {
		return (timestamp / 3600) / 1000;
	}

	public static Date copyValue(Date dtFrom, Date dtTo, int field) {

		GregorianCalendar c1 = new GregorianCalendar();
		GregorianCalendar c2 = new GregorianCalendar();

		c1.setTime(dtTo);
		c2.setTime(dtFrom);
		c1.set(field, c2.get(field));

		return c1.getTime();
	}

	public static String getDisplayableTime(int timeWithMinutes) {
		return TimeUtils.getDisplayableTime(new BigDecimal(timeWithMinutes));
	}

	public static String getDisplayableTime(BigDecimal timeWithMinutes) {
		if (timeWithMinutes == null) {
			return "";
		}

		boolean negative = false;

		if (timeWithMinutes.intValue() < 0) {
			negative = true;
			timeWithMinutes = timeWithMinutes.multiply(new BigDecimal(-1));
		}

		String strTimeWithMinutes = String.valueOf(timeWithMinutes.intValue());
		int minutesStart = Math.max(strTimeWithMinutes.length() - 2, 0);
		String timeToken = strTimeWithMinutes.substring(0, minutesStart).trim();
		String minuteToken = strTimeWithMinutes.substring(minutesStart).trim();
		Integer time = new Integer(0);
		Integer minute = new Integer(0);
		String result = null;

		if ((timeToken != null) && (timeToken.length() > 0)) {
			if ((timeToken.length() == 1) && timeToken.equals("-")) {
				timeToken = null;
			}

			if (timeToken != null) {
				time = new Integer(timeToken);
			}
		}

		if ((minuteToken != null) && (minuteToken.length() > 0)) {
			minute = new Integer(minuteToken);
		}

		StringBuffer buf = new StringBuffer();
		buf.append(time);
		buf.append(":");
		buf.append(StringUtils.stringZero(minute, 2));

		if (buf.length() < 5) {
			result = StringUtils.padl(buf.toString(), 5, '0');
		} else {
			result = buf.toString();
		}

		if (negative) {
			result = "-" + result;
		}

		return result;
	}

	public static BigDecimal getHoraDecimal(Timestamp data) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(data.getTime());

		String hora = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String minutos = String.valueOf(cal.get(Calendar.MINUTE));

		if (minutos.length() == 1) {
			minutos = "0" + minutos;
		}

		return new BigDecimal(hora + minutos);
	}

	public static long getMonthEnd(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.clear(Calendar.HOUR_OF_DAY);
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		c.clear(Calendar.AM_PM);

		return c.getTimeInMillis();
	}

	public static long getMonthStart(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.clear(Calendar.HOUR_OF_DAY);
		c.clear(Calendar.HOUR);
		c.clear(Calendar.MINUTE);
		c.clear(Calendar.SECOND);
		c.clear(Calendar.MILLISECOND);
		c.clear(Calendar.AM_PM);

		return c.getTimeInMillis();
	}

	public static String getNow(String formatPattern) {
		SimpleDateFormat df = new SimpleDateFormat(formatPattern);

		return df.format(new Date());
	}

	public static Timestamp getNow() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static int getTimeInMinutes(Timestamp t1) {
		Calendar c = new GregorianCalendar();
		c.setTime(t1);

		return (c.get(Calendar.HOUR_OF_DAY) * 60) + c.get(Calendar.MINUTE);
	}

	public static long getToday() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.HOUR);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		calendar.clear(Calendar.AM_PM);

		return calendar.getTime().getTime();
	}

	public static long getToday(int[] clearFields) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());

		for (int i = 0; i < clearFields.length; i++) {
			calendar.clear(clearFields[i]);
		}

		return calendar.getTimeInMillis();
	}

	public static Timestamp getValueOrNow(Timestamp value) {
		Timestamp result;

		if (value == null) {
			result = new Timestamp(System.currentTimeMillis());
		} else {
			result = value;
		}

		return result;
	}

	public static int getWeekOfYear(Date d) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTimeInMillis(d.getTime());

		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	public static BigDecimal getYearMonth(Timestamp time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time.getTime());

		int year = c.get(Calendar.YEAR);
		int mounth = c.get(Calendar.MONTH) + 1;

		return new BigDecimal((year * 100) + mounth);
	}

	/**
	 * Método refente a data 0 no delphi, 30/12/1899 00:00:00.
	 * Ex. delphi:
	 *     var dtIni: TDateTime;
	 *     dtIni = 0; //-> 30/12/1899 00:00:00
	 * @return
	 */
	public static long getZeroDate() {
		return zeroDate;
	}

	public static Timestamp getTimeOrZero(Timestamp time) {
		if (time == null) {
			return new Timestamp(zeroDate);
		}
		return time;
	}

	public static boolean isValidTime(BigDecimal timeWithMinutes) {
		int time = 0;
		int minute = 0;

		if (timeWithMinutes == null) {
			return true;
		}

		String strTimeWithMinutes = String.valueOf(timeWithMinutes.intValue());
		int minutesStart = Math.max(strTimeWithMinutes.length() - 2, 0);

		try {
			String timeToken = strTimeWithMinutes.substring(0, minutesStart).trim();
			String minuteToken = strTimeWithMinutes.substring(minutesStart).trim();
			time = 0;
			minute = 0;

			if ((timeToken != null) && (timeToken.length() > 0)) {
				time = Integer.parseInt(timeToken);
			}

			if ((minuteToken != null) && (minuteToken.length() > 0)) {
				minute = Integer.parseInt(minuteToken);
			}

			return (time >= 0) && (minute >= 0) && (minute <= 59);
		} catch (RuntimeException e) {
			System.out.println("Debug: strTimeWithMinutes=" + strTimeWithMinutes);
			throw e;
		}
	}

	public static BigDecimal minutes2Time(int minutes) {
		return minutes2Time(new BigDecimal(minutes));
	}

	public static BigDecimal minutes2Time(BigDecimal minutes) {
		int time = 0;
		int minute = 0;

		if (minutes == null) {
			return new BigDecimal(0);
		}

		time = minutes.intValue() / 60;
		minute = minutes.intValue() % 60;

		return new BigDecimal((time * 100) + minute);
	}

	public static long minutes2Timestamp(long minutes) {
		return (minutes * 60 * MILLISECONDS_PER_SECONDS);
	}

	public static BigDecimal time2Minutes(int timeWithMinutes) {
		return time2Minutes(new BigDecimal(timeWithMinutes));
	}

	public static BigDecimal time2Minutes(BigDecimal timeWithMinutes) {
		int time = 0;
		int minute = 0;

		if (timeWithMinutes == null) {
			return new BigDecimal(0);
		}

		String strTimeWithMinutes = String.valueOf(timeWithMinutes.intValue());
		int minutesStart = Math.max(strTimeWithMinutes.length() - 2, 0);

		try {
			String timeToken = strTimeWithMinutes.substring(0, minutesStart).trim();
			String minuteToken = strTimeWithMinutes.substring(minutesStart).trim();
			time = 0;
			minute = 0;

			if ((timeToken != null) && (timeToken.length() > 0)) {
				time = Integer.parseInt(timeToken);
			}

			if ((minuteToken != null) && (minuteToken.length() > 0)) {
				minute = Integer.parseInt(minuteToken);
			}
		} catch (RuntimeException e) {
			System.out.println("Debug: strTimeWithMinutes=" + strTimeWithMinutes);
			throw e;
		}

		return new BigDecimal((time * 60) + minute);
	}

	public static BigDecimal timestamp2BigDecimal(Timestamp time) {
		SimpleDateFormat df = new SimpleDateFormat("HHmmss");
		String strTime = df.format(time);

		return new BigDecimal(Integer.parseInt(strTime));
	}

	public static long timestamp2Minutes(long timestamp) {
		return (timestamp / MILLISECONDS_PER_SECONDS) / 60;
	}

	public static String milisegundos2HHmmss(long timeMillis) throws Exception {
		SimpleDateFormat horasFormat = new SimpleDateFormat("HH:mm:ss");
		long time = timeMillis / 1000;
		int seconds = (int) (time % 60);
		int minutes = (int) ((time % 3600) / 60);
		int hours = (int) (time / 3600);

		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.SECOND, seconds);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.HOUR_OF_DAY, hours);
		return horasFormat.format(cal.getTime());
	}
	
	public static String formatMillis2ElapsedTime(long milliseconds) {
		
		return String.format("%02d:%02d:%02d",TimeUnit.MILLISECONDS.toHours(milliseconds),
			    TimeUnit.MILLISECONDS.toMinutes(milliseconds) - 
			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
			    TimeUnit.MILLISECONDS.toSeconds(milliseconds) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
	}
	
	public static String formatMillis2SimpleElapsedTime(long milliseconds){

		if(milliseconds < MILLISECONDS_PER_SECONDS){
			return milliseconds + "ms";
			
		} else if(milliseconds < MILLISECONDS_PER_MINUTE){
			return Long.toString(TimeUnit.MILLISECONDS.toSeconds(milliseconds)) + "s";
		
		} else if(milliseconds < MILLISECONDS_PER_HOUR) {
			return Long.toString(TimeUnit.MILLISECONDS.toMinutes(milliseconds)) + "m";
		
		} else {
			return Long.toString(TimeUnit.MILLISECONDS.toMinutes(milliseconds)) + "h";
		}
	}

	public static long toDate(String s) throws Exception {
		return toDate(s, "dd/MM/yyyy");
	}

	public static long toDate(String s, String pattern) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat(pattern);

		return df.parse(s).getTime();
	}

	public static BigDecimal toDecimalTime(String timeWithMinutes) {
		timeWithMinutes = StringUtils.getEmptyAsNull(timeWithMinutes);

		if (timeWithMinutes == null) {
			return null;
		}

		int timeSeparatorIndex = timeWithMinutes.indexOf(':');

		if (timeSeparatorIndex < 0) {
			throw new IllegalArgumentException("Formato de hora decimal inválido: " + timeWithMinutes);
		}

		int time = Integer.parseInt(timeWithMinutes.substring(0, timeSeparatorIndex)) * 100;
		int minutes = Integer.parseInt(timeWithMinutes.substring(timeSeparatorIndex + 1));

		return new BigDecimal(time + minutes);
	}

	public static Timestamp toTimestamp(String s) throws Exception {
		return new Timestamp(toDate(s));
	}

	public static Timestamp toTimestamp(String s, String pattern) throws Exception {
		return new Timestamp(toDate(s, pattern));
	}

	public static long weekendFoward(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);

		int[] days = (int[]) weekendJump.get(new Integer(c.get(Calendar.DAY_OF_WEEK)));

		if (days != null) {
			c.add(Calendar.DAY_OF_WEEK, days[1]);
		}

		return c.getTimeInMillis();
	}

	public static long weekendReward(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);

		int[] days = (int[]) weekendJump.get(new Integer(c.get(Calendar.DAY_OF_WEEK)));

		if (days != null) {
			c.add(Calendar.DAY_OF_WEEK, days[0]);
		}

		return c.getTimeInMillis();
	}

	private static String insertTimeSeparator(String hora) {
		int tam = hora.length();
		int cont = 1;
		StringBuffer result = new StringBuffer(hora);

		for (int i = tam; i > 1; i--) {
			if (cont == 2) {
				result.insert(i - 1, ':');
				cont = 1;
			} else {
				cont++;
			}
		}

		return result.toString();
	}

	public static class Schedule {
		public static final int			EVERY_DAY			= 0;
		public static final int			PER_DAY_OF_WEEK		= 1;
		public static final int			PER_DAY_OF_MONTH	= 2;
		private List<Integer>			minutesAtDay;
		private Map<Integer, Object>	daysOfMonth;
		private Map<Integer, Object>	daysOfWeek;
		private Map<Integer, Object>	months;
		private int						hours;
		private int						minutes;
		private int						type;

		public Schedule() {
			daysOfWeek = new HashMap<Integer, Object>();
			daysOfMonth = new HashMap<Integer, Object>();
			months = new HashMap<Integer, Object>();
			minutesAtDay = new ArrayList<Integer>();
		}

		public void addDayOfMonth(int dayOfMonth) {
			daysOfMonth.put(new Integer(dayOfMonth), null);
		}

		public void addDayOfWeek(int dayOfWeek) {
			daysOfWeek.put(new Integer(dayOfWeek), null);
		}

		public void addHourOfDay(int hourWithMinutes) {
			minutesAtDay.add(new Integer(time2Minutes(hourWithMinutes).intValue()));
		}

		public void addMonth(int month) {
			months.put(new Integer(month), null);
		}

		public Timestamp getNextTimeout(Timestamp baseDateTime) {
			/*
			 * Quando esta lista estiver vazia significa que estamos usando apenas um horário. Para que o algoritmo seja único adicionamos este horário na lista e o processamento segue normalmente.
			 */
			if (minutesAtDay.isEmpty()) {
				minutesAtDay.add(new Integer((hours * 60) + minutes));
			}

			Collections.sort(minutesAtDay, new Comparator<Integer>() { // a lista de horários deve estar sempre ordenada.
						public int compare(Integer i1, Integer i2) {
							return i1.compareTo(i2);
						}
					});

			GregorianCalendar gc = new GregorianCalendar();

			long today = System.currentTimeMillis();

			gc.setTimeInMillis(baseDateTime.getTime());

			int baseMinutes = (gc.get(Calendar.HOUR_OF_DAY) * 60) + gc.get(Calendar.MINUTE);

			gc.setTimeInMillis(today);

			int scheduledMinutes = getNextMinuteToRun(baseMinutes);

			if (scheduledMinutes < 0) { // se não temos mais horários para hoje devemos somar 1 ao dia. Isso evita loops.
				gc.add(Calendar.DAY_OF_MONTH, 1);
			}

			while (!months.isEmpty() && !months.containsKey(new Integer(gc.get(Calendar.MONTH) + 1))) {
				gc.add(Calendar.MONTH, 1);
				gc.set(Calendar.DAY_OF_MONTH, 1);
				scheduledMinutes = -1; // sempre que mudamos o dia devemos resetar o horário
			}

			switch (type) {
				case EVERY_DAY:

					// nada mais a fazer.
					break;

				case PER_DAY_OF_WEEK:

					while (!daysOfWeek.isEmpty() && !daysOfWeek.containsKey(new Integer(gc.get(Calendar.DAY_OF_WEEK)))) {
						gc.add(Calendar.DAY_OF_MONTH, 1);
						scheduledMinutes = -1; // sempre que mudamos o dia devemos resetar o horário
					}

					break;

				case PER_DAY_OF_MONTH:

					if (daysOfMonth.containsKey(0)) {
						daysOfMonth.put(gc.getActualMaximum(Calendar.DAY_OF_MONTH), null);
					}

					while (!daysOfMonth.isEmpty() && !daysOfMonth.containsKey(new Integer(gc.get(Calendar.DAY_OF_MONTH)))) {
						gc.add(Calendar.DAY_OF_MONTH, 1);
						scheduledMinutes = -1; // sempre que mudamos o dia devemos resetar o horário
					}
					break;

				default:
					break;
			}

			if (scheduledMinutes < 0) {
				scheduledMinutes = getNextMinuteToRun(-1); // isso pega o primeiro horário da lista
			}

			gc.set(Calendar.HOUR_OF_DAY, scheduledMinutes / 60);
			gc.set(Calendar.MINUTE, (scheduledMinutes % 60));
			gc.set(Calendar.SECOND, 0);

			return new Timestamp(gc.getTimeInMillis());
		}

		public void setHoras(int horas) {
			this.hours = horas;
		}

		public void setMinutos(int minutos) {
			this.minutes = minutos;
		}

		public void setPeriod(int startHour, int endHour, int hourPeriod) {
			int startAsMinutes = time2Minutes(startHour).intValue();
			int endAsMinutes = time2Minutes(endHour).intValue();
			int periodAsMinutes = time2Minutes(hourPeriod).intValue();

			int next = startAsMinutes;

			do {
				minutesAtDay.add(new Integer(next));
				next += periodAsMinutes;
			} while (next <= endAsMinutes);
		}

		public void setType(int tipoDias) {
			this.type = tipoDias;
		}

		private int getNextMinuteToRun(int currentMinute) {
			for (Integer min : minutesAtDay) {
				if (min.intValue() > currentMinute) {
					return min.intValue();
				}
			}

			return -1;
		}
	}
	
	public static Timestamp buildData(int dia, int mes, int ano){
		Calendar calendar = new GregorianCalendar();
		calendar.set(ano, mes, dia);
		clearTime(calendar);
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static Timestamp getInicioPeriodo(Timestamp dataBase, int tipoPeriodo){
		return getInicioPeriodo(dataBase, TipoPeriodo.values()[tipoPeriodo]);
	}
	
	public static Timestamp getInicioPeriodo(Timestamp dataBase, TipoPeriodo tipoPeriodo){
		Calendar calendar = Calendar.getInstance(new Locale("pt", "BR"));
		
		calendar.setTimeInMillis(dataBase.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH);
		
		switch (tipoPeriodo) {
			case DIA:
				break;
			
			case SEMANA:
				calendar.add(Calendar.WEEK_OF_YEAR, -1);
				calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				break;
			
			case DEZENA:
				if(dia <= 10){
					dia = 1;
				}else if(dia <= 20){
					dia = 11;
				}else{
					dia = 21;
				}
				
				calendar.set(Calendar.DAY_OF_MONTH, dia);
				break;
				
			case QUINZENA:
				calendar.set(Calendar.DAY_OF_MONTH, (dia <= 15 ? 1 : 16));
				break;
				
			case MES:
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				break;
				
			case BIMESTRE:
				mes = (int) Math.floor(mes / 2) * 2;
				calendar.set(Calendar.MONTH, mes);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				break;
				
			case TRIMESTRE:
				mes = (int) Math.floor(mes / 3) * 3;
				calendar.set(Calendar.MONTH, mes);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				break;
				
			case QUADRIMESTRE:
				mes = (int) Math.floor(mes / 4) * 4;
				calendar.set(Calendar.MONTH, mes);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				break;
				
			case SEMESTRE:
				mes = (int) Math.floor(mes / 6) * 6;
				calendar.set(Calendar.MONTH, mes);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				break;
				
			case ANO:
				calendar.set(Calendar.MONTH, Calendar.JANUARY);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				break;
				
			default:
				throw new IllegalArgumentException("Tipo de período inválido!");
		}
		
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public enum TipoPeriodo {
		DIA,
		SEMANA,
		DEZENA,
		QUINZENA,
		MES,
		BIMESTRE,
		TRIMESTRE,
		QUADRIMESTRE,
		SEMESTRE,
		ANO,
		PERIODO_UNICO
	}

	public static Timestamp clearFields(Timestamp timestamp, int ... fields) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTimeInMillis(timestamp.getTime());
		
		for (int f : fields) {
			calendar.clear(f);
		}
		
		return new Timestamp(calendar.getTimeInMillis());
	}
	
	public static int getDayOfMonth(Timestamp dataInstalacao) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dataInstalacao.getTime());
		int diaInstalacao = calendar.get(Calendar.DAY_OF_MONTH);
		return diaInstalacao;
	}
	
	public static Timestamp getHorarioExec(String horarioExec, int diasAdicionais) throws Exception{

		horarioExec = StringUtils.stringZero(horarioExec, 4, false);
		int split = horarioExec.length() - 2;

		String configHour = horarioExec.substring(0, split);
		String configMinute = horarioExec.substring(split);

		if ((horarioExec.length() > 4) || Integer.valueOf(configHour) > 23 || Integer.valueOf(configMinute) > 59) {
			throw new Exception("'Horário de execução' com formato inválido");
		}

		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.DAY_OF_MONTH, diasAdicionais);
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(configHour));
		cal.set(Calendar.MINUTE, Integer.valueOf(configMinute));
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Timestamp(cal.getTimeInMillis());
	}
	
	public static int getValueFieldTimestamp(Timestamp timestamp, int field) throws Exception {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(timestamp.getTime());

		return calendar.get(field);
	}
	
	public static Timestamp getHorarioExec(String horarioExec) throws Exception{
		return getHorarioExec(horarioExec, 0);
	}
	
	public static String getProximoDiaUtil(String inputDate, String formatInput)throws Exception{
		return getProximoDiaUtil(inputDate, formatInput, null);
	}
	
	public static String getProximoDiaUtil(String inputDate, String formatInput, String outPutFormat)throws Exception{
		SimpleDateFormat sdfOut = null;
		
		if(outPutFormat == null){
			sdfOut = new SimpleDateFormat("dd/MM/yyyy");
		}else{
			sdfOut = new SimpleDateFormat(outPutFormat);
		}
		
		
		return sdfOut.format(getProximoDiaUtil(new SimpleDateFormat(formatInput).parse(inputDate).getTime()));
		
	}
	
	
	/*
	 * Gildo Neto
	 */
	public static Timestamp getTimestamp(Object strTimestamp){
		if(StringUtils.isNotEmpty(strTimestamp)){
			Timestamp timesTamp = null;
			if(StringUtils.isNotEmpty(strTimestamp)){
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					java.util.Date parsedDate = sdf.parse(strTimestamp.toString());
					timesTamp = new Timestamp(parsedDate.getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return timesTamp;
		}else{
			return null;
		}
	}
	
	public static void main(String[] args) throws Exception{
		String dia = "31/08/2013";
		String in = "dd/MM/yyyy";
		
		System.out.println(getProximoDiaUtil(dia, in));
	}
	
	public static long getProximoDiaUtil(long dateMillis) throws Exception{
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTimeInMillis(dateMillis);
		
		while(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return cal.getTimeInMillis();
	}

	public static String formataIntervalo(long intervaloEmMilissegundos) {
		Intervalo intervalo = new Intervalo(intervaloEmMilissegundos);

		return intervalo.getIntervaloFormatadoPorExtenso();
	}

	public static class Intervalo {
		private long dias;
		private long horas;
		private long minutos;
		private long segundos;
		private long milissegundos;

		public Intervalo(long intervaloEmMilissegundos) {
			dias = intervaloEmMilissegundos / MILLISECONDS_PER_DAY;
			intervaloEmMilissegundos = intervaloEmMilissegundos % MILLISECONDS_PER_DAY;

			horas = intervaloEmMilissegundos / MILLISECONDS_PER_HOUR;
			intervaloEmMilissegundos = intervaloEmMilissegundos % MILLISECONDS_PER_HOUR;

			minutos = intervaloEmMilissegundos / MILLISECONDS_PER_MINUTE;
			intervaloEmMilissegundos = intervaloEmMilissegundos % MILLISECONDS_PER_MINUTE;

			segundos = intervaloEmMilissegundos / MILLISECONDS_PER_SECONDS;
			intervaloEmMilissegundos = intervaloEmMilissegundos % MILLISECONDS_PER_SECONDS;

			milissegundos = intervaloEmMilissegundos;
		}

		public String getIntervaloFormatadoPorExtenso() {
			StringBuffer intervalo = new StringBuffer();

			if (dias > 0) {
				intervalo.append(dias).append(" dia").append(dias == 1 ? " " : "s ");
			}

			if (horas > 0) {
				intervalo.append(horas).append(" hora").append(horas == 1 ? " " : "s ");
			}

			if (minutos > 0) {
				intervalo.append(minutos).append(" minuto").append(minutos == 1 ? " " : "s ");
			}

			if (segundos > 0) {
				intervalo.append(segundos).append(" segundo").append(segundos == 1 ? " " : "s ");
			}

			intervalo.append(milissegundos).append(" milissegundo").append(milissegundos == 1 ? " " : "s ");

			return intervalo.toString();
		}

		public long getDias() {
			return dias;
		}

		public long getHoras() {
			return horas;
		}

		public long getMinutos() {
			return minutos;
		}

		public long getSegundos() {
			return segundos;
		}

		public long getMilissegundos() {
			return milissegundos;
		}
	}
}
