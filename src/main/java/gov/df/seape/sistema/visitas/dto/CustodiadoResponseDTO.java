package gov.df.seape.sistema.visitas.dto;

public class CustodiadoResponseDTO extends PessoaResponseDTO {
    
    private String numeroProntuario;
    
    private String vulgo;
    
    private Long unidadePenalId;
    
    private String nomeUnidadePenal;

    // Construtores
    public CustodiadoResponseDTO() {
        super();
    }

    public CustodiadoResponseDTO(Long id, String nome, String cpf, String numeroProntuario, 
                                  String vulgo, Long unidadePenalId, String nomeUnidadePenal) {
        super(id, nome, cpf, null); // Deixei data de nascimento como null
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