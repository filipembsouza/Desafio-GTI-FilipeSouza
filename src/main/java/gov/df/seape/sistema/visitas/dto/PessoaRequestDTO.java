package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 45, message = "Nome deve ter entre 3 e 45 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "(^\\d{11}$)", message = "CPF deve conter exatamente 11 dígitos numéricos, sem pontos ou traços")
    private String cpf;

    @Past(message = "Data de nascimento deve ser no passado")
    @NotBlank(message = "Data de nascimento é obrigatória")
    private LocalDate dataNascimento;
}