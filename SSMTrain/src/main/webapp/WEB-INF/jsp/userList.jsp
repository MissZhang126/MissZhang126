<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/1
  Time: 9:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户列表</title>
    <script type="text/javascript" src="/js/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="/js/jqPaginator.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">

    <%--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
    <%--<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">--%>
</head>
<body>
<div class="container">
    <table class="table">
        <tr>
            <td>ID</td>
            <td>用户</td>
            <td>状态</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${pageInfo.list}" var="list2">
            <%--<c:if test="${list2.status eq '0' || list2.status eq '1'}">--%>
            <tr>
                <td>${list2.id}</td>
                <td>${list2.userName}</td>
                <td><c:if test="${list2.status eq '0'}">已禁用</c:if>
                    <c:if test="${list2.status eq '1'}">已启用</c:if>
                    <c:if test="${list2.status eq '2'}">已删除</c:if>
                </td>
                <td><shiro:hasPermission name="user:updateUser">
                    <button id="model1" class="btn btn-warning" data-toggle="modal" data-target="#myModal"
                            onclick="modal(${list2.id})">修改
                    </button>
                </shiro:hasPermission>
                    <shiro:hasPermission name="user:deleteUser">
                        <c:if test="${list2.status eq '1' || list2.status eq '0'}"><a class="btn btn-danger"
                                                                                      href="/changeStatus/${list2.id}/2">删除</a></c:if>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="user:stop">
                    <c:if test="${list2.status eq '1'}"><a class="btn btn-primary"
                                                           href="/changeStatus/${list2.id}/0">禁用</a></c:if></td>
                </shiro:hasPermission>
            </tr>
            <%--</c:if>--%>
        </c:forEach>
    </table>
    <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="returnUp()">返回</button>

    <div class="pagination-layout">
        <div class="pagination">
            <ul class="pagination" total-items="pageInfo.totalRows" max-size="10" boundary-links="true">

            </ul>
        </div>
    </div>
</div>
<div id="myModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4>角色修改</h4>
            </div>
            <form action="/updateRole">
                <div class="modal-body">
                    <c:forEach items="${list}" var="list1">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="rid" value="${list1.RId}">${list1.RName}
                            </label>
                        </div>
                    </c:forEach>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success" data-dismiss="modal" onclick="getRoleIds()">确认
                    </button>
                    <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="application/javascript">

    <%--返回--%>
    function returnUp() {
        window.location.href = "/login/check1";
    }

    var if_firstime = true;


    window.onload = function () {
        $('.pagination').jqPaginator({
            totalPages: ${pageInfo.pages},
            visiblePages: 5,
            currentPage: ${pageInfo.pageNum},

            first: '<li class="first"><a href="javascript:void(0);">第一页</a></li>',
            prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
            next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
            last: '<li class="last"><a href="javascript:void(0);">最末页 </a></li>',
            page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',

            onPageChange: function (num) {
                if (if_firstime) {
                    if_firstime = false;
                } else if (!if_firstime) {
                    changePage(num);
                }

            }
        });
    }

    function changePage(num) {

        location.href = "/getUser?pageNum=" + num;

    }

    //    回填 修改点击事件
    function modal(id) {
        /*location.href = "/getAllRoleById?id="+id;*/
        var id = id;

        $.ajax({

            //            请求路径
            url: '/getAllRoleById',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                "id": id
            },
            //            期望的返回值类型(默认String，一般写json)
            dataType: 'json',
            //            同步还是异步 true是异步
            async: true,
//            traditional:true,
            contentType: "application/json",
            traditional: true,
            //            回调函数
            success: function (result) {
//                console.log(result);

//                清空复选框
                $.each($("input:checkbox"), function (j, input) {
                    $(input).prop("checked", "");
                })

//                循环对比 相同的打钩
                $.each($("input:checkbox"), function (j, input) {
//                    console.log(role.rid);
                    $.each(result.data, function (i, role) {
//                        console.log(input.value);
                        if (role.rid == input.value) {
                            $(input).prop("checked", true);
                        }
                    })
                })


            },
            error: function () {
//                alert("请求失败");
            }
        })

    }

    //    修改
    function getRoleIds() {
        var ids = [];

//        获取所有选中的角色复选框
        $.each($("input:checkbox:checked"), function (j, input) {
            ids.push(input.value);
        })

        $.ajax({

            //            请求路径
            url: '/updateRole',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                ids: ids
                /*"id":id*/
            },
            //            期望的返回值类型(默认String，一般写json)
            dataType: 'json',
            //            同步还是异步 true是异步
            async: true,
            contentType: "application/json",
            traditional: true,
            //            回调函数
            success: function (result) {
                alert(result.msg);
                // 刷新页面
                window.location.reload();

            },
            error: function () {
//                alert("请求失败");
            }
        })
    }
</script>
</body>
</html>
