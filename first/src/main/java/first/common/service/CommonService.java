package first.common.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import first.common.vo.CreateUserVo;

public interface CommonService {

	Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception;
	
	//Service
	public HSSFWorkbook boardlistExcelDownload() throws Exception;
	
	public void boardlistExcelUpload(File destFile) throws Exception;
//	public List<CreateUserVo> uploadExcelFile(MultipartFile excelFile);
	
	
	
}
