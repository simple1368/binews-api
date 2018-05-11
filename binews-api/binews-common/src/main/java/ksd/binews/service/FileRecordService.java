package ksd.binews.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ksd.binews.entity.FileRecord;
import ksd.binews.mapper.FileRecordMapper;
import ksd.binews.utils.PublicUtil;

@Service
public class FileRecordService {

	@Autowired
	private FileRecordMapper fileRecordMapper;
	
	
	public int insert(FileRecord fileRecord) {
		
		fileRecord.setCreateTime(PublicUtil.getCurrentTimestamp());
		return fileRecordMapper.insertSelective(fileRecord);
	}
	
	public FileRecord queryFileRecord(String id) {
		return fileRecordMapper.selectByPrimaryKey(id);
	}
}
