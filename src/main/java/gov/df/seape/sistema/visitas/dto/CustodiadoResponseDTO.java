package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para transferência de dados de custodiado na resposta da API.
 * Expõe informações básicas do custodiado.
 */
public class CustodiadoResponseDTO extends PessoaResponseDTO {
    
    /**
     * Número do prontuário do custodiado.
     */
    private String numeroProntuario;
    
    /**
     * Vulgo ou apelido do custodiado.
     */
    private String vulgo;
    
    /**
     * ID da unidade penal onde o custodiado está alocado.
     */
    private Long unidadePenalId;

    /**
     * Nome da unidade penal.
     */
    private String nomeUnidadePenal;

    // Construtores
    public CustodiadoResponseDTO() {
        super();
    }

    public CustodiadoResponseDTO(Long id, String nome, String cpf, 
                                  String numeroProntuario, String vulgo, 
                                  Long unidadePenalId, String nomeUnidadePenal) {
        super(id, nome, cpf, null);
        this.numeroProntuario = numeroProntuario;
        this.vulgo = vulgo;
        this.unidadePenalId = unidadePenalId;
        this.nomeUnidadePenal = nomeUnidadePenal;
    }

    // Getters e Setters
    public String getNumeroProntuario() {
        return numeroProntuario;
    }

    public void setNumeroProntuario(String numeroProntuario) {
        this.numeroProntuario = numeroProntuario;
    }

    public String getVulgo() {
        return vulgo;
    }

    public void setVulgo(String vulgo) {
        this.vulgo = vulgo;
    }

    public Long getUnidadePenalId() {
        return unidadePenalId;
    }

    public void setUnidadePenalId(Long unidadePenalId) {
        this.unidadePenalId = unidadePenalId;
    }

    public String getNomeUnidadePenal() {
        return nomeUnidadePenal;
    }

    public void setNomeUnidadePenal(String nomeUnidadePenal) {
        this.nomeUnidadePenal = nomeUnidadePenal;
    }
}