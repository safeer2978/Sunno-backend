package com.sunno.viewservice.Model.persistence;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="genre")
public class Genre {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    int id;
    String img_url;
    String name;

}
