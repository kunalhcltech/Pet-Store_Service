package com.hcltech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @SequenceGenerator(name = "ORDER_SEQ",initialValue = 100,allocationSize = 1)
    @GeneratedValue(generator ="ORDER_SEQ",strategy = GenerationType.SEQUENCE)
    private Long purchaseId;

    @ManyToOne
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private LocalDate purchaseDate;

    private Boolean returned = false;

    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private Date createdOn;
    @UpdateTimestamp
    @Column(insertable = false, updatable = true)
    private Date modifiedDate;

}