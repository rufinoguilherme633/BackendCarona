# Fatec Carona

Aplicativo web para oferecer e encontrar caronas entre alunos da FATEC.

## Tecnologias
- Java + Spring Boot
- JWT
- MySql Workbanch
- Api externas
  - Open Street mapping
  - ViaCep
  - 
 # Como rodar

 ## 1 instalar lombok 
# **Como configurar o lombok no Eclipse**[Gustavo Furtado de Oliveira Alves](https://dicasdeprogramacao.com.br/autor/gustavo-furtado-de-oliveira-alves/)[{ Dicas de Java }](https://dicasdeprogramacao.com.br/categoria/dicas-de-java/)[2 Comentários](https://dicasdeprogramacao.com.br/como-configurar-o-lombok-no-eclipse/#disqus_thread)

Como já comentamos [**aqui**](https://dicasdeprogramacao.com.br/como-configurar-o-lombok-no-intellij-idea/), o Lombok facilita muito o desenvolvimento de códigos java, minimizando a quantidade de código necessário no seu projeto.

O Lombok atua no processo de compilação do seu código adicionando códigos automaticamente. Tirando de você a necessidade de escrever alguns códigos como por exemplo os métodos GET, SET, hashcode, equals etc.

Se você usa o Eclipse como ambiente de desenvolvimento e quer usar o Lombok no seu projeto, o que você precisa fazer é ...

# **1. Baixar o jar do Lombok**

Você pode baixar o jar do Lombok diretamente do site do Projeto Lombok, mas esse aqui é um link direto para baixar o arquivo jar do Lombok.

[**>> Clique AQUI para baixar o lombok.jar**](https://projectlombok.org/downloads/lombok.jar)

# **2. Executar o jar do Lombok**

Para executar o Lombok basta dar "dois cliques" no arquivo ou, se você preferir, você pode usar o seguinte comando no prompt:

`java -jar lombok.jar`

![](https://dicasdeprogramacao.com.br/images/como-configurar-o-lombok-no-eclipse/executar-lombok.jar.gif)

# **3. Selecionar o local onde está o eclipse**

Como você pode ver no gif acima, após executar o `lombok.jar`, aparece uma tela do instalador do Lombok e a primeira coisa que ele faz é scanear o seu sistema operacional para tentar encontrar "eclipses". No meu caso ele encontrou uma pasta onde tem um eclipse.

Mas se o seu eclipse não for encontrado pelo instalador do Lombok, você pode clicar em `Specify location...` e selecionar a pasta onde está o seu eclipse.

# **4. Instalar o Lombok no eclipse**

Com a pasta do seu eclipse selecionada (você pode selecionar mais de uma), basta clicar no botão `Install / Update`.

![](https://dicasdeprogramacao.com.br/images/como-configurar-o-lombok-no-eclipse/instalar-lombok-no-eclipse.gif)

# **5. Reiniciar o eclipse**

Agora você precisa fechar (se estiver aberto) e abrir o eclipse novamente.

# **6. Adicionar o Lombok como dependência do seu projeto.**

Por fim, você deve adicionar o jar do lombok no seu projeto. Se você estiver utilizando o maven, basta adicionar o código abaixo no bloco `<dependencies>` do seu `pom.xml`.

**`<dependency>**    **<groupId>**org.projectlombok**</groupId>**    **<artifactId>**lombok**</artifactId>**    **<version>**1.18.2**</version>**    **<scope>**provided**</scope></dependency>**`

# **7. Rebuild do seu projeto.**

Se o build não estiver automático no seu eclipse, você precisa fazer o rebuild. Atalho: `Ctrl+B`.

Pronto, seu eclipse está apto a funcionar com o Lombok.
