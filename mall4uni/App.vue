<script>
	//app.js
	var http = require("./utils/http.js");
	var util = require('./utils/util.js')

	export default {
		onLaunach: function() {},
		onShow: function() {
    	console.log('mall4j.v230313')
			// 判断浏览器环境
			var ua = navigator.userAgent.toLowerCase();
			// if (ua.search(/MicroMessenger/i) > -1 && !uni.getStorageSync('appType')) {
			// 	// 微信环境
			// 	uni.setStorageSync('appType', 2)
			// 	http.mpAuthLogin()
			// }
			// const state = util.getUrlKey('state')
			// const code = util.getUrlKey('code')
			// if ((state == 'needCode' || state == 'unNeedCode') && code) {
			// 	let path = window.location.href
			// 	if (path.indexOf('code=') > 0 && path.indexOf('&state=unNeedCode') > -1) {
			// 		http.mpLogin(null, code)
			// 		path = path.substring(0, path.indexOf('code=') - 1)
			// 		history.replaceState({}, '', path)
			// 	}
				http.getCartCount()
			// }
		},
		globalData: {
			// 定义全局请求队列
			requestQueue: [],
			// 是否正在进行登陆
			isLanding: false,
			// 购物车商品数量
			totalCartCount: 0
		},
		methods: {

		}
	};
</script>
<style>
	@import "./app.css";

	/* 隐藏头部 */
	uni-page-head {
		display: none;
	}

	/* 轮播图指示点 */
	uni-swiper .uni-swiper-dots-horizontal {
		bottom: 20px !important;
	}
</style>
