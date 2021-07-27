package first.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import first.common.common.CommandMap;
import first.common.dao.CommonDAO;
import first.common.util.ExcelRead;
import first.common.util.ExcelReadOption;
import first.common.vo.CreateUserVo;
import first.sample.dao.SampleDAO;
import first.sample.service.SampleService;

@Service("commonService")
public class CommonServiceImpl implements CommonService{
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="commonDAO")
	private CommonDAO commonDAO;
	
	@Resource(name="sampleDAO")
	private SampleDAO sampleDAO;
	
	

	@Override
	public Map<String, Object> selectFileInfo(Map<String, Object> map) throws Exception {
		return commonDAO.selectFileInfo(map);
	}
	
	@Override
	public HSSFWorkbook boardlistExcelDownload() throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        HSSFSheet sheet = workbook.createSheet("엑셀시트명");
        
        HSSFRow row = null;
        
        HSSFCell cell = null;
        
        //게시판 객체 저장용  생성
        
	    List<Object> resultBoardList = sampleDAO.selectALLBoardList();
        
//	    log.debug("resultBoardList:  "+resultBoardList);
        row = sheet.createRow(0);
        String[] headerKey = {"인덱스", "생성일자", "제목"};
//        
        for(int i=0; i<headerKey.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headerKey[i]);
        }
        // 문자열 파싱 및 엑셀 형식으로 데이터 삽입.
        for(int i=0; i<resultBoardList.size(); i++) {
        	row = sheet.createRow(i + 1);
        	
        	String tmp = resultBoardList.get(i).toString();
        	String[] tmp2 = tmp.split(",");
        	// idx 문자열 파싱
        	String[] idx_str = tmp2[0].split("=");
        	String idx = idx_str[1];
        	// 엑셀 idx 데이터 삽입
        	cell = row.createCell(0);
        	cell.setCellValue(idx);
        	// 생성일자 문자열 파싱
        	String[] crea_str = tmp2[3].split("=");
        	String crea = crea_str[1];
        	// 엑셀 생성일자 데이터 삽입
        	cell = row.createCell(1);
        	cell.setCellValue(crea);
        	// 제목 문자열 파싱.
        	String[] title_str = tmp2[4].split("=");
        	String title = title_str[1];
        	// 엑셀 제목 데이터 삽입
        	cell = row.createCell(2);
        	cell.setCellValue(title);
        	log.debug("인덱스:  "+idx+"   제목:   "+title+"   생성일자:   "+crea);
        }
        // 엑셀 데이터 삽입 형식
//        for(int i=0; i<resultBoardList.size(); i++) {
//            row = sheet.createRow(i + 1);
//            StbcsTaskHstVO vo = list.get(i);
//            
//            cell = row.createCell(0);
//            cell.setCellValue(vo.getEx1());
//            
//            cell = row.createCell(1);
//            cell.setCellValue(vo.getEx2());
//            
//            cell = row.createCell(2);
//            cell.setCellValue(vo.getEx3());
//            
//            cell = row.createCell(3);
//            cell.setCellValue(vo.getEx4());
// 
//        }
        
        return workbook;
	}
//	
	@Override
    public void boardlistExcelUpload(File destFile) throws Exception{
        ExcelReadOption excelReadOption = new ExcelReadOption();
        excelReadOption.setFilePath(destFile.getAbsolutePath());
        excelReadOption.setOutputColumns("A","B","C","D","E","F","G");
        excelReadOption.setStartRow(2);
        
        
        List<Map<String, String>>excelContent = ExcelRead.read(excelReadOption);
        
        HashMap<String,Object> dbContent = new HashMap<String,Object>();
        for(Map<String, String> article: excelContent){
        	//TB_BOARD IDX
            log.debug(article.get("A"));
        	dbContent.put("IDX",article.get("A"));
        	//TB_BOARD PARENT_IDX
        	dbContent.put("PARENT_IDX", null);
        	//TB_BOARD TITLE
            log.debug(article.get("B"));
            dbContent.put("TITLE",article.get("B"));
        	//TB_BOARD CONTENTS
            log.debug(article.get("C"));
            dbContent.put("CONTENTS",article.get("C"));
            log.debug(article.get("D"));
            dbContent.put("HIT_CNT",article.get("D"));
            log.debug(article.get("E"));
            dbContent.put("DEL_GB",article.get("E"));
            log.debug(article.get("F"));
            dbContent.put("CREA_DTM",article.get("F"));
            log.debug(article.get("G"));
            dbContent.put("CREA_ID",article.get("G"));
            sampleDAO.insertBoard(dbContent);
        }
        
	}


	//Service
//	public List<CreateUserVo> uploadExcelFile(MultipartFile excelFile){
//		List<CreateUserVo> list = new ArrayList<CreateUserVo>();
//		try {
//			//OPPackage를 이용하여 excel파일을 읽어들인다.
//			OPCPackage opcPackage = OPCPackage.open(excelFile.getInputStream());
//			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
//			
//			// 첫번째 시트 불러오기
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			
//			for(int i=1; i<sheet.getLastRowNum() + 1; i++) {
//				CreateUserVo createUserVo = new CreateUserVo();
//				XSSFRow row = sheet.getRow(i);
//				
//				// 행이 존재하지 않으면 패스
//				if(null == row) {
//					continue;
//				}
//				
//				// 행의 1번째 열(아이디)
//				XSSFCell cell = row.getCell(0);
//				
//				if(null != cell)
//					createUserVo.setUser_id(cell.getStringCellValue());
//				// 행의 2번째 열(이름)
//				cell = row.getCell(1);
//				if(null != cell)
//					createUserVo.setUser_name(cell.getStringCellValue());
//				// 행의 3번째 열(사용 만료일)
//				cell = row.getCell(2);
//				if(null != cell)
//					createUserVo.setExpire(cell.getStringCellValue());
//				// 행의 4번째 열(부서명)
//				cell = row.getCell(3);
//				if(null != cell)
//					createUserVo.setDepthname(cell.getStringCellValue());
//				// 행의 5번째 열(휴대전화 번호)
//				cell = row.getCell(4);
//				if(null != cell)
//					createUserVo.setPhone(cell.getStringCellValue());
//				// 행의 6번째 열(이메일)
//				cell = row.getCell(5);
//				if(null != cell)
//					createUserVo.setEmail(cell.getStringCellValue());
//				// 행의 7번째 열(설명)
//				cell = row.getCell(6);
//				if(null != cell)
//					createUserVo.setDesc(cell.getStringCellValue());
//				// 행의 8번째 열(사무실 코드)
//				cell = row.getCell(7);
//				if(null != cell)
//					createUserVo.setOffice_code(cell.getStringCellValue());
//				
//				list.add(createUserVo);
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}
	
}
