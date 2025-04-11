package com.hcltech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    @Id
    @SequenceGenerator(name = "CATEGORY_SEQ",initialValue = 100,allocationSize = 1)
    @GeneratedValue(generator ="CATEGORY_SEQ",strategy = GenerationType.SEQUENCE)
    private Long petId;

    private String petName;

    private Integer age;

    private String breed;

    private String gender;

    private Double price;

    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "pet_tag",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(insertable = false, updatable = true)
    private Date modifiedDate;
}