package com.centro.api.java.login;

import com.centro.platform.api.java.util.CentroCertificateWrapper;
import com.centro.platform.api.java.security.CentroLogin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Object login(@RequestHeader("username") String username, @RequestHeader("password") String password) throws Exception {
		
		CentroCertificateWrapper wrapper = new CentroCertificateWrapper();
		wrapper.setCertificate(CentroLogin.loginUser(username, password));
		return wrapper;
	}
	
}
