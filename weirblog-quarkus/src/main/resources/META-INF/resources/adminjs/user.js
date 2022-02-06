var userDataGrid;
        $(function () {
            userDataGrid = $('#user_datagrid').datagrid({
            	method: 'get',
                url: '/user/list',
                fit: true,
                fitColumns: true,
                border: false,
                pagination: true,
                idField: 'id',
                pagePosition: 'both',
                checkOnSelect: true,
                selectOnCheck: true,
                pageSize: 5,
                pageList: [5, 10, 20],
                columns: [[{
                    field: 'id',
                    title: '编号',
                    width: 100,
                    checkbox: true
                }, {
                    field: 'userName',
                    title: '用户名',
                    width: 100,
                    sortable: true
                }, {
                    field: 'email',
                    title: '邮箱',
                    width: 100,
                    sortable: true
                }]],
                toolbar: [{
                    text: '增加',
                    iconCls: 'ext-icon-add',
                    handler: function () {
                        userAdd();
                    }
                }, '-', {
                    text: '编辑',
                    iconCls: 'ext-icon-pencil',
                    handler: function () {
                        userEdit();
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'ext-icon-pencil_delete',
                    handler: function () {
                        userDelete();
                    }
                }],
                onRowContextMenu: function (e, rowIndex, rowData) {
                    e.preventDefault();
                    $(this).datagrid('unselectAll');
                    $(this).datagrid('selectRow', rowIndex);
                    $('#user_menu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            });

        });

        function userAdd() {
            var dialog = parent.modalDialog({
                title: '用户添加',
                width: 350,
                height: 300,
                url: '/admin/user_add.jsp',
                buttons: [{
                    text: '添加',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.useradd_submitForm(dialog, userDataGrid, parent.$);
                    }
                }]
            });
        }

        function userEdit() {
            var rows = userDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '用户修改',
                    width: 350,
                    height: 300,
                    url: '/user/editUI?id=' + rows[0].userId,
                    buttons: [{
                        text: '添加',
                        handler: function () {
                            dialog.find('iframe').get(0).contentWindow.useredit_submitForm(dialog, userDataGrid, parent.$);
                        }
                    }]
                });
            } else {
                parent.$.messager.alert('提示', '请选择一条记录进行修改');
            }
        }

        function editUserForm() {
            var rows = userDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                if ($("#admin_editUserForm").form('validate')) {
                    $.post('/user/edit', $("#admin_editUserForm").serialize(), function (j) {
                        if (j.success) {
                            userDataGrid.datagrid('load');
                            $('#admin_editUser').dialog('close');
                            userDataGrid.datagrid('uncheckAll');
                        }
                        $.messager.show({
                            title: '提示',
                            msg: j.msg,
                            timeout: 5000,
                            showType: 'slide'
                        });
                    }, 'json');
                }
            }
        }

        function userDelete() {
            var rows = userDataGrid.datagrid('getChecked');
            var ids = [];
            if (rows.length > 0) {
                $.messager.confirm('确认', '您确认想要删除记录吗？', function (r) {
                    if (r) {
                        for (var i = 0; i < rows.length; i++) {
                            ids.push(rows[i].userId);
                        }
                        $.post('/user/delete', {userIds: ids.join(',')}, function (j) {
                            if (j.success) {
                                userDataGrid.datagrid('load');
                                $('#admin_addUser').dialog('close');
                            }
                            userDataGrid.datagrid('uncheckAll');
                            $.messager.show({
                                title: '提示',
                                msg: j.msg,
                                timeout: 5000,
                                showType: 'slide'
                            });
                        }, 'json');
                    }
                });
            } else {
                $.messager.show({
                    title: '提示',
                    msg: '请勾选要删除的记录',
                    timeout: 5000,
                    showType: 'slide'
                });
            }
        }