package com.ednevnik.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "nastavnik")
@Getter
@Setter
@NoArgsConstructor
public class NastavnikEntity extends KorisnikEntity {

}
