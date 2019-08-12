<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/8/1
  Time: 9:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>权限展示</title>
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
    <div style="height: 500px;">
        <div class="col-lg-4">
            <div class="text-center"><kbd>一级权限</kbd></div>
            <c:forEach items="${firstP}" var="firstP">
                <a class="list-group-item text-center" onmouseover="showbtn(this)" onmouseout="hidebtn(this)"
                   onclick="showMsg(this,${firstP.PId})">
                        ${firstP.PName}
                    <div style="float: right;" hidden="hidden">
                        <shiro:hasPermission name="user:updatePer">
                            <button type="button" class="btn btn-warning btn-xs" data-toggle="modal"
                                    data-target="#myModal" onclick="message(${firstP.PId})">
                                <span class="glyphicon glyphicon-pencil"></span> 修改
                            </button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="user:deletePer">
                            <button type="button" class="btn btn-danger btn-xs" onclick="deletePer(${firstP.PId})">
                                <span class="glyphicon glyphicon-remove"></span>删除
                            </button>
                        </shiro:hasPermission>
                    </div>
                </a>
            </c:forEach>
        </div>
        <div class="col-lg-4 col-lg-push-0">
            <div class="text-center"><kbd>二级权限</kbd></div>
            <div name="two"></div>

        </div>
        <div class="col-lg-4 col-lg-push-0">
            <div class="text-center"><kbd>三级级权限</kbd></div>
            <div name="three"></div>

        </div>
    </div>
    <shiro:hasPermission name="user:addPer">
        <button type="button" class="btn btn-warning" data-toggle="modal" data-target="#myModal1">新增</button>
    </shiro:hasPermission>
    <button type="button" class="btn btn-danger" data-dismiss="modal" onclick="returnUp()">返回</button>
</div>


<%--权限修改模态框--%>
<div id="myModal" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4>角色修改</h4>
            </div>
            <div class="modal-body">
                <label>修改权限名称</label>
                <input type="text" name="pName"/>
            </div>
            <%--<div class="box">
                <ul id="treeC" class="ztree"></ul>
            </div>--%>
            <div class="modal-footer">
                <button type="submit" class="btn btn-success" data-dismiss="modal" onclick="update()">确认</button>
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<%--新增--%>
<div id="myModal1" class="modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4>新增权限</h4>
            </div>
            <div class="modal-body">
                <label>权限名称：</label>
                <input type="text" name="pNameAdd"/>
                <label>权限url：</label>
                <input type="text" name="url"/>
                <div class="box">
                    <ul id="tree" class="ztree"></ul>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal" onclick="add()">确认</button>
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>


