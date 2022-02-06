function item_masonry(){ 
	$('.item img').load(function(){ 
		$('.infinite_scroll').masonry({ 
			itemSelector: '.masonry_brick',
			columnWidth:231,
			gutterWidth:19								
		});		
	});
		 //columnWidth 函数递增控制div左边距
	$('.infinite_scroll').masonry({ 
		itemSelector: '.masonry_brick',
		columnWidth:231,
		gutterWidth:19								
	});	
}
$(function(){
//滚动条下拉事件
	function item_callback(){
		$('.item').mouseover(function(){
			$(this).css('box-shadow', '0 1px 5px rgba(35,25,25,0.5)');
			//alert(1);
			$('.btns',this).show();
		}).mouseout(function(){
			$(this).css('box-shadow', '0 1px 3px rgba(34,25,25,0.2)');
			$('.btns',this).hide();		 	
		});
		item_masonry();	
	}
	item_callback();  
	$('.item').fadeIn();
	var sp = 1
	$(".infinite_scroll").infinitescroll({
		navSelector  	: "#more",
		nextSelector 	: "#more a",
		itemSelector 	: ".item",
		loading:{
			img: "images/masonry_loading_1.gif",
			msgText: '正在加载中....',
			finishedMsg: '木有了,看看下一页',
			finished: function(){
				sp++;
				if(sp>=10){ //到第10页结束事件
					$("#more").remove();
					$("#infscr-loading").hide();
					$("#pagebox").show();
					$(window).unbind('.infscr');
				}
			}	
		},errorCallback:function(){ 
			$("#pagebox").show();
		}
	},function(newElements){
		var $newElems = $(newElements);
		$('.infinite_scroll').masonry('appended', $newElems, false);
		$newElems.fadeIn();
		item_callback();
		return;
	});
});
function setTop(imgId){
	$.get('/photo/topImg/' + imgId, function(j) {
		if (j.success) {
			alert("封面设置成功");
		}else{
			alert("封面设置失败");
		}
	}, 'json');
}
function setDelete(imgId){
	if(window.confirm('你确定要删除吗？')){
		$.get('/photo/deletePhoto/' + imgId, function(j) {
			if (j.success) {
				alert("图片删除成功");
			}else{
				alert("图片删除失败");
			}
		}, 'json');
        return true;
     }else{
        return false;
    }
	
}