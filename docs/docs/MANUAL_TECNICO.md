# Manual Técnico — Mini ERP

**Universidad de San Carlos de Guatemala**
Centro Universitario de Occidente
División Ciencias de la Ingeniería — Ingeniería en Ciencias y Sistemas
Curso: Manejo e Implementación de Archivos

---

## Índice

1. [Objetivo del documento](#1-objetivo-del-documento)
2. [Arquitectura general](#2-arquitectura-general)
3. [Tecnologías utilizadas](#3-tecnologías-utilizadas)
4. [Estructura del proyecto](#4-estructura-del-proyecto)
5. [Base de datos](#5-base-de-datos)
6. [Migraciones con Flyway](#6-migraciones-con-flyway)
7. [Arquitectura del backend por capas](#7-arquitectura-del-backend-por-capas)
8. [Anotaciones de Spring: qué hace cada una y por qué](#8-anotaciones-de-spring-qué-hace-cada-una-y-por-qué)
9. [Seguridad: autenticación con JWT](#9-seguridad-autenticación-con-jwt)
10. [Acceso a datos con JdbcTemplate](#10-acceso-a-datos-con-jdbctemplate)
11. [Transacciones](#11-transacciones)
12. [Módulos del sistema](#12-módulos-del-sistema)
13. [Generación de la factura PDF](#13-generación-de-la-factura-pdf)
14. [Bitácora del sistema](#14-bitácora-del-sistema)
15. [Reportes](#15-reportes)
16. [Arquitectura del frontend](#16-arquitectura-del-frontend)
17. [API REST: catálogo de endpoints](#17-api-rest-catálogo-de-endpoints)
18. [Instalación y ejecución](#18-instalación-y-ejecución)
19. [Decisiones de diseño](#19-decisiones-de-diseño)

---

## 1. Objetivo del documento

Este manual describe la construcción interna del sistema Mini ERP: cómo está
organizado el código, por qué se tomó cada decisión técnica y qué hace cada
pieza. Está dirigido a una persona que necesite mantener, corregir o ampliar
el sistema.

El manual de usuario, que explica cómo **operar** el sistema, es un documento
aparte.

---

## 2. Arquitectura general

El sistema sigue una **arquitectura cliente-servidor** compuesta por dos
aplicaciones completamente independientes que se comunican por HTTP mediante
una API REST.

```
┌──────────────────┐      HTTP / JSON      ┌──────────────────┐      JDBC      ┌──────────────┐
│     CLIENTE      │  ──────────────────▶  │     SERVIDOR     │  ───────────▶  │ BASE DE DATOS│
│                  │                       │                  │                │              │
│   Angular 21     │  ◀──────────────────  │  Spring Boot     │  ◀───────────  │  PostgreSQL  │
│   Bootstrap 5    │      API REST         │  Java 21         │                │    proy1     │
│ localhost:4200   │                       │ localhost:8080   │                │  puerto 5432 │
└──────────────────┘                       └──────────────────┘                └──────────────┘
```

Puntos clave de esta separación:

- **El cliente nunca se conecta a la base de datos.** Angular solo conoce URLs
  de la API. No tiene credenciales de PostgreSQL ni sabe qué tablas existen.
- **El servidor es el único responsable de hablar con PostgreSQL.** Toda regla
  de negocio (validar existencias, calcular IVA, registrar movimientos) vive
  en el servidor, no en el navegador. Esto es importante porque el código que
  corre en el navegador puede ser manipulado por el usuario; el del servidor no.
- **Cada aplicación se ejecuta por separado**, en su propio puerto, y puede
  desplegarse en máquinas distintas sin cambiar el código (solo la URL base).

---

## 3. Tecnologías utilizadas

| Componente | Tecnología | Versión |
|---|---|---|
| Lenguaje del servidor | Java | 21 |
| Framework del servidor | Spring Boot | 4.1.1 |
| Gestor de dependencias | Maven | (wrapper incluido) |
| Framework del cliente | Angular | 21 |
| Estilos | Bootstrap | 5 |
| Base de datos | PostgreSQL | 18.6 |
| Migraciones de BD | Flyway | (incluido en Spring Boot) |
| Acceso a datos | Spring JDBC (JdbcTemplate) | — |
| Seguridad | Spring Security + JJWT | 0.12.6 |
| Generación de PDF | OpenPDF | 1.3.30 |
| Manejo de JSON en el servidor | org.json | 20250107 |

---

## 4. Estructura del proyecto

```
Proyecto01/
├── cliente/                      Aplicación Angular
│   └── src/app/
│       ├── components/           Pantallas de la aplicación
│       ├── services/             Comunicación con la API REST
│       ├── guards/               Protección de rutas
│       ├── interceptors/         Envío automático del token
│       ├── app.routes.ts         Definición de rutas
│       └── app.config.ts         Configuración global
│
└── servidor/                     Aplicación Spring Boot
    ├── pom.xml                   Dependencias del proyecto
    └── src/main/
        ├── java/com/example/servidor/
        │   ├── Model/            Clases de datos (POJOs)
        │   ├── DB/               Acceso a datos (consultas SQL)
        │   ├── Services/         Lógica de negocio
        │   ├── Controller/       Endpoints REST
        │   └── config/           Seguridad y JWT
        └── resources/
            ├── application.properties
            └── db/migration/     Scripts SQL de Flyway
```

### Por qué esta división de paquetes

Cada paquete tiene **una sola responsabilidad**. Esto se conoce como separación
de responsabilidades y es uno de los requisitos del proyecto:

- **`Model`**: solo guarda datos. No consulta, no valida, no calcula.
- **`DB`**: solo ejecuta SQL. No decide reglas de negocio.
- **`Services`**: solo aplica reglas de negocio. No sabe nada de HTTP.
- **`Controller`**: solo recibe peticiones HTTP y devuelve respuestas. No
  contiene lógica de negocio ni SQL.
- **`config`**: configuración del framework (seguridad, CORS, filtros).

La ventaja práctica: si una consulta SQL falla, se busca en `DB`. Si una
validación está mal, en `Services`. Si una URL no responde, en `Controller`.
No hay que revisar todo el proyecto.

---

## 5. Base de datos

La base de datos se llama **`proy1`** y contiene 12 tablas.

### Diagrama de relaciones

```
        Rol ──1───N── Usuario
                         │ 1
                         │
          ┌──────────────┼──────────────┬─────────────────┐
          │ N            │ N            │ N               │ N
       Compra          Venta      LogSistema        (registra)
          │ 1            │ 1
          │              │
          │ N            │ N
   DetalleCompra    DetalleVenta
          │ N            │ N
          └──────┬───────┘
                 │ 1
              Producto ──1───N── MovimientoInventario
                 │ N
                 │
         ProductoProveedor
                 │ N
                 │
             Proveedor ──1───N── Compra

          Cliente ──1───N── Venta
```

### Descripción de las tablas

| Tabla | Propósito | Llave primaria |
|---|---|---|
| `Rol` | Áreas del sistema y sus permisos | `id_rol` (SERIAL) |
| `Usuario` | Empleados con acceso al sistema | `id_usuario` VARCHAR(5) |
| `Producto` | Catálogo de productos y existencias | `id_producto` VARCHAR(5) |
| `Proveedor` | Proveedores de la empresa | `id_proveedor` VARCHAR(5) |
| `ProductoProveedor` | Qué producto ofrece cada proveedor (N:M) | `(id_producto, id_proveedor)` |
| `Cliente` | Clientes de la empresa | `id_cliente` (SERIAL) |
| `Compra` | Cabecera de cada compra | `id_compra` (SERIAL) |
| `DetalleCompra` | Productos de cada compra | `(id_compra, id_producto)` |
| `Venta` | Cabecera de cada venta | `id_venta` (SERIAL) |
| `DetalleVenta` | Productos de cada venta | `(id_venta, id_producto)` |
| `MovimientoInventario` | Historial de entradas y salidas | `id_movimiento` (SERIAL) |
| `LogSistema` | Bitácora de acciones de usuarios | `id_log` (SERIAL) |

### Decisiones de diseño de la base de datos

**Llaves primarias compuestas en las tablas de detalle.**
`DetalleCompra` y `DetalleVenta` usan `(id_compra, id_producto)` y
`(id_venta, id_producto)` como llave primaria, en lugar de un id propio
autoincremental. Esto ahorra espacio (no se guarda una columna extra) y, más
importante, **impide por diseño que un mismo producto se repita dos veces
dentro de la misma compra o venta**: la base de datos rechazaría el segundo
registro. Lo mismo aplica a `ProductoProveedor`.

**Restricciones CHECK.**
Las reglas de negocio más importantes están escritas en la propia base de
datos, no solo en Java:

```sql
precio   NUMERIC(12,2) NOT NULL CHECK (precio >= 0),
stock    NUMERIC(12,2) NOT NULL CHECK (stock >= 0),
cantidad NUMERIC(12,2) NOT NULL CHECK (cantidad > 0),
tipo_movimiento VARCHAR(10) NOT NULL CHECK (tipo_movimiento IN ('ENTRADA','SALIDA')),
origen          VARCHAR(10) NOT NULL CHECK (origen IN ('COMPRA','VENTA'))
```

El `CHECK (stock >= 0)` es la garantía final de que **nunca habrá existencias
negativas**: aunque un error de programación intentara restar de más, PostgreSQL
rechazaría la operación.

**Baja lógica (soft delete).**
Ninguna entidad se elimina físicamente. `Producto`, `Proveedor`, `Cliente` y
`Usuario` tienen una columna `activo BOOLEAN`. Para "eliminar" se ejecuta:

```sql
UPDATE Producto SET activo = FALSE WHERE id_producto = ?;
```

Esto responde al requisito de conservar la información histórica: si se borrara
un producto físicamente, todas las compras y ventas que lo referencian
quedarían rotas (de hecho, PostgreSQL ni siquiera lo permitiría por las llaves
foráneas).

**Índices.**
Se crearon índices sobre las columnas que más se filtran en los reportes:

```sql
CREATE INDEX idx_compra_fecha ON Compra(fecha);
CREATE INDEX idx_venta_fecha ON Venta(fecha);
CREATE INDEX idx_movimiento_producto ON MovimientoInventario(id_producto);
CREATE INDEX idx_log_usuario ON LogSistema(id_usuario);
```

Un índice funciona como el índice de un libro: permite a PostgreSQL saltar
directamente a las filas buscadas en vez de recorrer la tabla completa. El
costo es espacio en disco y un pequeño retraso en cada INSERT, por eso solo
se ponen donde realmente se usan.

---

## 6. Migraciones con Flyway

La base de datos **no se crea a mano**. Se construye a partir de scripts SQL
versionados que Flyway ejecuta automáticamente al arrancar el servidor.

Ubicación: `servidor/src/main/resources/db/migration/`

| Archivo | Contenido |
|---|---|
| `V1__crear_tablas.sql` | Creación de las 12 tablas, restricciones e índices |
| `V2__datos_iniciales.sql` | Roles y los 4 usuarios del sistema |
| `V3__datos_iniciales_operativos.sql` | Proveedores, productos, clientes, compras y ventas |
| `V4__logs_iniciales.sql` | Registros de bitácora de ejemplo |

### Cómo funciona

1. Al arrancar, Flyway revisa la tabla `flyway_schema_history` de la base de
   datos para saber qué migraciones ya se aplicaron.
2. Ejecuta en orden las que faltan.
3. Registra cada una con un *checksum* (una huella del contenido del archivo).

**Regla crítica de mantenimiento:** nunca se debe modificar un archivo `V...sql`
que ya fue aplicado. Flyway detecta el cambio de checksum y se niega a arrancar
con el error *"Migration checksum mismatch"*. Para corregir algo, se crea una
migración nueva con el siguiente número.

El formato del nombre es obligatorio: `V` + número + **doble guion bajo** +
descripción + `.sql`. Si el nombre no cumple, Flyway ignora el archivo en
silencio.

### Contraseñas en la carga inicial

Las contraseñas de los usuarios **no se guardan en texto plano**. En
`V2__datos_iniciales.sql` se insertan ya cifradas con BCrypt:

```sql
INSERT INTO Usuario (id_usuario, nombre_usuario, email, password, id_rol, activo) VALUES
    ('U0001', 'Admin General', 'admin@minierp.com',
     '$2b$10$cZKypEwLggE7c98D.4g5nO2rW1KzO.mTf5jruaotEQIOTe33HJWZW',
     (SELECT id_rol FROM Rol WHERE permisos = 'ADMIN'), TRUE);
```

Nótese el uso de una subconsulta `(SELECT id_rol FROM Rol WHERE permisos = 'ADMIN')`
en lugar de escribir el número 1 directamente: así el script funciona sin
importar qué id autoincremental le haya tocado a cada rol.

---

## 7. Arquitectura del backend por capas

Una petición atraviesa siempre las mismas cuatro capas, en el mismo orden:

```
   Angular
      │  HTTP POST /api/ventas  { JSON }
      ▼
┌─────────────────────────────────────────────────────┐
│ Controller   VentaController                        │
│ Recibe el JSON, lo convierte a objeto Venta,        │
│ llama al Service y arma la respuesta HTTP.          │
└─────────────────────────────────────────────────────┘
      │  registrarVenta(venta)
      ▼
┌─────────────────────────────────────────────────────┐
│ Service      VentaService                           │
│ Valida existencias, calcula subtotal, IVA y total,  │
│ coordina las operaciones dentro de una transacción. │
└─────────────────────────────────────────────────────┘
      │  insertarVenta(venta), disminuirStock(...)
      ▼
┌─────────────────────────────────────────────────────┐
│ DB           VentaDB                                │
│ Ejecuta las sentencias SQL con JdbcTemplate.        │
└─────────────────────────────────────────────────────┘
      │  JDBC
      ▼
   PostgreSQL
```

Y en sentido inverso para las consultas, usando los **RowMapper** para convertir
cada fila del resultado en un objeto de `Model`.

### Ejemplo concreto: registrar una venta

1. **Controller** recibe el JSON y Spring lo convierte en un objeto `Venta`.
2. **Service** valida: ¿existe el cliente?, ¿hay suficiente stock de cada
   producto?, ¿las cantidades son positivas? Si algo falla, devuelve un mensaje
   de error y no se toca la base de datos.
3. **Service** calcula subtotal, IVA (12%) y total. Este cálculo **no se
   confía al cliente**: aunque Angular muestre una vista previa, el servidor
   recalcula todo con los precios reales de la base de datos.
4. **DB** inserta la venta y devuelve el id generado; luego inserta cada línea
   de detalle, cada movimiento de inventario y actualiza el stock.
5. **Controller** devuelve el id de la venta, que Angular usa para pedir la
   factura PDF.

---

## 8. Anotaciones de Spring: qué hace cada una y por qué

Esta sección explica el significado de cada anotación utilizada en el proyecto.

### El concepto base: el contenedor de Spring

Antes de las anotaciones hay que entender el problema que resuelven. En Java
tradicional, para usar una clase se crea una instancia:

```java
ProductoDB productoDB = new ProductoDB();
```

Esto trae dos problemas en una aplicación web:

1. `ProductoDB` necesita una conexión a la base de datos. Habría que crearla
   también, y cerrarla, y manejar un pool de conexiones a mano.
2. Si cada clase hace `new ProductoDB()`, se crean decenas de instancias
   innecesarias de lo mismo.

Spring resuelve esto con un **contenedor de objetos** (el `ApplicationContext`).
Al arrancar, Spring crea **una sola instancia** de cada clase marcada con una
anotación de componente, la guarda, y se la entrega a quien la necesite. Esos
objetos administrados se llaman **beans**.

En la práctica: el proyecto **nunca escribe `new`** para estas clases. Se piden
en el constructor y Spring las inyecta. Esto se llama **inyección de
dependencias**.

```java
@Service
public class ProductoService {

    private final ProductoDB productoDB;   // no se crea con new

    public ProductoService(ProductoDB productoDB) {   // Spring lo inyecta aquí
        this.productoDB = productoDB;
    }
}
```

---

### `@Repository` — capa de acceso a datos

**Dónde se usa:** `ProductoDB`, `ProveedorDB`, `ClienteDB`, `CompraDB`,
`VentaDB`, `InventarioDB`, `ReporteDB`, `LogDB`, `LogInDB`.

**Qué hace:**

1. Le indica a Spring que esta clase es un bean y debe administrarla (igual
   que `@Component`).
2. Declara que su responsabilidad es **el acceso a datos**. Esto es
   documentación viva: cualquiera que abra la clase sabe qué esperar de ella.
3. Activa la **traducción de excepciones**: convierte las excepciones propias
   de PostgreSQL (`SQLException`, `PSQLException`) en excepciones genéricas de
   Spring (`DataAccessException`). Gracias a esto, si algún día el sistema
   migrara a otro motor de base de datos, las capas superiores no cambiarían.

```java
@Repository
public class ProductoDB {
    private final JdbcTemplate jdbcTemplate;

    public ProductoDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

**Por qué esta clase y no otra:** porque aquí, y solo aquí, se escriben
sentencias SQL.

---

### `@Service` — capa de lógica de negocio

**Dónde se usa:** `ProductoService`, `ProveedorService`, `ClienteService`,
`CompraService`, `VentaService`, `InventarioService`, `ReporteService`,
`FacturaService`, `LogService`, `JwtService`, `ValidarLogIn`.

**Qué hace:** marca la clase como bean administrado por Spring y declara que
contiene **reglas de negocio**.

**Por qué es una capa separada del Controller:** las reglas del negocio no
dependen de HTTP. Validar que hay stock suficiente antes de vender es una regla
de la empresa, no de la web. Si mañana el sistema se usara desde una aplicación
de escritorio, la capa `Services` se reutilizaría sin cambios.

```java
@Service
public class VentaService {

    @Transactional
    public String registrarVenta(Venta venta) {
        String error = validarVenta(venta);   // regla: no vender sin existencias
        if (error != null) {
            return error;
        }
        // cálculo de subtotal, IVA y total
    }
}
```

Técnicamente `@Service` y `@Repository` hacen casi lo mismo (ambos derivan de
`@Component`). La diferencia es semántica y, en el caso de `@Repository`, la
traducción de excepciones. Usarlas correctamente hace el código autoexplicativo.

---

### `@RestController` — capa de exposición HTTP

**Dónde se usa:** `LoginController`, `ProductoController`, `ProveedorController`,
`ClienteController`, `CompraController`, `VentaController`,
`InventarioController`, `ReporteController`, `LogController`.

**Qué hace:** es la combinación de dos anotaciones:

- `@Controller`: marca la clase como bean y como receptora de peticiones web.
- `@ResponseBody`: indica que **lo que devuelvan los métodos se convierte
  directamente en el cuerpo de la respuesta** (normalmente JSON), en lugar de
  interpretarse como el nombre de una página HTML.

Sin `@ResponseBody`, si un método devolviera la palabra `"productos"`, Spring
buscaría una plantilla HTML llamada `productos.html`. Con `@RestController`,
devuelve el texto o el objeto convertido a JSON, que es lo que necesita una
API REST consumida por Angular.

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarProductos());
    }
}
```

Spring convierte automáticamente la lista de objetos `Producto` a un arreglo
JSON usando la librería Jackson, leyendo los métodos `get...()` de la clase.
Por eso los modelos necesitan getters: sin ellos, el JSON saldría vacío.

---

### `@RequestMapping` — ruta base del controlador

Define el prefijo de URL para todos los métodos de la clase:

```java
@RequestMapping("/api/productos")
```

Así, un `@GetMapping("/{id}")` dentro de esa clase responde a
`/api/productos/PR001`.

---

### `@GetMapping`, `@PostMapping`, `@PutMapping` — verbos HTTP

Indican a qué método HTTP responde cada función:

| Anotación | Verbo | Uso en el proyecto |
|---|---|---|
| `@GetMapping` | GET | Consultar datos (listados, búsquedas, reportes) |
| `@PostMapping` | POST | Crear registros nuevos (login, crear producto, registrar venta) |
| `@PutMapping` | PUT | Actualizar registros existentes (editar, desactivar) |

**Detalle importante sobre el orden:** cuando dos rutas pueden confundirse, la
más específica debe declararse primero:

```java
@GetMapping("/inactivos")      // primero: ruta fija
public ResponseEntity<List<Producto>> listarInactivos() { ... }

@GetMapping("/{id}")           // después: comodín
public ResponseEntity<Producto> buscarPorId(@PathVariable String id) { ... }
```

Si estuvieran al revés, Spring interpretaría la palabra "inactivos" como si
fuera un id de producto.

**Nota sobre la desactivación:** se usa `@PutMapping("/{id}/desactivar")` y no
`@DeleteMapping`, porque semánticamente **no se está borrando nada**: se
actualiza el campo `activo` a `false`. El registro permanece en la base de datos.

---

### `@PathVariable` — parte variable de la URL

Extrae un valor que viene dentro de la ruta:

```java
@GetMapping("/{id}")
public ResponseEntity<Producto> buscarPorId(@PathVariable String id) { ... }
```

Al llamar a `/api/productos/PR001`, el parámetro `id` recibe `"PR001"`.

---

### `@RequestParam` — parámetros de consulta

Extrae valores que vienen después del `?` en la URL:

```java
@GetMapping("/movimientos")
public ResponseEntity<List<MovimientoInventario>> movimientos(
        @RequestParam(name = "idProducto", required = false) String idProducto,
        @RequestParam(name = "fechaInicio", required = false) String fechaInicio) { ... }
```

Al llamar a `/api/inventario/movimientos?idProducto=PR001&fechaInicio=2026-01-01`,
cada parámetro recibe su valor.

`required = false` permite que el parámetro se omita; en ese caso llega como
`null` y el filtro simplemente no se aplica. Así, un mismo endpoint sirve para
"todos los movimientos" y para "los movimientos filtrados".

---

### `@RequestBody` — cuerpo de la petición

Convierte el JSON enviado por Angular en un objeto Java:

```java
@PostMapping
public ResponseEntity<String> crear(@RequestBody Producto producto) { ... }
```

Si Angular envía `{"nombreProducto":"Silla","categoria":"Sillas","precio":250}`,
Spring crea un objeto `Producto` y llama a sus métodos `set...()` para llenarlo.
Por eso los modelos necesitan un **constructor vacío** y **setters**: sin ellos,
Spring no puede construir el objeto.

---

### `@Configuration` — clase de configuración

**Dónde se usa:** `SecurityConfig`.

Indica que la clase no contiene lógica de negocio, sino **definiciones de
objetos que Spring debe crear y administrar**. Spring la procesa al arrancar.

---

### `@Bean` — objeto administrado creado manualmente

**Dónde se usa:** dentro de `SecurityConfig`.

`@Repository` y `@Service` sirven para clases propias del proyecto, donde se
puede poner la anotación sobre la clase. Pero `BCryptPasswordEncoder` pertenece
a Spring Security: no se puede modificar su código fuente para anotarla.

`@Bean` resuelve esto: el método crea el objeto y Spring administra el resultado
como un bean único.

```java
@Bean
public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

A partir de aquí, cualquier clase puede pedir un `BCryptPasswordEncoder` en su
constructor y Spring le entregará **siempre esta misma instancia**.

---

### `@Component` — bean genérico

**Dónde se usa:** `JwtAuthFilter`.

Es la anotación base. `@Service`, `@Repository` y `@Controller` son
especializaciones de `@Component`. Se usa cuando la clase no encaja en ninguna
de las capas anteriores: `JwtAuthFilter` no es lógica de negocio ni acceso a
datos, es un filtro de infraestructura.

---

### `@Value` — leer configuración externa

Inyecta un valor desde `application.properties`:

```java
@Value("${jwt.secret}")
private String secreto;

@Value("${inventario.stock-minimo:17}")
private double stockMinimo;
```

El `:17` después del nombre es un **valor por defecto**: si la propiedad no
existe en el archivo, se usa 17 y la aplicación arranca igual.

**Por qué no escribir estos valores directamente en el código:** la clave secreta
del JWT y el nivel de stock mínimo son configuración, no lógica. Cambiarlos no
debería requerir recompilar el proyecto.

---

### `@Transactional` — operaciones todo o nada

**Dónde se usa:** `CompraService.registrarCompra()` y
`VentaService.registrarVenta()`.

Esta es la anotación más importante del proyecto desde el punto de vista de la
integridad de los datos.

**El problema que resuelve:** registrar una venta implica cuatro operaciones
separadas sobre la base de datos:

1. Insertar la cabecera en `Venta`
2. Insertar cada línea en `DetalleVenta`
3. Insertar un `MovimientoInventario` de tipo SALIDA por producto
4. Restar la cantidad del `stock` de cada producto

Si el sistema fallara después del paso 2 (un corte de energía, un error de
programación, una restricción violada), quedaría una venta registrada **sin
haber descontado el inventario**. El sistema mostraría existencias que en
realidad ya se vendieron. El inventario quedaría permanentemente incorrecto.

**Cómo lo resuelve:** `@Transactional` envuelve todo el método en una
transacción de base de datos. PostgreSQL mantiene los cambios "pendientes"
hasta que el método termina correctamente:

- Si el método **termina bien** → se confirman todos los cambios (`COMMIT`).
- Si se lanza **cualquier excepción** → se deshacen todos los cambios
  (`ROLLBACK`), como si nada hubiera pasado.

```java
@Transactional
public String registrarVenta(Venta venta) {
    // ... validaciones ...
    Integer idVenta = ventaDB.insertarVenta(venta);

    for (int i = 0; i < venta.getDetalles().size(); i++) {
        DetalleVenta detalle = venta.getDetalles().get(i);
        ventaDB.insertarDetalle(idVenta, detalle);
        ventaDB.insertarMovimientoSalida(detalle.getIdProducto(), detalle.getCantidad(), idVenta);
        ventaDB.disminuirStock(detalle.getIdProducto(), detalle.getCantidad());
    }
    return null;
}
```

Con esto se cumple el requisito del proyecto: *"Las operaciones realizadas en
un área pueden afectar la información utilizada por otras áreas; el sistema
deberá mantener la consistencia de los datos durante todo el proceso."*

**Detalle de implementación:** `@Transactional` funciona porque Spring crea un
*proxy* alrededor del bean. Por eso solo surte efecto cuando el método se llama
**desde otra clase** (por ejemplo, desde el Controller), no cuando una clase se
llama a sí misma internamente.

---

### Resumen de anotaciones

| Anotación | Capa | Propósito |
|---|---|---|
| `@Repository` | DB | Acceso a datos + traducción de excepciones SQL |
| `@Service` | Services | Lógica de negocio |
| `@RestController` | Controller | Endpoints REST que devuelven JSON |
| `@Component` | config | Bean genérico (filtros, utilidades) |
| `@Configuration` | config | Clase que define beans |
| `@Bean` | config | Objeto administrado creado manualmente |
| `@RequestMapping` | Controller | Ruta base del controlador |
| `@GetMapping` / `@PostMapping` / `@PutMapping` | Controller | Verbo HTTP del método |
| `@PathVariable` | Controller | Valor dentro de la URL |
| `@RequestParam` | Controller | Parámetro después del `?` |
| `@RequestBody` | Controller | JSON del cuerpo convertido a objeto |
| `@Value` | cualquiera | Valor leído de application.properties |
| `@Transactional` | Services | Operación todo o nada |
| `@Override` | cualquiera | Indica que se sobrescribe un método heredado |

---

## 9. Seguridad: autenticación con JWT

### Por qué JWT y no variables de sesión

En una aplicación de escritorio, guardar el usuario que inició sesión en una
variable estática global funciona, porque **solo una persona usa el programa a
la vez**.

En un servidor web esto es un error grave: el mismo proceso atiende a **todos
los usuarios simultáneamente**. Si el usuario logueado se guardara en una
variable compartida, cuando el Usuario B inicia sesión sobrescribiría los datos
del Usuario A, y A podría terminar operando con la identidad y los permisos de B.

La solución es que el servidor **no recuerde nada entre peticiones**
(*stateless*). En su lugar, la identidad viaja dentro de cada petición, dentro
de un token firmado.

### Qué es un JWT

Un JSON Web Token es una cadena de texto con tres partes separadas por puntos:

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtaW5pZXJwLmNvbSIsImlkVXN1YXJpbyI6IlUwMDAxIn0.K8fT2...
└──── cabecera ────┘ └──────────── contenido ────────────┘ └─── firma ───┘
```

- **Cabecera**: el algoritmo de firma.
- **Contenido (claims)**: los datos del usuario. En este proyecto: el email,
  el `idUsuario` y el `idRol`.
- **Firma**: un código calculado con una clave secreta que solo conoce el
  servidor.

El contenido **no está cifrado**, cualquiera puede leerlo. Lo que garantiza la
firma es que **nadie puede alterarlo**: si alguien cambiara el `idRol` para
darse permisos de administrador, la firma dejaría de coincidir y el servidor
rechazaría el token.

Por eso un JWT nunca debe contener información sensible como contraseñas.

### Flujo completo

```
1. LOGIN
   Angular ──POST /api/auth/login {email, password}──▶ LoginController
                                                            │
                                    ValidarLogIn ◀──────────┘
                                         │
                            LogInDB: SELECT ... WHERE email = ?
                                         │
                            BCrypt.matches(password, hashGuardado)
                                         │
                                  JwtService.generarToken()
                                         │
   Angular ◀──{success, token, idUsuario, idRol}────────────┘
        │
        └──▶ localStorage


2. CUALQUIER PETICIÓN POSTERIOR
   Angular ──GET /api/productos──▶ AuthInterceptor agrega:
                                   Authorization: Bearer eyJhbGci...
                                              │
                                              ▼
                                   JwtAuthFilter (servidor)
                                   - lee el header
                                   - valida la firma y la expiración
                                   - registra al usuario en SecurityContextHolder
                                              │
                                              ▼
                                   SecurityConfig: ¿esta ruta requiere login?
                                              │
                                     sí ──────┴────── no
                                      │               │
                            ¿hay usuario?        deja pasar
                             │        │
                            sí       no
                             │        │
                        Controller   401
```

### Cifrado de contraseñas con BCrypt

Las contraseñas **nunca se guardan en texto plano**. Se almacena un *hash*
generado con BCrypt:

```
Contraseña real:  admin123
Hash guardado:    $2b$10$cZKypEwLggE7c98D.4g5nO2rW1KzO.mTf5jruaotEQIOTe33HJWZW
```

BCrypt tiene dos características importantes:

1. **Es de un solo sentido.** Del hash no se puede recuperar la contraseña.
2. **Usa un salt aleatorio.** La misma contraseña cifrada dos veces produce
   hashes **diferentes**, y ambos son válidos.

Esta segunda característica explica una decisión de diseño que a primera vista
sorprende: **la consulta SQL del login no compara la contraseña.**

```java
// DB/LogInDB.java
String sql = "SELECT * FROM Usuario WHERE email = ?";   // solo por email
```

Un `WHERE password = ?` nunca funcionaría, porque habría que comparar el texto
plano contra un hash, y jamás coincidirían. La verificación se hace en Java:

```java
// Services/ValidarLogIn.java
boolean correcta = passwordEncoder.matches(passwordTextoPlano, user.getPassword());
```

`matches()` extrae el salt del hash guardado, cifra la contraseña ingresada con
ese mismo salt y compara los resultados.

### `JwtAuthFilter`: el guardia de cada petición

Hereda de `OncePerRequestFilter`, que garantiza que se ejecute exactamente una
vez por petición HTTP.

```java
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // Sin token: se deja pasar sin autenticar (útil para /api/auth/login)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);   // quita "Bearer " (7 caracteres)

        try {
            Claims claims = jwtService.validarToken(token);

            String idUsuario = claims.get("idUsuario", String.class);
            Integer idRol = claims.get("idRol", Integer.class);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + idRol));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(idUsuario, null, authorities);

            // Línea clave: registra quién es el usuario para ESTA petición
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            System.err.print(e);   // token inválido o expirado: no se autentica
        }

        filterChain.doFilter(request, response);
    }
}
```

`SecurityContextHolder` es un almacén temporal asociado al hilo que atiende la
petición actual. Se limpia al terminar. Esto es lo que reemplaza a la variable
global de sesión, y es lo que permite que la bitácora sepa qué usuario realizó
cada acción:

```java
// Services/LogService.java
Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
String idUsuario = autenticacion.getName();
```

### `SecurityConfig`: las reglas

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
    http
        .cors(cors -> {})
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

Línea por línea:

- **`.cors(cors -> {})`**: activa la configuración de CORS definida en el mismo
  archivo.
- **`.csrf(csrf -> csrf.disable())`**: CSRF es una protección diseñada para
  aplicaciones que usan sesiones con cookies. Como este sistema usa JWT sin
  sesiones, no aplica; si no se desactivara, Spring bloquearía incluso el login.
- **`SessionCreationPolicy.STATELESS`**: indica que el servidor no debe crear
  ni guardar sesiones. Cada petición se autentica por sí sola con su token.
- **`.requestMatchers("/api/auth/**").permitAll()`**: el login es público, por
  necesidad: no se puede exigir un token para obtener un token.
- **`.anyRequest().authenticated()`**: todo lo demás requiere que el
  `JwtAuthFilter` haya validado un token.
- **`.addFilterBefore(...)`**: inserta el filtro propio antes del filtro
  estándar de Spring Security. Sin esta línea, el filtro nunca se ejecutaría.

**Nota sobre la sintaxis con flechas (`cors -> {}`):** es la única forma
soportada por la versión actual de Spring Security para configurar `HttpSecurity`;
el estilo anterior está obsoleto. Corresponde a la configuración del framework,
no a la lógica de negocio del sistema.

### CORS

Los navegadores bloquean, por seguridad, que una página servida desde un origen
(`localhost:4200`) haga peticiones a otro (`localhost:8080`), salvo que el
servidor lo autorice explícitamente:

```java
configuracion.setAllowedOrigins(List.of("http://localhost:4200"));
configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
configuracion.setAllowedHeaders(List.of("*"));
configuracion.setAllowCredentials(true);
```

Sin esta configuración, el navegador bloquearía todas las llamadas de Angular
antes de que llegaran al servidor.

### Control de acceso por rol

El sistema define cuatro roles, correspondientes a las áreas de la empresa:

| id_rol | Área | Acceso |
|---|---|---|
| 1 | Administración | Todos los módulos, incluidos Reportes y Bitácora |
| 2 | Compras | Proveedores y Compras |
| 3 | Inventario | Productos e Inventario |
| 4 | Ventas | Clientes y Ventas |

El control se aplica en el **navbar de Angular**: cada opción del menú se
muestra solo si el rol del usuario corresponde.

```typescript
esCompras(): boolean {
  return this.idRol === 1 || this.idRol === 2;
}
```

El rol se obtiene del token devuelto en el login y se guarda en `localStorage`.

La infraestructura del backend está preparada para restringir también a nivel
de endpoint, ya que `JwtAuthFilter` asigna la autoridad `ROLE_1`, `ROLE_2`, etc.
Para activarlo bastaría con agregar reglas en `SecurityConfig`:

```java
.requestMatchers("/api/compras/**").hasAnyAuthority("ROLE_1", "ROLE_2")
```

---

## 10. Acceso a datos con JdbcTemplate

El proyecto usa **SQL escrito manualmente** con `JdbcTemplate`, no un ORM como
Hibernate/JPA.

**Motivo:** el objetivo específico del proyecto indica *"Aplicar operaciones de
consulta y manipulación de información utilizando PostgreSQL y SQL"*. Además,
el módulo de reportes requiere consultas con agrupaciones, rankings y
agregaciones que con un ORM terminarían escribiéndose igualmente en SQL nativo.
Usar un solo enfoque mantiene el código coherente.

### La conexión a la base de datos

No existe ninguna clase `DBConnectionSingleton` en el proyecto, y es
intencional. Spring Boot crea automáticamente un **pool de conexiones**
(HikariCP) a partir de la configuración:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/proy1
spring.datasource.username=postgres
spring.datasource.password=...
```

Un pool mantiene varias conexiones abiertas y las presta a cada petición que
las necesita. Esto es indispensable en un servidor web: si todas las peticiones
compartieran **una sola** conexión (como haría un singleton clásico), dos
usuarios simultáneos podrían interferir entre sí.

El `ApplicationContext` de Spring cumple aquí el papel del patrón Singleton:
administra una única instancia del `DataSource` y la inyecta donde se necesite.

### `JdbcTemplate`

Ejecuta el SQL y se encarga del código repetitivo: pedir la conexión al pool,
preparar la sentencia, recorrer resultados, cerrar todo y traducir errores.

```java
private final String QUERY_BUSCAR_POR_ID = "SELECT * FROM Producto WHERE id_producto = ?";

public Producto buscarPorId(String idProducto) {
    List<Producto> resultado = jdbcTemplate.query(
        QUERY_BUSCAR_POR_ID,
        new ProductoRowMapper(),
        idProducto                  // reemplaza el ?
    );
    return resultado.isEmpty() ? null : resultado.get(0);
}
```

**Los signos `?` y la inyección SQL.** Los valores nunca se concatenan al texto
del SQL. Se pasan como parámetros y el driver los envía por separado. Esto
elimina el riesgo de **inyección SQL**: si alguien escribiera
`'; DROP TABLE Producto; --` en un campo de búsqueda, se trataría como un texto
a buscar, no como código a ejecutar.

### Los RowMapper

`JdbcTemplate` devuelve un `ResultSet`, que es la tabla cruda de resultados. Un
`RowMapper` traduce **una fila** a **un objeto** del paquete `Model`:

```java
public class ProductoRowMapper implements RowMapper<Producto> {

    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getString("id_producto"));
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setCategoria(rs.getString("categoria"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getDouble("stock"));
        p.setActivo(rs.getBoolean("activo"));
        return p;
    }
}
```

Se ejecuta automáticamente una vez por cada fila del resultado. Está escrito
como una clase propia (no como expresión lambda) para mantener el estilo
orientado a objetos del proyecto.

### Obtener el id generado: `RETURNING`

Cuando se inserta una compra o una venta, PostgreSQL genera el id
automáticamente (`SERIAL`). Para insertar luego los detalles hace falta conocer
ese id. Se resuelve con la cláusula `RETURNING` de PostgreSQL:

```java
private final String QUERY_INSERTAR_VENTA =
    "INSERT INTO Venta (id_cliente, id_usuario, fecha, subtotal, iva, total) " +
    "VALUES (?, ?, now(), ?, ?, ?) RETURNING id_venta";

public Integer insertarVenta(Venta venta) {
    return jdbcTemplate.queryForObject(QUERY_INSERTAR_VENTA, Integer.class, ...);
}
```

El `INSERT` devuelve el id recién creado en la misma operación.

---

## 11. Transacciones

Explicadas en detalle en la sección de `@Transactional`. En resumen: los dos
métodos que modifican varias tablas a la vez (`registrarCompra` y
`registrarVenta`) son transaccionales, de modo que el inventario nunca quede
en un estado intermedio inconsistente.

---

## 12. Módulos del sistema

### Productos

CRUD completo con baja lógica. El **stock no es editable manualmente**: se crea
en 0 y solo cambia mediante compras y ventas. Esto garantiza que toda variación
de existencias tenga un movimiento que la respalde y un origen rastreable.

El id se genera automáticamente con formato `PR001`, `PR002`... porque la tabla
usa `VARCHAR(5)` en lugar de `SERIAL`:

```java
public String generarSiguienteId() {
    String maximo = jdbcTemplate.queryForObject("SELECT MAX(id_producto) FROM Producto", String.class);
    if (maximo == null) {
        return "PR001";
    }
    int numero = Integer.parseInt(maximo.substring(2)) + 1;
    return String.format("PR%03d", numero);
}
```

### Proveedores y Clientes

CRUD equivalente. `Cliente` usa `id_cliente SERIAL`, por lo que el id lo genera
PostgreSQL y no hace falta generarlo en Java. Se valida que el NIT no esté
repetido antes de insertar, para no violar la restricción `UNIQUE`.

### Compras

Registra una compra con varias líneas de detalle. Efecto sobre el inventario:

| Operación | Efecto |
|---|---|
| `INSERT INTO Compra` | Cabecera: proveedor, usuario, fecha, total |
| `INSERT INTO DetalleCompra` | Una fila por producto |
| `INSERT INTO MovimientoInventario` | Tipo `ENTRADA`, origen `COMPRA` |
| `UPDATE Producto SET stock = stock + ?` | Aumenta la existencia |

No existe edición ni eliminación de compras: el historial debe conservarse. Una
corrección se haría registrando un movimiento de ajuste, no alterando el pasado.

### Ventas

Similar a compras, pero con dos diferencias importantes:

**1. Validación de existencias antes de registrar:**

```java
if (producto.getStock() < detalle.getCantidad()) {
    return "Existencia insuficiente de " + producto.getNombreProducto() +
           ". Disponible: " + producto.getStock() +
           ", solicitado: " + detalle.getCantidad();
}
```

**2. Cálculo de subtotal, IVA y total en el servidor:**

```java
private static final double PORCENTAJE_IVA = 0.12;

double iva = subtotal * PORCENTAJE_IVA;
double total = subtotal + iva;
```

Angular muestra una vista previa de estos montos mientras se arma la venta,
pero el servidor **los recalcula** con los precios reales de la base de datos.
Los valores enviados por el cliente se descartan. Esto evita que alguien
manipule los totales desde el navegador.

El efecto sobre el inventario es el inverso al de compras: movimiento `SALIDA`
con origen `VENTA`, y `stock = stock - ?`.

### Inventario

Tres funciones:

1. **Existencias actuales** de todos los productos activos.
2. **Alerta de existencias bajas**: productos cuyo stock es menor o igual al
   mínimo configurado en `application.properties`
   (`inventario.stock-minimo=17`). Se usa un valor configurable en lugar de una
   columna por producto para mantener simple el CRUD.
3. **Historial de movimientos**, con filtros por producto, tipo de movimiento y
   rango de fechas.

El historial responde al requisito de *"determinar cómo y por qué se han
producido cambios en las cantidades disponibles de un producto"*. La consulta
enlaza cada movimiento con la compra o venta que lo originó para mostrar el
proveedor o cliente involucrado:

```sql
SELECT m.*, p.nombre_producto,
       COALESCE(pv.nombre_proveedor, cl.nombre) AS tercero,
       COALESCE(c.id_usuario, v.id_usuario) AS id_usuario
FROM MovimientoInventario m
INNER JOIN Producto p ON m.id_producto = p.id_producto
LEFT JOIN Compra c    ON m.origen = 'COMPRA' AND m.id_referencia = c.id_compra
LEFT JOIN Proveedor pv ON c.id_proveedor = pv.id_proveedor
LEFT JOIN Venta v     ON m.origen = 'VENTA' AND m.id_referencia = v.id_venta
LEFT JOIN Cliente cl  ON v.id_cliente = cl.id_cliente
WHERE 1 = 1
```

Se usa `LEFT JOIN` porque cada movimiento proviene **o** de una compra **o** de
una venta, nunca de ambas: con `INNER JOIN` no se obtendría ninguna fila.
`COALESCE` devuelve el primer valor no nulo de los dos.

---

## 13. Generación de la factura PDF

La factura se genera con **OpenPDF** en el servidor.

**Decisión de diseño: el PDF no se almacena.** Se construye en el momento a
partir de los datos de la base de datos, cada vez que se solicita. Ventajas:

- No ocupa espacio en disco ni requiere respaldos adicionales.
- Cualquier factura, de cualquier fecha, está siempre disponible.
- Si se corrigiera un dato del cliente, la factura reimpresa lo reflejaría.

El endpoint devuelve los bytes del PDF con las cabeceras adecuadas para que el
navegador lo descargue como archivo:

```java
@GetMapping("/{id}/factura")
public ResponseEntity<byte[]> descargarFactura(@PathVariable Integer id) {
    byte[] pdf = facturaService.generarFactura(id);

    if (pdf == null) {
        return ResponseEntity.notFound().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "factura_" + id + ".pdf");

    return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
}
```

La factura incluye: nombre de la empresa, número de factura, fecha, datos del
cliente (nombre y NIT), **el usuario que registró la venta**, la tabla de
productos con cantidades y precios, y el desglose de subtotal, IVA y total.

En Angular se recibe como `Blob` (datos binarios) y se dispara la descarga:

```typescript
descargarFactura(id: number): Observable<Blob> {
  return this.http.get(`${this.apiUrl}/${id}/factura`, { responseType: 'blob' });
}

guardarPdf(archivo: Blob, nombre: string): void {
  const url = URL.createObjectURL(archivo);
  const enlace = document.createElement('a');
  enlace.href = url;
  enlace.download = nombre;
  enlace.click();
  setTimeout(() => URL.revokeObjectURL(url), 1000);
}
```

`responseType: 'blob'` es necesario porque, por defecto, Angular intentaría
interpretar la respuesta como JSON y fallaría al recibir un archivo binario.

---

## 14. Bitácora del sistema

Registra las acciones relevantes de los usuarios en la tabla `LogSistema`.

**Dónde se registra:** en los controllers, **después** de que la operación se
completó con éxito. Esto es deliberado: el registro queda fuera de la
transacción de negocio, de modo que un fallo al guardar un log nunca provoque
que se revierta una venta o una compra.

```java
// Services/LogService.java
public void registrar(String accion, String modulo, String detalle) {
    String idUsuario = obtenerUsuarioActual();

    if (idUsuario == null) {
        System.err.println("No se registro el log: no hay usuario autenticado");
        return;
    }

    try {
        logDB.insertarLog(idUsuario, accion, modulo, recortar(detalle));
    } catch (DataAccessException e) {
        System.err.println("Error al registrar el log: " + e.getMessage());
    }
}
```

El usuario se obtiene del `SecurityContextHolder`, es decir, del token de la
petición actual. No se confía en un id enviado por el cliente.

**Constantes en lugar de textos sueltos:**

```java
public static final String MODULO_PRODUCTOS = "PRODUCTOS";
public static final String ACCION_CREAR = "CREAR";
```

Si se escribiera `"PRODCUTOS"` por error en una llamada, el log quedaría con un
módulo inexistente y no aparecería en los filtros. Con constantes, un error de
escritura no compila.

**Acciones registradas:** `INICIO_SESION`, `CREAR`, `ACTUALIZAR`, `DESACTIVAR`,
`REGISTRAR` (compras y ventas) y `GENERAR_FACTURA`.

**Módulos:** `AUTENTICACION`, `PRODUCTOS`, `PROVEEDORES`, `CLIENTES`, `COMPRAS`,
`VENTAS`.

La consulta admite filtros por módulo y rango de fechas, que se agregan solo si
tienen valor:

```java
if (modulo != null && !modulo.trim().isEmpty()) {
    sql.append(" AND l.modulo = ?");
    parametros.add(modulo);
}
```

---

## 15. Reportes

Once reportes agrupados en cuatro categorías, todos con filtro por rango de
fechas.

| Categoría | Reporte |
|---|---|
| Productos e inventario | Top 10 más vendidos |
| | Productos con menor existencia |
| | Productos con mayor cantidad de movimientos |
| | Historial de movimientos de un producto |
| Compras y proveedores | Compras dentro de un rango de fechas |
| | Top 5 proveedores por monto acumulado |
| | Productos adquiridos con mayor frecuencia |
| Ventas y clientes | Ventas dentro de un rango de fechas |
| | Top 10 clientes por monto acumulado |
| | Top 10 productos por ingresos generados |
| | Resumen de ventas agrupado por período |
| Bitácora | Interacciones de los usuarios |

### Diseño de las consultas

Todas las consultas de ranking devuelven **las mismas seis columnas**, mediante
alias:

```sql
SELECT p.id_producto     AS codigo,
       p.nombre_producto AS nombre,
       p.categoria       AS detalle,
       SUM(dv.cantidad)  AS cantidad,
       COUNT(*)          AS conteo,
       SUM(dv.cantidad * dv.precio_unitario) AS monto
FROM DetalleVenta dv
INNER JOIN Venta v    ON dv.id_venta = v.id_venta
INNER JOIN Producto p ON dv.id_producto = p.id_producto
WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
GROUP BY p.id_producto, p.nombre_producto, p.categoria
ORDER BY SUM(dv.cantidad) DESC
LIMIT 10
```

Esto permite que **un solo modelo** (`ReporteFila`) y **un solo RowMapper**
sirvan para ocho reportes distintos. Cuando un reporte no necesita alguna
columna, se llena con `0` (por ejemplo, `0 AS cantidad` en el top de
proveedores).

### Manejo del filtro de fechas

Las consultas tienen siempre la misma forma, sin partes opcionales:

```sql
WHERE CAST(v.fecha AS DATE) BETWEEN ? AND ?
```

Cuando el usuario no selecciona fechas, el **servicio** envía un rango muy
amplio, de modo que el filtro incluye todos los registros:

```java
private static final String FECHA_MINIMA = "1900-01-01";
private static final String FECHA_MAXIMA = "2999-12-31";

private Date fechaDesde(String fecha) {
    if (fecha == null || fecha.trim().isEmpty()) {
        return Date.valueOf(FECHA_MINIMA);
    }
    return Date.valueOf(fecha);
}
```

Ventaja: el SQL queda fijo y legible, sin construirse dinámicamente por partes.

### Elementos SQL utilizados

| Elemento | Función |
|---|---|
| `INNER JOIN` | Combina filas de dos tablas que coinciden en la llave |
| `SUM(...)` | Suma valores dentro de cada grupo |
| `COUNT(*)` | Cuenta filas dentro de cada grupo |
| `GROUP BY` | Agrupa las filas que comparten el mismo valor |
| `ORDER BY ... DESC` | Ordena de mayor a menor |
| `LIMIT n` | Devuelve solo las primeras n filas (el "Top") |
| `CAST(fecha AS DATE)` | Descarta la hora para comparar solo el día |
| `BETWEEN ? AND ?` | Rango inclusivo en ambos extremos |
| `TO_CHAR(fecha, 'YYYY-MM')` | Convierte la fecha a texto para agrupar por mes |

El resumen por período usa tres consultas separadas (día, mes, año) en lugar de
concatenar el formato al SQL, evitando construir sentencias con texto variable.

---

## 16. Arquitectura del frontend

### Estructura

```
src/app/
├── components/      Una carpeta por pantalla (.ts, .html, .css)
├── services/        Un servicio por entidad: comunicación HTTP
├── guards/          auth-guard: protege rutas
├── interceptors/    auth-interceptor: agrega el token
├── app.routes.ts    Mapa de rutas
└── app.config.ts    Configuración global
```

### Servicios

Cada servicio concentra las llamadas HTTP de una entidad, de modo que los
componentes no conocen URLs:

```typescript
@Injectable({ providedIn: 'root' })
export class Producto {
  private apiUrl = 'http://localhost:8080/api/productos';

  constructor(private http: HttpClient) { }

  listarProductos(): Observable<ProductoInterface[]> {
    return this.http.get<ProductoInterface[]>(this.apiUrl);
  }
}
```

`providedIn: 'root'` crea una instancia única compartida por toda la aplicación
(el equivalente de un `@Service` en Spring).

### Interceptor

Agrega el token a **todas** las peticiones salientes, de forma automática:

```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  if (token) {
    const reqConToken = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(reqConToken);
  }
  return next(req);
};
```

Sin el interceptor, habría que agregar la cabecera manualmente en cada una de
las decenas de llamadas del sistema.

### Guard

Impide acceder a rutas protegidas sin haber iniciado sesión:

```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  if (authService.estadoLogin()) {
    return true;
  }
  router.navigate(['/login']);
  return false;
};
```

Es una protección de **experiencia de usuario**, no de seguridad: la seguridad
real está en el servidor, que rechaza toda petición sin token válido.

### Signals

Angular 21 funciona sin Zone.js por defecto, por lo que asignar un valor a una
propiedad normal no actualiza la vista. Se usan **signals**, que notifican
automáticamente el cambio:

```typescript
productos = signal<ProductoInterface[]>([]);

this.productos.set(datos);     // escribir
```

```html
@if (productos().length > 0) {     <!-- leer: con paréntesis -->
  @for (producto of productos(); track producto.idProducto) { ... }
}
```

Un signal es una variable que avisa cuándo cambia. La única diferencia práctica
es la sintaxis: se lee con `()` y se escribe con `.set(...)`.

### Configuración global

```typescript
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor]))
  ]
};
```

**Nota sobre SSR:** el proyecto se generó inicialmente con renderizado del lado
del servidor (Server-Side Rendering) activado. Se **desactivó** porque con SSR
el código se ejecuta primero en Node.js, donde `localStorage` no existe, lo que
rompía la lectura del token. SSR está pensado para sitios públicos que necesitan
posicionamiento en buscadores; un sistema interno detrás de un login no obtiene
beneficio de él.

---

## 17. API REST: catálogo de endpoints

Todos los endpoints requieren la cabecera `Authorization: Bearer <token>`,
excepto el login.

### Autenticación

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Inicia sesión y devuelve el token |

### Productos

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/productos` | Lista los productos activos |
| GET | `/api/productos/inactivos` | Lista los productos desactivados |
| GET | `/api/productos/{id}` | Busca un producto |
| POST | `/api/productos` | Crea un producto |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| PUT | `/api/productos/{id}/desactivar` | Baja lógica |

### Proveedores

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/proveedores` | Lista los proveedores activos |
| GET | `/api/proveedores/inactivos` | Lista los desactivados |
| GET | `/api/proveedores/{id}` | Busca un proveedor |
| POST | `/api/proveedores` | Crea un proveedor |
| PUT | `/api/proveedores/{id}` | Actualiza un proveedor |
| PUT | `/api/proveedores/{id}/desactivar` | Baja lógica |

### Clientes

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/clientes` | Lista los clientes activos |
| GET | `/api/clientes/inactivos` | Lista los desactivados |
| GET | `/api/clientes/{id}` | Busca un cliente |
| POST | `/api/clientes` | Crea un cliente |
| PUT | `/api/clientes/{id}` | Actualiza un cliente |
| PUT | `/api/clientes/{id}/desactivar` | Baja lógica |

### Compras

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/compras` | Historial de compras |
| GET | `/api/compras/{id}` | Compra con su detalle |
| POST | `/api/compras` | Registra una compra |

### Ventas

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/ventas` | Historial de ventas |
| GET | `/api/ventas/{id}` | Venta con su detalle |
| POST | `/api/ventas` | Registra una venta y devuelve su id |
| GET | `/api/ventas/{id}/factura` | Descarga la factura en PDF |

### Inventario

| Método | Ruta | Parámetros |
|---|---|---|
| GET | `/api/inventario/existencias` | — |
| GET | `/api/inventario/alertas` | — |
| GET | `/api/inventario/stock-minimo` | — |
| GET | `/api/inventario/movimientos` | `idProducto`, `tipo`, `fechaInicio`, `fechaFin` |

### Reportes

Todos aceptan `fechaInicio` y `fechaFin` (formato `yyyy-MM-dd`).

| Método | Ruta |
|---|---|
| GET | `/api/reportes/productos-mas-vendidos` |
| GET | `/api/reportes/productos-menor-existencia` |
| GET | `/api/reportes/productos-mas-movimientos` |
| GET | `/api/reportes/compras` |
| GET | `/api/reportes/top-proveedores` |
| GET | `/api/reportes/productos-mas-adquiridos` |
| GET | `/api/reportes/ventas` |
| GET | `/api/reportes/top-clientes` |
| GET | `/api/reportes/productos-mayores-ingresos` |
| GET | `/api/reportes/resumen-ventas` (además: `agrupacion` = `DIA`/`MES`/`ANIO`) |

### Bitácora

| Método | Ruta | Parámetros |
|---|---|---|
| GET | `/api/logs` | `modulo`, `fechaInicio`, `fechaFin` |

### Códigos de respuesta

| Código | Significado |
|---|---|
| 200 | Operación exitosa |
| 400 | Datos inválidos; el cuerpo contiene el mensaje de error |
| 401 | Credenciales incorrectas o token ausente/inválido |
| 403 | Sin permiso, o la ruta solicitada no existe |
| 404 | El recurso solicitado no existe |

---

## 18. Instalación y ejecución

### Requisitos

| Software | Versión mínima |
|---|---|
| JDK | 21 |
| Node.js | 20.19 / 22.12 / 24 |
| Angular CLI | 21 |
| PostgreSQL | 18 |

### Paso 1: crear la base de datos

En pgAdmin o psql, crear la base **vacía**:

```sql
CREATE DATABASE proy1;
```

No se debe crear ninguna tabla manualmente: Flyway lo hace al arrancar el
servidor.

### Paso 2: configurar el servidor

Editar `servidor/src/main/resources/application.properties`:

```properties
spring.application.name=servidor

spring.datasource.url=jdbc:postgresql://localhost:5432/proy1
spring.datasource.username=postgres
spring.datasource.password=SU_CONTRASEÑA

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

jwt.secret=CLAVE_SECRETA_LARGA
jwt.expiration-ms=86400000

inventario.stock-minimo=17
```

### Paso 3: ejecutar el servidor

```bash
cd servidor
./mvnw spring-boot:run          # Linux / macOS
.\mvnw.cmd spring-boot:run      # Windows PowerShell
```

En el primer arranque, Flyway crea las tablas y carga los datos iniciales. Debe
aparecer en consola:

```
Successfully applied 4 migrations to schema "public"
Tomcat started on port 8080 (http)
Started ServidorApplication
```

### Paso 4: ejecutar el cliente

En otra terminal:

```bash
cd cliente
npm install
ng serve
```

La aplicación queda disponible en `http://localhost:4200`.

### Usuarios de prueba

| Correo | Contraseña | Rol |
|---|---|---|
| admin@minierp.com | admin123 | Administración |
| compras@minierp.com | compras123 | Compras |
| inventario@minierp.com | inventario123 | Inventario |
| ventas@minierp.com | ventas123 | Ventas |

### Problemas frecuentes

| Síntoma | Causa y solución |
|---|---|
| `Port 8080 was already in use` | Hay otra instancia corriendo. `netstat -ano \| findstr :8080` y `taskkill /PID <pid> /F` |
| `Migration checksum mismatch` | Se modificó un archivo de migración ya aplicado. Crear una migración nueva en lugar de editar la anterior |
| `No migrations found` | El nombre del archivo no cumple el formato `V1__nombre.sql` (doble guion bajo), o no está en `db/migration` |
| Error 403 en todos los endpoints | El token expiró o el servidor se reinició. Cerrar sesión y volver a entrar |
| Error 403 en un endpoint nuevo | El servidor no se reinició tras agregar el controlador |
| La tabla no muestra datos aunque la petición devuelva 200 | Falta leer el signal con paréntesis en el HTML: `productos()` en vez de `productos` |

---

## 19. Decisiones de diseño

Resumen de las decisiones técnicas y su justificación.

| Decisión | Justificación |
|---|---|
| JdbcTemplate en lugar de JPA/Hibernate | El proyecto pide aplicar SQL explícitamente; los reportes con agregaciones se escriben mejor en SQL directo |
| JWT en lugar de sesiones de servidor | Un servidor web atiende usuarios concurrentes; una variable de sesión compartida se sobrescribiría entre ellos |
| BCrypt para las contraseñas | Algoritmo diseñado para contraseñas, con salt aleatorio, compatible con Spring Security |
| Baja lógica en lugar de DELETE | El proyecto exige conservar la información histórica; un borrado físico rompería las llaves foráneas |
| `@Transactional` en compras y ventas | Evita que el inventario quede inconsistente si una operación falla a medias |
| El stock no se edita manualmente | Toda variación debe tener un movimiento que la respalde y un origen identificable |
| IVA calculado en el servidor | El código del navegador puede manipularse; el del servidor no |
| El PDF se genera al vuelo, no se guarda | Ahorra almacenamiento y permite descargar cualquier factura en cualquier momento |
| Los logs se registran fuera de la transacción | Un fallo al guardar un log no debe revertir una venta |
| Un modelo genérico para los reportes de ranking | Las ocho consultas devuelven la misma estructura; evita crear ocho clases casi idénticas |
| Llaves primarias compuestas en las tablas de detalle | Ahorran espacio e impiden por diseño que un producto se repita en la misma operación |
| SSR desactivado en Angular | `localStorage` no existe en Node.js; SSR no aporta beneficios a un sistema interno |
| Constantes para módulos y acciones de la bitácora | Un error de escritura se detecta al compilar, no en producción |

### Limitaciones conocidas

- **Valuación de inventario (UEPS/PEPS):** no implementada. El historial de
  movimientos registra cantidades y precios unitarios, que es la información
  base necesaria para calcularla si se requiriera.
- **Control de acceso por rol en el backend:** actualmente las restricciones por
  área se aplican en el menú de Angular. El backend valida que el usuario esté
  autenticado y ya asigna la autoridad correspondiente al rol, por lo que
  activar la restricción por endpoint requiere únicamente agregar reglas en
  `SecurityConfig`.
- **Edición de compras y ventas:** no existe, por diseño, para preservar el
  historial.

---

*Fin del manual técnico.*