<script type="application/javascript">


    <%--返回--%>
    function returnUp() {
        window.location.href = "/login/check1";
    }

    <%--删除 点击事件--%>
    function deletePer(pId) {
        var pId = pId;

        $.ajax({

            //            请求路径
            url: '/deletePer',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                "pId": pId
            },
            dataType: 'json',
            //            同步还是异步 true是异步
            async: true,
            contentType: "application/json",
            traditional: true,
            //            回调函数
            success: function (result) {
                alert(result.msg);
                // 删除成功后刷新页面
                window.location.reload();

            },
            error: function () {
                alert("请求失败");
            }
        })

    }

    <%--回写权限名称--%>
    <%--点击事件 修改按钮--%>
    function message(pId) {

        var pId = pId;
        $.ajax({

            //            请求路径
            url: '/messagePer',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                "pId": pId
            },
            dataType: 'json',
            //            同步还是异步 true是异步
            async: true,
            contentType: "application/json",
            traditional: true,
            //            回调函数
            success: function (result) {
//            清空上一次的值
//            $("input[name='pName']").val("");

//            赋值到输入框 回写
                $("input[name='pName']").val(result.msg);


            },
            error: function () {
                alert("请求失败");
            }
        })

    }

    //  修改
    function update() {

//        获取input框内容 ： 姓名
        var a = document.getElementsByName("pName");
        var pName = a[0].value;
        console.log(pName);
        //        获取选中节点
        /* var treeObj = $.fn.zTree.getZTreeObj("tree");

         var nodes = treeObj.getCheckedNodes(true);

         if (nodes.length > 0) {
         var id = nodes[0].pId;
         } else {
         var id = "x";
         }
         */
        $.ajax({

            //            请求路径
            url: '/updatePer',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                pName: pName
//                id:id
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

    //    显示按钮
    function showbtn(obj) {
        $(obj).find("div").removeAttr("hidden")
    }

    //    隐藏按钮
    function hidebtn(obj) {
        $(obj).find("div").attr("hidden", "hidden")
    }

    //    一级菜单点击事件
    function showMsg(obj, pId) {
        $(obj).parent().find("a").removeClass("active")
        $(obj).addClass("active")


        var pId = pId;
        var str = "";
        $.ajax({

            //            请求路径
            url: '/perSecond',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                "pId": pId
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
//                点击一集菜单的时候 清空三级菜单显示内容
                $("div[name='three']").html("");
                $.each(result.data, function (i, per) {
//                    console.log(per.pname);
                    per.pname.toJSON;
                    str += '<a class="list-group-item text-center" onmouseover="showbtn(this)" onmouseout="hidebtn(this)" onclick="showMsg1(this,' + per.pid + ')"><span name="second">' + per.pname + '</span><div style="float: right;" hidden><shiro:hasPermission name="user:updatePer"><button type="button" class="btn btn-warning btn-xs" data-toggle="modal" data-target="#myModal" onclick="message(' + per.pid + ')"><span class="glyphicon glyphicon-pencil"></span> 修改 </button></shiro:hasPermission><shiro:hasPermission name="user:deletePer"><button type="button" class="btn btn-danger btn-xs" onclick="deletePer(' + per.pid + ')"><span class="glyphicon glyphicon-remove"></span> 删除 </button></shiro:hasPermission></div></a>'
//                    $("div[name='second']").html(per.pname);
                })
                $("div[name='two']").html(str);

            },
            error: function () {
                alert("请求失败");
            }
        })
    }

    //    二级菜单点击事件
    function showMsg1(obj, pId) {
        $(obj).parent().find("a").removeClass("active")
        $(obj).addClass("active")

        var pId = pId;
        var str = "";
        $.ajax({

            //            请求路径
            url: '/perSecond',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                "pId": pId
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

                $.each(result.data, function (i, per) {
//                    console.log(per.pname);
                    per.pname.toJSON;
                    str += '<a class="list-group-item text-center" onmouseover="showbtn(this)" onmouseout="hidebtn(this)" onclick="showMsg2(this,' + per.pid + ')"><span name="third">' + per.pname + '</span><div style="float: right;" hidden><shiro:hasPermission name="user:updatePer"><button type="button" class="btn btn-warning btn-xs" data-toggle="modal" data-target="#myModal" onclick="message(' + per.pid + ')"><span class="glyphicon glyphicon-pencil"></span> 修改 </button></shiro:hasPermission><shiro:hasPermission name="user:deletePer"><button type="button" class="btn btn-danger btn-xs" onclick="deletePer(' + per.pid + ')"><span class="glyphicon glyphicon-remove"></span> 删除 </button></shiro:hasPermission></div></a>'
                })
                $("div[name='three']").html(str);

            },
            error: function () {
                alert("请求失败");
            }
        })
    }

    //    点击事件 三级菜单
    function showMsg2(obj, pId) {
        $(obj).parent().find("a").removeClass("active")
        $(obj).addClass("active")
    }


    //    ztree设置
    var setting = {

        view: {
            dblClickExpand: true,
            autoCheckTrigger: true,
            selectedMulti: false,//可以多选
            showLine: true
        },
        check: {
            enable: true,//显示复选框
            chkStyle: "radio",
            /*其中Y表示被checkbox被勾选时的联动情况，N表示取消勾选时的联动情况。
             chkboxTyoe:{"Y":"ps","N":"ps"}
             "p"表示操作会影响父节点，“s”表示操作会影响子节点。*/
            chkboxType: {"Y": "", "N": ""}
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
//                新增
                $.fn.zTree.init($("#tree"), setting, data);
//                修改
                $.fn.zTree.init($("#treeC"), setting, data);
            }
        });
    });

    //    新增
    function add() {
//        获取选中节点
        var treeObj = $.fn.zTree.getZTreeObj("tree");

        var nodes = treeObj.getCheckedNodes(true);

        if (nodes.length > 0) {
            var id = nodes[0].pId;
        } else {
            var id = "x";
        }

//        获取input框内容 ： 姓名
        var a = document.getElementsByName("pNameAdd");
        var b = document.getElementsByName("url");
        var pName = a[0].value;
        var url = b[0].value;

        $.ajax({

            //            请求路径
            url: '/addPer',
            //            请求方式
            type: 'get',
            //            method=type
            //            请求参数
            data: {
                id: id,
                pName: pName,
                url: url
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
