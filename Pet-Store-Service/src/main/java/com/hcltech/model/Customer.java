package com.hcltech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements UserDetails{

    @Id
    @SequenceGenerator(name = "CUSTOMER_SEQ",initialValue = 100,allocationSize = 1)
    @GeneratedValue(generator ="CUSTOMER_SEQ",strategy = GenerationType.SEQUENCE)
    private Long customerId;

    private String customerName;

    @Column(unique = true)
    private String customerEmail;

    public String password;

    private String customerPhone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> Orders;

    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private Date createdOn;
    @UpdateTimestamp
    @Column(insertable = false, updatable = true)
    private Date modifiedDate;


    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // No roles for now
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return this.customerEmail;
    }
}