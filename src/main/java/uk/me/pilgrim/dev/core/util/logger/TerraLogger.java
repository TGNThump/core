package uk.me.pilgrim.dev.core.util.logger;

import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.PomData;
import uk.me.pilgrim.dev.core.util.text.Text;

public class TerraLogger {
	
	public static String prefix=Text.upperCaseFirst(PomData.NAME);
	
	public static void info(Object msg) {
		info("" + msg, "");
	}
	
	public static void debug(Object msg) {
		debug("" + msg, "");
	}
	
	public static void warn(Object msg) {
		warn("" + msg, "");
	}
	
	public static void error(Object msg) {
		error("" + msg, "");
	}
	
	public static void clear(Object msg) {
		clear("" + msg, "");
	}
	
	public static void info(String string) {
		info(string, "");
	}
	
	public static void debug(String string) {
		debug(string, "");
	}
	
	public static void warn(String string) {
		warn(string, "");
	}
	
	public static void error(String string) {
		error(string, "");
	}
	
	public static void clear(String string){
		clear(string, "");
	}
	
	public static void info(String msg, Object... args) {
		System.out.println(Text.of("[<l>" + prefix + "<r>][<l>INFO<r>] " + Text.of(msg, args) + "<r>"));
	}
	
	public static void debug(String msg, Object... args) {
		if (Core.isDevMode())
			System.out.println(Text.of("[<l>" + prefix + "<r>][<l>DEBUG<r>] " + Text.of(msg, args) + "<r>"));
	}
	
	public static void warn(String msg, Object... args) {
		System.out.println(Text.of("[<l>" + prefix + "<r>][<l>WARN<r>] " + Text.of(msg, args) + "<r>"));
	}
	
	public static void error(String msg, Object... args) {
		System.out.println(Text.of("[<l>" + prefix + "<r>][<l>ERROR<r>] " + Text.of(msg, args) + "<r>"));
	}
	
	public static void clear(String msg, Object... args) {
		System.out.println(Text.of(Text.of(msg, args) + "<r>"));
	}
	
	public static void blank() {
		System.out.println(" ");
	}
	
	public static class tools {
		
		public static String repeat(String str, int times) {
			StringBuilder ret = new StringBuilder();
			for (int i = 0; i < times; i++) {
				ret.append(str);
			}
			return ret.toString();
		}
	}
	
}