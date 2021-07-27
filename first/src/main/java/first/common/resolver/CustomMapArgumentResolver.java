package first.common.resolver;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import first.common.common.CommandMap;

 /*
  HandlerMethodArgumentResolver는 사용자 요청이 Controller에 도달하기 전에 그 요청의 파라미터들을 수정할 수 있도록 해준다.
  HandlerMethodArgumentResolver는 컨트롤러의 파라미터가 Map 형식이면 동작하지 않는다. 
  엄밀히 말을하면, 스프링 3.1에서 HandlerMethodArgumentResolver를 이용하여 그러한 기능을 만들더라도, 
  컨트롤러의 파라미터가 Map 형식이면 우리가 설정한 클래스가 아닌, 스프링에서 기본적으로 설정된 ArgumentResolver를 거치게 된다. 
 */

 /*
 HandlerMethodArgumentResolver 인터페이스를 상속.
 두가지 메서드를 반드시 구현해야 하는데, supportsParameter 메서드와 resolveArgument 메서드가 그것이다. 
 */
public class CustomMapArgumentResolver implements HandlerMethodArgumentResolver{
	
	/*
	 * supportsParameter 메서드는 Resolver가 적용 가능한지 검사하는 역할을 한다.
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return CommandMap.class.isAssignableFrom(parameter.getParameterType());
	}
	
	/*
	 * resolverArgument 메서드는 파라미터와 기타 정보를 받아서 실제 객체를 반환한다.
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		CommandMap commandMap = new CommandMap();
		
		//request에 담겨있는 모든 키(key)와 값(value)을 commandMap에 저장하였다.
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		Enumeration<?> enumeration = request.getParameterNames();
		
		/*
		   request에 있는 값을 iterator를 이용하여 하나씩 가져오는 로직이다. 
		   마지막으로 모든 파라미터가 담겨있는 commandMap을 반환
		*/
		String key = null;
		String[] values = null;
		while(enumeration.hasMoreElements()){
			key = (String) enumeration.nextElement();
			values = request.getParameterValues(key);
			if(values != null){
				commandMap.put(key, (values.length > 1) ? values:values[0] );
			}
		}
		return commandMap;
	}

}
