package co.edu.autonoma.mercadolocalapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productores", schema = "personas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String correo;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false, length = 80)
    private String municipio;

    @Column(nullable = false)
    private boolean activo;

    public ProductorEntity(String nombre, String correo, String telefono, String municipio) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.municipio = municipio;
        this.activo = true;
    }
}