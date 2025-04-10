package com.hcltech.repository;

import com.hcltech.model.Category;
import com.hcltech.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
}
