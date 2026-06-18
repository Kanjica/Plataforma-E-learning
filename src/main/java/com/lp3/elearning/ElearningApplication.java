package com.lp3.elearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableCaching
public class ElearningApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    	dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
		
		SpringApplication.run(ElearningApplication.class, args);
	}
}


// Requisitos Funcionais:
// ▪ Cadastro de cursos com título, descrição, instrutor e carga horária
// ▪ Cadastro de módulos dentro de cada curso
// ▪ Cadastro de aulas dentro de cada módulo (título, conteúdo,
// vídeo/link)
// ▪ Cadastro e autenticação de usuários (Aluno, Instrutor)
// ▪ Matrícula de alunos em cursos
// ▪ Marcação de aulas como concluídas
// ▪ Cálculo automático de progresso do aluno (% de conclusão)
// ▪ Dashboard do aluno com cursos em andamento
// ▪ Certificado de conclusão ao finalizar 100% do curso
// ▪ Sistema de avaliação de cursos pelos alunos
// ▪ Painel do instrutor para gerenciar seus cursos
// ▪ Fórum de dúvidas por curso
// o Requisitos Não Funcionais:
// ▪ Progresso deve ser salvo automaticamente
// ▪ Barra de progresso visual em cada curso
// ▪ Validação de sequência (não pular módulos)
// ▪ Interface amigável e intuitiva para navegação
// ▪ Responsividade total para estudo mobile
// ▪ Sistema deve suportar múltiplos alunos assistindo
// simultaneamente
// ▪ Certificado deve ser gerado em formato PDF ou imagem
// ▪ Tempo de carregamento de aulas < 2 segundos
// ▪ Sistema de busca eficiente de cursos