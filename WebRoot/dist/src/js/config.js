(function(){
	var int2str = function(num){
		if(typeof num !== 'number'){
			return '00';
		}
		else{
			return num > 9 ? num.toString() : num > 0 ? '0' + num : '00';
		}
	};
	var int3str = function(num){
		if(typeof num !== 'number'){
			return '000';
		}
		else{
			return num > 99 ? num.toString() : '0' + int2str(num);
		}
	};
	var getTime = function(){
		var date = new Date();
		var timeStr = int2str(date.getHours()) + ':' +
				int2str(date.getMinutes()) + ':' +
				int2str(date.getSeconds()) + ' ' + int3str(date.getMilliseconds());
		return timeStr;
	};

	var ua = navigator.userAgent;
	var isIE = /(Trident|MSIE)/i.test(ua);
	var lowIEVersion = /MSIE\s?(\d+\.\d+)/i.exec(ua);
	var isLowIE = lowIEVersion !== null ? parseInt(lowIEVersion[1]) > 8 ? false : true : false;
	var log = console && console.log || function(){};
	var error = function(){
		var args = arguments;
		var str;
		var i;
		var len;
		if(isLowIE){
			str = '[' + getTime() + ']';
			for(i = 0, len = args.length; i < len; i++){
				str += ' ' + args[i];
			}
			log(str);
		}
		else{
			str = isIE ? ['[%s]', getTime()] :
				['%c[%s]%c', 'color:red;font-weight:bold;', getTime(), 'color:inherit;'];
			for(i = 0, len = args.length; i < len; i++){
				str.push(args[i]);
			}
			log.apply(console, str);
		}
	};

	if(typeof ROOT_URL !== 'string'){
		error('base url is not defined');
	}
	else{
		var jquery = 'jquery-1.11.3.min.js';
		var raphael = 'raphael' + (seajsEnv.mode === 'development' ? '-2.1.4.js' : '-2.1.4.min.js');
		var eve = 'eve.min.js';
		if(typeof seajs !== 'undefined'){
			window.seajsEnv = window.seajsEnv || {
				'mode': 'development',
				//'mode': 'production',
				'debug': !true
			};
			seajs.config({
				'base': ROOT_URL + (seajsEnv.mode === 'development' ? '/dist/src' : '/dist/public') + '/js',
				'vars': {
					'locate': 'zh_CN'
				},
				'alias': {
					'jQuery': ROOT_URL + '/dist/public/libs/js/' + jquery,
					'eve': ROOT_URL + '/public/libs/js/' + eve,

					'utils': 'modules/utils',
					'polyfill': 'modules/polyfill',
					'lang': 'i18n/{locate}/sys',
					'log': 'modules/log',
					'cookie': 'modules/cookie',
					'onlynum': 'libs/only-number',

					'validator': 'modules/validator',
					'form': 'modules/form',
					'verify-tel': 'modules/verify-tel',
					'verify-img': 'modules/verify-img',
					'verify-email': 'modules/verify-email',
					'tab': 'modules/tab',
					'ring': 'modules/ring',
					'accordion': 'modules/accordion',
					'slides': 'modules/slides',
					'editor': 'modules/editor',
					'progress': 'modules/progress',
					'lightbox': 'modules/fancybox',
					'dialog': 'modules/dialog',
					'page': 'modules/page',
					'fullpage': 'modules/fullpage',

					//css
					'fancyboxCss': [ ROOT_URL +'/public/lib/fancybox/jquery.fancybox.css', ROOT_URL + '/public/lib/fancybox/helpers/jquery.fancybox-buttons.css'],
				},
				preload: [
					Function.prototype.bind ? '' : 'polyfill',
				]
			});
			seajs.use('log', function(_){
				_.log('Seajs is ready, version:', seajs.version);
			});
		}
		else{
			error('Can not find Seajs. Please check the network or script tags.');
		}
	}
})();
