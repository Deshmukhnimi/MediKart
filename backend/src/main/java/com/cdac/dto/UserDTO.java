package com.cdac.dto;

import com.cdac.Entity.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String name;
    private String email;
    private Category category;
    private String address;
    private String contactNo;
}
