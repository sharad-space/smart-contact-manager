package com.scm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
//	Pagiantion
	
	@Query("from Contact as c where c.user.id=:userId")
	public Page<Contact> findContactByUser(@RequestParam("userId") int userId,Pageable pageable);

}
