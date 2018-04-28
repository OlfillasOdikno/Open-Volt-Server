package de.olfillasodikno.rvgl.server.structures;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public interface Hashable {
	public int hash();

	public static int hash(Class<?> clazz){
		URL url = clazz.getResource(clazz.getSimpleName() + ".class");
		try {
			InputStream is = url.openStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int r;
			byte[] buffer = new byte[1024];
			while ((r = is.read(buffer)) >= 0) {
				bos.write(buffer, 0, r);
			}
			return Arrays.hashCode(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
