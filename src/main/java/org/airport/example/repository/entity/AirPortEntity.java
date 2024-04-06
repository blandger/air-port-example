package org.airport.example.repository.entity;

import jakarta.persistence.*;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Size;
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
//@Table(name = "airports", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@Table(name = "airports", uniqueConstraints = {@UniqueConstraint(columnNames = "id"), @UniqueConstraint(columnNames = "code")})
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AirPortEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull @NotEmpty @Size(max = 250, min = 5)
    @Column(nullable = false)
    private String name;
    /**
     * Airport code, unique
     */
//    @NotNull @NotEmpty @Size(max = 25, min = 3)
    @Column(unique = true, nullable = false)
    private String code;
    /**
     * Airport city name, not unique
     */
//    @NotNull @NotEmpty @Size(max = 250, min = 5)
    @Column(nullable = false)
    private String city;

    @ToString.Exclude @EqualsAndHashCode.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
    private UserEntity user;

    @Column(name = "CREATED", nullable = false, updatable = false, insertable = false)
    private Date created;
}
