package co.edu.autonoma.mercadolocalapi.config;

import co.edu.autonoma.mercadolocalapi.repository.OfertaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PersistenciaCheck implements CommandLineRunner {

    private final OfertaRepository repository;

    public PersistenciaCheck(OfertaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        System.out.println("Total: " + repository.count());
    }
}