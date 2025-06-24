#!/bin/bash

echo "🏋️ Bella Fit - Backend API"
echo "=========================="

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ Java não encontrado. Instale o Java 17 ou superior."
    exit 1
fi

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado. Instale o Maven 3.6+."
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 ou superior é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java $JAVA_VERSION encontrado"
echo "✅ Maven encontrado"

# Limpar e compilar
echo "🔨 Compilando projeto..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação"
    exit 1
fi

echo "✅ Compilação concluída"

# Executar aplicação
echo "🚀 Iniciando aplicação..."
echo "📡 API disponível em: http://localhost:8080/api"
echo "🔐 Credenciais padrão: admin / admin123"
echo ""
echo "Pressione Ctrl+C para parar"
echo ""

mvn spring-boot:run 