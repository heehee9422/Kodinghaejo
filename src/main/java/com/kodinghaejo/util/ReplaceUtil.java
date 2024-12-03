package com.kodinghaejo.util;

public class ReplaceUtil {

	public String escapeHtml(String str) {
		if (str == null || str.equals("")) return "";

		return str.replace("&", "&amp;")
							.replace("<", "&lt;")
							.replace(">", "&gt;")
							.replace("\"", "&quot;")
							.replace("'", "&apos;")
							.replace("\t", "\\t")
							.replace("\n", "\\n");
	}
	
	public String unescapeHtml(String str) {
		if (str == null || str.equals("")) return "";

		return str.replace("&amp;", "&")
							.replace("&lt;", "<")
							.replace("&gt;", ">")
							.replace("&quot;", "\"")
							.replace("&apos;", "'")
							.replace("\\t", "\t")
							.replace("\\n", "\n");
	}
	
}