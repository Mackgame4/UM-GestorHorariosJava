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
  caption: "Modelo de Dominio.",
  kind: image,
  image("./images/modelodedominio.jpg", width: 140%)
)

#pagebreak()

= Diagrama de Use Case e Use Cases
#v(15pt)

- Diagrama de Use Case
#figure(
  caption: "Diagrama de Use Case.",
  kind: image,
  image("./images/diagramausecases.png", width: 120%)
)

\

- Uses Cases Desenvolvidos
#figure(
  caption: "Descrição de Use Case 'Iniciar Sessão'.",
  kind: image,
  image("./images/usecase_1.png", width: 100%)
)
#v(15pt)
#figure(
  caption: "Descrição de Use Case 'Ver Horário'.",
  kind: image,
  image("./images/usecase_2.png", width: 100%)
)
#v(15pt)
#figure(
  caption: "Descrição de Use Case 'Gerar Credenciais'.",
  kind: image,
  image("./images/usecase_3.png", width: 100%)
)
#v(15pt)
#figure(
  caption: "Descrição de Use Case 'Alocar Alunos Manualmente'.",
  kind: image,
  image("./images/usecase_4.png", width: 100%)
)
#v(15pt)
#figure(
  caption: "Descrição de Use Case 'Troca Manual de Turno'.",
  kind: image,
  image("./images/usecase_5.png", width: 100%)
)
#v(15pt)
#figure(
  caption: "Descrição de Use Case 'Escolher Horário paa Alunos com Estatudo'.",
  kind: image,
  image("./images/usecase_6.png", width: 100%)
)
#v(15pt)
#figure(
  caption: "Descrição de Use Case 'Gerar Horário'.",
  kind: image,
  image("./images/usecase_7.png", width: 100%)
)

#pagebreak()

= Conclusão
#v(15pt)
Na nossa opinião, esta fase do projeto revelou-se uma experiência enriquecedora, proporcionando uma abordagem mais estruturada e organizada ao desenvolvimento do sistema. O tempo dedicado à análise de requisitos, à modelação do domínio e à elaboração dos use cases permitiu-nos consolidar uma visão clara e coerente do problema.

A criação dos modelos facilitou a compreensão das necessidades do sistema e das suas funcionalidades, contribuindo para o desenvolvimento de soluções mais consistentes. Consideramos que o trabalho realizado nesta fase inicial oferece uma base sólida para as etapas seguintes de implementação, promovendo um progresso mais eficiente e alinhado com os objetivos estabelecidos.