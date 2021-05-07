package com.example.fileuploadservice.model.user

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
@Table(name = "user")
class UserDao(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var username: String? = null,

    @Column
    @JsonIgnore
    var password: String? = null,
)