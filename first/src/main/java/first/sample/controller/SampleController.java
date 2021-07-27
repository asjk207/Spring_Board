package first.sample.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import first.common.common.CommandMap;
import first.sample.service.SampleService;

//Controller는 단순히 어떤 주소와 화면을 연결하고, 비지니스 로직을 호출하는 역할을 한다.
@Controller
public class SampleController {
	Logger log = Logger.getLogger(this.getClass());
	
	public static int cnt =0;
	@Resource(name = "sampleService")
	private SampleService sampleService;

	// 최초 실행시 sample/openBoardList.do주소로 연동되어 아래의 메소드가 실행된다.
	@RequestMapping(value = "/sample/openBoardList.do")
	public ModelAndView openBoardList(CommandMap commandMap) throws Exception {
		// 메서드 실행시 boardList.jsp파일이 실행된다.
		ModelAndView mv = new ModelAndView("/sample/boardList");
		log.debug("openBoardList_Map 확인"+commandMap.getMap());
		
		// 목록을 저장할수 있는 List를 선언 한다.
		// 글번호, 글제목, 작성일 등의 내용을 Map에 저장하려는 것이다.
		// Map은 다시 키(key)와 값(value)로 구분되어지는데, 각각의 컬럼인 글번호, 글제목, 작성일 등의 키와 실제 값이 저장된다
    	Map<String,Object> resultMap = sampleService.selectBoardList(commandMap.getMap());
    	
    	mv.addObject("paginationInfo", (PaginationInfo)resultMap.get("paginationInfo"));
    	mv.addObject("list", resultMap.get("result"));

		return mv;
	}

	@RequestMapping(value = "/sample/openBoardWrite.do")
	public ModelAndView openBoardWrite(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardWrite");

		return mv;
	}

	/*
	 * 파라미터로 HttpServletRequest를 추가로 받는다는 것이다. 우리가 화면에서 전송한 모든 데이터는
	 * HttpServletRequest에 담겨서 전송되고, 그것을 HandlerMethodArgumentResolver를 이용하여
	 * CommandMap에 담아주었다 HttpServletRequest에는 모든 텍스트 데이터 뿐만이 아니라 화면에서 전송된 파일정보도 함께
	 * 담겨있다.
	 */
	@RequestMapping(value = "/sample/insertBoard.do")
	public ModelAndView insertBoard(CommandMap commandMap, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardList.do");
		sampleService.insertBoard(commandMap.getMap(), request);
		return mv;
	}
	/*
	 * map에서 2가지를 가져온 후, 각각 mv에 넣어주는 것을 확인해야한다.
	 *  6번째 줄의 map.get("map")은 기존의 게시글 상세정보이다. 
	 *  7번째 줄의 map.get("list")는 첨부파일의 목록을 가지고 있다. 게시글 상세정보와 첨부파일 정보를 각각 보내주는것을 확인하자.
	 */
	@RequestMapping(value = "/sample/openBoardDetail.do")
	public ModelAndView openBoardDetail(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardDetail");
		
		Map<String, Object> map = sampleService.selectBoardDetail(commandMap.getMap());
		mv.addObject("map", map.get("map"));
		mv.addObject("list", map.get("list"));
		return mv;
	}

	@RequestMapping(value = "/sample/openBoardUpdate.do")
	public ModelAndView openBoardUpdate(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("/sample/boardUpdate");

		Map<String, Object> map = sampleService.selectBoardDetail(commandMap.getMap());
		mv.addObject("map", map.get("map"));
		mv.addObject("list", map.get("list"));
		return mv;
	}

	@RequestMapping(value = "/sample/updateBoard.do")
	public ModelAndView updateBoard(CommandMap commandMap, HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardDetail.do");

		sampleService.updateBoard(commandMap.getMap(), request);

		mv.addObject("IDX", commandMap.get("IDX"));
		return mv;
	}

	@RequestMapping(value = "/sample/deleteBoard.do")
	public ModelAndView deleteBoard(CommandMap commandMap) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/sample/openBoardList.do");

		sampleService.deleteBoard(commandMap.getMap());

		return mv;
	}
	
	@RequestMapping(value = "/sample/deleteSelectBoard.do")
	public ModelAndView deleteSelectBoard(HttpServletRequest request) throws Exception {
		ModelAndView mv = new ModelAndView("redirect:/");
		
//		log.debug(request.getParameterValues("valueArr"));
		String[] ajaxMsg = request.getParameterValues("valueArr");
		int size = ajaxMsg.length;
		for(int i=0; i<size; i++) {
			log.debug(ajaxMsg[i]);
			sampleService.deleteSelectBoard(ajaxMsg[i]);
		}

		return mv;
	}
}
