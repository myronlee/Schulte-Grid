package com.diandian.CoolCo.schulte;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Utils {
	public static String readString(InputStream in) throws Exception {
		byte[] data = new byte[1024];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((length = in.read(data)) != -1) {
			bout.write(data, 0, length);
		}
		return new String(bout.toByteArray(), "UTF-8");

	}
}
