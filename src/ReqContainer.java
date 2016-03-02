import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.net.httpserver.*;
import ir.ramtung.coolserver.*;
import java.lang.reflect.*;

class ReqContainer{
	public static Class loadClass(String serviceRootFolder, String type) {

		File root = new File(serviceRootFolder);
		String requestName = type + "Request";
		Class clazz = Object.class;

		// System.err.println(requestName);
		// String[] classes = root.list(new FilenameFilter() {
		// 	public boolean accept(File dir, String name) {
		// 		return name.endsWith(".class");
		// 	}
		// });
		// String targetClass = requestName.substring(0, requestName.indexOf(".class"));
		try {
			clazz = Class.forName(requestName);
			System.out.println("Class loaded: " + requestName);
			return clazz;
		} catch (Exception ex) {
			System.err.println("Unable to load class: " + requestName);
			return null;
		}


	}
	private static boolean exists(String[] classes, String targetClass){
		for (String className : classes) {
			System.err.println(className);
			if(className.equals(targetClass))
				return true;
			
		}
		return false;
	}
}