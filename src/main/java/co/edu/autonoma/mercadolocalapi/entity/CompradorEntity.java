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

import java.time.OffsetDateTime;

public class CompradorEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column (nullable = false, length = 150)
    private String correo;

    @Column (nullable = false, length = 20)
    private String telefono;

    @Column (name = "creado_en", nullable = false)
    private OffsetDateTime creadoEn;

    public CompradorEntity(String nomre, String correo, String telefono) {
        this.nombre = nomre;
        this.correo = correo;
        this.telefono = telefono;
        this.creadoEn = OffsetDateTime.now();
    }

}
