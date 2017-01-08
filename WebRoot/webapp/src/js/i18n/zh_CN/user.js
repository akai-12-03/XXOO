define(function(require, exports, module){
	var lang = {
	};
	module.exports = function(str){
		if(typeof str === 'string' && typeof lang[str] === 'string'){
			return lang[str];
		}
		else{
			return str;
		}
	};
});