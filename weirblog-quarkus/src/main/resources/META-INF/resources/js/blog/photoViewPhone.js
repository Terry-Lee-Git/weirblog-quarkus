var total = ${fn:length(photos)};
	var phone_thumbnails = document.getElementsByName("phone_thumbnails");
	var phone_imgs = document.getElementsByName("phone_imgs");
	//console.info(total);
	var zWin = $(window);
	var render = function(){
		var tmpl = '';
		var padding = 2;
		var scrollBarWidth = 0;
		var winWidth = $(window).width();
		var picWidth = Math.floor((winWidth-padding*3-scrollBarWidth)/4);
		for(var i=0;i<total;i++){
			var p = padding;
			if(i%4==1){
				p = 0;
			}
			tmpl+='<li data-id="'+i+'" class="animated bounceIn" style="width:'+picWidth+'px;height:'+picWidth+'px;padding-left:'+p+'px;padding-top:'+padding+'px;"><img src="'+phone_thumbnails[i].value+'"></li>';
		}
		$('#container').html(tmpl);
	}
	render();
	var cid;
	var wImage = $('#large_img');
	var domImage = wImage[0];

	var loadImg = function(id,callback){
		$('#container').css({height:zWin.height(),'overflow':'hidden'})
		$('#large_container').css({
			width:zWin.width(),
			height:zWin.height()
		}).show();
		var imgsrc = phone_imgs[id].value;
		var ImageObj = new Image();
		ImageObj.src = imgsrc;
		ImageObj.onload = function(){
			var w = this.width;
			var h = this.height;
			var winWidth = zWin.width();
			var winHeight = zWin.height();
		    var realw = parseInt((winWidth - winHeight*w/h)/2);
			var realh = parseInt((winHeight - winWidth*h/w)/2);

			wImage.css('width','auto').css('height','auto');
			wImage.css('padding-left','0px').css('padding-top','0px');
			if(h/w>1.2){
				 wImage.attr('src',imgsrc).css('height',winHeight).css('padding-left',realw+'px');;
			}else{	
				 wImage.attr('src',imgsrc).css('width',winWidth).css('padding-top',realh+'px');
			}
			
			callback&&callback();
		}

		
	}
	$('#container').delegate('li','tap',function(){
		var _id = cid = $(this).attr('data-id');
		loadImg(_id);
	});

	$('#large_container').tap(function(){
		$('#container').css({height:'auto','overflow':'auto'})
		$('#large_container').hide();
	});
	$('#large_container').mousedown(function(e){
		e.preventDefault();
	});
	var lock = false;
	$('#large_container').swipeLeft(function(){
		if(cid >= total-1){
			return;
		}
		if(lock){
			return;
		}
		
		cid++;
		
		lock =true;
		loadImg(cid,function(){
			domImage.addEventListener('webkitAnimationEnd',function(){
				wImage.removeClass('animated bounceInRight');
				domImage.removeEventListener('webkitAnimationEnd');
				lock = false;
			},false);
			wImage.addClass('animated bounceInRight');
		});
	});

	$('#large_container').swipeRight(function(){
		if(lock){
			return;
		}
		cid--;
		lock =true;
		if(cid>=0){
			loadImg(cid,function(){
				domImage.addEventListener('webkitAnimationEnd',function(){
					wImage.removeClass('animated bounceInLeft');
					domImage.removeEventListener('webkitAnimationEnd');
					lock = false;
				},false);
				wImage.addClass('animated bounceInLeft');
			});
		}else{
			cid = 1;
			lock =false;
		}
	});