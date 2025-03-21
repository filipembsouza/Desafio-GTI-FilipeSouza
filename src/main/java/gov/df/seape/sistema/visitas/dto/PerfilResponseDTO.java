package gov.df.seape.sistema.visitas.dto;

import java.time.LocalDate;
import java.time.Period;

import gov.df.seape.sistema.visitas.model.Perfil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PerfilResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private Integer idade;
    
    public PerfilResponseDTO(Long id, String nome, String cpf, LocalDate dataNascimento, Integer idade) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
        calcularIdade();
    }
    
    public void calcularIdade() {
        if (this.dataNascimento != null) {
            this.idade = Period.between(this.dataNascimento, LocalDate.now()).getYears();
        }
    }

    public PerfilResponseDTO(Perfil perfil) {
     
    }
}
