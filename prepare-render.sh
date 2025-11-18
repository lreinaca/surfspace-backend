#!/bin/bash
# Script de preparación para despliegue en Render

echo "════════════════════════════════════════════════════════════"
echo "  🚀 PREPARANDO PROYECTO PARA DESPLIEGUE EN RENDER"
echo "════════════════════════════════════════════════════════════"
echo ""

# Colores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 1. Verificar que estamos en el directorio correcto
echo -e "${BLUE}1️⃣  Verificando ubicación del proyecto...${NC}"
if [ ! -f "build.gradle" ]; then
    echo "❌ Error: No estás en el directorio del proyecto"
    echo "   Ejecuta: cd /Users/lreinac/Desktop/ProyectoFinalAppsEmpresariales/surfspaceBacked"
    exit 1
fi
echo -e "${GREEN}✅ Directorio correcto${NC}"
echo ""

# 2. Compilar el proyecto
echo -e "${BLUE}2️⃣  Compilando proyecto...${NC}"
./gradlew clean build -x test
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Compilación exitosa${NC}"
else
    echo "❌ Error en compilación"
    exit 1
fi
echo ""

# 3. Verificar archivos necesarios
echo -e "${BLUE}3️⃣  Verificando archivos de configuración...${NC}"
files=("Dockerfile" "render.yaml" "DEPLOY-RENDER.md" "build/libs/SurfSpace-App-0.0.1-SNAPSHOT.jar")
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✅ $file${NC}"
    else
        echo -e "${YELLOW}⚠️  $file no encontrado${NC}"
    fi
done
echo ""

# 4. Generar JWT Secret
echo -e "${BLUE}4️⃣  Generando JWT Secret...${NC}"
JWT_SECRET=$(openssl rand -base64 32)
echo -e "${GREEN}✅ JWT Secret generado:${NC}"
echo "   $JWT_SECRET"
echo ""
echo "   💾 Guárdalo para configurarlo en Render"
echo ""

# 5. Verificar Git
echo -e "${BLUE}5️⃣  Verificando Git...${NC}"
if [ -d ".git" ]; then
    echo -e "${GREEN}✅ Repositorio Git inicializado${NC}"
    echo "   Branch actual: $(git branch --show-current)"
    echo "   Último commit: $(git log -1 --oneline)"
else
    echo -e "${YELLOW}⚠️  Git no inicializado${NC}"
    echo "   Ejecuta: git init && git add . && git commit -m 'Initial commit'"
fi
echo ""

# 6. Resumen y próximos pasos
echo "════════════════════════════════════════════════════════════"
echo -e "${GREEN}  ✅ PROYECTO LISTO PARA DESPLEGAR EN RENDER${NC}"
echo "════════════════════════════════════════════════════════════"
echo ""
echo "📋 PRÓXIMOS PASOS:"
echo ""
echo "1️⃣  Subir a GitHub (si no lo has hecho):"
echo "   git init"
echo "   git add ."
echo "   git commit -m 'Preparado para Render'"
echo "   git remote add origin https://github.com/TU_USUARIO/surfspace-backend.git"
echo "   git push -u origin main"
echo ""
echo "2️⃣  Ir a Render.com y crear cuenta"
echo "   👉 https://render.com"
echo ""
echo "3️⃣  Crear PostgreSQL Database:"
echo "   - Name: surfspace-db"
echo "   - Database: surfspace"
echo "   - Plan: Free"
echo ""
echo "4️⃣  Crear Web Service:"
echo "   - Conectar tu repositorio de GitHub"
echo "   - Environment: Docker o Java"
echo "   - Build Command: ./gradlew clean build -x test"
echo "   - Start Command: java -Dserver.port=\$PORT -jar build/libs/SurfSpace-App-0.0.1-SNAPSHOT.jar"
echo ""
echo "5️⃣  Configurar Variables de Entorno en Render:"
echo "   SPRING_PROFILES_ACTIVE=prod"
echo "   JWT_SECRET=$JWT_SECRET"
echo "   (Database URL se configura automáticamente)"
echo ""
echo "📖 Guía completa: Lee DEPLOY-RENDER.md"
echo ""
echo "════════════════════════════════════════════════════════════"

