package com.sword.aluguelCarros.Model;

import com.sword.aluguelCarros.Model.Enum.UserRole;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        // Define o papel com base no campo 'role'
        if(this.role == null) {
            this.role = UserRole.USER;
        }
    }
}