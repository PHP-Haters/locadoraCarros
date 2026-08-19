package com.sword.aluguelCarros.Config;

import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Model.Enum.UserRole;
import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Repository.CarroRepository;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner carrosDataLoader(CarroRepository carroRepository) {
        return args -> {

            if (carroRepository.count() == 0) {

                carroRepository.save(new Carro(
                        null,
                        "Siena",
                        "Fiat",
                        100.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Onix",
                        "Chevrolet",
                        120.0F,
                        true,
                        false
                ));


                carroRepository.save(new Carro(
                        null,
                        "Polo",
                        "Volkswagen",
                        125.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Corolla",
                        "Toyota",
                        180.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Civic",
                        "Honda",
                        200.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "T-Cross",
                        "Volkswagen",
                        170.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Creta",
                        "Hyundai",
                        190.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Tracker",
                        "Chevrolet",
                        180.0F,
                        true,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Compass",
                        "Jeep",
                        240.0F,
                        true,
                        false
                ));


                carroRepository.save(new Carro(
                        null,
                        "Renegade",
                        "Jeep",
                        190.0F,
                        false,
                        false
                ));

                carroRepository.save(new Carro(
                        null,
                        "Audi A3",
                        "Audi",
                        300.0F,
                        true,
                        false
                ));
            }
        };
    }

    @Bean
    CommandLineRunner usuariosDataLoader(UsuarioRepository usuarioRepository) {
        return args -> {

            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(new Usuario(
                        null,
                        "Admin",
                        "admin@gmail.com",
                        "admin123",
                        UserRole.ADMIN
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "Fabio",
                        "fabio@gmail.com",
                        "fabio123",
                        UserRole.USER
                ));
            }
        };
    }
}