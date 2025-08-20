package com.scm.controllers;

import java.awt.print.Printable;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.dao.ContactRepository;
import com.scm.dao.UserRepositery;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepositery userRepositery;
	
	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {
		String usename = principal.getName();
		User user = userRepositery.getUserByUserName(usename);
		model.addAttribute("user", user);

	}

	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {

		return "normal/user_dashboard";
	}

//	open add form controller

	@GetMapping("/add-contact")
	public String openContactForm(Model model) {

		model.addAttribute("title", "add-Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

//	process- add-contact
	@PostMapping("/process-contact")
	public String addContact(@ModelAttribute Contact contact,Principal principal,HttpSession session) {
		
		try {
			
			String name = principal.getName();
			User user = userRepositery.getUserByUserName(name);
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepositery.save(user);
		    // Debug logs
		   
		    session.setAttribute("message", new Message("Your contact added successfully","success"));
		    
		    
		    
		    
			
		} catch (Exception e) {
			e.printStackTrace();
			 session.setAttribute("message", new Message("Somthing went wrong !!","danger"));
			 
		}
	 

	    
	    return "normal/add_contact_form";
	}
	
//	show contact
	
	@GetMapping("/show-contact/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model,Principal principal) {
		
		
		model.addAttribute("title","View-Contact");
		
		String name = principal.getName();
		User user = this.userRepositery.getUserByUserName(name);
		Pageable pageable=PageRequest.of(page, 5);
		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(),pageable);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPage", contacts.getTotalPages());
		
		
		
		
		return "normal/view_contact";
	}
	
// hundlering for view profile
	@GetMapping("/show-profile")
	public String showProfile(Model model) {
		
		
		model.addAttribute("title","View-Profile");
		
		return "normal/view_profile";
	}
	
	@GetMapping("/{cId}/contact")
	public String showProfileDeatils(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		
		
		model.addAttribute("title","Profile Details");
		
		 Contact contact = this.contactRepository.findById(cId).get();
		 String name = principal.getName();
		 User user = this.userRepositery.getUserByUserName(name);
		 
		 if (user.getId()==contact.getUser().getId()) {
			 model.addAttribute("contact", contact);
		}else {
			
		}
		
		
		return "normal/profile_detail";
	}
	
//	controller for delete contact
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId,Model model,Principal principal,HttpSession session) {
		
		Contact contact = this.contactRepository.findById(cId).get();
		
		 String name = principal.getName();
		 
		 User user = this.userRepositery.getUserByUserName(name);
		 
		if(user.getId()==contact.getUser().getId()) {
			this.contactRepository.delete(contact);
			session.setAttribute("message", new Message("Contact Deleted Successfilly !", "success"));
			
		}
			
		
		
		return "redirect:/user/show-contact/0";
	}
	
	
	@PostMapping("/update/{cid}")
	public String upadteContact(@PathVariable("cid") Integer cId,Model model) {
		
		Contact contact = this.contactRepository.findById(cId).get();

		model.addAttribute("title", "update contact");
		
		model.addAttribute("contact", contact);
		
		
		return "normal/update_contact";
	}
	
	
	
//	process update form
	@PostMapping("/process-update")
	public String processUpdateForm(@ModelAttribute Contact contact,Principal principal, HttpSession session) {
		
		try {
			String name = principal.getName();
			User user = this.userRepositery.getUserByUserName(name);
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Contact Updated ", "success"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Name "+ contact.getName());
		System.out.println("Name "+ contact.getCId());
		
		return "redirect:/user/"+contact.getCId()+"/contact";
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
