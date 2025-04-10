package com.hcltech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @SequenceGenerator(name = "CATEGORY_SEQ",initialValue = 100,allocationSize = 1)
    @GeneratedValue(generator ="CATEGORY_SEQ",strategy = GenerationType.SEQUENCE)
    private Long tagId;

    private String tagName;

    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(insertable = false, updatable = true)
    private Date modifiedDate;
}