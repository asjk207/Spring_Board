<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form id="form1" name="form1" method="post" enctype="multipart/form-data">
	<input type="file" id="fileInput" name="fileInput">
	<button type="button" onclick="doExcelUploadProcess()"> 엑셀 업로드 작업</button>
</form>
<%@ include file="/WEB-INF/include/include-body.jspf" %>
<script type="text/javascript">
	function doExcelUploadProcess(){
		var form = new FormData(document.getElemnetById('form1'));
		var url = "/first/common/excelUpload.do";
		$.ajax({
			url: url,
			data: form,
			processType:false,
			type:"POST",
			success: function(data){
				var htmlValue;
				
				for (var i = 0; i < data.length; i++){
					user_id.push(data[i].user_id);
					uaser_name.push(data[i].user_name);
						     expire.push(data[i].expire);
						     deptname.push(data[i].deptname);
						     phone.push(data[i].phone);
						     email.push(data[i].email);
						     desc.push(data[i].desc);
						     office_code.push(data[i].office_code);
						     
					htmlValue += "<tr>";
				htmlValue += "<td>"+data[i].user_id+"</td>";
				htmlValue += "<td>"+data[i].user_name+"</td>";
				htmlValue += "<td>"+data[i].expire+"</td>";
				htmlValue += "<td>"+data[i].deptname+"</td>";
				htmlValue += "<td>"+data[i].phone+"</td>";
				htmlValue += "<td>"+data[i].email+"</td>";
				htmlValue += "<td>"+data[i].desc+"</td>";
				htmlValue += "<td>"+data[i].office_code+"</td>";
				htmlValue += "</tr>";
				}
				
				${"#muntiUserInfo"}.html(htmlValue)
			},
			error:function(xhr, status, error){
				console.log("xhr:"+xhr+", status:"+status+", error:"+error);
			}
		})
	}
	</script>
</body>
</html>