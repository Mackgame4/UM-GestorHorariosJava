all: dev

MYSQL_JAR=lib/mysql-connector-java-8.0.22.jar

build:
	@javac -classpath src;$(MYSQL_JAR) -d bin src/Main.java

run:
	@java -cp bin;$(MYSQL_JAR) Main

dev:
	$(MAKE) build
	$(MAKE) run

clean:
	@rm -rf bin/*

relatorio: relatorio_build

relatorio_build:
	@echo "Compilando relatorio..."
	@typst compile relatorio/relatorio.typ

relatorio_watch:
	@echo "Assistindo alteracoes no relatorio..."
	@typst watch relatorio/relatorio.typ

relatorio_clean:
	@rm -rf relatorio/relatorio.pdf