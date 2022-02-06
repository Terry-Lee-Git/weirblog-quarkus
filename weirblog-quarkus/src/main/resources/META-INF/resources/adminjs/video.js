var videoDataGrid;
        $(function () {
            videoDataGrid = $('#video_datagrid').datagrid({
            	method:'get',
                url: '/video/list',
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
                    hidden: true
                }, {
                    field: 'vname',
                    title: '名称',
                    width: 100
                }, {
                    field: 'vdesc',
                    title: '简介',
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
                        videoAdd();
                    }
                }, '-', {
                    text: '编辑',
                    iconCls: 'ext-icon-pencil',
                    handler: function () {
                        videoEdit();
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'ext-icon-pencil_delete',
                    handler: function () {
                        videoDelete();
                    }
                }],
                onRowContextMenu: function (e, rowIndex, rowData) {
                    e.preventDefault();
                    $(this).datagrid('unselectAll');
                    $(this).datagrid('selectRow', rowIndex);
                    $('#video_menu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            });

        });

        function videoAdd() {
            var dialog = parent.modalDialog({
                title: '视频添加',
                width: 350,
                height: 350,
                url: '/video/addUI',
                buttons: [{
                    text: '添加',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.videoadd_submitForm(dialog, videoDataGrid, parent.$);
                    }
                }]
            });
        }

        function videoEdit() {
            var rows = videoDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '视频修改',
                    width: 350,
                    height: 350,
                    url: '/video/editUI/' + rows[0].id,
                    buttons: [{
                        text: '添加',
                        handler: function () {
                            dialog.find('iframe').get(0).contentWindow.videoadd_submitForm(dialog, videoDataGrid, parent.$);
                        }
                    }]
                });
            } else {
                parent.$.messager.alert('提示', '请选择一条记录进行修改');
            }
        }

        function videoDelete() {
            var rows = videoDataGrid.datagrid('getChecked');
            var ids = [];
            if (rows.length > 0) {
                $.messager.confirm('确认', '您确认想要删除记录吗？', function (r) {
                    if (r) {
                        for (var i = 0; i < rows.length; i++) {
                            ids.push(rows[i].videoId);
                        }
                        $.post('/video/delete', {videoIds: ids.join(',')}, function (j) {
                            if (j.success) {
                                videoDataGrid.datagrid('load');
                                $('#admin_addvideo').dialog('close');
                            }
                            videoDataGrid.datagrid('uncheckAll');
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

        function videoUpload() {
            var rows = videoDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '视频添加',
                    width: 550,
                    height: 460,
                    url: '/video/uploadUI?id=' + rows[0].id
                });
            } else {
                parent.$.messager.alert('提示', '请选择一个相册进行上传');
            }

        }

        function videoImgView() {
            var rows = videoDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                var dialog = parent.modalDialog({
                    title: '视频浏览',
                    width: 800,
                    height: 500,
                    url: '/video/view?id=' + rows[0].id
                });
            } else {
                parent.$.messager.alert('提示', '请选择一个相册');
            }

        }