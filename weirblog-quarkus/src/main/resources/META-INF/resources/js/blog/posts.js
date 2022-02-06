$(function() {
		$.post('${pageContext.request.contextPath}/posts/pnum', {
			id : ${posts.id}
		}, function(j) {
		}, 'json');
	});
	
	function submitComment(){
		if(flagEamil){
			var inputCheck = $("#inputCheck").val();
			var inputName = $("#inputName").val();
			if(inputCheck ==null || inputCheck.trim() ==""){
				$("#error").show();
				$("#error").html("您不打算验证了么？");
				return;
			}
			if(inputName ==null || inputName.trim() ==""){
				$("#error").show();
				$("#error").html("请输入您的网名");
				return;
			}
		}
		
		var inputContent = $("#inputContent").val();
		if(inputContent==null || inputContent.trim().length==0){
			$("#error").show();
			$("#error").html("你就不想说点什么");
			$("#inputContent").focus();
			return;
		}
		
		$("#commentsubmit").attr('disabled',true);
		$.post('${pageContext.request.contextPath}/comments/add', $("#commentform").serialize(), function(j) {
			if (j.success) {
				window.location.reload();
			}else{
				$("#error").show();
				$("#error").html(j.msg);
			}
		}, 'json');
	}
	var flagEamil = true;
	function sendEmailCheck(){
		var email = $("#inputEmail").val();
		if(email !=null && email.trim() !="" && email.length>0){
			$.post('${pageContext.request.contextPath}/user/checkemail', {email:email}, function(j) {
				if(j.success){
					if(j.msg!="Y"){
						$("#showCheck").show();
						$("#error").show();
						$("#error").html("您的邮箱是新邮箱，请您验证,验证码已发出，请在2分钟内验证");
						sendMessage();
					}else{
						flagEamil=false;
						$("#error").show();
						$("#error").html("您的邮箱已通过验证，可以直接评论了");
						$("#btnSendCode").attr("disabled", "true");
						$("#commentsubmit").removeAttr("disabled");
					}
				}else{
					$("#error").show();
					$("#error").html(j.msg);
				}
			}, 'json');
		}else{
			$("#error").html("邮箱不能为空");
			$("#error").show();
		}
	}
	function checkImg(){
		var inputCheck = $("#inputCheck").val();
		if(inputCheck !=null && inputCheck.trim() !="" && inputCheck.length>0){
			$.post('${pageContext.request.contextPath}/user/checkImg', {checkImg:inputCheck}, function(j) {
				if(j.success){
					$("#error").show();
					$("#error").html(j.msg);
					$("#commentsubmit").removeAttr("disabled");
				}else{
					$("#error").show();
					$("#error").html("验证码错误，请确认您的验证码");
					$("#inputCheck").focus();
				}
				
			}, 'json');
		}else{
			$("#error").html("验证码不能为空");
			$("#error").show();
			$("#inputCheck").focus();
		}
		
	}
	function reply(name){
		$("#inputContent").html("@"+name);
		$("#inputEmail").focus();
	}
	
	
	var InterValObj; // timer变量，控制时间
	var count = 120; // 间隔函数，1秒执行
	var curCount;// 当前剩余秒数

	function sendMessage() {
		var email = $("#inputEmail").val();
		if(email !=null && email.trim() !="" && email.length>0){
			curCount = count;
			$("#btnSendCode").attr("disabled", "true");
		    $("#btnSendCode").html("请在" + curCount + "秒内输入验证码");
		    InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次
		    // sendEmailCheck();
		}else{
			$("#error").html("邮箱不能为空");
			$("#error").show();
		}
	}

	// timer处理函数
	function SetRemainTime() {
	            if (curCount == 0) {                
	                window.clearInterval(InterValObj);// 停止计时器
	                $("#btnSendCode").removeAttr("disabled");// 启用按钮
	                $("#btnSendCode").html("重新发送验证码");
	            }
	            else {
	                curCount--;
	                $("#btnSendCode").html("请在" + curCount + "秒内输入验证码");
	            }
	        }