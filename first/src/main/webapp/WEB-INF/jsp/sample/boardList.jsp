<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
</head>
<body>
	<header>
			<h2>게시판 목록</h2>
			
			<div class="btn_list">
				<input type="button" value="엑셀 다운로드" class="btn btn-excel-download" onClick="fn_downloadExcel();">
				<form id="excelUploadForm" name="excelUploadForm" enctype="multipart/form-data" method="post" 
                                action= "/first/common/excelupload.do">
			    	<div class="contents">
				        <dl class="vm_name">
				                <dd><input id="excelFile" type="file" name="excelFile" /></dd>
				        </dl>        
				    </div>
				    <div class="bottom">
				        <button type="button" id="addExcelImpoartBtn" class="btn" onclick="excelUpload()" ><span>엑셀 파일 업로드</span></button> 
				    </div>
				</form> 
			</div>
	</header>
	<table class="board_list">
		<colgroup>
			<col width="5%"/>
			<col width="10%"/>
			<col width="*"/>
			<col width="15%"/>
			<col width="20%"/>
		</colgroup>
		<thead>
			<tr>
				<th scope="col"><input id="allCheck" type="checkbox" name="allCheck"/></th>
				<th scope="col">글번호</th>
				<th scope="col">제목</th>
				<th scope="col">조회수</th>
				<th scope="col">작성일</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${fn:length(list) > 0}">
					<c:forEach var="row" items="${list}" varStatus="status">
						<tr>
							<td class="text_ct"><input name="RowCheck" type="checkbox" value="${row.IDX }"/></td>
							<td>${row.IDX }</td>
							<td class="title">
								<a href="#this" name="title">${row.TITLE }</a>
								<input type="hidden" id="IDX" value="${row.IDX }">
							</td>
							<td>${row.HIT_CNT }</td>
							<td>${row.CREA_DTM }</td>
						</tr>
					</c:forEach>	
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="4">조회된 결과가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>	
		</tbody>
	</table>
	<footer>
		<div class="footer_button_list">
			<input type="button" value="선택삭제" class="btn btn-outline-info" onClick="deleteValue();">
			<a href="#this" class="btn" id="write">글쓰기</a>
			<c:if test="${not empty paginationInfo}">
				<ui:pagination paginationInfo = "${paginationInfo}" type="text" jsFunction="fn_search"/>
			</c:if>
			<input type="hidden" id="currentPageNo" name="currentPageNo"/>
		</div>
	</footer>
	

	
	<br/>
	
	
	<%@ include file="/WEB-INF/include/include-body.jspf" %>
	<script type="text/javascript">
		$(document).ready(function(){
			var chkObj = document.getElementsByName("RowCheck");
			var rowCnt = chkObj.length;
			
			$("#write").on("click", function(e){ //글쓰기 버튼
				e.preventDefault();
				fn_openBoardWrite();
			});	
			
			$("a[name='title']").on("click", function(e){ //제목 
				e.preventDefault();
				fn_openBoardDetail($(this));
			});
			
			$("input[name='allCheck']").click(function(){ // 체크박스 전체 선택
				var chk_listArr = $("input[name='RowCheck']");
				for (var i=0; i<chk_listArr.length; i++){
					chk_listArr[i].checked = this.checked;
					console.log(chk_listArr[i].checked);
				}
			});
			
			$("input[name='RowCheck']").click(function(){ // 체크박스 단일 선택
				if($("input[name='RowCheck']:checked").length == rowCnt){
					if($("input[name='allCheck']:checked").length == rowCnt){
						$("input[name='allCheck']")[0].checked = true;
					}
					else{
						$("input[name='allCheck']")[0].checked = false;
					}
				}
			});
			
		});
		function fn_openBoardWrite(){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardWrite.do' />");
			comSubmit.submit();
		}
		
		function fn_openBoardDetail(obj){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardDetail.do' />");
			comSubmit.addParam("IDX", obj.parent().find("#IDX").val());
			comSubmit.submit();
		}
		
		function fn_search(pageNo){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardList.do' />");
			comSubmit.addParam("currentPageNo", pageNo);
			comSubmit.submit();
		}
		/*
		function fn_uploadExcel(){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/common/excelUpload.do' />");
			comSubmit.submit();
		}*/
		
		function fn_downloadExcel(){
			var t_pageNo = document.getElementsByName("currentPageNo");
			console.log("pageNo: "+t_pageNo);
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/common/exceldownload.do' />");
			comSubmit.addParam("currentPageNo", t_pageNo);
			comSubmit.submit();
		}
		// 선택 삭제용 함수
		function deleteValue(){
			var url = "/first/sample/deleteSelectBoard.do";  // Controller로 보내고자 하는 URL
			var valueArr = new Array();
			//var JSONObject idx_JSON = new JSONObject();
			//data = JSON.stringify()
			
			var list = $("input[name='RowCheck']");
		
			for(var i = 0; i < list.length; i++){
				if(list[i].checked){
					valueArr.push(list[i].value);
				}
			}
			if (valueArr.length == 0){
				alert("선택된 글이 없습니다.");
			}
			else{
				var chk = confirm("정말 삭제하시겠습니까?");
				console.log(valueArr);
				$.ajax({
					url : url,					// 전송 URL
					type : 'GET',				// POST 방식
					traditional: true,
					data : {
						valueArr : valueArr		// 보내고자 하는 data 변수 설정
					},
					success: function(jdata){
						if(jdata = 1) {
							alert("삭제 성공");
							location.replace("/first");								// list 로 페이지 새로고침

						}
						else{
							alert("삭제 실패");
						}
					}
				});
			}			
		}
		
		// 파일 업로드시 엑셀 파일 확장자 체크.
		function excelCheckFileType(filePath) {
            var fileFormat = filePath.split(".");
            if (fileFormat.indexOf("xlsx") > -1 || fileFormat.indexOf("xls") > -1) {
                return true;
            } else {
                return false;
            }

        }
		// 파일 업로드시 엑셀파일만 업로드하는지를 체크하는 로직
        function excelUpload() {
            var file = $("#excelFile").val();
            if (file == "" || file == null) {
                alert("파일을 선택해주세요.");
                return false;
            } else if (!excelCheckFileType(file)) {
                alert("엑셀 파일만 업로드 가능합니다.");
                return false;
            }

            if (confirm("업로드 하시겠습니까?")) {
                var options = {
                    success : function(data) {
                        alert("모든 데이터가 업로드 되었습니다.");

                    },
                    type : "POST"
                };
                $("#excelUploadForm").ajaxSubmit(options);

            }
        }

		/*
		function fn_check_delete(obj){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/deleteSelectBoard.do' />");
			comSubmit.addParam("IDX", obj.parent().find("#IDX").val());
			comSubmit.submit();
		}*/
		
	</script>	
</body>
</html>