<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/1
  Time: 9:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色列表</title>
    <script type="text/javascript" src="/js/jquery-3.4.1.js"></script>
    <script type="text/javascript" src="/js/jqPaginator.js"></script>
    <link rel="stylesheet" href="/css/zTreeStyle.css"/>
    <script type="text/javascript" src="/js/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="/js/jquery.ztree.excheck.js"></script>

    <script type="application/javascript" src="/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/css/bootstrap.css">
    <%--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
    <%--<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">--%>

    <style type="text/css">
        ul.ztree {
            margin-top: 10px;
            border: 1px solid #617775;
            background: #f0f6e4;
            /*  width: 220px; */
            height: 350px;
            overflow-y: scroll;
            overflow-x: auto;
        }

        .box {
            width: 200px;
            margin: auto;
        }
    </style>

</head>
<body>
<div class="container">

    <table class="table">
        <tr>
            <td>ID</td>
            <td>角色</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${pageInfo.list}" var="list">
            <tr>
                <td>${list.RId}</td>
                <td>${list.RName}</td>
                <td><shiro:hasPermission name="user:updateRole">
                    <button class="btn btn-warning" data-toggle="modal" data-target="#myModal"
                            onclick="modal(${list.RId})">修改
                    </button>
                </shiro:hasPermission>
                    <shiro:hasPermission name="user:deleteRole">
                        <a href="/deleteRole/${list.RId}" class="btn btn-danger">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
    <div class="pagination-layout">
        <div class="pagination">
            <ul class="pagination" total-items="pageInfo.totalRows" max-size="10" boundary-links="true">

            </ul>
        </div>
    </div>
    <shiro:hasPermission name="user:addRole">
        <button class="btn btn-warning" data-toggle="modal" data-target="#myModal2">新增</button>
    </shiro:hasPermission>
    <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="returnUp()">返回</button>

</div>
<div id="myModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4>权限修改</h4>
            </div>

            <div class="modal-body">
                <div class="box">
                    <ul id="tree1" class="ztree"></ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal" onclick="getPerIds()">确认</button>
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>

</div>

<%--新增--%>
<div id="myModal2" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4>新增角色</h4>
            </div>
            <%--<form action="/addRole" method="post">--%>
            <div class="modal-body">
                <label>角色名称：</label>
                <input type="text" name="rName"/>
                <div class="box">
                    <ul id="tree2" class="ztree"></ul>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal" onclick="add()">确认</button>
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
            </div>
            <%--</form>--%>
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

        location.href = "/getRole?pageNum=" + num;

    }


    //    回填数据
    function modal(rId) {


        var zTree1 = $.fn.zTree.getZTreeObj("tree1");
        zTree1.checkAllNodes(false);


        var rId = rId;

        $.ajax({

            //            请求路径
            url: '/getAllPer',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                "rId": rId
            },
            //            期望的返回值类型(默认String，一般写json)
            dataType: 'json',
            //            同步还是异步 true是异步
            async: true,
            contentType: "application/json",
            traditional: true,
            //            回调函数
            success: function (result) {
                if (result.data.length > 0) {
                    $.each(result.data, function (i, per) {
//                                1、根据id获取树的某个节点：
                        var zTree = $.fn.zTree.getZTreeObj("tree1");
//                                console.log(zTree)
                        var node = zTree.getNodeByParam("pId", per.pid);
//                                console.log(node);
//                                2、设置node节点选中状态：
                        zTree.selectNode(node);

                        node.checked = true;
//                                3、设置node节点checked选中
                        zTree.checkNode(node, true, true);

                    })
                }

            },
            error: function () {
//                alert("请求失败");
            }
        })

    }


    //    修改
    function getPerIds() {
        var ids = [];
//        var ids2 = [];
//        var checkedNode = tree1.getTreeCheckedNodes(true);

//        获取选中的树节点
        var treeObj = $.fn.zTree.getZTreeObj("tree1");
        var nodes = treeObj.getCheckedNodes(true);
//        console.log(nodes);
//        放入数组中
        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].pId);
//            ids2.push(nodes[i].name);

        }

//        console.log(ids);

        $.ajax({

            //            请求路径
            url: '/updatePerRole',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                ids: ids
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

    //    ztree设置
    var setting = {

        view: {
            dblClickExpand: true,
            selectedMulti: true,//可以多选
            showLine: true
        },
        check: {
            enable: true,//显示复选框
            chkStyle: "checkbox",
            /*其中Y表示被checkbox被勾选时的联动情况，N表示取消勾选时的联动情况。
             chkboxTyoe:{"Y":"ps","N":"ps"}
             "p"表示操作会影响父节点，“s”表示操作会影响子节点。*/
            chkboxType: {"Y": "p", "N": "ps"}
        },
        data: {
            key: {
                title: "t"
            },
            simpleData: {
                enable: true,
                idKey: "pId",
                pIdKey: "parentId",
                rootPId: 0
            }
        },
        async: {

            enable: true,
            autoParam: ["mid=pid"]
        }

    };


    //请求controller获取数据
    $(document).ready(function () {
        jQuery.ajax({
            url: "/tree",
            type: "POST",
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            success: function (data) {
//                修改
                $.fn.zTree.init($("#tree1"), setting, data);
//                新增
                $.fn.zTree.init($("#tree2"), setting, data);
            }
        });
    });

    /*//点击节点的onclick事件
     function getInfoId(id) {
     var id = id;
     //        alert(id);

     }
     */
    //    新增
    function add() {

        var ids = [];
//        获取选中节点
        var treeObj = $.fn.zTree.getZTreeObj("tree2");

        var nodes = treeObj.getCheckedNodes(true);
//        console.log(nodes);

        for (var i = 0; i < nodes.length; i++) {
            ids.push(nodes[i].pId);
        }
        console.log(ids);
//        获取input框内容 ： 姓名
        var a = document.getElementsByName("rName");
        var rNames = [];
        for (var j = 0; j < a.length; j++) {
            rNames.push(a[j].value);
        }
//        console.log(rNames);


        $.ajax({

            //            请求路径
            url: '/addRole',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                ids: ids,
                rNames: rNames
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
