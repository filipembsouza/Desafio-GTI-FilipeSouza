Sistema de Gestão de Visitas Prisionais

Descrição do Projeto
Sistema web desenvolvido para gerenciar o fluxo de visitas em unidades prisionais, permitindo agendamento, consulta e cancelamento de visitas com rigorosos controles de segurança.

Tecnologias Utilizadas
Linguagem: Java 17

Framework: Spring Boot 3

Segurança: OAuth2 + JWT

Banco de Dados: H2 Database (in-memory)

Documentação: Swagger OpenAPI

Testes: JUnit 5, Mockito

Gerenciamento de Dependências: Maven

Requisitos do Sistema
JDK 17+

Maven 3.6+

Git

Estrutura do Projeto
arduino
Copiar
sistema-visitas/
├── .vscode/
│   └── ... (arquivos de configuração do VSCode)
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── gov/
│   │   │       └── df/
│   │   │           └── seape/
│   │   │               └── sistema/
│   │   │                   └── visitas/
│   │   │                       ├── config/
│   │   │                       │   ├── DataInitializer.java
│   │   │                       │   ├── LoggingConfig.java
│   │   │                       │   ├── OpenApiConfig.java
│   │   │                       │   └── SecurityConfig.java
│   │   │                       ├── controller/
│   │   │                       │   ├── AgendamentoVisitaController.java
│   │   │                       │   ├── CustodiadoController.java
│   │   │                       │   ├── DiagnosticoController.java
│   │   │                       │   ├── LoginController.java
│   │   │                       │   ├── PessoaController.java
│   │   │                       │   ├── StatusController.java
│   │   │                       │   ├── TesteController.java
│   │   │                       │   ├── UnidadePenalController.java
│   │   │                       │   ├── UsuarioController.java
│   │   │                       │   ├── VincPerfilFuncionalidadeController.java
│   │   │                       │   └── VisitanteController.java
│   │   │                       ├── dto/
│   │   │                       │   ├── AgendamentoVisitaRequestDTO.java
│   │   │                       │   ├── AgendamentoVisitaResponseDTO.java
│   │   │                       │   ├── CustodiadoRequestDTO.java
│   │   │                       │   ├── CustodiadoResponseDTO.java
│   │   │                       │   ├── DiagnosticoRequestDTO.java
│   │   │                       │   ├── DiagnosticoResponseDTO.java
│   │   │                       │   ├── LoginRequestDTO.java
│   │   │                       │   ├── LoginResponseDTO.java
│   │   │                       │   ├── PerfilRequestDTO.java
│   │   │                       │   ├── PerfilResponseDTO.java
│   │   │                       │   ├── PessoaRequestDTO.java
│   │   │                       │   ├── PessoaResponseDTO.java
│   │   │                       │   ├── StatusRequestDTO.java
│   │   │                       │   ├── StatusResponseDTO.java
│   │   │                       │   ├── UnidadePenalRequestDTO.java
│   │   │                       │   ├── UnidadePenalResponseDTO.java
│   │   │                       │   ├── UsuarioRequestDTO.java
│   │   │                       │   ├── UsuarioResponseDTO.java
│   │   │                       │   ├── VincPerfilFuncionalidadeRequestDTO.java
│   │   │                       │   └── VincPerfilFuncionalidadeResponseDTO.java
│   │   │                       ├── exception/
│   │   │                       │   ├── AgendamentoConflitanteException.java
│   │   │                       │   ├── GlobalExceptionHandler.java
│   │   │                       │   ├── HorarioNaoPermitidoException.java
│   │   │                       │   ├── OperacaoInvalidaException.java
│   │   │                       │   └── RecursoNaoEncontradoException.java
│   │   │                       ├── model/
│   │   │                       │   ├── AgendamentoVisita.java
│   │   │                       │   ├── Custodiado.java
│   │   │                       │   ├── Funcionalidade.java
│   │   │                       │   ├── Perfil.java
│   │   │                       │   ├── Pessoa.java
│   │   │                       │   ├── Status.java
│   │   │                       │   ├── UnidadePenal.java
│   │   │                       │   ├── Usuario.java
│   │   │                       │   └── VincPerfilFuncionalidade.java
│   │   │                       ├── repository/
│   │   │                       │   ├── AgendamentoVisitaRepository.java
│   │   │                       │   ├── CustodiadoRepository.java
│   │   │                       │   ├── FuncionalidadeRepository.java
│   │   │                       │   ├── PerfilRepository.java
│   │   │                       │   ├── PessoaRepository.java
│   │   │                       │   ├── StatusRepository.java
│   │   │                       │   ├── UnidadePenalRepository.java
│   │   │                       │   ├── UsuarioRepository.java
│   │   │                       │   └── VincPerfilFuncionalidadeRepository.java
│   │   │                       ├── security/
│   │   │                       │   └── AgendamentoVisitaSecurityTest.java (exemplo)
│   │   │                       ├── service/
│   │   │                       │   ├── AgendamentoVisitaService.java
│   │   │                       │   ├── CustodiadoService.java
│   │   │                       │   ├── FuncionalidadeService.java
│   │   │                       │   ├── PerfilService.java
│   │   │                       │   ├── PessoaService.java
│   │   │                       │   ├── StatusService.java
│   │   │                       │   ├── UnidadePenalService.java
│   │   │                       │   ├── UsuarioService.java
│   │   │                       │   ├── VincPerfilFuncionalidadeService.java
│   │   │                       │   └── impl/
│   │   │                       │       ├── AgendamentoVisitaServiceImpl.java
│   │   │                       │       ├── CustodiadoServiceImpl.java
│   │   │                       │       ├── FuncionalidadeServiceImpl.java
│   │   │                       │       ├── PerfilServiceImpl.java
│   │   │                       │       ├── PessoaServiceImpl.java
│   │   │                       │       ├── StatusServiceImpl.java
│   │   │                       │       ├── UnidadePenalServiceImpl.java
│   │   │                       │       ├── UsuarioServiceImpl.java
│   │   │                       │       └── VincPerfilFuncionalidadeServiceImpl.java
│   │   │                       ├── util/
│   │   │                       │   └── HorarioVisitaUtil.java
│   │   │                       └── SistemaVisitasApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   └── ... (arquivos estáticos, se houver)
│   │       ├── templates/
│   │       │   └── ... (templates, se houver)
│   │       ├── application.properties
│   │       ├── application-test.properties
│   │       └── data.sql
│   ├── integration/
│   │   └── ... (testes de integração adicionais, se houver)
│   └── test/
│       └── ... (testes unitários e de integração adicionais)
├── target/
│   └── ... (arquivos gerados após compilação)
├── .gitignore
├── .gitattributes
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
Configuração e Instalação
Clonar o Repositório
bash
Copiar
git clone https://github.com/filipembsouza/sistema-visitas.git
cd sistema-visitas
Compilar o Projeto
bash
Copiar
mvn clean install
Executar a Aplicação
bash
Copiar
mvn spring-boot:run
Segurança e Autenticação
Perfis de Acesso
ADMIN: Acesso completo ao sistema

