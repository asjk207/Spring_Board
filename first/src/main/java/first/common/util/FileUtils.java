package first.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

//@Component 어노테이션을 이용하여 이 객체의 관리를 스프링이 담당하도록 할 계획이다.

@Component("fileUtils")
public class FileUtils {
	//	그 다음 18번째 줄 private static final String filePath = "C:\\dev\\file\\; 부분은 파일이 저장될 위치를 선언해주었다. 	
	// 지금은 로컬에서만 하기 때문에 저장위치를 소스에 명시하였는데, 나중에 이 부분은 properties를 이용하여 로컬과 서버의 저장위치를 따로따로 관리할 예정이다.
	private static final String filePath = "C:\\dev\\file\\";

	public List<Map<String, Object>> parseInsertFileInfo(Map<String, Object> map, HttpServletRequest request)
			throws Exception {
		
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
		MultipartFile multipartFile = null;
		
		String originalFileName = null;
		String originalFileExtension = null;
		String storedFileName = null;
		/*
		 * 그 다음으로 29번째 줄 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(); 부분은 클라이언트에서 전송된 파일 정보를 담아서 반환을 해줄 List이다. 
		 * 여태까지는 단 하나의 파일만 전송을 하였지만, 다중파일전송을 하도록 수정할 계획이기 때문에 미리 그에 맞도록 구성하였다.
		 */
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
//		32번째 줄은 ServiceImpl 영역에서 전달해준 map에서 신규 생성되는 게시글의 번호를 받아오도록 하였다.
		Map<String, Object> listMap = null;
		
		String boardIdx = (String) map.get("IDX");
		File file = new File(filePath);
		
		//파일을 저장할 경로에 해당폴더가 없으면 폴더를 생성하도록 하였다. 
		if (file.exists() == false) {
			file.mkdirs();
		}
		

		while (iterator.hasNext()) {
			multipartFile = multipartHttpServletRequest.getFile(iterator.next());
			if (multipartFile.isEmpty() == false) {
				/*
				 * 파일의 정보를 받아서 새로운 이름으로 변경하는 부분이다.
				 * 먼저 파일의 원본이름을 받아와서 해당 파일의 확장자를 알아낸 후, 
				 * 아까 CommonUtils 클래스에 만들었던 getRandomString() 메서드를 이용하여 32자리의 랜덤한 파일이름을 생성하고 원본파일의 확장자를 다시 붙여주었다. 
				 */
				originalFileName = multipartFile.getOriginalFilename();
				originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				storedFileName = CommonUtils.getRandomString() + originalFileExtension;
				
				// 서버에 실제 파일을 저장하는 부분이다. 
				// multipartFile.transferTo() 메서드를 이용하여 지정된 경로에 파일을 생성하는것을 알 수 있다. 
				file = new File(filePath + storedFileName);
				multipartFile.transferTo(file);
				
				
				listMap = new HashMap<String, Object>();
				listMap.put("BOARD_IDX", boardIdx);
				listMap.put("ORIGINAL_FILE_NAME", originalFileName);
				listMap.put("STORED_FILE_NAME", storedFileName);
				listMap.put("FILE_SIZE", multipartFile.getSize());
				list.add(listMap);
			}
		}
		return list;
	}

	
	public List<Map<String, Object>> parseUpdateFileInfo(Map<String, Object> map, HttpServletRequest request) throws Exception{
		MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
    	Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
    	
    	MultipartFile multipartFile = null;
    	String originalFileName = null;
    	String originalFileExtension = null;
    	String storedFileName = null;
    	
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> listMap = null; 
        
        String boardIdx = (String)map.get("IDX");
        String requestName = null;
        String idx = null;
        
        
        while(iterator.hasNext()){
        	multipartFile = multipartHttpServletRequest.getFile(iterator.next());
        	if(multipartFile.isEmpty() == false){
        		originalFileName = multipartFile.getOriginalFilename();
        		originalFileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        		storedFileName = CommonUtils.getRandomString() + originalFileExtension;
        		
        		multipartFile.transferTo(new File(filePath + storedFileName));
        		
        		listMap = new HashMap<String,Object>();
        		listMap.put("IS_NEW", "Y");
        		listMap.put("BOARD_IDX", boardIdx);
        		listMap.put("ORIGINAL_FILE_NAME", originalFileName);
        		listMap.put("STORED_FILE_NAME", storedFileName);
        		listMap.put("FILE_SIZE", multipartFile.getSize());
        		list.add(listMap);
        	}
        	else{
        		requestName = multipartFile.getName();
            	idx = "IDX_"+requestName.substring(requestName.indexOf("_")+1);
            	if(map.containsKey(idx) == true && map.get(idx) != null){
            		listMap = new HashMap<String,Object>();
            		listMap.put("IS_NEW", "N");
            		listMap.put("FILE_IDX", map.get(idx));
            		list.add(listMap);
            	}
        	}
        }
		return list;	
	}
}
