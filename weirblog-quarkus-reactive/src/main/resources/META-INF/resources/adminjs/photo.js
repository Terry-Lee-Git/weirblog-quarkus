var photoDataGrid;
        $(function () {
            photoDataGrid = $('#photo_datagrid').datagrid({
            	method:'get',
                url: '/photo/list',
                fit: true,
                fitColumns: true,
                border: false,
                pagination: true,
                idField: 'id',
                pagePosition: 'both',
                checkOnSelect: true,
                selectOnCheck: true,
                pageSize: 10,
                pageList: [10, 20, 50],
                columns: [[{
                    field: 'id',
                    title: '编号',
                    width: 100,
                    hidden: true
                }, {
                    field: 'name',
                    title: '相册名称',
                    width: 100
                }, {
                    field: 'createDate',
                    title: '创建日期',
                    width: 100,
                    formatter: function (value, row, index) {
                        return getSmpFormatDateByLong(value, true);
                    }
                }, {
                    field: 'updateDate',
                    title: '修改日期',
                    width: 100,
                    formatter: function (value, row, index) {
                        if (value != null) {
                            return getSmpFormatDateByLong(value, true);
                        }
                    }
                }]],
                toolbar: [{
                    text: '增加',
                    iconCls: 'ext-icon-add',
                    handler: function () {
                        photoAdd();
                    }
                }, '-', {
                    text: '编辑',
                    iconCls: 'ext-icon-pencil',
                    handler: function () {
                        photoEdit();
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'ext-icon-pencil_delete',
                    handler: function () {
                        photoDelete();
                    }
                }],
                onRowContextMenu: function (e, rowIndex, rowData) {
                    e.preventDefault();
                    $(this).datagrid('unselectAll');
                    $(this).datagrid('selectRow', rowIndex);
                    $('#photo_menu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            });

        });

        function photoAdd() {
            var dialog = parent.modalDialog({
                title: '相册添加',
                width: 350,
                height: 200,
                url: '/photo/addUI',
                buttons: [{
                    text: '添加',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.photoadd_submitForm(dialog, photoDataGrid, parent.$);
                    }
                }]
            });
        }

        function photoEdit() {
            var rows = photoDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '用户修改',
                    width: 350,
                    height: 200,
                    url: '/photo/editUI/' + rows[0].id,
                    buttons: [{
                        text: '添加',
                        handler: function () {
                            dialog.find('iframe').get(0).contentWindow.photoadd_submitForm(dialog, photoDataGrid, parent.$);
                        }
                    }]
                });
            } else {
                parent.$.messager.alert('提示', '请选择一条记录进行修改');
            }
        }

        function photoDelete() {
            var rows = photoDataGrid.datagrid('getChecked');
            var ids = [];
            if (rows.length > 0) {
                $.messager.confirm('确认', '您确认想要删除记录吗？', function (r) {
                    if (r) {
                        for (var i = 0; i < rows.length; i++) {
                            ids.push(rows[i].id);
                        }
                        $.ajax({
                            type: 'DELETE',
                            url: '/photo/delete',
                            data: {photoIds:ids.join(',')},
                            success: function(j){
                            		photoDataGrid.datagrid('load');
                                    $('#admin_addphoto').dialog('close');
	                            	photoDataGrid.datagrid('uncheckAll');
	                                parent.$.messager.show({
	                                    title: '提示',
	                                    msg: j.msg,
	                                    timeout: 5000,
	                                    showType: 'slide'
	                                });
                            },
                            error: function(j){
                            	photoDataGrid.datagrid('load');
                                $('#admin_addphoto').dialog('close');
                            	photoDataGrid.datagrid('uncheckAll');
                                parent.$.messager.show({
                                    title: '提示',
                                    msg: j.msg,
                                    timeout: 5000,
                                    showType: 'slide'
                                });
                            }
                        });
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

        function photoUpload() {
            var rows = photoDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '相册添加',
                    width: 550,
                    height: 460,
                    url: '/photo/uploadUI/' + rows[0].id
                });
            } else {
                parent.$.messager.alert('提示', '请选择一个相册进行上传');
            }

        }

        function photoImgView() {
            var rows = photoDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '相册浏览',
                    width: 800,
                    height: 500,
                    url: '/photo/view/' + rows[0].id
                });
            } else {
                parent.$.messager.alert('提示', '请选择一个相册');
            }

        }