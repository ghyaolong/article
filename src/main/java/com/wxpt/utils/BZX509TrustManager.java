package com.wxpt.utils;

import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class BZX509TrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

	public static SSLSocketFactory getSSFactory() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		// 声明SSL上下文
		// SSLContext sslContext = null;
		// 实例化主机名验证接口
		// HostnameVerifier hnv = new MyHostnameVerifier();
		TrustManager[] tm = { new BZX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// sslContext = getSSLContext(password, keyStorePath, trustStorePath);
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		return ssf;
	}

	/**
	 * 获得SSLSocketFactory.
	 * 
	 * @param password
	 *            密码
	 * @param keyStorePath
	 *            密钥库路径
	 * @param trustStorePath
	 *            信任库路径
	 * @return SSLSocketFactory
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
    private static SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath) throws Exception {
		// 实例化密钥库
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		// 获得密钥库
		KeyStore keyStore = getKeyStore(password, keyStorePath);
		// 初始化密钥工厂
		keyManagerFactory.init(keyStore, password.toCharArray());

		// 实例化信任库
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		// 获得信任库
		KeyStore trustStore = getKeyStore(password, trustStorePath);
		// 初始化信任库
		trustManagerFactory.init(trustStore);
		// 实例化SSL上下文
		SSLContext ctx = SSLContext.getInstance("TLS");
		// 初始化SSL上下文
		ctx.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
		// 获得SSLSocketFactory
		return ctx;
	}

	/**
	 * 获得KeyStore.
	 * 
	 * @param keyStorePath
	 *            密钥库路径
	 * @param password
	 *            密码
	 * @return 密钥库
	 * @throws Exception
	 */
	public static KeyStore getKeyStore(String password, String keyStorePath) throws Exception {
		// 实例化密钥库
		KeyStore ks = KeyStore.getInstance("JKS");
		// 获得密钥库文件流
		FileInputStream is = new FileInputStream(keyStorePath);
		// 加载密钥库
		ks.load(is, password.toCharArray());
		// 关闭密钥库文件流
		is.close();
		return ks;
	}

}
