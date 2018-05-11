package ksd.binews.mapper;

import ksd.binews.entity.FileRecord;

public interface FileRecordMapper {
    
	int insertSelective(FileRecord fileRecord);
	
	FileRecord selectByPrimaryKey(String id);
}