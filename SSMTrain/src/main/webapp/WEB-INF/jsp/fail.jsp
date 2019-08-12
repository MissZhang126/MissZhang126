<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/1
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>失败</title>
</head>
<body>
<h1 style="color: red">
    <%--判断传过来的是否为空 当密码错误时值为空--%>
    <c:if test="${msg == null || '' eq msg}">账号或密码有误</c:if>
    ${msg}
</h1>
<hr>
点击返回<a href="/login/toLogin">注册/登录</a>页面
</body>
</html>
