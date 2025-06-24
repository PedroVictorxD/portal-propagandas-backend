# 🏋️ Bella Fit - Backend API

Backend Spring Boot para o sistema de propagandas da academia Bella Fit.

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## 📋 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- PostgreSQL 12+

## 🔧 Configuração

### 1. Configurar PostgreSQL

```bash
# Instalar PostgreSQL (Ubuntu/Debian)
sudo apt update
sudo apt install postgresql postgresql-contrib

# Acessar PostgreSQL
sudo -u postgres psql

# Criar banco de dados
CREATE DATABASE bellafit_propagandas;

# Criar usuário (opcional)
CREATE USER bellafit_user WITH PASSWORD 'bellafit_pass';
GRANT ALL PRIVILEGES ON DATABASE bellafit_propagandas TO bellafit_user;

# Sair do PostgreSQL
\q
```

### 2. Executar Script SQL

```bash
# Conectar ao banco e executar o schema
psql -U postgres -d bellafit_propagandas -f database/schema.sql
```

### 3. Configurar Variáveis de Ambiente

Copie o arquivo `env.example` para `.env` e configure:

```bash
# Configurações do Banco de Dados
DATABASE_URL=jdbc:postgresql://localhost:5432/bellafit_propagandas
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Configurações de Segurança
JWT_SECRET=bellafit-secret-key-2024-change-in-production
JWT_EXPIRATION=86400000

# Configurações do Servidor
SERVER_PORT=8080
```

## 🏃‍♂️ Executando o Projeto

### Desenvolvimento

```bash
# Instalar dependências
mvn clean install

# Executar aplicação
mvn spring-boot:run
```

### Produção

```bash
# Build do projeto
mvn clean package

# Executar JAR
java -jar target/bellafit-propagandas-1.0.0.jar
```

## 📡 Endpoints da API

### Autenticação
- `POST /api/auth/login` - Login do usuário
- `GET /api/auth/health` - Status da API

### Clientes
- `GET /api/clients` - Listar todos os clientes
- `GET /api/clients/{id}` - Buscar cliente por ID
- `POST /api/clients` - Criar novo cliente
- `PUT /api/clients/{id}` - Atualizar cliente
- `DELETE /api/clients/{id}` - Desativar cliente
- `GET /api/clients/search?term={term}` - Buscar clientes

### Propagandas
- `GET /api/advertisements` - Listar propagandas ativas
- `GET /api/advertisements/{id}` - Buscar propaganda por ID
- `POST /api/advertisements` - Criar nova propaganda
- `PUT /api/advertisements/{id}` - Atualizar propaganda
- `DELETE /api/advertisements/{id}` - Desativar propaganda
- `GET /api/advertisements/client/{clientId}` - Propagandas de um cliente

## 🔐 Credenciais Padrão

- **Username**: admin
- **Password**: admin123

## 📊 Estrutura do Banco

### Tabelas Principais

1. **users** - Usuários administradores
2. **clients** - Clientes da academia
3. **advertisements** - Propagandas dos clientes

### Relacionamentos

- Cliente → Propagandas (1:N)
- Usuário → Logs (1:N)

## 🎯 Funcionalidades

### ✅ Sistema de Autenticação
- Login com username e senha
- Tokens de autenticação
- Controle de acesso por roles

### 👥 Gestão de Clientes
- CRUD completo de clientes
- Busca e filtros
- Validação de dados

### 📺 Gestão de Propagandas
- Upload de arquivos (imagens/vídeos)
- Controle de datas e status
- Cálculo automático de dias restantes
- Priorização de exibição

### 📈 Dashboard e Relatórios
- Métricas em tempo real
- Status de pagamentos
- Propagandas expirando

## 🛠️ Desenvolvimento

### Estrutura do Projeto

```
src/main/java/com/bellafit/
├── BellaFitApplication.java
├── controller/
│   ├── AuthController.java
│   ├── ClientController.java
│   └── AdvertisementController.java
├── service/
│   ├── AuthService.java
│   ├── ClientService.java
│   └── AdvertisementService.java
├── repository/
│   ├── UserRepository.java
│   ├── ClientRepository.java
│   └── AdvertisementRepository.java
├── entity/
│   ├── User.java
│   ├── Client.java
│   └── Advertisement.java
└── dto/
    ├── LoginRequest.java
    ├── LoginResponse.java
    ├── ClientDTO.java
    └── AdvertisementDTO.java
```

### Comandos Úteis

```bash
# Limpar e compilar
mvn clean compile

# Executar testes
mvn test

# Gerar documentação
mvn javadoc:javadoc

# Verificar dependências
mvn dependency:tree
```

## 🚀 Deploy

### Docker (Recomendado)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/bellafit-propagandas-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Build e Execução

```bash
# Build da imagem
docker build -t bellafit-propagandas .

# Executar container
docker run -p 8080:8080 bellafit-propagandas
```

## 🔧 Configurações Avançadas

### Logging

```yaml
logging:
  level:
    com.bellafit: DEBUG
    org.springframework.security: DEBUG
  file:
    name: logs/bellafit-api.log
```

### Performance

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 20
        order_inserts: true
        order_updates: true
```

## 🐛 Troubleshooting

### Erro de Conexão com Banco
1. Verificar se PostgreSQL está rodando
2. Verificar credenciais no `application.yml`
3. Executar script SQL

### Erro de Porta
```bash
# Verificar porta em uso
netstat -tulpn | grep :8080

# Matar processo
kill -9 <PID>
```

### Erro de Memória
```bash
# Aumentar heap
java -Xmx2g -jar target/bellafit-propagandas-1.0.0.jar
```

## 📞 Suporte

Para suporte técnico:
- Email: suporte@bellafit.com
- WhatsApp: (84) 99131-9673 