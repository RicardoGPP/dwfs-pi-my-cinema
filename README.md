 # MyCinema

MyCinema é uma plataforma web desenvolvida como parte da disciplina de Projetos Integrados do curso de Pós-graduação em Desenvolvimento Web Fullstack da PUC Minas. O objetivo do projeto é oferecer uma experiência intuitiva na escolha de filmes em cinemas locais. Com funcionalidades que permitem visualizar os filmes em cartaz, acessar informações detalhadas, comentários e sentimentos dos espectadores, o MyCinema visa facilitar a vida dos cinéfilos e dos funcionários dos cinemas.

O diferencial do MyCinema está na integração com APIs modernas para enriquecer a experiência do usuário:
- **TMDB**: Utilizado para fornecer informações completas sobre os filmes, como sinopse, elenco e pôsteres.
- **OpenAI**: Utilizada para gerar resumos inteligentes dos sentimentos dos espectadores sobre os filmes, proporcionando uma visão geral das opiniões e críticas do público.

## Funcionalidades

### Para Clientes do Cinema
- **Consulta de Filmes**: Veja facilmente quais filmes estão em cartaz, entrarão ou acabaram de sair de cartaz.
- **Detalhes do Filme**: Informações completas sobre o filme, incluindo resumos gerados por IA (OpenAI) a partir dos comentários dos espectadores.
- **Comentários e Sentimentos**: Visualize o sentimento geral dos espectadores em relação a cada filme.
- **Edição de Perfil**: Os usuários podem fazer edições em seus dados pessoais no site, incluindo informações do perfil.

### Para Funcionários do Cinema
- **Cadastro de Filmes e Sessões**: Cadastre novos filmes, associe-os a uma unidade e defina as datas e horários das sessões.
- **Gestão de Unidades de Cinema**: Cadastre novas unidades e gerencie as salas disponíveis.

## Tecnologias Utilizadas

### Backend
- **Java com Quarkus**: O backend foi desenvolvido utilizando o framework Quarkus, por ser leve e ideal para aplicativos cloud-native.
- **PostgreSQL**: Banco de dados para armazenamento das informações de filmes, sessões e usuários.
- **Integrações de API**:
  - **TMDB**: API utilizada para obter informações dos filmes, como descrição, elenco e pôsteres.
  - **OpenAI**: Utilizada para gerar resumos de sentimento dos comentários dos espectadores sobre os filmes.

### Frontend
- **Vue.js 3**: Framework JavaScript utilizado para desenvolver uma interface de usuário moderna e reativa.
- **PrimeVue**: Biblioteca de componentes UI para Vue.js, proporcionando uma interface amigável e moderna.

### Infraestrutura e Deploy
- **AWS**: Utilizada para hospedar a aplicação.
  - **Elastic Beanstalk**: Utilizado para gerenciar o ambiente do backend.
  - **S3**: Utilizado para armazenar e servir os arquivos estáticos do frontend.
- **Render**: Banco de dados PostgreSQL em produção.
- **GitHub Actions**: Workflow de CI/CD configurado para buildar, testar e realizar o deploy da aplicação.

## Estrutura do Repositório

```
MyCinema/
  ├── backend/          # Código do backend em Java (Quarkus)
  ├── frontend/         # Código do frontend em Vue.js 3
  ├── infrastructure/   # Armazena o arquivo de configuração do CloudFormation
  ├── .github/workflows # Arquivos do GitHub Actions para CI/CD
  └── README.md         # Este arquivo
```

## Deploy na AWS

### Workflow do GitHub Actions
O deploy na AWS é realizado por um workflow do GitHub Actions, que segue os seguintes passos:

1. **Provisionamento de Infraestrutura**:
   - A infraestrutura da aplicação é definida através de um arquivo CloudFormation encontrado na pasta `infrastructure/`. Este arquivo cria os seguintes recursos:
     - **Buckets S3**: Um bucket para o backend e outro para o frontend, onde os artefatos são armazenados.
       - O bucket do frontend é configurado para servir o site estático, disponibilizando os arquivos do Vue.js diretamente ao usuário.
     - **Elastic Beanstalk Application**: Criação da aplicação MyCinema no Elastic Beanstalk, com um ambiente para o backend.
     - **Elastic Beanstalk Backend Environment**: Ambiente configurado para rodar o backend em uma instância EC2 utilizando a stack "64bit Amazon Linux 2023 v4.3.2 running Corretto 21". Também são configuradas variáveis de ambiente como informações de banco de dados, chaves de API, e credenciais de administrador.

2. **Build e Deploy**:
   - **Elastic Beanstalk**: Utilizado para gerenciar o ambiente do backend, garantindo uma implantação escalável e de fácil manutenção.
   - **S3**: O frontend é armazenado e servido como um site estático diretamente pelo bucket S3, garantindo alta disponibilidade e fácil distribuição de conteúdo.

## Deploy na Render

O banco de dados PostgreSQL em produção está sendo gerenciado pela plataforma Render, garantindo escalabilidade e alta disponibilidade.
