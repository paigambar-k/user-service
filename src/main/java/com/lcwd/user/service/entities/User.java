package com.lcwd.user.service.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "micro_users")
@Entity
public class User {


    @Id
    @Column(name="ID")
    private String userId;

    @Column(name="NAME",length = 20)
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ABOUT")
    private String about;

    //it will give rating information
    @Transient
    private List<Rating> ratings=new ArrayList<>();

}
