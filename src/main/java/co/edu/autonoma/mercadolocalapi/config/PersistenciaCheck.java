package co.edu.autonoma.mercadolocalapi.config;

import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersistenciaCheck implements CommandLineRunner {

    private final OfertaRepository repository;

    @Override
    public void run(String... args) {
        System.out.println("Total: " + repository.count());
    }
}