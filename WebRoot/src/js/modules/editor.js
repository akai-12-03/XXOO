/**
 * Module Editor v1.1
 * 富文本编辑器模块
 * based on kindeditor
 *
 * @author: lixiaodong
 * @description: 将指定 ID 的元素嵌入该编辑器。
 *
 * @usage:
 *
 * ```html
 *     <div id="editor"/>
 * ```
 *
 *  ```javascript
 *     seajs.use("editor", function(){
 *         new editor;
 *     });
 * ```
 */

define(function(require, exports, module){
	"use strict";

	var richEditor = require("rich-editor");
	var _ = require('log');
	var log = _.log;
	var warn = _.warn;

	log('Module Editor is loading...');

	var Editor = function(id){
		log('Editor is initializing...');

		this.id = id || "#editor";
		this.params = {
			height: 316,
			width: 678,
			resizeType: 1,
			themeType: "simple",
			uploadJson:ROOT_URL + "/upload_json.jsp"
		};

		this._init();
		log('Editor is initialized.');
		return this;
	};

	Editor.prototype = {
		_init: function(){
			var self = this;
			self.editor = KindEditor.create(self.id, self.params);

			return self;
		}
	};

	module.exports = Editor;
	log('Module Editor is loaded.');
});
