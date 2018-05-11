package ksd.binews.api;

import java.net.URLEncoder;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ksd.binews.utils.HttpUtil;

public class HuobiApi {
	
	private static final String baseUrl = "https://api.huobipro.com";
	
	private static final String proxyUrl = "127.0.0.1";
	
	private static final Integer proxyPort = 1021;

	/**
	 * 参数处理
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private static String parseParam(Map<String, Object> params) throws Exception{
		Map<String, Object> treeMap = new TreeMap<String, Object>(
			new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
		});
		if (params != null && !params.isEmpty()) {
			treeMap.putAll(params);
		}
		
		String timestamp = Instant.now().toString();
		timestamp = timestamp.substring(0, timestamp.lastIndexOf("."));
		
		treeMap.put("Timestamp", URLEncoder.encode(timestamp,"UTF-8"));
		
		int i = 0;
		StringBuilder sb = new StringBuilder();
		
		Set<String> keySet = treeMap.keySet();
        Iterator<String> iter = keySet.iterator();
        
        while (iter.hasNext()) {
            String key = iter.next();
            if (i == 0) {
            	sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(key).append("=").append(treeMap.get(key));
			i++;
        }
        return sb.toString();
	}
	
	/**
	 * 发送GET请求
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static String sendGet(String url, Map<String, Object> paramMap) throws Exception{
		
		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("Content-Type","application/x-www-form-urlencoded");
		headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.117 Safari/537.36");
		headerMap.put("Accept-Language", "zh-cn");
		
		String param = parseParam(paramMap);
		
		byte[] b = HttpUtil.doGet(baseUrl + url + param, headerMap, proxyUrl, proxyPort);
		return new String(b);
	}
}
