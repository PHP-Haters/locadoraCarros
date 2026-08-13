package com.sword.aluguelCarros.Config;

import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Repository.CarroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(CarroRepository carroRepository) {
        return args -> {

            if (carroRepository.count() == 0) {

                carroRepository.save(new Carro(
                        null,
                        "Siena",
                        "Fiat",
                        100.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "Onix",
                        "Chevrolet",
                        120.0F,
                        true
                ));


                carroRepository.save(new Carro(
                        null,
                        "Polo",
                        "Volkswagen",
                        125.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "Corolla",
                        "Toyota",
                        180.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "Civic",
                        "Honda",
                        200.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "T-Cross",
                        "Volkswagen",
                        170.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "Creta",
                        "Hyundai",
                        190.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "Tracker",
                        "Chevrolet",
                        180.0F,
                        true
                ));

                carroRepository.save(new Carro(
                        null,
                        "Compass",
                        "Jeep",
                        240.0F,
                        true
                ));


                carroRepository.save(new Carro(
                        null,
                        "Renegade",
                        "Jeep",
                        190.0F,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Audi A3",
                        "Audi",
                        300.0F,
                        true
                ));
            }
        };
    }
}