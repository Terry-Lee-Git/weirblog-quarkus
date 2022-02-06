var menu_row = -1;
        var menu_treeGrid;
        $(function () {
            menu_treeGrid = $('#menu_treeGrid').treegrid({
            	method: 'get',
                url: '/menu/allTree',
                idField: 'id',
                treeField: 'name',
                parentField: 'pid',
                fit: true,
                fitColumns: false,
                border: false,
                columns: [[{
                    title: '编号',
                    field: 'id',
                    width: 150,
                    hidden: true
                }, {
                    title: '菜单名称',
                    field: 'name',
                    width: 180,
                    editor: {
                        type: 'validatebox',
                        options: {
                            required: true
                        }
                    }
                }, {
                    title: 'url',
                    field: 'url',
                    width: 180
                }, {
                    field: 'pid',
                    title: '父菜单ID',
                    width: 150,
                    hidden: true
                }, {
                    field: 'pname',
                    title: '父菜单',
                    width: 80
                }]],
                toolbar: [{
                    iconCls: 'ext-icon-add',
                    text: '添加',
                    handler: function () {
                        addMenuFun('c');
                    }
                }],
                onContextMenu: function (e, row) {
                    e.preventDefault();
                    $(this).treegrid('unselectAll');
                    $(this).treegrid('select', row.id);
                    $('#menu_menu').menu('show', {
                        left: e.pageX,
                        top: e.pageY
                    });
                }
            });
        });

        function addMenuFun(m) {
            var pid = null;
            if (menu_treeGrid.treegrid('getSelected') != null) {
                var id = menu_treeGrid.treegrid('getSelected').id;
                if (m == 'p') {
                    pid = id;
                } else if (m == 'c') {
                    var node = menu_treeGrid.treegrid('getParent', id);
                    if (node != null) {
                        pid = menu_treeGrid.treegrid('getParent', id).id;
                    } else {
                        pid = null;
                    }
                }
            } else {
                pid = null;
            }
            var dialog = parent.modalDialog({
                title: '菜单添加',
                width: 500,
                height: 600,
                url: '/menu/addUI',
                buttons: [{
                    text: '添加',
                    handler: function () {
                        //console.info($(dialog.find('iframe').get(0).contentWindow.document).find("#menu_pid").val("sss"));
                        var menu_add = dialog.find('iframe').get(0).contentWindow;
                        menu_add.document.getElementById("menu_pid").value = pid;
                        //menu_add.find("#menu_pid").val(pid);

                        //console.info(menu_add.find("#menu_pid").val());
                        menu_add.menuadd_submitForm(dialog, menu_treeGrid, parent.west_tree, parent.$);
                    }
                }]
            });
        }

        function deleteMenuFun() {
            var node = menu_treeGrid.treegrid('getSelected');
            if (node) {
                parent.$.messager.confirm('确认', '您确认想要删除记录吗？', function (r) {
                    if (r) {
//                        $.post('/menu/delete/' + node.id, {id: node.id}, function (j) {
//                            if (j.success) {
//                                menu_treeGrid.treegrid('reload');
//                                parent.west_tree.tree('reload');
//                            }
//                            parent.$.messager.show({
//                                title: '提示',
//                                msg: j.msg,
//                                timeout: 5000,
//                                showType: 'slide'
//                            });
//                        }, 'json');
                        $.ajax({
                            type: 'DELETE',
                            url: '/menu/delete/' + node.id,
                            //data: {ids:""+arr,_method:'delete'},//'ids='+arr+'&_method=delete',
                            success: function(j){
                            	if (j.success) {
                                    menu_treeGrid.treegrid('reload');
                                    parent.west_tree.tree('reload');
                                }
                                parent.$.messager.show({
                                    title: '提示',
                                    msg: j.msg,
                                    timeout: 5000,
                                    showType: 'slide'
                                });
                            },
                            error: function(j){
                            	if (j.success) {
                                    menu_treeGrid.treegrid('reload');
                                    parent.west_tree.tree('reload');
                                }
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
            }
        }

        function editMenuFun() {
            var node = menu_treeGrid.treegrid('getSelected');
            var dialog = parent.modalDialog({
                title: '菜单修改',
                width: 300,
                height: 200,
                url: '/menu/editUI/' + node.id,
                buttons: [{
                    text: '提交',
                    handler: function () {
                        dialog.find('iframe').get(0).contentWindow.menuadd_submitForm(dialog, menu_treeGrid, parent.west_tree, parent.$);
                    }
                }]
            });
        }