Usuário: admin

Senha: admin

USER: Acesso limitado

Usuário: user

Senha: user

Endpoints Principais
Agendamentos
POST /api/agendamentos: Criar novo agendamento

GET /api/agendamentos: Listar agendamentos

PUT /api/agendamentos/{id}: Atualizar agendamento

DELETE /api/agendamentos/{id}: Cancelar agendamento

Custodiados
POST /api/custodiados: Cadastrar custodiado

GET /api/custodiados: Listar custodiados

Visitantes
POST /api/visitantes: Cadastrar visitante

GET /api/visitantes: Listar visitantes

Outros endpoints podem ser consultados na documentação da API.

Documentação da API
Swagger UI: http://localhost:8080/swagger-ui.html

Documentação OpenAPI: http://localhost:8080/v3/api-docs

Testes
Executar Testes Unitários
bash
Copiar
mvn test
Executar Testes de Integração
bash
Copiar
mvn verify
Logs
Os logs são gerados em logs/sistema-visitas.log e podem ser configurados via application.properties.

Contribuição
Faça um fork do projeto.

Crie uma branch para a nova funcionalidade:
git checkout -b feature/nova-funcionalidade

Realize os commits das alterações:
git commit -m 'Adiciona nova funcionalidade'

Envie a branch para o repositório remoto:
git push origin feature/nova-funcionalidade

Abra um Pull Request.

Licença
MIT License

Contato
Email: filipe.souza@seape.df.gov.br

Organização: SEAPE-DF (Secretaria de Administração Penitenciária do Distrito Federal)

Desafio GTI
Projeto desenvolvido para o Processo Seletivo GTI 01/2025

Observações Importantes
As configurações de segurança estão definidas em SecurityConfig.java.

Os endpoints estão protegidos com OAuth2.

O tratamento de exceções é centralizado.