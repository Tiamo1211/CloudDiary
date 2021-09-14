	//登录页图片切换
	var currentindex = 1;
	
	$(function(){
		$("#flash").css("background-image","url("+$("#flash1").attr("bgUrl")+")");//设置banner背景颜色名称 这里的flash就是div的ID
	});		

	
	function changeflash(i) {	
		currentindex=i;
		for (j=1;j<=2;j++){//此处的2代表你想要添加的幻灯片的数量与下面的5相呼应
			if (j==i){
				$("#flash"+j).fadeIn("normal");
				$("#flash"+j).css("display","block");
				$("#f"+j).removeClass();
				$("#f"+j).attr("class","dq");
				//alert("#f"+j+$("#f"+j).attr("class"))
				$("#flash").css("background-image","url("+$("#flash"+j).attr("bgUrl")+")");
			}else{
				$("#flash"+j).css("display","none");
				$("#f"+j).removeClass();
				$("#f"+j).attr("class","no");
			}
		}
	}
	function startAm(){
		timerID = setInterval("timer_tick()",2000);//8000代表间隔时间设置
	}
	function stopAm(){
		clearInterval(timerID);
	}
	function timer_tick() {
		currentindex = currentindex >=2 ? 1 : currentindex + 1;//此处的5代表幻灯片循环遍历的次数
		changeflash(currentindex);
	}
	$(document).ready(function(){
		$(".flash_bar div").mouseover(function(){
			stopAm();
		}).mouseout(function(){
			startAm();
		});
		startAm();
	});
	
	// input
	$(function(){
		//鼠标焦点
		$(":input.user").focus(function(){
			$(this).addClass("userhover");                          
		}).blur(function(){
			$(this).removeClass("userhover")
		});
		$(":input.pwd").focus(function(){
			$(this).addClass("pwdhover");                          
		}).blur(function(){
			$(this).removeClass("pwdhover")
		});
		
		//输入用户名
		$(".user").focus(function(){
			var username = $(this).val();
			if(username == ''){
			   $(this).val('');
			}
		 });
		 $(".user").blur(function(){
			var username = $(this).val();
			if(username == ''){
			   $(this).val('');
			}
		 });
		 
		 //输入密码
		 $(".pwd").focus(function(){
			var password = $(this).val();
			if(password == ''){
			   $(this).val('');
			}
		 });
		 $(".pwd").blur(function(){
			var password = $(this).val();
			if(password == ''){
			   $(this).val('');
			}
		 });
	
	});


	/**
	 * 用户登录表单校验
	 * 	1. 获取用户名称与密码
	 * 	2. 判断用户名或密码是否为空
	 * 		如果为空，提示用户
	 *  3. 如果不为空，提交表单
	 *
	 *  注：表单元素需要设置那么属性值
	 */
	function checkLogin() {

		// 1. 获取用户名称与密码
		var userName = $("#userName").val(); // 用户名
		var userPwd = $("#userPwd").val(); // 密码

		// 2. 判断用户名或密码是否为空
		/*if (isEmpty(userName)) {
			// 如果为空，则提示用户
			$("#msg").html("用户名称不能为空！");
			return;
		}
		if (isEmpty(userPwd)) {
			// 如果为空，则提示用户
			$("#msg").html("用户密码不能为空！");
			return;
		}*/

		// 3. 如果不为空，提交表单
		$("#loginForm").submit();
	}