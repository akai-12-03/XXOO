package com.dept.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class URLController extends WebController {

	@RequestMapping(value = "{path}")
	public String turnToView(@PathVariable(value = "path") String path) {

		return path;
	}

	@RequestMapping(value = "{folder}/{path}")
	public String turnToFolderView(
			@PathVariable(value = "folder") String folder,
			@PathVariable(value = "path") String path) {

		return folder + "/" + path;
	}


}
