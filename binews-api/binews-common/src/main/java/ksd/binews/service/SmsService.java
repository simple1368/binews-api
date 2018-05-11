package ksd.binews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ksd.binews.utils.PublicUtil;

@Service
public class SmsService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${sms.api.url}")
	private String url;
	
	@Value("${sms.api.uid}")
	private String uid;
	
	@Value("${sms.api.psw}")
	private String psw;

	public String sendMessage(String phone, String content) {
		if (content.getBytes().length > 200) {
			return "短信内容过长，请重新编辑";
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> param = new LinkedMultiValueMap();
		param.add("cmd", "send");
		param.add("uid", uid);
		param.add("psw", psw);
		param.add("mobiles", phone);
		param.add("msgid", getMsgId());
		param.add("msg", content);
		
		HttpEntity<MultiValueMap<String,String>> entity = new HttpEntity<MultiValueMap<String,String>>(param, headers);
		
		String result = restTemplate.postForObject(url, entity, String.class);
		
		return result;
	}
	
	private String getMsgId(){
		String start = "sms";
		int random = PublicUtil.getRandom();
		
		return start + random + PublicUtil.getCurrentTimestamp().getTime();
	}
	
}
