package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.UserRepositery;
import com.scm.entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepositery userRepositery;

	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home-SCM");

		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {

		model.addAttribute("title", "About-SCM");

		return "about";
	}

	@GetMapping("/signup")
	public String singnup(Model model) {

		model.addAttribute("title", "Register-SCM");
		model.addAttribute("user", new User());

		return "signup";
	}

//	this is for hundler for register data
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {
			if (!agreement) {
				System.out.println("You have not agreed to the terms and conditions!");
				throw new Exception("You have not agreed to the terms and conditions!");
			}
			if (result.hasErrors()) {
				
				System.out.println("Error "+result.toString());
				model.addAttribute("user",user);	
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

			User savedUser = userRepositery.save(user);

			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered!", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
			return "signup";
		}
	}

//	handler for custom login form
	
	@GetMapping("/signin")
	public String customLogin(Model model) {
		
		model.addAttribute("title", "Login Page..");
		
		return "login";
	}
}
