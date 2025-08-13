package com.cdac.dto;

import com.cdac.Entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class UserResponseDTO {

	    private Long userId;

	    private String name;

	    private String email;

	    private Category category;

	    private String address;
	    
	    private String contactNo;
	}


