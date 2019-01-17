package com.hpe.cmwa.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class VariantTool {

	public enum state {
		TEXT, DOLLAR_FOUND, POUND_FOUND, LEFT_BRACKET_FOUND, VARIABLE_NAME_FIRST_CHAR, VARIBALE_NAME_FOLLOW_CHAR, RIGHT_BRACKET_FOUND, EQUAL_FOUND, VALUE_FOUND, SEMICOLON_FOUND, ERROR, SPLITER_FOUND, AMBIT_FOUND, CONTENT_FOUND, CHANNEL_FOUND, CONTENT, START
	};

	public static String eval(String expr,  Map<String, Object> props,  Map<String, Object> context) throws UnsupportedEncodingException {

		StringBuffer varName = new StringBuffer();
		StringBuffer result = new StringBuffer();
		state lastVarType = state.START;
		state nowState = state.TEXT;
		int i = 0;
		while (i < expr.length()) {
			char ch = expr.charAt(i++);
			nowState = getNextState(ch, nowState);
			switch (nowState) {
				case TEXT:
					lastVarType = state.START;
					if (varName.length() > 0) {
						result.append(varName);
						varName.delete(0, result.length());
					}
					result.append(ch);
					break;
				case DOLLAR_FOUND:
					lastVarType = state.DOLLAR_FOUND;
					if (varName.length() > 0) {
						result.append(varName);
						varName.delete(0, result.length());
					}
					varName.append(ch);
					break;
				case POUND_FOUND:
					lastVarType = state.POUND_FOUND;
					if (varName.length() > 0) {
						result.append(varName);
						varName.delete(0, result.length());
					}
					varName.append(ch);
					break;
				case LEFT_BRACKET_FOUND:
					if (varName.length() > 1) {
						result.append(varName);
						varName.delete(0, result.length());
					}
					varName.append(ch);
					break;
				case VARIABLE_NAME_FIRST_CHAR:
					varName.append(ch);
					break;
				case VARIBALE_NAME_FOLLOW_CHAR:
					varName.append(ch);
					break;
				case RIGHT_BRACKET_FOUND:
					varName.append(ch);
					String value = getValue(varName.substring(2, varName.length() - 1), props, context);
					if (value == null) {
						result.append(varName);
						varName.delete(0, varName.length());
					} else {
						if (lastVarType == state.POUND_FOUND){
							value = new sun.misc.BASE64Encoder().encode(value.getBytes("GB2312"));
							value = value.replaceAll("\n", "");
							value = value.replaceAll("\r", "");
							value =URLEncoder.encode(value,"GB2312");
						}
						result.append(value);
						varName.delete(0, varName.length());
					}
					break;
			}
		}
		if (i == expr.length() && varName.length() > 0) {
			result.append(varName);
		}
		return result.toString();
	}

	private static String getValue(String expr,  Map<String, Object> props,  Map<String, Object> context) {

		if (StringUtils.isBlank(expr))
			return null;
		if (props == null && context == null) {
			return null;
		}
		if (props != null && props.containsKey(expr)) {
			return props.get(expr)==null?"":props.get(expr).toString();
		}
		if (context != null && context.containsKey(expr)) {
			return context.get(expr)==null?"":context.get(expr).toString();
		}
		return null;

	}

	private static state getNextState(char ch, state previousState) {

		state currentState = previousState;
		switch (previousState) {
			case TEXT:
				if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
			case DOLLAR_FOUND:
				if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else if (ch == '(') {
					currentState = state.LEFT_BRACKET_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
			case POUND_FOUND:
				if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else if (ch == '(') {
					currentState = state.LEFT_BRACKET_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
			case LEFT_BRACKET_FOUND:
				if (isVariableNameFirstChar(ch)) {
					currentState = state.VARIABLE_NAME_FIRST_CHAR;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
			case VARIABLE_NAME_FIRST_CHAR:
				if (isVariableNameFollowed(ch)) {
					currentState = state.VARIBALE_NAME_FOLLOW_CHAR;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
			case VARIBALE_NAME_FOLLOW_CHAR:
				if (isVariableNameFollowed(ch)) {
					currentState = state.VARIBALE_NAME_FOLLOW_CHAR;
				} else if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else if (ch == ')') {
					currentState = state.RIGHT_BRACKET_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
			case RIGHT_BRACKET_FOUND:
				if (ch == '$') {
					currentState = state.DOLLAR_FOUND;
				} else if (ch == '#') {
					currentState = state.POUND_FOUND;
				} else {
					currentState = state.TEXT;
				}
				break;
		}
		return currentState;
	}


	private static boolean isVariableNameFirstChar(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_';
	}

	private static boolean isVariableNameFollowed(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '.' || ch == ':' || (ch >= '0' && ch <= '9');
	}

}