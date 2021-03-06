package first.common.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import first.common.common.CommandMap;
import first.common.service.CommonService;
import first.common.vo.CreateUserVo;
import first.sample.service.SampleService;

@Controller
public class CommonController {
	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "commonService")
	private CommonService commonService;
	private SampleService sampleService;

	@RequestMapping(value = "/common/downloadFile.do")
	public void downloadFile(CommandMap commandMap, HttpServletResponse response) throws Exception {
		Map<String, Object> map = commonService.selectFileInfo(commandMap.getMap());
		String storedFileName = (String) map.get("STORED_FILE_NAME");
		String originalFileName = (String) map.get("ORIGINAL_FILE_NAME");

		byte fileByte[] = FileUtils.readFileToByteArray(new File("C:\\dev\\file\\" + storedFileName));

		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		response.setHeader("Content-Disposition",
				"attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.getOutputStream().write(fileByte);

		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	//Servlet??? HttpServletResponse????????? Content Type, ????????????, ?????? ??????????????? ????????? ?????????
	@RequestMapping(value="/common/exceldownload.do")
    public void excelDownload( HttpServletResponse response ) throws Exception {
	      OutputStream out = null;
	      
	      try {
	          HSSFWorkbook workbook = commonService.boardlistExcelDownload();
	          
	          response.reset();
	          response.setHeader("Content-Disposition", "attachment;filename=stbcs_history.xls");
	          response.setContentType("application/vnd.ms-excel");
	          out = new BufferedOutputStream(response.getOutputStream());
	          
	          workbook.write(out);
	          out.flush();
	          
	      } catch (Exception e) {
	          log.error("exception during downloading excel file : {}", e);
	      } finally {
	          if(out != null) out.close();
	      }    
	 }
	/*
	 * ???????????? @ResponseBody ??? ?????????????????? ?????? ????????? ??????????????? ???????????? ?????? View ??? ????????? 
	 * ???????????? ?????? HTTP Response Body ??? ?????? ???????????? ?????????.
	 * ?????? ???????????? ?????? ???????????? ????????? ????????? ?????? MessageConverter ?????? ????????? ????????? ??? ???????????? ?????????.
	 */
	@ResponseBody
    @RequestMapping(value = "/common/excelupload.do", method = RequestMethod.POST)
    public ModelAndView excelUploadAjax(MultipartHttpServletRequest request)  throws Exception{
        MultipartFile excelFile =request.getFile("excelFile");
        log.debug("?????? ?????? ????????? ????????????");
        if(excelFile==null || excelFile.isEmpty()){
            throw new RuntimeException("??????????????? ?????? ??? ?????????.");
        }
        
        File destFile = new File("C:\\Excel_Test\\"+excelFile.getOriginalFilename());
        log.debug("?????? ?????? ?????? : " + destFile);
        try{
            excelFile.transferTo(destFile);
        }catch(IllegalStateException | IOException e){
            throw new RuntimeException(e.getMessage(),e);
        }
        
        commonService.boardlistExcelUpload(destFile);
        
        //FileUtils.delete(destFile.getAbsolutePath());
        
        ModelAndView view = new ModelAndView();
        view.setViewName("");
        return view;
    }

	
	//Servlet??? HttpServletResponse????????? Content Type, ????????????, ?????? ??????????????? ????????? ?????????
//	@RequestMapping(value="/common/exceldownload.do")
//    public void excelDownload( HttpServletResponse response ) throws Exception {
//		// ?????? 97~2003?????? ??????
//	//      Workbook wb = new HSSFWorkbook();
//		 // ?????? ?????????????????? ??? ???????????? ?????? ???????????? ?????????????????? ???????????? ?????? ????????????. (?????? 2007 ?????? ??????.)
//		 // ?????? ???????????? XSSF ????????? ??????
//	      Workbook wb = new XSSFWorkbook();
//	      Sheet sheet = wb.createSheet("????????? ??????");
//	      Row row = null;
//	      Cell cell = null;
//	      int rowNum = 0;
//	
//	      // Header
//	      row = sheet.createRow(rowNum++);
//	      cell = row.createCell(0);
//	      cell.setCellValue("??????");
//	      cell = row.createCell(1);
//	      cell.setCellValue("??????");
//	      cell = row.createCell(2);
//	      cell.setCellValue("??????");
//	
//	      // Body
//	      for (int i=0; i<3; i++) {
//	          row = sheet.createRow(rowNum++);
//	          cell = row.createCell(0);
//	          cell.setCellValue(i);
//	          cell = row.createCell(1);
//	          cell.setCellValue(i+"_name");
//	          cell = row.createCell(2);
//	          cell.setCellValue(i+"_title");
//	      }
//	
//	      // ????????? ????????? ????????? ??????
//	      response.setContentType("ms-vnd/excel");
//	//      response.setHeader("Content-Disposition", "attachment;filename=example.xls");
//	      response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");
//	
//	      // Excel File Output
//	      wb.write(response.getOutputStream());
//	      wb.close();
//	 }


//	@RequestMapping(value="/common/exceldownload.do")
//    public void excelDownload( HttpServletRequest request ,HttpServletResponse response ,HttpSession session, CreateUserVo param) throws Exception {
//        
//        OutputStream out = null;
//        
//        try {
//            HSSFWorkbook workbook = commonService.listExcelDownload(param);
//            
//            response.reset();
//            response.setHeader("Content-Disposition", "attachment;filename=stbcs_history.xls");
//            response.setContentType("application/vnd.ms-excel");
//            out = new BufferedOutputStream(response.getOutputStream());
//            
//            workbook.write(out);
//            out.flush();
//            
//        } catch (Exception e) {
//            log.error("exception during downloading excel file : {}", e);
//        } finally {
//            if(out != null) out.close();
//        }    
//    }
	
	
//	@RequestMapping(value = "/common/excelUpload.do")
//	public void uploadExcelFile(MultipartHttpServletRequest request, Model model, HttpServletResponse response) throws Exception {
//		log.debug("########### /admin/uploadExcelFile.do Start!!! ###############");
//		ModelAndView mv = new ModelAndView("redirect:/sample/excelUpload");
//		
//		
//		response.setCharacterEncoding("UTF-8");
//		try {
//			PrintWriter printWriter = response.getWriter();
//			JSONObject jsonObject = new JSONObject();
//			
//			MultipartFile file = null;
//		Iterator<String> iterator = request.getFileNames();
//		if(iterator.hasNext()) {
//			file = request.getFile(iterator.next());
//		}
//		List<CreateUserVo> list = commonService.uploadExcelFile(file);
//		if(list != null) {
//						 jsonObject.put("rs", "0000");
//		}else {
//				jsonObject.put("rs", "9999");
//		}
//		printWriter.print(list.toString());
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		log.debug("######## /admin/uploadExcelFile.do End!! #################");
//		
//		return mv;
//	}
}
