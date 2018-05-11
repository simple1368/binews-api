package ksd.binews.common;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ksd.binews.entity.LoggerInfo;
import ksd.binews.mapper.LoggerInfoMapper;
import net.sf.json.JSONObject;

public class Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
	
	private CountDownLatch latch;
	@Autowired
	private LoggerInfoMapper loggerInfoMapper;

    @Autowired
    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String message) {
    	LOGGER.info("receiveMessage : " + message);
    	JSONObject jsonObject=JSONObject.fromObject(message);
    	LoggerInfo loggerInfo=(LoggerInfo)JSONObject.toBean(jsonObject, LoggerInfo.class);
    	
    	loggerInfoMapper.insertSelective(loggerInfo);
    	
        latch.countDown();
    }
}
