package org.airport.example.repository.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity for 'airports' table.
 */
@Entity
@Table(name = "airports", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "code")})
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AirPortEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Airport name
     */
    private String name;
    /**
     * Airport code, unique
     */
    @Column(unique = true)
    private String code;
    /**
     * Airport city name, not unique
     */
    private String city;

//    @EqualsAndHashCode.Exclude
    @ToString.Exclude @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    private UserEntity user;

    @Column(name = "CREATED", nullable = false, updatable = false, insertable = false)
    private Date created;
}
