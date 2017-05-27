package com.wxpt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FileUploadUtil {

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	/*
	 * public static void main(String... args) throws Exception { String path =
	 * "D:\\1.png"; InputStream in = null; byte[] data = null; // 读取图片字节数组 try {
	 * in = new FileInputStream(path); data = new byte[in.available()];
	 * in.read(data); in.close(); } catch (IOException e) { e.printStackTrace();
	 * } // 对字节数组Base64编码 BASE64Encoder encoder = new BASE64Encoder(); String
	 * imgValue = encoder.encode(data); BufferedInputStream bin = new
	 * BufferedInputStream(new FileInputStream("D:\\1.png"));
	 * ByteArrayOutputStream out = new ByteArrayOutputStream(1024); byte[] temp
	 * = new byte[1024]; int size = 0; while ((size = bin.read(temp)) != -1) {
	 * out.write(temp, 0, size); } byte[] contents = out.toByteArray();
	 * bin.close(); imgValue = encoder.encode(contents); String url =
	 * "http://images.syhbuy.cn"; url = url +
	 * "/index.php?act=remote&op=uploadImg&dir=/app/yueyang/"; Map<String,
	 * Object> paramsMap = new HashMap<String, Object>(); paramsMap.put("img",
	 * ""); paramsMap.put("name", "1"); // paramsMap.put("width", "250"); //
	 * paramsMap.put("height", "250"); // paramsMap.put("ext", "后缀名称数组"); //
	 * paramsMap.put("new name", "test"); HttpResponse httpRespons = null;
	 * httpRespons = HttpRequester.sendPost(url, paramsMap); if (httpRespons !=
	 * null) { String content = httpRespons.getContent();
	 * System.out.println(content); }
	 */

	public static void main(String[] args) throws Exception {
		String strImg = GetImageStr();
		System.out.println(strImg);

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("image", "data:image/jpeg;base64," + strImg);
		// paramsMap.put("name", "1.jpg");
		// paramsMap.put("width", "250");
		// paramsMap.put("height", "250");
		// paramsMap.put("ext", "后缀名称数组");
		// paramsMap.put("new name", "test");

		String url = "http://api.syhbuy.cn/api/index.php?module=upload&method=base_file";

		HttpResponse httpRespons = null;
		httpRespons = HttpRequester.sendPost(url, paramsMap);
		if (httpRespons != null) {
			String content = httpRespons.getContent();
			System.out.println(content);
		}

		// GenerateImage(strImg);
	}

	// 图片转化成base64字符串
	public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		String imgFile = "D:\\1.jpg";// 待处理的图片
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过的字节数组字符串
		return encoder.encode(data);
	}

	public static String GetImageStr2() {
		String imgFile = "D:\\1.png";// 待处理的图片
		InputStreamReader isr = null;
		String content = "";
		try {
			isr = new InputStreamReader(new FileInputStream(new File(imgFile)), "GBK");
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null) {
				content = content + temp;
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	// base64字符串转化成图片
	public static boolean GenerateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
		if (imgStr == null) // 图像数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			String imgFilePath = "d://222.jpg";// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

}
