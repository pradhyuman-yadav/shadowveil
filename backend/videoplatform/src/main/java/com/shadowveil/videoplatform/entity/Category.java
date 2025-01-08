package com.shadowveil.videoplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @ColumnDefault("nextval('categories_category_id_seq')")
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

}