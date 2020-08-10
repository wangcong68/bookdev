package com.wangcong.library.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Long uid;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String username;

    @Column(name = "telephone")
    private String tel;

    @Column(name = "headUrl")
    private String headUrl;

    @Column(name = "root")
    private Long  root;

}
