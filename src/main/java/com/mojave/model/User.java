package com.mojave.model;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username", "email"
        })})
@TypeDef(
        name = "JSON",
        typeClass = JsonStringType.class
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", length = 100)
    String name;

    @Column(name = "username", nullable = false, length = 40)
    String username;

    @Column(name = "email", nullable = false, length = 50)
    String email;

    @Column(name = "password")
    String password;

    @Type(type = "JSON")
    @Column(columnDefinition = "JSON")
    Set<Role> roles = new HashSet<>();

}


