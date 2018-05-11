package ksd.binews.utils;

import java.net.URLEncoder;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HuobiApiUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HuobiApiUtil.class);
	
	private static final String baseUrl = "https://api.huobipro.com";
	
	private static final String proxyUrl = "127.0.0.1";
	
	private static final Integer proxyPort = 1021;

	private static String accessKey="4db5219f-2b9daea9-2c8be7e3-e7611";
	
	private static String secretKey = "7c748e2f-0e5b215b-4deb5829-37eba";
	
	/**
	 * 签名
	 */
	public static String signature(String method, String baseUrl, String url,String timestamp, 
			Map<String, Object> param) throws Exception {
		
		Map<String, Object> treeMap = new TreeMap<String, Object>(
			new Comparator<String>() {
				public int compare(String obj1, String obj2) {
					// 降序排序
					return obj1.compareTo(obj2);
				}
		});
		if (param != null && !param.isEmpty()) {
			treeMap.putAll(param);
		}
		treeMap.put("AccessKeyId", accessKey);
		treeMap.put("SignatureMethod", "HmacSHA256");
		treeMap.put("SignatureVersion", "2");
		treeMap.put("Timestamp", URLEncoder.encode(timestamp, "UTF-8"));
		
		int i = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(method.toUpperCase()).append("\n")
			.append(baseUrl).append("\n")
			.append(url).append("\n");
		
		Set<String> keySet = treeMap.keySet();
        Iterator<String> iter = keySet.iterator();
        
        while (iter.hasNext()) {
            String key = iter.next();
            if (i > 0) {
				sb.append("&");
			}
			sb.append(key).append("=").append(treeMap.get(key));
			i++;
        }
        return  URLEncoder.encode(Base64.encode(HmacSHA256Signer.sign(sb.toString(),secretKey).getBytes("UTF-8")),"UTF-8");
	}
	
	/**
	 * 
	 * @param method
	 * @param url
	 * @param headerMap
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static String sendRequest(String method, String url, Map<String, Object> headerMap, Map<String, Object> paramMap) throws Exception{
		String timestamp = Instant.now().toString();
		timestamp = timestamp.substring(0, timestamp.lastIndexOf("."));
		if (method.toLowerCase().equals("get")) {
			Map<String, Object> requestParam = new HashMap<String, Object>();
			if (paramMap != null && !paramMap.isEmpty()) {
				requestParam.putAll(paramMap);
			}
			//requestParam.put("AccessKeyId", accessKey);
			//requestParam.put("SignatureMethod", "HmacSHA256");
			//requestParam.put("SignatureVersion", 2);
			requestParam.put("Timestamp", URLEncoder.encode(timestamp,"UTF-8"));
			//requestParam.put("Signature", 
			//		HuobiApiUtil.signature(method,"api.huobi.pro","/v1/common/symbols", timestamp, paramMap));
			byte[] b = HttpUtil.doGet(baseUrl + url, headerMap, proxyUrl, proxyPort);
			return new String(b);
		}
		if (method.toLowerCase().equals("post")) {
			
		}
		return null;
	}
	
	@Test
	public static void main(String[] args) throws Exception {
		String response = sendRequest("GET","/v1/common/symbols", null, null);
		System.out.println(response);
	}
}
