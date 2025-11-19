# Guía para Probar JWT en SurfSpace Backend

## 🚀 Paso 1: Arrancar la aplicación

### Opción A: Con Docker Compose (Recomendado)
```bash
# 1. Generar y exportar el secreto JWT
export JWT_SECRET="$(openssl rand -base64 32)"

# 2. Levantar los servicios
docker-compose up --build

# En otra terminal, ver logs:
docker-compose logs -f surfspace-app
```

### Opción B: Ejecución local con Gradle
```bash
# 1. Asegúrate de tener MariaDB corriendo en localhost:3306
# 2. Exporta el secreto JWT
export JWT_SECRET="$(openssl rand -base64 32)"

# 3. Arranca la app
./gradlew bootRun
```

---

## 📝 Paso 2: Crear un usuario de prueba

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Alice Lopez",
    "email": "alice@example.com",
    "contrasena": "MiPassSegura123",
    "telefono": "3000000000",
    "rol": "VISITANTE"
  }'
```

**Respuesta esperada:** 
```json
{
  "idUsuario": 1,
  "nombre": "Alice Lopez",
  "email": "alice@example.com",
  "telefono": "3000000000",
  "rol": "VISITANTE"
}
```

---

## 🔐 Paso 3: Obtener token JWT (Login)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "contrasena": "MiPassSegura123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsImlhdCI6MTczMTg2..."
}
```

**💡 Guarda este token en una variable:**
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsImlhdCI6MTczMTg2..."
```

---

## ✅ Paso 4: Probar endpoint protegido CON token (debe funcionar)

```bash
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/secure/me
```

**Respuesta esperada:**
```json
{
  "email": "alice@example.com",
  "authorities": [
    {
      "authority": "ROLE_VISITANTE"
    }
  ],
  "authenticated": true
}
```

---

## ❌ Paso 5: Probar endpoint protegido SIN token (debe fallar)

```bash
curl http://localhost:8080/api/secure/me
```

**Respuesta esperada:** HTTP 401 Unauthorized

---

## 🔴 Paso 6: Probar con token inválido (debe fallar)

```bash
curl -H "Authorization: Bearer token_inventado_123" \
  http://localhost:8080/api/secure/me
```

**Respuesta esperada:** HTTP 401 Unauthorized

---

## 🔍 Paso 7: Inspeccionar el contenido del token

### Opción A: Con Python
```bash
python3 -c "
import sys, base64, json
token = '$TOKEN'
payload = token.split('.')[1]
payload += '=' * (-len(payload) % 4)
print(json.dumps(json.loads(base64.urlsafe_b64decode(payload)), indent=2))
"
```

### Opción B: Usando jwt.io
1. Visita https://jwt.io
2. Pega tu token en el campo "Encoded"
3. Verás el payload decodificado (sub, iat, exp, etc.)

---

## 🧪 Paso 8: Probar otro endpoint protegido

```bash
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/secure/test
```

**Respuesta esperada:**
```
¡JWT funciona! Usuario: alice@example.com
```

---

## 📊 Verificación en Swagger UI

1. Abre http://localhost:8080/swagger-ui/index.html
2. Verás un botón **"Authorize"** (icono de candado) en la parte superior derecha
3. Haz clic y pega tu token (sin "Bearer ", solo el token)
4. Ahora puedes probar los endpoints protegidos desde Swagger
5. Busca la sección **"Security Test"** y prueba:
   - `GET /api/secure/me`
   - `GET /api/secure/test`

---

## 🐛 Troubleshooting

### Error: "Socket fail to connect to localhost"
- Si usas Docker: asegúrate de que el perfil activo es `dev` y que el servicio `db` está corriendo
- Verifica logs: `docker-compose logs -f db`

### Error: "JWT secret is not configured"
- Asegúrate de haber exportado `JWT_SECRET` antes de arrancar
- En Docker Compose, el compose.yml ya lo configura

### Login devuelve 401 "Credenciales inválidas"
- Verifica que el usuario existe en la BD
- Verifica que la contraseña es correcta
- Revisa logs del backend para ver detalles del error de autenticación

### Token funciona pero endpoint devuelve 401
- Verifica que el token no haya expirado (válido por 24 horas)
- Verifica que estás usando el header correcto: `Authorization: Bearer <token>`
- Revisa logs del JwtAuthFilter para ver errores de parsing

---

## 📌 Resumen de URLs importantes

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **API Docs JSON:** http://localhost:8080/v3/api-docs
- **Crear usuario:** POST http://localhost:8080/api/users
- **Login:** POST http://localhost:8080/api/auth/login
- **Test JWT:** GET http://localhost:8080/api/secure/me (requiere token)

---

## ✨ Ejemplo completo en un script

```bash
#!/bin/bash

# Configurar
export JWT_SECRET="$(openssl rand -base64 32)"
BASE_URL="http://localhost:8080"

echo "1️⃣  Creando usuario..."
curl -X POST $BASE_URL/api/users \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test User","email":"test@example.com","contrasena":"Pass1234","telefono":"3001234567","rol":"VISITANTE"}'

echo -e "\n\n2️⃣  Obteniendo token..."
RESPONSE=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","contrasena":"Pass1234"}')

TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | sed 's/"token":"//')

echo "Token obtenido: ${TOKEN:0:50}..."

echo -e "\n\n3️⃣  Probando endpoint protegido CON token..."
curl -H "Authorization: Bearer $TOKEN" $BASE_URL/api/secure/me

echo -e "\n\n4️⃣  Probando endpoint protegido SIN token (debe fallar)..."
curl -i $BASE_URL/api/secure/me 2>&1 | grep "HTTP"

echo -e "\n\n✅ Pruebas completadas!"
```

Guarda este script como `test-jwt.sh`, dale permisos con `chmod +x test-jwt.sh` y ejecútalo con `./test-jwt.sh`

