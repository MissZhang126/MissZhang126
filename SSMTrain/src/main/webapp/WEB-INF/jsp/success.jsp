<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/1
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>主页</title>
    <script type="application/javascript" src="/js/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="/js/jqPaginator.js"></script>
    <script type="application/javascript" src="/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">
    <style>
        a {
            display: block;
            height: 33px;
            width: 200px;
            text-align: center;
            background: orangered;
            color: #ffffff;
            font-size: 20px;
            font-weight: bold;
            text-decoration: none;
            padding-top: 3px;
            margin: 8px auto;
            border-radius: 10px;
        }

        h1 {
            color: #316AC5;
            width: 200px;
            margin: auto;
        }
    </style>

</head>
<body>
<h1>首页</h1>
<shiro:hasAnyRoles name="管理员,普通用户,人事,经理,员工,物流员,财务经理,物流经理">
    <a href="/getUser">用户管理</a>
</shiro:hasAnyRoles>

<shiro:hasAnyRoles name="管理员,经理">
    <a href="/getRole">角色管理</a>
</shiro:hasAnyRoles>

<shiro:hasAnyRoles name="管理员,权限管理员">
    <a href="/perFirst">权限管理</a>
</shiro:hasAnyRoles>

<a href="shirologout">登出</a>
</body>
</html>
