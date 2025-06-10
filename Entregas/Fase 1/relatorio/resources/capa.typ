#import "meta.typ" as meta

#let cover(
    title: meta.titulo,
    subtitle: meta.subtitulo,
    group: meta.group,
    link: meta.link,
    department: meta.departamento,
    degree: meta.curso,
    semester: meta.semestre
) = {
    v(1cm)
    align(center, image(meta.globals.logo, width: 26%))

    v(5mm)
    upper(align(center, text(font: meta.globals.font, 1.55em, weight: 600, title)))

    v(5mm)
    smallcaps(align(center, text(font: meta.globals.font, 1.38em, weight: 500, subtitle)))

    v(5mm)
    align(center, text(font: meta.globals.font, 1.2em, weight: 500, group) + v(1mm) + text(font: meta.globals.font, 1.0em, weight: 100, link))

    v(15mm)
    align(center, text(font: meta.globals.font, 0.8em, weight: 100, department))
    align(center, text(font: meta.globals.font, 0.8em, weight: 100, degree + " " + semester))

    v(15mm)
    align(center, text(font: meta.globals.font, 0.8em, weight: 100, "Equipa de Trabalho:") + v(1mm))
    table(
      columns: 5,
      align: center,
      stroke: none,
      table.header(
        [#image("../images/photo_f.png", width: 90%)],
        [#image("../images/photo_fl.png", width: 90%)],
        [#image("../images/photoR.png", width: 90%)],
        [#image("../images/photo_ap.jpeg", width: 89%)],
        [#image("../images/9e435e0f-6c66-44c8-8c12-6d8c79a52ee9.jpg", width: 90%)],
      ),
      [Fábio Magalhães \ A104365],
      [Filipe Fernandes \ A104185],
      [João Macedo \ A104080],
      [André Pinto \ A104267],
      [Tiago Pereira \ A96429],
    )

    pagebreak()
}