package br.com.tagme.commons.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static final char[]	digits		= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	public static final int		ONLY_DIGITS	= 1;
	public static final int		ONLY_ALPHAS	= 2;

	private static String		charsWithAccentuation;
	private static String		charsWithoutAccentuation;

	static {
		charsWithAccentuation = "ÁÉÍÓÚ_ÃÕ_ÂÊÎÔÛ_ÀÈÌÒÙ_Ü_Ç_áéíóú_ãõ_âêîôû_àèìòù_ü_ç";
		charsWithoutAccentuation = "AEIOU_AO_AEIOU_AEIOU_U_C_aeiou_ao_aeiou_aeiou_u_c";
	}

	private StringBuffer		strbuf;

	public StringUtils() {
		strbuf = new StringBuffer();
	}

	public StringUtils(String str) {
		strbuf = new StringBuffer((str == null) ? "" : str);
	}

	public static Collection buildTokens(String s, String separator, TokenBuilder builder) {
		Collection c = new ArrayList();

		StringTokenizer tokenizer = new StringTokenizer(s, separator);

		while (tokenizer.hasMoreTokens()) {
			Object value = builder.buildToken(tokenizer.nextToken());

			if (value != null) {
				c.add(value);
			}
		}

		return c;
	}

	public static Set buildTokensAsSet(String s, String separator, TokenBuilder builder) {
		Set c = new HashSet();

		StringTokenizer tokenizer = new StringTokenizer(s, separator);

		while (tokenizer.hasMoreTokens()) {
			Object value = builder.buildToken(tokenizer.nextToken());

			if (value != null) {
				c.add(value);
			}
		}

		return c;
	}
	
	public static String buildRaizCnpj(String cnpj){
		String raiz = null;
		
		if(StringUtils.getEmptyAsNull(cnpj) != null && cnpj.length() >= 8){
			raiz = cnpj.substring(0,8);
		}
				
		return raiz;
	}

	public static Collection<String> getLines(String s) {
		Collection<String> r = new ArrayList<String>();

		s = StringUtils.getEmptyAsNull(s);

		if (s != null) {
			BufferedReader reader = new BufferedReader(new StringReader(s));

			String line = null;

			try {
				while ((line = reader.readLine()) != null) {
					r.add(line);
				}
			} catch (IOException ignored) {
			} // efeito colateral da classe Reader
		}

		return r;
	}

	public static BigDecimal convertToBigDecimal(String s) {
		boolean negativeSign = false;
		s = getEmptyAsNull(s);

		if (s == null) {
			return null;
		}

		negativeSign = s.charAt(0) == '-';

		int decReward = 0;
		boolean hasNonDigit = false;

		for (int i = s.length() - 1; i >= 0; i--) {
			char c = s.charAt(i);

			if ((c != '-') && !Character.isDigit(c)) {
				hasNonDigit = true;

				break;
			}

			decReward++;
		}

		s = s.replaceAll("\\D", "");

		StringBuffer buf = new StringBuffer(s);

		if (hasNonDigit) {
			buf.insert(buf.length() - decReward, '.');
		}

		BigDecimal bigDecimalToReturn = new BigDecimal(buf.toString());

		if (negativeSign) {
			bigDecimalToReturn = bigDecimalToReturn.multiply(new BigDecimal(-1));
		}

		return bigDecimalToReturn;
	}

	/**
	 * Similar ao copy do Delphi. Muito utilizada em modelos de impressão.
	 */

	public static String copy(String val, int pos, int qtd) {
		pos = (pos > 0) ? (--pos) : 0;

		if (val == null) {
			val = "";
		}

		if (pos + qtd > val.length()) {
			val = padr(val, pos + qtd);
		}

		return val.substring(pos, pos + qtd);
	}

	/**
	 * Similar ao copy do Delphi. Muito utilizada em modelos de impressão.
	 */
	public static String copy(Object val, int pos, int qtd) {
		if (val instanceof Timestamp) {
			val = TimeUtils.formataDDMMYYYY(val);
		}

		if (val == null) {
			val = "";
		}

		return copy(val.toString(), pos, qtd);
	}

	/**
	 * Para uso em modelos TXT.
	 */
	public static String substr(Object o, int start, int count) {
		String s = o == null ? "" : o.toString();

		start = (start > 0) ? (--start) : 0;

		if (start >= s.length()) {
			return "";
		}

		count = Math.min(start + count, s.length());

		return s.substring(start, count);
	}

	public static void delimeterReplace(String toReplace, StringBuffer s, String openDelim, String closeDelim, int pos) {
		int start = s.lastIndexOf(openDelim, pos);

		if (start == -1) {
			return;
		}

		int end = s.indexOf(closeDelim, start + openDelim.length());

		if (end <= pos) {
			return;
		}

		s.replace(start, end + closeDelim.length(), toReplace);
	}

	public static String delimeterSubstring(String s, String openDelim, String closeDelim, int pos) {
		int start = s.lastIndexOf(openDelim, pos);

		if (start == -1) {
			return "";
		}

		start += openDelim.length();

		int end = s.indexOf(closeDelim, start);

		if (end <= pos) {
			return "";
		}

		return s.substring(start, end);
	}

	/**
	 * Muito utilizada em modelos de impressão.
	 */
	public static String formatNumeric(String format, Object value, int minSize) {
		String valstr = (value != null) ? value.toString() : "";
		DecimalFormat dfFormat = new DecimalFormat(format);
		String valueFormat = dfFormat.format(new BigDecimal(valstr));

		// Não está igual ao MGE
		// Não mexer sem falar com o Gualberto
		return padl(valueFormat, minSize == 0 ? format.length() : minSize);
	}

	public static String formatNumeric(String format, Object value) {
		return formatNumeric(format, value, 0);
	}

	public static String formatNumeric(Object valor) {
		return formatNumeric("#,##0.00;(#,##0.00)", valor, 0);
	}

	public static String formatTimestamp(Timestamp t, String format) {
		try {
			DateFormat df = new SimpleDateFormat(format);

			return df.format(new Date(t.getTime()));
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para uma string válida: " + t);
		}
	}

	public static String formataCep(String cep) {
		return isNotEmpty(cep) ? new StringFormat().format(cep, "##.###-###") : "";
	}

	public static String formataCfop(String format, int codCFO) {
		return new StringFormat().format(String.valueOf(codCFO), format);
	}

	public static String formataCgcCpf(String cgccpf) {
		String tipPessoa = "";

		if (cgccpf == null) {
			return "";
		}

		if (cgccpf.trim().length() == 11) {
			tipPessoa = "F";
		} else if (cgccpf.trim().length() == 14) {
			tipPessoa = "J";
		}

		return formataCgcCpf(tipPessoa, cgccpf);
	}

	public static String formataCgcCpf(String tipPessoa, String cgccpf) {
		String cgccpfFormatado = "";

		String cgccpfSomenteDigitos = cgccpf.replaceAll("[^\\d]", "");

		if (cgccpfSomenteDigitos.length() == 11) {
			cgccpfFormatado = StringUtils.toCPF(cgccpfSomenteDigitos);
		} else if (cgccpfSomenteDigitos.length() == 14) {
			cgccpfFormatado = StringUtils.toCNPJ(cgccpfSomenteDigitos);
		}

		return cgccpfFormatado;
	}

	public static String formataPlaca(String placa) {
		return isNotEmpty(placa) ? new StringFormat().format(placa, "###-####") : "";
	}

	public static String formataTelefone(String telefone) {
		return isNotEmpty(telefone) ? new StringFormat().format(telefone, telefone.trim().length() <= 12 ? "####-####-####" : "####-#####-####") : "";
	}

	public static String formataTelefone2(String telefone) {
		return isNotEmpty(telefone) ? new StringFormat().format(telefone, telefone.trim().length() <= 12 ? "(####) ####-####" : "(####) #####-####") : "";
	}
	
	public static String formatSizeBytesToString(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    
	    return new DecimalFormat("#,##0").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static String getCollectionCommaSeparated(Collection<?> col) {
		return getCollectionCommaSeparated(col, false);
	}

	public static String getCollectionCommaSeparated(Collection<?> col, boolean wrapLines) {
		StringBuffer buf = new StringBuffer(col.size() * 64);

		for (Iterator<?> ite = col.iterator(); ite.hasNext();) {
			buf.append(ite.next() + (wrapLines ? ",\n" : ", "));
		}

		int lastIndexOfComma = buf.lastIndexOf(", ");

		if (lastIndexOfComma > 0) {
			buf.setLength(lastIndexOfComma);
		}

		return buf.toString();
	}

	public static String getListCommaSeparated(List<?> list) {
		return getListCommaSeparated(list, false);
	}
	
	public static String getListCommaSeparated(List<?> list, boolean wrapLines) {
		StringBuilder buf = new StringBuilder();

		int s = list.size();

		for (int i = 0; i < s - 1; i++) {
			buf.append(list.get(i).toString()).append(", ");

			if (wrapLines) {
				buf.append("\n");
			}
		}

		if (s > 0) {
			buf.append(list.get(s - 1).toString());
		}

		return buf.toString();
	}

	public static String getArrayCommaSeparated(Object[] objs) {
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < objs.length - 1; i++) {
			buf.append(objs[i].toString()).append(", ");
		}

		if (objs.length > 0) {
			buf.append(objs[objs.length - 1].toString());
		}

		return buf.toString();
	}

	public static int getDelimiterClosingPosition(CharSequence s, char open, char close, int start) {
		int curPos = start;

		int openBraces = 0;

		do {
			char c = s.charAt(curPos);

			if (c == open) {
				openBraces++;
			} else if (c == close) {
				openBraces--;
			}

			if (openBraces == 0) {
				break;
			}

			curPos++;
		} while (curPos < s.length());

		return (curPos < s.length()) ? curPos : (-1);
	}

	public static int getDelimiterOpeningPosition(CharSequence s, char open, char close, int start) {
		int curPos = start;

		int openBraces = 0;

		do {
			char c = s.charAt(curPos);

			if (c == open) {
				openBraces++;
			} else if (c == close) {
				openBraces--;
			}

			if (openBraces == 0) {
				break;
			}

			curPos--;
		} while (curPos >= 0);

		return (curPos >= 0) ? curPos : (-1);
	}

	public static String getEmptyAsNull(String s) {
		if(s == null || s.length() == 0){
			return null;
		}
		
		String trimed = s.trim();
		
		return trimed.length() == 0 ? null : trimed;
	}

	public static String getNullAsEmpty(String arg0) {
		if (arg0 == null) {
			return "";
		}

		return arg0;
	}

	public static String getNullAsEmpty(Object arg0) {
		if (arg0 == null) {
			return "";
		}

		return arg0.toString();
	}

	public static boolean isDelimitersBalanced(String s, char open, char close) {
		int count = 0;
		int curPos = 0;

		while (curPos < s.length()) {
			char c = s.charAt(curPos++);

			if (c == open) {
				count++;
			} else if (c == close) {
				count--;
			}
		}

		return (open == close) ? ((count & 1) == 0) : (count == 0);
	}

	public static boolean isEmpty(Object s) {
		if (s == null) {
			return true;
		}

		return getEmptyAsNull(s.toString()) == null;
	}

	public static boolean isEmpty(String s) {
		return getEmptyAsNull(s) == null;
	}

	public static boolean isNotEmpty(Object s) {
		if (s == null) {
			return false;
		}

		return getEmptyAsNull(s.toString()) != null;
	}

	public static String joinArray(Object[] source, String separator) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < source.length; i++) {
			if (buf.length() > 0) {
				buf.append(separator);
			}

			buf.append(source[i].toString());
		}

		return buf.toString();
	}

	public static void main(String[] args) throws Exception{
		System.out.println(getProximoDiaUtil("14/07/2013", "dd/MM/yyyy", -30));
	}

	public static String pad(Object value, int width) {
		return padr(value, width, '\u0020');
	}

	public static String padc(Object value, int width, char fill) {
		String valstr = (value != null) ? value.toString() : "";
		StringBuffer sbuf = new StringBuffer();

		if (valstr.length() <= width) {
			int f1;
			int f2;
			int difer;
			difer = width - valstr.length();
			f1 = difer / 2;
			f2 = difer - f1;

			for (int i = 0; i < f1; i++)
				sbuf.append(fill);

			sbuf.append(valstr);

			for (int i = 0; i < f2; i++)
				sbuf.append(fill);
		} else {
			sbuf.append(valstr.substring(0, width));
		}

		return sbuf.toString();
	}

	public static String padl(Object value, int width, char fill) {
		StringBuffer valstr = new StringBuffer((value != null) ? value.toString() : "");

		if (valstr.length() > 0) {
			int last = 0;
			//fazemos um LTRIM para evitar problemas de perda de informação valiosa por conta de chamadas encadeadas a PADL
			while (last < valstr.length() && valstr.charAt(last) == fill) {
				last++;
			}

			valstr.delete(0, last);
		}

		if (valstr.length() < width) {
			while (valstr.length() < width) {
				valstr.insert(0, fill);
			}
		} else {
			valstr.delete(width, valstr.length());
		}

		return valstr.toString();
	}

	public static String padl(Object value, int width) {
		return padl(value, width, '\u0020');
	}

	public static String padr(Object value, int width, char fill) {
		String valstr = (value != null) ? value.toString() : "";
		StringBuffer sbuf = new StringBuffer(valstr);

		if (valstr.length() <= width) {
			for (int i = 0; i < (width - valstr.length()); i++)
				sbuf.append(fill);
		} else {
			sbuf.setLength(width);
		}

		return sbuf.toString();
	}

	public static String padr(Object value, int width, String fill) {
		if (fill == null) {
			throw new IllegalArgumentException("O argumento fill não pode ser nulo");
		}

		if (fill.length() <= 0) {
			throw new IllegalArgumentException("O argumento fill deve ter comprimento maior que zero!");
		}

		String valstr = (value != null) ? value.toString() : "";
		StringBuffer sbuf = new StringBuffer(valstr);

		if (valstr.length() <= width) {
			for (int i = 0; i < (width - valstr.length()); i++)
				sbuf.append(fill.charAt(i % fill.length()));
		} else {
			sbuf.setLength(width);
		}

		return sbuf.toString();
	}

	public static String replaceAccentuatedChars(String text) {
		StringBuffer buf = new StringBuffer(text);

		replaceAccentuatedChars(buf);

		return buf.toString();
	}

	public static void replaceAccentuatedChars(StringBuffer source) {
		for (int i = 0; i < source.length(); i++) {
			char sourceChar = source.charAt(i);

			for (int j = 0; j < charsWithAccentuation.length(); j++) {
				char charWithAcc = charsWithAccentuation.charAt(j);

				if (Character.toUpperCase(sourceChar) == Character.toUpperCase(charWithAcc)) {
					char charToReplace = charsWithoutAccentuation.charAt(j);

					if (Character.isLowerCase(sourceChar)) {
						charToReplace = Character.toLowerCase(charToReplace);
					}

					source.deleteCharAt(i);
					source.insert(i, charToReplace);

					break;
				}
			}
		}
	}

	public StringUtils append(String str) {
		strbuf = null;
		strbuf = new StringBuffer(str);

		return this;
	}

	public int countChars(char c) {
		int count = 0;

		for (int i = 0; i < strbuf.toString().length(); i++)
			if (strbuf.toString().charAt(i) == c) {
				count++;
			}

		return count;
	}

	public static int countChars(String s, char c) {
		int count = 0;
		char[] chars = s.toCharArray();

		for (int i = 0; i < chars.length; i++)
			if (chars[i] == c) {
				count++;
			}

		return count;
	}

	public static int countOcorrences(String search, StringBuffer buf) {
		int startIndex = 0;
		int fromIndex = 0;
		int count = 0;

		while ((startIndex = buf.indexOf(search, fromIndex)) > -1) {
			count++;
			fromIndex = (startIndex + search.length());
		}

		return count;
	}

	@Deprecated
	public static boolean empty(String str) {
		if (str == null) {
			return true;
		}

		int len = str.length();

		if (len > 0) {
			for (int i = 0; i < len; i++) {
				if (str.charAt(i) != '\u0020') {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * recebe uma string no formato anoMes e e retorna formatado mes/ano exemplo 200712 -> 12/2007
	 */
	public static String monthYear(String yearMonth) {
		if (getEmptyAsNull(yearMonth) == null) {
			return null;
		}

		String ano = yearMonth.substring(0, 4);
		String mes = yearMonth.substring(4, yearMonth.length());

		return mes + "/" + ano;
	}

	public static void replicate(StringBuffer buf, String s, int count) {
		for (int i = 0; i < count; i++) {
			buf.append(s);
		}
	}

	public static String replicate(String s, int count) {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < count; i++) {
			buf.append(s);
		}

		return buf.toString();
	}

	public StringUtils deleteChars(char c) {
		return deleteChars(c, 0);
	}

	public StringUtils deleteChars(char c, int count) {
		return deleteChars(c, count, false);
	}

	public StringUtils deleteChars(char c, int count, boolean from) {
		int indexof;
		int deleteds = 0;

		try {
			while (((indexof = (!from) ? strbuf.toString().indexOf(c) : strbuf.toString().lastIndexOf(c)) != -1) && ((count > 0) ? (deleteds < count) : true)) {
				strbuf.deleteCharAt(indexof);
				deleteds++;
			}
		} catch (IndexOutOfBoundsException ignored) {
		}

		return this;
	}

	public StringUtils deleteExcept(int start, int end) {
		return deleteExcept(start, end, null);
	}

	public StringUtils deleteExcept(int start, int end, String except) {
		try {
			for (int i = start, next = start; i < end; i++) {
				char c = strbuf.charAt(next);

				if ((except != null) && (-1 != except.indexOf(c))) {
					next++;

					continue;
				}

				strbuf.deleteCharAt(next);
			}
		} catch (IndexOutOfBoundsException iob) {
		}

		return this;
	}

	public StringUtils format(String str, String strFormat, char blankChar) {
		int strSize;
		int insertPointStart = 0;

		if (str == null) {
			strSize = 0;
		} else {
			strSize = str.length();
		}

		strbuf.setLength(0);
		strbuf.append(strFormat);

		try {
			for (int i = 0, strIndex = 0; i < strFormat.length(); i++) {
				char c = strFormat.charAt(i);

				if (c == '#') {
					if (strIndex < strSize) {
						strbuf.setCharAt(i, str.charAt(strIndex));
						strIndex++;
					} else {
						strbuf.setCharAt(i, blankChar);
					}
				} else if (c == '|') {
					int insertPoint = strbuf.indexOf("|", insertPointStart);
					strbuf.deleteCharAt(insertPoint);
					strbuf.insert(insertPoint, str);
					insertPointStart = insertPoint + str.length();
				}
			}
		} catch (IndexOutOfBoundsException iob) {
		}

		return this;
	}

	/**
	 * Similar ao LeftStr do Delphi. Muito utilizada em modelos de impressão.
	 */
	public static String left(Object value, int count) {
		String valstr = (value != null) ? value.toString() : "";

		return valstr.length() > count ? valstr.substring(0, count) : valstr;
	}

	/**
	 * Similar ao RightStr do Delphi. Muito utilizada em modelos de impressão.
	 */
	public static String right(Object value, int count) {
		String valstr = (value != null) ? value.toString() : "";

		return valstr.substring(Math.max(valstr.length() - count, 0), valstr.length());
	}

	public StringUtils padc(int width) {
		return padc(width, '\u0020');
	}

	public StringUtils padc(int width, char fill) {
		String tmp = strbuf.toString();
		strbuf.setLength(0);
		strbuf.append(padc(tmp, width, fill));

		return this;
	}

	public static String padc(Object value, int width) {
		return padc(value, width, '\u0020');
	}

	public StringUtils padl(int width) {
		return padl(width, '\u0020');
	}

	public StringUtils padl(int width, char fill) {
		String tmp = strbuf.toString();
		strbuf.setLength(0);
		strbuf.append(padl(tmp, width, fill));

		return this;
	}

	public StringUtils padr(int width) {
		return padr(width, '\u0020');
	}

	public StringUtils padr(int width, char fill) {
		String tmp = strbuf.toString();
		strbuf.setLength(0);
		strbuf.append(padr(tmp, width, fill));

		return this;
	}

	public static String padr(Object value, int width) {
		return padr(value, width, '\u0020');
	}

	public static String removePontuacao(String s) {
		if (s == null) {
			return null;
		}

		return s.replaceAll("[.,;:?!]", "");
	}

	public static Map removeTokens(StringBuffer inputBuffer, String delimeters, String tokenId) {
		Map literals = new TreeMap();
		StringBuffer currentString = new StringBuffer();
		StringBuffer workBuffer = new StringBuffer();
		boolean isInsideString = false;
		char stringDelimeter = 0;
		char c = 0;

		// montar os arrays separando os delemitadores de entrada e saida.
		if ((delimeters.length() % 2) > 0) { // os delimitadores devem ser sempre em pares (entrada/saida)
			throw new IllegalArgumentException("removeTokens com largura de delimitador inválida.");
		}

		byte[] delimeterBytes = delimeters.getBytes();
		int delimeterSize = delimeterBytes.length / 2;
		byte[] enterDelimeter = new byte[delimeterSize];
		byte[] exitDelimeter = new byte[delimeterSize];

		for (int i = 0, arrayIdx = 0; i < delimeterSize; i++) {
			enterDelimeter[i] = delimeterBytes[arrayIdx++];
			exitDelimeter[i] = delimeterBytes[arrayIdx++];
		}

		String inDelimeter = new String(enterDelimeter);
		String outDelimeter = new String(exitDelimeter);
		int delimeterIndex = 0;
		char[] chars = inputBuffer.toString().toCharArray();
		boolean inOutEquals = false;

		for (int i = 0, size = chars.length, stringCount = 0; i < size; i++) {
			c = chars[i];

			if (isInsideString) {
				char previousChar = (i > 0) ? chars[i - 1] : 0;
				char nextChar = (i < (size - 1)) ? chars[i + 1] : 0;
				char currentChar = chars[i];

				if ((stringDelimeter == c) && (!inOutEquals || ((previousChar != stringDelimeter) && (nextChar != stringDelimeter)) || (currentChar == stringDelimeter))) {
					currentString.append(c);
					isInsideString = false;
					stringDelimeter = 0;

					String token = "${" + tokenId + stringCount + '}';
					workBuffer.append(token);
					literals.put(token, currentString.toString());
					currentString.setLength(0);
					stringCount++;
				} else {
					currentString.append(c);
				}
			} else {
				if ((delimeterIndex = inDelimeter.indexOf(c)) > -1) {
					isInsideString = true;
					stringDelimeter = outDelimeter.charAt(delimeterIndex);
					inOutEquals = inDelimeter.charAt(delimeterIndex) == outDelimeter.charAt(delimeterIndex);
					currentString.append(c);
				} else {
					workBuffer.append(c);
				}
			}
		}

		if (isInsideString) {
			throw new IllegalArgumentException("Delimitador '" + stringDelimeter + "' desbalanceado");
		}

		inputBuffer.setLength(0);
		inputBuffer.append(workBuffer.toString());

		return literals;
	}

	/**
	 Este algoritmo processa o string de entrada removendo os conteúdos que estejam entre aspas (simples ou duplas).
	 O conteúdo destas ocorrências é retornado na Map envolvido pelo caractere definido pelo argumento 'normalizedDelimiter'.
	 O algorítmo está preparado para tratar aspas escapadas com '\', ou seja, \' ou \" não são consideradas aspas, além disso o \ é removido do buffer final.

	  É similar ao método removeTokens, porém é específico para tratar aspas e suas peculiaridades.
	  A map de retorno é comptatível com o método rollbackTokens.
	 */
	public static Map removeQuotes(StringBuffer s, char normalizedDelimiter) {
		Map m = new HashMap();

		int index = 0;
		boolean isInside = false;
		char openDelimiter = 0;
		StringBuffer tokenBuf = new StringBuffer();
		StringBuffer workBuf = new StringBuffer();

		int tokenCount = 0;
		boolean isLast = false;

		while (!isLast) {
			char curChar = s.charAt(index);
			isLast = index == s.length() - 1;

			char prev = index > 0 ? s.charAt(index - 1) : 0;
			char next = isLast ? 0 : s.charAt(index + 1);

			boolean isEscape = curChar == '\\' && (next == '\'' || next == '\"');
			boolean isQuotes = curChar == '\'' || curChar == '"';

			if (isInside) {
				if (isQuotes) {
					if (prev == '\\') { //este caractere foi escapado ?
						tokenBuf.append(curChar); // então ele vai pro buffer do token
					} else if (openDelimiter == curChar) { // é o delimitador que abriu a série ? então fechamos o token
						String tokenKey = "${S" + tokenCount + "}";
						tokenBuf.append(normalizedDelimiter); //finaliza o token com o caractere definido
						m.put(tokenKey, tokenBuf.toString());
						workBuf.append(tokenKey);
						tokenCount++;
						isInside = false;
					} else {
						tokenBuf.append(curChar);
					}
				} else {
					//se o caractere atual é \ e o próximo é aspas simples ou dupla então ignoramos a barra, pois é escape.
					if (curChar != '\\' || (next != '\'' && next != '"')) {
						tokenBuf.append(curChar);
					}
				}
			} else {
				if (isQuotes && prev != '\\') {
					tokenBuf.setLength(0);
					tokenBuf.append(normalizedDelimiter); //inicializa o token com o caractere definido
					isInside = true;
					openDelimiter = curChar;
				} else if (!isEscape) { //retirammos os caracteres de escape para aspas simples e duplas
					workBuf.append(curChar);
				}
			}
			index++;
		}

		if (isInside) {
			throw new IllegalStateException("Aspas desbalanceadas: " + openDelimiter);
		}

		s.setLength(0);
		s.append(workBuf.toString());

		return m;
	}

	public StringUtils replaceChars(char toFind, char toReplace) {
		return replaceChars(toFind, toReplace, 0);
	}

	public StringUtils replaceChars(char toFind, char toReplace, int count) {
		return replaceChars(toFind, toReplace, count, false);
	}

	public StringUtils replaceChars(char toFind, char toReplace, int count, boolean from) {
		int indexof;
		int replaces = 0;

		while (((indexof = (!from) ? strbuf.toString().indexOf(toFind) : strbuf.toString().lastIndexOf(toFind)) != -1) && ((count > 0) ? (replaces < count) : true)) {
			strbuf.setCharAt(indexof, toReplace);
			replaces++;
		}

		return this;
	}

	public StringUtils replacePattern(char toReplace) {
		return replacePattern(toReplace, 0);
	}

	public StringUtils replacePattern(char toReplace, int start) {
		return replacePattern(toReplace, start, strbuf.length());
	}

	public StringUtils replacePattern(char toReplace, int start, int end) {
		return replacePattern(toReplace, start, end, null);
	}

	public StringUtils replacePattern(char toReplace, int start, int end, String exceptChars) {
		try {
			for (int i = start; i <= end; i++) {
				if ((exceptChars == null) || (-1 == exceptChars.indexOf(strbuf.charAt(i)))) {
					strbuf.setCharAt(i, toReplace);
				}
			}
		} catch (IndexOutOfBoundsException iob) {
		}

		return this;
	}

	public StringUtils replaceString(String stringToFind, String stringToReplace) {
		String str = StringUtils.replaceString(strbuf.toString(), stringToFind, stringToReplace);
		strbuf.delete(0, strbuf.length());
		strbuf.append(str);

		return this;
	}

	public static String replaceString(String source, String stringToFind, String stringToReplace) {
		StringBuffer buf = new StringBuffer(source);
		int startSearch = 0;
		int foundIndex;
		int targetSize = stringToFind.length();

		while ((foundIndex = buf.toString().indexOf(stringToFind, startSearch)) != -1) {
			buf.delete(foundIndex, foundIndex + targetSize);
			buf.insert(foundIndex, stringToReplace);
		}

		return buf.toString();
	}

	public static void replaceString(String search, String replace, StringBuffer buf) {
		replaceString(search, replace, buf, true);
	}
	
	public static void replaceString(String search, String replace, StringBuffer buf, boolean replaceAll) {
		int startIndex = 0;

		if (replace == null) {
			replace = "";
		}

		boolean replaceConstainsSearch = replace.indexOf(search) > -1;

		while ((startIndex = buf.indexOf(search, startIndex)) > -1) {
			buf.replace(startIndex, startIndex + search.length(), replace);

			if (replaceConstainsSearch) { //evita loop infinito
				startIndex += replace.length();
			}

			if (!replaceAll) {
				break;
			}
		}
	}

	public static void rollbackTokens(StringBuffer buf, Map tokens, String tokenId) {
		Matcher m = Pattern.compile("\\$\\{" + tokenId + "\\d+\\}", Pattern.DOTALL).matcher(buf);
		Collection c = new ArrayList();

		while (m.find()) {
			c.add(m.group());
		}

		for (Iterator ite = c.iterator(); ite.hasNext();) {
			String token = (String) ite.next();
			StringUtils.replaceString(token, (String) tokens.get(token), buf, false);
		}

		/*
		 * while(m.find()) { do { StringUtils.replaceString(m.group(), ( String ) tokens.get(m.group()), buf, false); }while(m.find()); //m.reset(); }
		 */
	}

	public static String rtrim(String s) {
		if (s == null) {
			return null;
		}

		int start = s.length() - 1;
		int i = start;

		for (; i >= 0; i--) {
			if (s.charAt(i) != ' ') {
				break;
			}
		}

		if (i < start) {
			s = s.substring(0, i + 1);
		}

		return s;
	}

	public static void rtrim(StringBuilder s) {
		if (s == null) {
			return;
		}

		int end = s.length() - 1;
		int i = end;

		while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
			i--;
		}

		if (i < end) {
			s.setLength(i + 1);
		}
	}
	
	public static String ltrim(String s) {
		if (s == null) {
			return null;
		}

		int start = 0;
		int end = s.length();
		int i = start;

		for (i = 0; i < end; i++) {
			if (s.charAt(i) != ' ') {
				break;
			}
		}

		if (i > start) {
			s = s.substring(i, end);
		}

		return s;
	}

	public StringUtils setPattern(int pattern) {
		for (int i = 0; i < strbuf.length();) {
			char c = strbuf.charAt(i);

			switch (pattern) {
				case ONLY_DIGITS:

					if (!Character.isDigit(c)) {
						strbuf.deleteCharAt(i);
					} else {
						i++;
					}

					break;

				case ONLY_ALPHAS:

					if (Character.isDigit(c)) {
						strbuf.deleteCharAt(i);
					} else {
						i++;
					}
			}
		}

		return this;
	}

	public StringUtils stringZero(int width) {
		String tmp = strbuf.toString();
		strbuf.setLength(0);
		strbuf.append(stringZero(tmp, width));

		return this;
	}

	public static String stringZero(Object value, int width, boolean truncOverFlow) {
		StringBuffer sbuf = new StringBuffer();
		String valstr = (value == null) ? "" : value.toString();

		if (valstr.length() > width) {
			if (truncOverFlow) {
				for (int i = 0; i < width; i++)
					sbuf.append('*');
			} else {
				return valstr;
			}
		} else if (valstr.length() <= width) {
			for (int i = 0; i < (width - valstr.length()); i++)
				sbuf.append('0');

			sbuf.append(valstr);
		}

		return sbuf.toString();
	}

	public static String stringZero(Object value, int width) {
		return stringZero(value, width, true);
	}

	public boolean empty() {
		return empty(strbuf.toString());
	}

	public static String hexStr2decStr(String s) {
		StringBuffer mainBuf = new StringBuffer();

		for (int i = 0; i < s.length(); i += 2) {
			mainBuf.append((char) Integer.parseInt(s.substring(i, i + 2), 16));
		}

		return mainBuf.toString();
	}

	public static byte[] toByteArray(String s) {
		try {
			return s.getBytes();
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para um array de bytes: " + s);
		}
	}

	/**
	 * Recebe uma string de 14 dígitos e retorna um CNPJ formatado
	 */
	public static String toCNPJ(String cnpjDigits) throws IllegalArgumentException {
		if (cnpjDigits.length() < 14) {
			throw new IllegalArgumentException("Número de CNPJ inválido: número de dígitos menor que 14");
		} else if (cnpjDigits.length() > 14) {
			throw new IllegalArgumentException("Número de CNPJ inválido: número de dígitos maior que 14");
		}

		StringFormat cnpjFormat = new StringFormat();

		cnpjFormat.setPattern("##.###.###/####-##");

		return cnpjFormat.format(cnpjDigits);
	}

	public static String toCPF(String cpfDigits) throws IllegalArgumentException {
		if (cpfDigits.length() < 11) {
			throw new IllegalArgumentException("Número de CPF inválido: número de dígitos menor que 11");
		} else if (cpfDigits.length() > 11) {
			throw new IllegalArgumentException("Número de CPF inválido: número de dígitos maior que 11");
		}

		StringFormat cnpjFormat = new StringFormat();

		cnpjFormat.setPattern("###.###.###-##");

		return cnpjFormat.format(cpfDigits);
	}

	public static char[] toCharArray(String s) {
		try {
			return s.toCharArray();
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para um array de caracteres: " + s);
		}
	}

	public static BigDecimal toCurrency(String s) {
		try {
			return new BigDecimal(new DecimalFormat("###,###,##0.00").parse(s).doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para um valor: " + s);
		}
	}

	public static Timestamp toDate(String s) {
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

			return new Timestamp(df.parse(s).getTime());
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para uma data válida: " + s);
		}
	}

	public static Timestamp toDateTime(String s) {
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			return new Timestamp(df.parse(s).getTime());
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para uma data e hora válida: " + s);
		}
	}

	public static String toHexString(byte[] b) {
		StringBuffer mainBuf = new StringBuffer(b.length * 2);
		int radix = 1 << 4;
		int mask = radix - 1;

		for (int i = 0; i < b.length; i++) {
			int value = b[i] & 0xFF;
			char[] c = { '0', '0' };
			int flipI = 2;

			do {
				c[--flipI] = digits[value & mask];
				value >>>= 4;
			} while (value != 0);

			mainBuf.append(c);
		}

		return mainBuf.toString();
	}

	public static BigDecimal toNumber(String s) {
		try {
			return new BigDecimal(s);
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para um número: " + s);
		}
	}

	public String toString() {
		return strbuf.toString();
	}

	public StringBuffer toStringBuffer() {
		return strbuf;
	}

	public static Timestamp toTime(String s) {
		try {
			DateFormat df = new SimpleDateFormat("HH:mm");

			return new Timestamp(df.parse(s).getTime());
		} catch (Exception e) {
			throw new IllegalArgumentException("Impossível converter para uma hora válida: " + s);
		}
	}

	/**
	 * Insere quebra de linhas em um StringBuffer de acordo com o tamanho máximo de linhas.
	 *
	 * @param buf
	 *                Referencia do buffer.
	 * @param lineWidth
	 *                Largura máxima de uma linha.
	 */
	public static void wrapLines(StringBuffer buf, int lineWidth) {
		int i = 0;
		int lastLineEnd = 0;

		while (i < buf.length()) {
			// o limite para a largura desta linha.
			int limit = Math.min(buf.length(), i + lineWidth);

			int endOfLine = i;

			// Procura a próxima linha ou chega ao limite.
			while ((endOfLine < limit) && (buf.charAt(endOfLine) != '\n')) {
				endOfLine++;
			}

			// terminou o buffer ?
			if (endOfLine >= buf.length()) {
				break;
			}

			// se paramos dentro de uma palavra devemos retroceder até o inicio da mesma ou do fom da ultima linha.
			while ((endOfLine > lastLineEnd) && !Character.isWhitespace(buf.charAt(endOfLine))) {
				endOfLine--;
			}

			// se estamos antes do inicio do buffer não tem jeito, a palavra vai ser quebrada.
			if (endOfLine == lastLineEnd) {
				endOfLine = limit;
			}

			// só inserimos uma nova linha se já não existir uma na posição.
			if (buf.charAt(endOfLine) != '\n') {
				buf.insert(endOfLine, '\n');
			}

			lastLineEnd = endOfLine;

			i = endOfLine + 1; // próximo bloco.

			// removermos os espaços em branco até a próxima palavra. Isso inclui caracteres de nova linha.
			while ((i < buf.length()) && Character.isWhitespace(buf.charAt(i))) {
				buf.deleteCharAt(i);
			}
		}
	}

	public static String getNomeArquivo(String caminho) {
		int lastLeftBar = caminho.lastIndexOf("\\");
		int lastRightBar = caminho.lastIndexOf("/");
		int index = lastLeftBar;

		if (lastLeftBar < lastRightBar) {
			index = lastRightBar;
		}

		if ((index > -1) && (caminho.length() > (index + 1))) {
			caminho = caminho.substring(index + 1);
		}

		return caminho;
	}

	public static String getResourceAsString(Class baseClass, String path) throws Exception {
		InputStream in = null;
		BufferedReader reader = null;

		try {
			in = baseClass.getResourceAsStream(path);

			if (in == null) {
				throw new IllegalStateException("Resource não encontrado: " + path);
			}

			reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer buf = new StringBuffer(512);

			String line = null;

			while ((line = reader.readLine()) != null) {
				buf.append(line);
				buf.append(System.getProperty("line.separator"));
			}

			return buf.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}

	public static interface TokenBuilder {
		Object buildToken(String token);
	}

	public static String removerCaracteresEspeciais(String s) throws Exception {
		//os intervalos de codePoint testados abaixo pertencem a tabela ISO-8859-1

		if (s != null) {
			StringBuffer stringConvertida = new StringBuffer(s);

			int codePoint;

			for (int i = 0; i < s.length(); i++) {
				codePoint = s.codePointAt(i);
				if ((codePoint > 13 && codePoint < 32) || (codePoint > 127 && codePoint < 160) || codePoint > 255 || codePoint == '¿') {
					stringConvertida.replace(i, i + 1, "?");
				}
			}

			return stringConvertida.toString();
		}

		return null;
	}

	public static String prefix(String str, int len) {
		return str.length() > len ? str.substring(0, len) : str;
	}

	public static String trim(String str) {
		String trimmedStr;

		if (str == null) {
			trimmedStr = "";
		} else {
			trimmedStr = str.trim();
		}

		return trimmedStr;
	}

	public static String digitoVerificador(String digitos) {
		int soma = 0;
		int multiplicador = 2;

		for (int i = digitos.length() - 1; i >= 0; i--) {
			int valorDigito = Integer.parseInt(digitos.substring(i, i + 1));

			soma += valorDigito * multiplicador++;

			if (multiplicador > 9) {
				multiplicador = 2;
			}
		}

		int resto = soma % 11;

		return String.valueOf((resto > 1) ? 11 - resto : 0);
	}

	public static String modulo10(String banda) {//Cálculo do Modulo10
		int soma = 0;
		int fator = 2;

		for (int i = StringUtils.getNullAsEmpty(banda).length() - 1; i >= 0; i--) {
			int mult = Integer.valueOf(String.valueOf(banda.charAt(i))).intValue() * fator;

			if (mult > 9) {
				String aux = String.valueOf(mult);
				mult = Integer.valueOf(String.valueOf(aux.charAt(0))).intValue() + Integer.valueOf(String.valueOf(aux.charAt(1))).intValue();
			}

			soma += mult;
			fator--;

			if (fator == 0) {
				fator = 2;
			}
		}

		soma = 10 - (soma % 10);

		return (soma >= 10) ? "0" : String.valueOf(soma);
	}

	public static String secureSubstring(String s, int start, int end) {
		if (s == null) {
			return null;
		}

		if (start >= s.length()) {
			return "";
		}

		if (end < start) {
			end = start;
		}

		return s.substring(start, Math.min(end, s.length()));
	}

	/**
	 * quando o servidor e linux o java não consegue resolver a path quando a mesma
	 * possui \\ 
	 * ex: nomeArquivo = "\\192.168.0.2\txt\REPREped.TXT"
	 * o arqMod abaixo fica: /home/mgeweb/jbossAS-405/bin/\\192.168.0.2\txt\REPREped.TXT
	 */
	public static String normalizeFileName(String nomeArquivo) {
		if (StringUtils.getEmptyAsNull(nomeArquivo) == null) {
			throw new IllegalArgumentException("Nome do arquivo inválido");
		}

		if (nomeArquivo.indexOf("\\") > -1) {
			nomeArquivo = nomeArquivo.replaceAll("\\\\", "/");
		}

		return nomeArquivo;
	}

	public static boolean safelyEquals(Object o1, Object o2) {
		return o1 == o2 || (o1 != null && o1.equals(o2));
	}

	public static String adicionaBarras(String pasta) {
		pasta = pasta.replaceAll("\\\\", "/");

		if (!pasta.startsWith("/")) {
			pasta = "/" + pasta;
		}

		if (!pasta.endsWith("/")) {
			pasta += "/";
		}

		return pasta;
	}

	public static String formataCartao(String nrocartao) throws Exception {
		if (isNotEmpty(nrocartao) && nrocartao.length() == 19) {
			String cartForm = new StringFormat().format(nrocartao, "#####.#####.####.####.#");
			StringBuffer buf = new StringBuffer(cartForm.substring(0, 17));
			buf.append("XXXX");
			buf.append(cartForm.substring(21,23));
			return  buf.toString();
		} else {
			return  "";
		}
		
	}
	

	public static String getProximoDiaUtil(String inputDate, String formatInput, int prazoDias)throws Exception{
		return getProximoDiaUtil(inputDate, formatInput, null, prazoDias);
	}
	
	public static String getProximoDiaUtil(String inputDate, String formatInput, String formatOutPut, int prazoDias) throws Exception{
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new SimpleDateFormat(formatInput).parse(inputDate));
		
		while(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		SimpleDateFormat sdf = null;
		
		if(formatOutPut != null){
			sdf = new SimpleDateFormat(formatInput);
		}else{
			sdf = new SimpleDateFormat("dd/MM/yyyy");
		}
		
		return sdf.format(cal.getTime());
		
	}
	
	

}
