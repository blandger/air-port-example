package org.airport.example.repository.entity;

import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity for 'users' table
 */
@Entity
//@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@EqualsAndHashCode
public class UserEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull @NotEmpty @Size(max = 250, min = 5)
    @Column(nullable = false)
    private String userName;

//    @NotNull @NotEmpty @Size(max = 60, min = 5) @Email
    @Column(unique = true, nullable = false)
    private String email;

//    @NotEmpty @NotEmpty @Size(max = 45, min = 8)
    @Column(nullable = false)
    private String password;

    @Column(name = "CREATED", nullable = false, updatable = false, insertable = false)
    private Date created;
}
