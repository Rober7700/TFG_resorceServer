package com.numen.springboot.rest.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.numen.springboot.rest.model.enumeration.EPrenda;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "prendas")
public class Prenda implements Serializable {

    private static final long serialVersionUID = 7310233981226144999L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EPrenda tipo;
    
    private boolean vendido;
    
    private boolean escaparate;
    
    private String talla;
    
    private String color;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    private List<String> fotos;
    
    @NotEmpty(message="no puede estar vacío")
    @Column(nullable = false)
    private String nombre;
    
    @NotEmpty(message="no puede estar vacío")
    @Column(nullable = false)
    private String descripcion;
    
    @NotNull(message = "no puede estar vacío")
    @Column(nullable = false)
    private BigDecimal precioOriginal; // Precio sin descuento
    
    @NotNull(message = "no puede estar vacío")
    @Column(nullable = false)
    private BigDecimal precioConDescuento; // Precio con descuento

    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }
    
}
