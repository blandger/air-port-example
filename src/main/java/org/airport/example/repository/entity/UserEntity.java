package org.airport.example.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * Entity for 'users' table
 */
@Entity
//@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @ToString.Exclude
    @Column(nullable = false)
    private String password;

    @Column(name = "CREATED", nullable = false, updatable = false, insertable = false)
    private Date created;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<AirPortEntity> owners;
}
