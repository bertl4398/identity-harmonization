/*   Copyright 2016 Karlsruhe Institute of Technology (KIT)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0
 */
package edu.kit.scc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

	@Value("${oauth2.authorizeUri}")
	private String oauth2AuthorizeUri;

	@Value("${oauth2.redirectUri}")
	private String oauth2RedirectUri;

	@Value("${oauth2.clientId}")
	private String oauth2ClientId;

	@RequestMapping("/login")
	public String login(Model model) {
		String redirectUrl = oauth2AuthorizeUri.replaceAll("/$", "");
		redirectUrl += "?response_type=code&scope=openid&client_id=";
		redirectUrl += oauth2ClientId;
		redirectUrl += "&redirect_uri=";
		redirectUrl += oauth2RedirectUri;
		log.debug("Redirect to {}", redirectUrl);

		return "redirect:" + redirectUrl;
	}

	@RequestMapping(path = "/oauth2")
	public String oauth2Authentication(@RequestParam(value = "code", required = true) String code, Model model) {
		log.debug(code);
		model.addAttribute("code", code);
		return "index";
	}
}