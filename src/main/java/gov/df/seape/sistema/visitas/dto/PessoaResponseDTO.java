package gov.df.seape.sistema.visitas.dto;

import java.time.LocalDate;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("unused")
@Data
@NoArgsConstructor
public class PessoaResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private Integer idade;
    
    public PessoaResponseDTO(Long id, String nome, String cpf, LocalDate dataNascimento, Integer idade) {
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
}