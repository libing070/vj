<%@ page import="com.asiainfo.biframe.privilege.IUser"%>
<%@ page import="com.icitic.web.UserVo"%>
<%
String userId = request.getParameter("userId");
if(null!=userId && !"".equals(userId)){
UserVo iuser = new UserVo(userId,"Test");
session.setAttribute("ssoUSER",iuser);
System.out.println(iuser.getUserid());
System.out.println(iuser.getUsername());
response.sendRedirect("/webapp/webos/index.html");
}
%>


  <form method="post" action="login.jsp">
	<input type="text" name="userId"><input type="submit">
  </form>
  
  