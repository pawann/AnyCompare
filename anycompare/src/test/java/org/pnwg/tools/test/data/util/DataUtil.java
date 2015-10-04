package org.pnwg.tools.test.data.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DataUtil {

	private final static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public static <T> T readData(String json, Class<T> clazz) throws IOException {
		return mapper.readValue(json, clazz);
	}

	public static <T> T readDataFromFile(String cpFilePath, Class<T> clazz) throws IOException {
		ClassPathResource resource = new ClassPathResource(cpFilePath, DataUtil.class.getClassLoader());
		String content = IOUtils.toString(resource.getInputStream());
		return mapper.readValue(content, clazz);
	}

	public static void writeData(OutputStream os, Object obj) throws IOException {
		mapper.writeValue(os, obj);
	}

	public static void writeDataToFile(String cpFilePath, Object obj) throws IOException {
		ClassPathResource resource = new ClassPathResource(cpFilePath, DataUtil.class.getClassLoader());
		File file = resource.getFile();
		FileOutputStream fop = new FileOutputStream(file);
		mapper.writeValue(fop, obj);
	}

}
