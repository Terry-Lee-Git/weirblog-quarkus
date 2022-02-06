$(function() {


	$("#file_upload").change(function() {
		var formData = new FormData();
		var str = $("input[type=file]").val();
		var fileName = getFileName(str);
		// var fileExt = str.substring(str.lastIndexOf('.') + 1); 
		formData.append("file", $("#file_upload")[0].files[0]);
		formData.append("name", fileName);
		// console.log("--------------"+fileName);

		$.ajax({
			url: "/posts/file-upload/" + + $("#post_id").val(),
			type: "post",
			// dataType: 'json',
			processData: false,
			contentType: false,
			data: formData
			/*success: function(responseStr) {
				console.log("--------responseStr------"+responseStr);
			},
			error: function() {
				alert("An error occured, please try again.");
			}*/
		}).done(function(d) {
			var jsonStr = JSON.stringify(d);
			var jsonData = JSON.parse(jsonStr);
			// console.log("--------jsonData------"+ jsonData.url);
			$("#show_file_upload").attr('src', jsonData.url);
			$("#post_pic").val(jsonData.url);
		}).fail(function() {

		});
	});
});

//获取文件名称
function getFileName(path) {
	var pos1 = path.lastIndexOf('/');
	var pos2 = path.lastIndexOf('\\');
	var pos = Math.max(pos1, pos2);
	if (pos < 0) {
		return path;
	}
	else {
		return path.substring(pos + 1);
	}
}