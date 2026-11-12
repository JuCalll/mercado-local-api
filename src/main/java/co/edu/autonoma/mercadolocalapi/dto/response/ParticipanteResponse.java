package co.edu.autonoma.mercadolocalapi.dto.response;

import co.edu.autonoma.mercadolocalapi.entity.CompradorEntity;
import co.edu.autonoma.mercadolocalapi.entity.ProductorEntity;

public record ParticipanteResponse(
        Long id,
        String nombre,
        ContactoResponse contacto
) {
        public static ParticipanteResponse desde(ProductorEntity productor, boolean mostrarContacto){
                ContactoResponse contacto = mostrarContacto
                        ? new ContactoResponse(productor.getTelefono(), productor.getCorreo())
                        : null;
                
                return new ParticipanteResponse(productor.getId(), productor.getNombre(), contacto);
        }

        public static ParticipanteResponse desde(CompradorEntity comprador, boolean mostrarContacto){
                ContactoResponse contacto = mostrarContacto
                        ? new ContactoResponse(comprador.getTelefono(), comprador.getCorreo())
                        : null;

                return new ParticipanteResponse(comprador.getId(), comprador.getNombre(), contacto);
        }
}
