package com.centro.api.java.controller.user;

import com.centro.api.java.models.user.UserDetail;
import com.centro.api.java.services.user.UserService;
import com.centro.api.java.services.user.repository.UserRepository;
import com.centro.platform.api.java.util.CentroCertificateWrapper;
import com.centro.platform.api.java.security.CentroLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {


	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Object login(@RequestHeader("username") String username, @RequestHeader("password") String password) throws Exception {
		
		CentroCertificateWrapper wrapper = new CentroCertificateWrapper();
		wrapper.setCertificate(CentroLogin.loginUser(username, password));
		return wrapper;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> signupUser(@RequestBody UserDetail requestDetail) throws Exception {
		UserDetail newUser = null;
		if(requestDetail != null) {
			newUser = userService.signupUser(requestDetail);
		}
		return ResponseEntity.ok(newUser);
	}


	@RequestMapping(method = RequestMethod.GET)
	public Object findAllUsers() throws Exception {
		List<UserDetail> allUsers = userService.findUsersAll();
		return allUsers;
	}
}
