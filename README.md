# 🛺 Fatec Carona

**Fatec Carona** é um aplicativo web desenvolvido como parte do Projeto Integrador da FATEC Cotia. O objetivo é facilitar o transporte dos alunos, permitindo que eles ofereçam ou encontrem caronas entre si, promovendo economia, praticidade e colaboração.

---

## 🎯 Objetivos

O projeto tem como finalidade:

- Ajudar alunos da FATEC com questões de mobilidade urbana;
- Estimular o uso compartilhado de veículos;
- Promover uma rede de apoio entre os estudantes da instituição.

---

## 🛠 Tecnologias Utilizadas

- **Back-end:** Java + Spring Boot  
- **Autenticação:** JWT (JSON Web Token)  
- **Banco de Dados:** MySQL (MySQL Workbench)
- **LOOMBOK**
- **APIs Externas:**  
  - [OpenStreetMap](https://www.openstreetmap.org/) (para mapeamento e rotas)  
  - [ViaCEP](https://viacep.com.br/) (para consulta de endereços via CEP)  

> ⚠️ **Atenção:**  
> A API do OpenStreetMap é open source e colaborativa, ou seja, os próprios usuários alimentam a base de dados.  
> Por isso, pode acontecer de alguns endereços não serem encontrados, o que pode impactar a experiência na aplicação.

---

## 🚀 Como Rodar o Projeto

### 1. Instalar o Lombok

O projeto utiliza **Lombok** para reduzir a verbosidade do código Java. Para configurar corretamente:

🔗 Acesse este tutorial:  

### [Gustavo Furtado de Oliveira Alves](https://dicasdeprogramacao.com.br/autor/gustavo-furtado-de-oliveira-alves/)[{ Dicas de Java }](https://dicasdeprogramacao.com.br/categoria/dicas-de-java/)[2 Comentários](https://dicasdeprogramacao.com.br/como-configurar-o-lombok-no-eclipse/#disqus_thread)

### 2. Importar o Projeto

- Importe como projeto **Maven** na sua IDE (Eclipse, IntelliJ, etc.).
- Certifique-se de que as dependências sejam baixadas corretamente.


📷 Exemplo:  
![image](https://github.com/user-attachments/assets/5944dc98-3d21-4bb1-8218-81a351551a8c)


- **OBS:** por causa do Lommbok pode ser necessário reiniciar a máquina para que as configurações surtam efeito.

  
### 3. Criar o Banco de Dados

- Execute o script `NovoBanco.sql`, disponível neste repositório.

  
### 4. Iniciar o Projeto
- Após configurar o banco e o Lombok, inicie a aplicação normalmente pela classe principal do Spring Boot.

---

### 📌 Primeiro Passo: Criar um Usuário

Você pode criar usuários dos tipos **motorista** ou **passageiro**.

### 🚗 Criar Motorista

URL para motorista 
Post "http://localhost:8080/users/criarMotorista"

Exemplo de JSON:

![image](https://github.com/user-attachments/assets/f26eb761-38aa-4ac6-b0e4-53d84fc719f8)


url para passageiro

Post "http://localhost:8080/users/criarPassageiro"

Exemplo de JSON:

| Exemplo de Requisição | JSON Exemplo |
|-----------------------|--------------|
| ![image](https://github.com/user-attachments/assets/43b01bc0-d4d2-4d7e-941b-baba80ae69f6) | {<br> "nome": "Guilherme Passageiro",<br> "sobrenome": "Rufino",<br> "email": "guilherme.rufinnoo@edxemplo.com",<br> "senha": "senhaSegura123",<br> "telefone": "(11) 91234-5678",<br> "foto": "https://example.com/foto.jpg",<br> "userTypeId": 1,<br> "genderId": 2,<br> "courseId": 3,<br> "userAddressesDTO": {<br> &nbsp;&nbsp;"cityId": 5095,<br> &nbsp;&nbsp;"logradouro": "Rua Raquel de Queiroz",<br> &nbsp;&nbsp;"numero": "123",<br> &nbsp;&nbsp;"bairro": "Santa Maria",<br> &nbsp;&nbsp;"cep": "06149-340"<br> }<br>} |



### Login 
> ⚠️ **Atenção:**  
> nessa rota você receberá um token que será que ser usado em todas as api

URL  para logar na aplicação
Post "http://localhost:8080/users/login"

Exemplo de JSON:

| Exemplo de Requisição | JSON Exemplo |
|-----------------------|--------------|
| ![image](https://github.com/user-attachments/assets/4022930d-674d-405a-89cd-83944572d2ce) | {<br> "email": "guilherme.rufinnoo@edxemplo.com",<br> "senha": "senhaSegura123"<br>} |

 





## 👨‍💻 Autores

- Guilherme Rufino – [LinkedIn](linkedin.com/in/guilherme-rufino-680042269)  
- Projeto Integrador – FATEC Cotia, 2025



