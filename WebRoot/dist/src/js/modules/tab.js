/**
 * Module Tab v2.0
 * a simple tab module
 * based on jQuery
 *
 * @author: Jerry
 * @email: superzcj_001@163.com
 * @copyright 2015 Jerry
 */
define(function(require, exports, module){
	"use strict";

	var $ = require('jQuery');
	var _ = require('log');

	var log = _.log;
	var error = _.error;
	if(typeof $ !== 'undefined' && typeof $.fn !== 'undefined' && typeof $.fn.jquery === 'string'){
		_.$();
		log('Module Tab is loading...');

		var Tab = function(selector, option){
			log('tab is initializing...');
			this.selector = selector || '.tab';

			if(typeof this.selector === 'string'){
				this.tabs = $(this.selector);
			}
			else{
				return this;
			}

			if(this.tabs.length === 0){
				error('Can not find tab container, please initialize tab by yourself.','[' + this.selector + ']');
				return this;
			}

			var ret = this._init(this.tabs, option);
			log('tab is initialized.');
			return ret;
		};

		Tab.prototype = {
			_init: function($tabs, option){
				var $this = this;
				if($tabs.length > 1){
					$tabs.each(function(index, el){
						var $el = $(el);
						$this._init($el, option);
					});
					return $tabs;
				}
				var $headers = $tabs.children('.tab-header');
				if($headers.length === 0){
					error('Can not find tab headers, please check your html', $tabs);
					return $this;
				}
				$headers.on('click', '.item', function(){
					var $item = $(this);
					var prevTab = $tabs.find('.item.active').removeClass('active').data('tab');
					var nextTab = $item.addClass('active').data('tab');
					$('.tab-content[data-tab='+prevTab+']').removeClass('active');
					$('.tab-content[data-tab='+nextTab+']').addClass('active');
				});
				return $tabs;
			}
		};
		module.exports = Tab;

		log('Module Tab is loaded.');
	}
	else{
		error('Can not find jQuery.');
		return false;
	}
});