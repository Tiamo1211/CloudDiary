<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="col-md-9">
    <div class="data_list">
        <div class="data_list_title">
            <span class="glyphicon glyphicon-list"></span>类型列表
            <span class="noteType_add">
            <button class="btn btn-sm btn-success" type="button" id="addBtn">添加类别</button>
        </span>
        </div>
        <div id="myDiv">

            <c:if test="${empty typeList}">
                <h2>暂未查询到类型数据！</h2>
            </c:if>
            <c:if test="${!empty typeList}">
            <table id="myTable" class="table table-hover table-striped">
                <tbody>
                <tr>
                    <th>编号</th>
                    <th>类型</th>
                    <th>操作</th>
                </tr>
                <c:forEach items="${typeList}" var="item">
                    <tr id="tr_${item.typeId}">
                        <td>${item.typeId}</td>
                        <td>${item.typeName}</td>
                        <td>
                            <button class="btn btn-primary" type="button" onclick="openUpdateDialog(${item.typeId})">修改</button>&nbsp;
                            <button class="btn btn-danger del" type="button" onclick="deleteType(${item.typeId})">删除</button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:if>
        </div>
    </div>
    <!-- 模态框（Modal） 添加和修改 -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;
                        </button>
                    <h4 class="modal-title" id="myModalLabel">新增</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="typename">类型名称</label>
                        <input type="hidden" name="typeId" id="typeId">
                        <input type="text" name="typename" class="form-control" id="typeName" placeholder="类型名称">
                    </div>
                </div>
                <div class="modal-footer">
                    <span id="msg" style="font-size: 12px; color: red"></span>
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span class="glyphicon glyphicon-remove"></span>关闭</button>
                    <button type="button" class="btn btn-primary" id="btn_submit">
                        <span class="glyphicon glyphicon-floppy-disk"></span>保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <script type="text/javascript" src="statics/js/type.js">

    </script>
</div>











