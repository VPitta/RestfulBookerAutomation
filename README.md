# Restful-Booker Automation

Automação de testes para a API [Restful-Booker](https://restful-booker.herokuapp.com/) utilizando **Java + Rest Assured + TestNG**.  
Inclui também a coleção do **Postman** para execução manual e **Allure Framework** para geração de relatórios.

---

## Estrutura do projeto

```
RestfulBookerAutomation/
├─ pom.xml
├─ src/
│ └─ test/java/com/restfulbooker/RestfulBookerTests.java
└─ postman/
└─ RestfulBooker.postman_collection.json
```

---

## Tecnologias utilizadas

- Java 17
- Maven
- Rest Assured
- TestNG
- JSON
- Postman (para testes manuais)
- Allure Framework (relatórios de teste)

---

## Funcionalidades testadas

- **Ping / HealthCheck**  
- **Get Booking IDs**  
- **Get Booking**  
- **Create Booking**  
- **Update Booking (PUT)**  
- **Partial Update Booking (PATCH)**  
- **Delete Booking**

> Cada teste cria um booking próprio para garantir independência.  
> PUT e PATCH utilizam **Basic Auth**.  
> DELETE utiliza **token** gerado dinamicamente.

---

## Como rodar a automação

1. Certifique-se de ter **Java 17** e **Maven** instalados.
2. Clone o repositório:

```bash
git clone https://github.com/seuusuario/RestfulBookerAutomation.git
cd RestfulBookerAutomation
mvn clean test
mvn allure:serve

```

##  Postman

A coleção Postman está dentro da pasta postman/.

Pode ser importada no Postman para execução manual de todos os endpoints.

Útil para validar respostas ou comparar com os testes automatizados.

## Observações

Cada teste é independente e garante que o booking exista antes de manipular.

PUT/PATCH usam Basic Auth (admin / password123).

DELETE pode usar token ou Basic Auth.

Relatórios gerados pelo Allure permitem análise detalhada dos resultados.
