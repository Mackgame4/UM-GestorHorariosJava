#import "resources/report.typ" as report

#show: report.styling.with(
    hasFooter: false
)

#report.index()

= Introdução
#v(15pt)
Neste relatório, apresenta-se o resultado da fase de análise de requisitos no contexto do desenvolvimento de um sistema de software para a gestão de turnos no curso de Engenharia Informática. O sistema terá como objetivo a gestão eficiente de turnos, considerando as particularidades de cada Unidade Curricular (UC) e as necessidades dos alunos, quantidade de alunos inscritos, e a não sobreposição de horários de turnos para o aluno. Isto constitui um desafio significativo, especialmente em cursos de grande dimensão (como usual neste caso).

Com base nas necessidades identificadas, foi elaborado um Modelo de Domínio, representando as principais entidades envolvidas, e um Modelo de Use Case, que descreve as funcionalidades essenciais do sistema. Esses modelos têm como objetivo estabelecer uma visão clara e estruturada do sistema a ser desenvolvido, servindo de base para a implementação futura.

O presente documento reflete o esforço de sistematização e análise das especificações e requisitos iniciais, demonstrando o trabalho, diagramas e modelos realizados/levantados para esse problema.

#pagebreak()

= Modelo de Domínio
#v(15pt)
#figure(
  caption: "Modelo de Domínio.",
  kind: image,
  image("./images/modelodedominio.jpg", width: 140%)
)

\
#pagebreak()

= Diagrama de Use Case
#v(15pt)

#figure(
  caption: "Diagrama de Use Case.",
  kind: image,
  image("./images/diagramausecases.png", width: 120%)
)
#pagebreak()

= Diagramas de Classes
#v(15pt)

#figure(
  caption: "Diagrama de Classes de Alunos",
  kind: image,
  image("./images/DiagramClasses_Alunos.png", width: 120%)
)

#figure(
  caption: "Diagrama de Classes de Turnos",
  kind: image,
  image("./images/DClasses_Turnos.png", width: 120%)
)

#figure(
  caption: "Diagrama de Classes DAO de Alunos",
  kind: image,
  image("./images/DClassesDAO_Alunos.png", width: 120%)
)

#figure(
  caption: "Diagrama de Classes DAO de Turnos",
  kind: image,
  image("./images/DClasses_Turbnos.png", width: 120%)
)

#figure(
  caption: "Diagrama de Classes de Arquitetura Geral",
  kind: image,
  image("./images/ArqGeral.png", width: 120%)
)
#pagebreak()

= Diagramas de Sequência Pós Implementação

#figure(
  caption: "Diagrama de Sequência de Gerar Horários (Parte Superior)",
  kind: image,
  image("./images/GerarHor-1.png", width: 120%)
)
#figure(
  caption: "Diagrama de Sequência de Gerar Horários (Parte Inferior)",
  kind: image,
  image("./images/GerarHor-2.png", width: 120%)
)

#figure(
  caption: "Diagrama de Sequência de Inscrever Turno Manualmente",
  kind: image,
  image("./images/InsTurnoManual.png", width: 120%)
)

#figure(
  caption: "Diagrama de Sequência de Load Alunos",
  kind: image,
  image("./images/loadAlunos.png", width: 120%)
)
#pagebreak()

= Diagrama de Componentes

#figure(
  caption: "Diagrama de Componentes",
  kind: image,
  image("./images/diagrama_de_componentes.png", width: 120%)
)
#pagebreak()

= Estrutura da Base de Dados
#v(15pt)
A estrutura da base de dados utilizada pelo sistema é representada pelas seguintes tabelas:
- *Alunos*: Armazena informações dos alunos, incluindo nome, número, e-mail e estado. A tabela também contém as relações entre os alunos e as Unidades Curriculares (UCs) a que estão inscritos.
- *Turnos*: Contém informações sobre turnos, como código, tipo, horários, sala e capacidade.
- *Horários*: Regista as associações de alunos a turnos, garantindo que as inscrições respeitam as regras de não sobreposição.
- *Unidades Curriculares (UCs)*: Guarda detalhes das UCs, incluindo nome e código, e estabelece relações com os turnos disponíveis.

== Detalhes Técnicos
- As tabelas foram criadas com base no script `SQLCreate.sql` e são populadas inicialmente através de `SQLPopulate.sql` e dos ficheiros CSV.
- Os DAOs (`AlunoDAO`, `TurnoDAO`, etc.) interagem diretamente com estas tabelas para garantir a consistência dos dados entre a aplicação e a base de dados.

== Operações Importantes
- *Inscrição de Alunos em Turnos*: O sistema verifica automaticamente a capacidade dos turnos e evita conflitos de horário antes de adicionar um aluno.
- *Consulta de Dados*: Métodos como `getAlunosList` e `getTurnosList` permitem extrair informações completas e bem estruturadas para exibição e manipulação.

#pagebreak()

= Utilização exemplo do programa
#v(15pt)
#figure(
  caption: "Menu inicial.",
  kind: image,
  image("./images/page_loginmenu.png", width: 100%)
)
1 - O menu inicial aparecerá ao inicial a aplicação e permite o inicio de sessão de um aluno ou um diretor de curso/regente de UC. A opção de registo/"Sign Up" foi mantida para efeitos de testagem da aplicação, dado que, e segundo os requesitos enunciados para o projeto. Apenas o Diretor de Curso poderá realizar este registo.

