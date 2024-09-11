# Aplicação para cadastro de integrantes e time Élin Duxus

## Detalhes para rodar a aplicação
#### Banco de Dados:
##### Postgres
##### Faça o download do pgAdmin 4

![image](https://github.com/user-attachments/assets/d21ad237-49ca-49a8-bb49-6c4562d96036)

## Esta aplicação tem Front-End
#### O Front-End está em outro repositório:
#### https://github.com/Quintanilha09/Front-End-Desafio-Duxus.git
#### Não esqueça de testar a API pelo front.

## Contexto da aplicação
#### http://localhost:8080
#### A aplicação está configurada para rodar na porta 8080. Verifique se a porta 8080 já está sendo usada por outro serviço, caso esteja, mate a execução desse serviço

## Endpoints Integrante
#### http://localhost:8080/integrante/cadastrar
#### http://localhost:8080/integrante/listar
#### http://localhost:8080/integrante/{id}
#### http://localhost:8080/integrante/atualizar/{id}
#### http://localhost:8080/integrante/excluir/{id}

## Endpoints Time
#### http://localhost:8080/time/cadastrar
#### http://localhost:8080/time/listar
#### http://localhost:8080/time/{id}
#### http://localhost:8080/time/atualizar/{id}
#### http://localhost:8080/time/deletar/{id}

## Endpoints Consultas Específicas
#### http://localhost:8080/times/data?data=yyyy-mm-dd
#### http://localhost:8080/integrante-mais-usado?dataInicial=yyyy-mm-dd&dataFinal=yyyy-mm-dd
#### http://localhost:8080/time-mais-comum?dataInicial=yyyy-mm-dd&dataFinal=yyyy-mm-dd
#### http://localhost:8080/funcao-mais-comum?dataInicial=yyyy-mm-dd&dataFinal=yyyy-mm-dd
#### http://localhost:8080/franquia-mais-famosa?dataInicial=yyyy-mm-dd&dataFinal=yyyy-mm-dd
#### http://localhost:8080/contagem-por-franquia?dataInicial=yyyy-mm-dd&dataFinal=yyyy-mm-dd
#### http://localhost:8080/contagem-por-funcao?dataInicial=yyyy-mm-dd&dataFinal=yyyy-mm-dd

## Foi utilizado o Postman para realizar as consultas pelos endpoints.
Não deixe de testar a API pelo Postman




