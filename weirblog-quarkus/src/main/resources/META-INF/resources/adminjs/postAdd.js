
KindEditor.lang({
	 audio : 'MP3'
	});
	 KindEditor.options.htmlTags['audio'] = ['src','controls','autoplay','type'];
	 KindEditor.options.htmlTags['source'] = ['src','controls','autoplay','type'];

	 KindEditor.ready(function(K) {
//		 alert($("#post_id").val());
		 editor = K.create('textarea[name="content"]', {
			 cssPath : '/kindeditor-4.1.10/plugins/code/prettify.css',
			 filePostName: "file",
				uploadJson : '/kindeditor/file-upload/' + $("#post_id").val(),
				fileManagerJson : '/kindeditor/file-manager',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				},
		   items : [
		     'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
		     'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		     'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
		     'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
		     'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		     'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
		      '|' ,'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
		     'anchor', 'link', 'unlink', '|', 'about','audio','media'
		   ]
		 });
			prettyPrint();
		});
