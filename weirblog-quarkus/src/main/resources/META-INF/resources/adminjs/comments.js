var commentsDataGrid;
$(function() {
	commentsDataGrid = $('#comments_datagrid').datagrid({
		method:'get',
		url : '/comments/list',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		idField : 'id',
		pagePosition : 'both',
		checkOnSelect:true,
		selectOnCheck:true,
		pageSize:5,
		pageList:[5,10,20],
		columns : [ [ {
			field : 'id',
			title : '编号',
			width : 100,
			checkbox : true
		}, {
			field : 'postsId',
			title : '文章ID',
			width : 20
		}, {
			field : 'name',
			title : '评论者',
			width : 50
		}, {
			field : 'IP',
			title : 'IP',
			width : 50
		}, {
			field : 'createDate',
			title : '评论时间',
			width : 50,
			formatter: function(value,row,index){
				return getSmpFormatDateByLong(value,true);
			}
		}, {
			field : 'profileImageUrl',
			title : '头像地址',
			width : 100
		}, {
			field : 'content',
			title : '内容',
			width : 100
		}] ],
		toolbar : [ {
			text : '增加',
			iconCls : 'ext-icon-add',
			handler : function() {
				commentsAdd();
			}
		}, '-', {
			text : '编辑',
			iconCls : 'ext-icon-pencil',
			handler : function() {
				commentsEdit();
			}
		}, '-', {
			text : '删除',
			iconCls : 'ext-icon-pencil_delete',
			handler : function() {
				commentsDelete();
			}
		}],
		onRowContextMenu:function(e, rowIndex, rowData){
			e.preventDefault();
			$(this).datagrid('unselectAll');
			$(this).datagrid('selectRow',rowIndex);
			$('#comments_menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});
	
});

function commentsAdd() {
	var dialog = parent.modalDialog({
		title : '用户添加',
		width : 350,
		height : 300,
		url : '/admin/comments_add',
		buttons : [ {
			text : '添加',
			handler : function() {
				dialog.find('iframe').get(0).contentWindow.commentsadd_submitForm(dialog, commentsDataGrid, parent.$);
			}
		} ]
	});
}

function commentsEdit(){
	var rows = commentsDataGrid.datagrid('getChecked');
	if(rows.length==1){
		var dialog = parent.modalDialog({
			title : '用户修改',
			width : 350,
			height : 300,
			url : '/comments/editUI?id='+rows[0].id,
			buttons : [ {
				text : '添加',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.commentsedit_submitForm(dialog, commentsDataGrid, parent.$);
				}
			} ]
		});
	}else{
		parent.$.messager.alert('提示','请选择一条记录进行修改');
	}
}
function editcommentsForm(){
	var rows = commentsDataGrid.datagrid('getChecked');
	if(rows.length==1){
		if ($("#admin_editcommentsForm").form('validate')) {
			$.post('/comments/edit', $("#admin_editcommentsForm").serialize(), function(j) {
				if (j.success) {
					commentsDataGrid.datagrid('load');
					$('#admin_editcomments').dialog('close');
					commentsDataGrid.datagrid('uncheckAll');
				}
				$.messager.show({
					title : '提示',
					msg : j.msg,
					timeout : 5000,
					showType : 'slide'
				});
			}, 'json');
		}
	}
}

function commentsDelete(){
	var rows = commentsDataGrid.datagrid('getChecked');
	var ids = [];
	if(rows.length>0){
		$.messager.confirm('确认','您确认想要删除记录吗？',function(r){
		    if (r){
		    	for(var i=0;i<rows.length;i++){
					ids.push(rows[i].id);
				}
				$.post('/comments/delete', {commentsIds:ids.join(',')}, function(j) {
					if (j.success) {
						commentsDataGrid.datagrid('load');
						$('#admin_addcomments').dialog('close');
					}
					commentsDataGrid.datagrid('uncheckAll');
					$.messager.show({
						title : '提示',
						msg : j.msg,
						timeout : 5000,
						showType : 'slide'
					});
				}, 'json');
		    }    
		});
	}else{
		$.messager.show({
			title : '提示',
			msg : '请勾选要删除的记录',
			timeout : 5000,
			showType : 'slide'
		});
	}
}