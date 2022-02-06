var postsDataGrid;
        $(function () {
            postsDataGrid = $('#posts_datagrid').datagrid({
            	method:'get',
                url: '/posts/list',
                fit: true,
                fitColumns: true,
                border: false,
                pagination: true,
                idField: 'id',
                pagePosition: 'both',
                checkOnSelect: true,
                selectOnCheck: true,
                pageSize: 10,
                pageList: [10, 20, 30],
                columns: [[{
                    field: 'id',
                    title: '编号',
                    width: 10,
                    checkbox: true
                }, {
                    field: 'postTitle',
                    title: '标题',
                    width: 200
                }, {
                    field: 'postPic',
                    title: '首图',
                    width: 60,
                    formatter: function (value, row, index) {
	                    if(value){
							return "<img src='"+value+"'style='width:100px;height:40px'>";
						}else{
							return null;
						}
                    }
                },{
                    field: 'createDate',
                    title: '创建时间',
                    width: 60,
                    formatter: function (value, row, index) {
                        return getSmpFormatDateByLong(value, true);
                    }
                }, {
                    field: 'updateDate',
                    title: '修改时间',
                    width: 60,
                    formatter: function (value, row, index) {
                        if (value != undefined) {
                            return getSmpFormatDateByLong(value, true);
                        }

                    }
                }, {
                    field: 'types',
                    title: '类型',
                    width: 100
                }, {
                    field: 'readNum',
                    title: '访问量',
                    width: 50
                }, {
                    field: 'commNum',
                    title: '评论量',
                    width: 50
                }]],
                toolbar: [{
                    text: '增加',
                    iconCls: 'ext-icon-add',
                    handler: function () {
                        postsAdd();
                    }
                }, '-', {
                    text: '编辑',
                    iconCls: 'ext-icon-pencil',
                    handler: function () {
                        postsEdit();
                    }
                }, '-', {
                    text: '删除',
                    iconCls: 'ext-icon-pencil_delete',
                    handler: function () {
                        postsDelete();
                    }
                }],
                onRowContextMenu: function (e, rowIndex, rowData) {
                    e.preventDefault();
                    $(this).datagrid('unselectAll');
                    $(this).datagrid('selectRow', rowIndex);
                    $('#posts_menu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            });

        });

        function userEdit() {

            var rows = postsDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                location.href = '/posts/post-edit/' + rows[0].id;
            } else {
                parent.$.messager.alert('提示', '请选择一条记录进行修改');
            }
        }
        function userShowUpload(){
	       var rows = postsDataGrid.datagrid('getChecked');
            if (rows.length == 1) {
                // location.href = '/posts/post-show/' + rows[0].id;
                var dialog = parent.modalDialog({
                    title: '首图修改',
                    width: 500,
                    height: 400,
                    url: '/posts/post-show/' + rows[0].id,
                    buttons: [{
                        text: '提交',
                        handler: function () {
                            dialog.find('iframe').get(0).contentWindow.photoadd_submitForm(dialog, postsDataGrid, parent.$);
                        }
                    }]
                });
            } else {
                parent.$.messager.alert('提示', '请选择一条记录进行修改');
            }
}