package co.edu.autonoma.mercadolocalapi.config;

import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersistenciaCheck implements CommandLineRunner {

    private final OfertaRepository repository;

    @Override
    public void run(String... args) {
        log.info("Ofertas registradas en la base: {}", repository.count());
    }
}