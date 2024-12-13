package com.backbyte.models;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Moto extends Vehiculo {
   private Integer cc;

   public Integer getCc() {
      return cc;
   }

   public void setCc(Integer cc) {
      this.cc = cc;
   }
}