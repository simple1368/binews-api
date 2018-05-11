package ksd.binews.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ksd.binews.dto.BaseResponse;
import ksd.binews.entity.FileRecord;
import ksd.binews.service.FileRecordService;
import ksd.binews.utils.PublicUtil;

@RestController
public class UploadController {

	@Value("${file.path}")
	private String filePath;

	@Value("${file.api}")
	private String api;

	@Autowired
	private FileRecordService fileRecordService;

	/**
	 * 上传
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public BaseResponse imgUpload(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
		if (file.isEmpty()) {
			return BaseResponse.error("文件为空");
		}
		// 文件名
		String fileName = file.getOriginalFilename();
		// 后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		// 文件大小
		long fileSize = file.getSize();

		String newFileName = PublicUtil.getUUID() + suffixName;

		if (StringUtils.isBlank(filePath)) {
			return BaseResponse.error("上传配置有误");
		}

		File dest = new File(filePath + "/" + newFileName);

		// 检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		try {

			file.transferTo(dest);

			String uuid = PublicUtil.getUUID();
			String uri = "/download?id=" + uuid;

			FileRecord fileRecord = new FileRecord(uuid, fileName, suffixName, new BigDecimal(fileSize), uri, newFileName);

			Map<String, Object> returnData = new HashMap<String, Object>();
			returnData.put("id", uuid);
			returnData.put("url", api + uuid);
			fileRecordService.insert(fileRecord);

			return BaseResponse.success(returnData);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BaseResponse.error();
	}

	/**
	 * 下载
	 * 
	 * @param id
	 * @param response
	 */
	@RequestMapping(path = "/download", method = RequestMethod.GET)
	public void downLoad(String id, HttpServletResponse response) {
		if (StringUtils.isBlank(id)) {
			return;
		}

		FileRecord fileRecord = fileRecordService.queryFileRecord(id);
		if (fileRecord == null) {
			return;
		}
		File file = new File(filePath + "/" + fileRecord.getNewFileName());
		if (file.exists()) { // 判断文件父目录是否存在
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;fileName=" + fileRecord.getNewFileName());

			FileInputStream fis = null; // 文件输入流
			BufferedInputStream bis = null;
			byte[] b = null;
			OutputStream ops = null;

			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				b = new byte[bis.available()];
				bis.read(b);
				ops = response.getOutputStream();
				ops.write(b);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ops.flush();
				bis.close();
				fis.close();
				ops.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