#figure(
  caption: "Página de Login.",
  kind: image,
  image("./images/page_login.png", width: 100%)
)
2 - Tela de login do sistema, permitindo o acesso de administradores e alunos. Aqui vemos o login como `admin` (utilizador colocado através de um script de pré-populacional SQL).

2.1 - Após o inicio de sessão estes são redirecionados para dois tipos de menus diferentes. Um caso este seja um Diretor de Curso ou outro caso seja um aluno.

#figure(
  caption: "Menu Diretor Curso.",
  kind: image,
  image("./images/page_dt.png", width: 100%)
)
3 - No menu de diretor o utilizador terá acesso a opções relativas à manipulação da informação e dados da aplicação. De entre outras as opções do menu, este deverá (num primeiro inicio da aplicação) realizar a leitura/carregamento dos dados de um ficheiro CSV para a base de dados do programa através da opção `1` do menu.

#figure(
  caption: "Menu Diretor Curso - Ver UCs.",
  kind: image,
  image("./images/page_dt_verucs.png", width: 100%)
)
4 - Nesta opção do menu do Diretor de Curso é exibida uma listagem de Unidades Curriculares disponíveis, com os respetivos códigos e descrições.

#figure(
  caption: "Menu Diretor Curso - Ver Turnos",
  kind: image,
  image("./images/page_dt_verturnos.png", width: 100%)
)
5 - Nesta opção do menu do Diretor de Curso é exibida uma listagem detalhada dos turnos disponíveis, incluindo informações de horário, sala, e capacidade.

#figure(
  caption: "Menu Diretor Curso - Ver Horarios.",
  kind: image,
  image("./images/page_dt_verhorarios.png", width: 100%)
)
6 -  Nesta opção do menu do Diretor de Curso é exibida uma listagem de horários disponíveis, com informações detalhadas dos turnos, incluindo código, tipo, horário, sala, e capacidade.

#figure(
  caption: "Menu Diretor Curso - Alocação Manual.",
  kind: image,
  image("./images/page_dt_alocmanual.png", width: 100%)
)
7 - Nesta opção é vê-se a função de alocação manual, que permite a inscrição direta de alunos em turnos específicos.

#figure(
  caption: "Menu Aluno - Ver Próprio Horário.",
  kind: image,
  image("./images/page_aluno_verhorario.png", width: 100%)
)
8 - Nesta opção do menu do Aluno temos a visualização de horários de um aluno em específico, com detalhes dos turnos inscritos.

#pagebreak()

= Integração CSV
#v(15pt)
Os ficheiros CSV fornecem uma fonte inicial de dados para o sistema:
- *alunos.csv*: Contém informações detalhadas dos alunos e as Unidades Curriculares associadas.
- *turnos.csv*: Inclui dados dos turnos, como horários, capacidade e relação com as UCs.

== Processo de Importação
O `CSVparser` realiza as seguintes etapas:
1. *Leitura do Ficheiro*: As linhas são lidas sequencialmente.
2. *Validação de Dados*: Linhas malformadas (e.g., com colunas incompletas) são registadas em logs para futura análise.
3. *Inserção na Base de Dados*: Os dados válidos são inseridos utilizando métodos de DAOs, como `createAluno` e `createTurno`.

== Melhorias Implementadas
- *Logs de Erros*: Registo detalhado de linhas inválidas para facilitar a depuração e correção.
- *Carga Inicial de Dados*: Automatização da integração com a base de dados para garantir consistência.

#pagebreak()

= Conclusão
#v(15pt)
Neste projeto, foi possível desenvolver e apresentar um sistema funcional para a gestão de turnos no curso de Engenharia Informática. Através da análise detalhada dos requisitos e da implementação cuidadosa, foi possível atingir os seguintes objetivos principais:

Gestão de Dados: A integração de ficheiros CSV e a utilização de uma base de dados estruturada permitiram o carregamento e a gestão eficiente de informações relacionadas a alunos, turnos e unidades curriculares.
Interatividade: O sistema oferece uma interface de menu clara e intuitiva, que suporta diversas funcionalidades como visualização de dados, geração de horários e alocação manual de alunos.
Consistência e Feedback: As mensagens de sucesso e erro exibidas durante as operações garantem uma boa experiência ao utilizador, permitindo que ações incorretas sejam identificadas e corrigidas facilmente.
Apesar do progresso significativo, identificaram-se algumas áreas que podem ser melhoradas em fases futuras:

Escalabilidade: Adicionar suporte para um maior número de utilizadores e turnos.
Otimização: Melhorar a eficiência de consultas à base de dados, especialmente em cenários com muitos alunos e turnos.
Interface Gráfica: Evoluir para uma interface gráfica que ofereça uma experiência mais moderna e visual aos utilizadores.
Acreditamos que o trabalho desenvolvido nesta etapa oferece uma base sólida para as fases seguintes do projeto, promovendo um progresso mais eficiente e alinhado com os objetivos definidos inicialmente.