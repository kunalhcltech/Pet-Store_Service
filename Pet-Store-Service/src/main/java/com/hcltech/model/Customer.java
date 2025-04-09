package com.hcltech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

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

}