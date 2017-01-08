/**
 * Module Log v2.0
 * used to print time and message in browser console
 * based on console.log
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * @copyright 2015 Jerry
 */
define(function(require, exports, module){
	//require i18n module
	var L = require('lang');

	//ployfill
	if(!('console' in window)){
		window.console = {};
	}
	if(!('log' in window.console)){
		window.console.log = function(){};
	}
	
	//utils functions
	var int2str = function(num){
		if(typeof num !== 'number'){
			return '00';
		}
		else{
			return num > 9 ? '' + num : num > 0 ? '0' + num : '00';
		}
	};
	var int3str = function(num){
		if(typeof num !== 'number'){
			return '000';
		}
		else{
			return num > 99 ? '' + num : '0' + int2str(num);
		}
	};
	var getTime = function(){
		var date = new Date();
		var timeStr = int2str(date.getHours()) + ':' +
				int2str(date.getMinutes()) + ':' +
				int2str(date.getSeconds()) + ' ' + int3str(date.getMilliseconds());
		return timeStr;
	};

	//check for browser support
	var ua = navigator.userAgent;
	var isIE = /(Trident|MSIE)/i.test(ua);
	var lowIEVersion = /MSIE\s?(\d+\.\d+)/i.exec(ua);
	var isLowIE = lowIEVersion !== null ? parseInt(lowIEVersion[1]) > 8 ? false : true : false;

	var log = console.log;

	//core function based on console.log
	var _log = function(args, color){
		var str;
		var i;
		var len;
		if(isLowIE){
			str = '[' + getTime() + ']';
			for(i = 0, len = args.length; i < len; i++){
				str += ' ' + L(args[i]);
			}
			log(str);
		}
		else{
			str = isIE ? ['[%s]', getTime()] :
				['%c[%s]%c', 'color:' + color + ';font-weight:bold;', getTime(), 'color:inherit;'];
			for(i = 0, len = args.length; i < len; i++){
				str.push(L(args[i]));
			}
			log.apply(console, str);
		}
	};

	//export functions
	if(seajsEnv.debug){
		module.exports = {
			log: function(){
				_log(arguments, 'green');
			},
			warn: function(){
				_log(arguments, 'orange');
			},
			error: function(){
				_log(arguments, 'red');
			},
			$: function(){
				if(typeof seajsEnv.$ !== 'undefined' && seajsEnv.$ === true){
				}
				else{
					seajsEnv.$ = true;
					this.log('jQuery is ready, version:', $.fn.jquery);
				}
			}
		};
	}
	else{
		module.exports = {
			log: function(){},
			warn: function(){},
			error: function(){},
			$: function(){}
		};
	}
});