/*! Tomcat360.com (c) 2015 
	Author: Renzhao
*/
define("modules/editor",["rich-editor","log"],function(i,t,o){"use strict";var e=(i("rich-editor"),i("log")),r=e.log;e.warn;r("Module Editor is loading...");var d=function(i){return r("Editor is initializing..."),this.id=i||"#editor",this.params={height:316,width:678,resizeType:1,themeType:"simple",uploadJson:ROOT_URL+"/upload_json.jsp"},this._init(),r("Editor is initialized."),this};d.prototype={_init:function(){var i=this;return i.editor=KindEditor.create(i.id,i.params),i}},o.exports=d,r("Module Editor is loaded.")});