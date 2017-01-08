/*!
 * Module Cookie v1.1
 * used to write, read or delete cookies
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * Copyright 2015 Jerry
 */
define(function(require, exports, module){
	"use strict";
	
	var doc = window.document;

	// Polyfill from mdn
	if (!Array.prototype.indexOf) {
		Array.prototype.indexOf = function(searchElement, fromIndex){
			var k;

			// 1. Let O be the result of calling ToObject passing
			//    the this value as the argument.
			/*jshint eqnull:true */
			if (this == null) {
				throw new TypeError('"this" is null or not defined');
			}

			var O = Object(this);

			// 2. Let lenValue be the result of calling the Get
			//    internal method of O with the argument "length".
			// 3. Let len be ToUint32(lenValue).
			var len = O.length >>> 0;

			// 4. If len is 0, return -1.
			if (len === 0) {
				return -1;
			}

			// 5. If argument fromIndex was passed let n be
			//    ToInteger(fromIndex); else let n be 0.
			var n = +fromIndex || 0;

			if (Math.abs(n) === Infinity) {
				n = 0;
			}

			// 6. If n >= len, return -1.
			if (n >= len) {
				return -1;
			}

			// 7. If n >= 0, then Let k be n.
			// 8. Else, n<0, Let k be len - abs(n).
			//    If k is less than 0, then let k be 0.
			k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);

			// 9. Repeat, while k < len
			while (k < len){
				// a. Let Pk be ToString(k).
				//   This is implicit for LHS operands of the in operator
				// b. Let kPresent be the result of calling the
				//    HasProperty internal method of O with argument Pk.
				//   This step can be combined with c
				// c. If kPresent is true, then
				//    i.  Let elementK be the result of calling the Get
				//        internal method of O with the argument ToString(k).
				//   ii.  Let same be the result of applying the
				//        Strict Equality Comparison Algorithm to
				//        searchElement and elementK.
				//  iii.  If same is true, return k.
				if (k in O && O[k] === searchElement){
					return k;
				}
				k++;
			}
			return -1;
		};
	}

	module.exports = {
		/**
		 * 获取当前所有cookie
		 * @return object
		 **/
		list: function(){
			var cookies = doc.cookie;
			var cookieArray = cookies.split('; ');
			var cookie = {};
			for(var i=0;i<cookieArray.length;i++){
				var kv = cookieArray[i].split('=');
				cookie[kv[0]] = kv[1];
			}
			return cookie;
		},
		/**
		 * @param: string key
		 * @return: object|string 
		 */
		get: function(key){
			var cookie = this.list();
			// log('c&k, ', cookie, key);
			if(key !== undefined){
				// log('ret', cookie[key])
				return cookie[key];
			}
			else{
				return cookie;
			}
		},

		/**
		 * 设置修改cookie(覆盖)
		 * @param: string key
		 * @param: string|number value
		 */
		set: function(key, value, time){
			var str = key + '=' + escape(value);
			if(typeof time === 'number' && time > 0){
			}
			else{
				time = 30;
			}
			var date = new Date();
			var ms = time * 3600 * 1000;
			date.setTime(date.getTime() + ms);
			str += '; expires' + date.toGMTString();
			doc.cookie = str;

			return this.get(key);
		},
		/**
		 * 设置修改cookie(追加)
		 * @param: string key
		 * @param: string|number value
		 */
		add: function(key, value){
			var oldValue = this.get(key);
			if(oldValue === undefined){
				return this.set(key, value);
			}
			else if(oldValue === ''){
				return this.set(key, value);
			}
			else{
				var oldValueArr = oldValue.split(',');
				var index = oldValueArr.indexOf(escape(value));
				if(index < 0){
					oldValueArr.push(escape(value));
				}
				var newValue = oldValueArr.join(',');
				var str = key + '=' + newValue + '; ';
				doc.cookie = str;
				return this.get(key);
			}
		},
		/**
		 *
		 *
		 */
		reply: function(key, value){
			var oldValue = this.get(key);
			var nv = [];
			var tmp = oldValue.split(',');
			for(var i=0;i<tmp.length;i++){
				if(tmp[i] !== value.toString()){
					nv.push(tmp[i]);
				}
			}
			var newValue = nv.join(',');
			var str = key + '=' + newValue + '; ';
			doc.cookie = str;
			return this.get(key);
		},
		/**
		 * 删除cookie
		 * @param: string key
		 * @param: object cookie list
		 */
		del: function(key){
			doc.cookie = key+"=;expires="+(new Date(0)).toGMTString();
			return this.list();
		}
	};
});