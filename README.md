# ObligatorioBDIII

Trabajo Obligatorio de **Bases de Datos III** (Licenciatura e Ingeniería en Informática, Universidad de la Empresa, curso 2026).

Es un **sistema de registro de notas de alumnos ("Bedelía")**: permite dar de alta alumnos, registrar las notas que obtienen en distintas actividades y consultar estadísticas (promedios, actividad más cursada, etc.). La letra completa está en [`BD3-Trabajo-Obligatorio-2026.pdf`](BD3-Trabajo-Obligatorio-2026.pdf).

El objetivo del trabajo no es el dominio en sí, sino la arquitectura: una aplicación **3-Tier** con manejo de concurrencia que puede persistir **indistintamente** en dos medios distintos, cambiando solo un archivo de configuración.

## Características principales

- **Arquitectura física 3-Tier**: cliente gráfico (Swing) ↔ servidor (Java RMI) ↔ persistencia.
- **Patrones de diseño**: Facade, Value Object, MVC, DAO y Abstract Factory.
- **Manejo de concurrencia mediante pool de conexiones**:
  - `PoolConexiones`: pool de conexiones JDBC para MySQL (tope y nivel de transaccionalidad configurables).
  - `PoolConexionesTxt`: monitor de lectura y escritura para la estructura de archivos de texto. Los requerimientos que modifican archivos son escritores (acceso exclusivo); los que no, son lectores (pueden entrar varios a la vez). Si hay un escritor esperando, los lectores nuevos esperan detrás para evitar inanición.
- **Dos mecanismos de persistencia intercambiables** (Abstract Factory):
  - **MySQL**: base de datos `Bedelia` con las tablas `Alumnos` y `Registros`.
  - **Archivos de texto plano**:
    - `alumnos.txt`: un alumno por línea (`cédula | nombre | apellido`), ordenado por cédula.
    - `registrosCCCCCCCC.txt` (uno por alumno, siendo `CCCCCCCC` la cédula): un registro por línea (`número | actividad | nota`), ordenado por número.
- La fachada **no menciona el medio de persistencia**: el pool y la fábrica concretos se cargan por reflexión a partir de `config/config.properties`.

## Funcionalidades

Definidas en [`IFachada`](src/logica/IFachada.java). En la interfaz gráfica hay una pestaña por entidad (Alumnos, Registros y Consultas), dividida en una sección por operación:

| Operación | Descripción |
|---|---|
| `nuevoAlumno` | Registra un alumno (cédula, nombre, apellido). |
| `registrarNota` | Agrega un registro (actividad y nota) a un alumno; el número lo asigna el sistema. |
| `listarAlumnos` | Lista todos los alumnos. |
| `obtenerRegistro` | Devuelve un registro de un alumno por número. |
| `listarRegistros` | Lista los registros de un alumno. |
| `promedioNotas` | Promedio de notas de un alumno. |
| `alumnoMayorPromedio` | Alumno con el mejor promedio. |
| `contarAlumnos` | Cantidad de alumnos con una nota dada en una actividad dada. |
| `actividadMasCursada` | Actividad que más veces cursó un alumno. |

## Estructura del proyecto

```
config/                 Configuración (config.properties) y drivers JDBC
src/
├── Main.java           Crea la base de datos Bedelia y carga los alumnos por defecto
├── grafica/            Capa de presentación (MVC)
│   ├── Aplicacion.java     Punto de entrada del cliente
│   ├── ventanas/           Vistas Swing (una por entidad: VentanaAlumnos, VentanaRegistros, VentanaConsultas)
│   └── controladores/      Controladores
├── logica/             Capa lógica
│   ├── IFachada / Fachada  Fachada remota (RMI)
│   ├── valueObjects/       VOs que viajan entre capas
│   └── excepciones/        Excepción Persistencia
├── fabricas/           Abstract Factory (FabricaAbstracta, FabricaMySQL, FabricaTxt)
├── persistencia/
│   └── poolConexiones/     IConexion, IPoolConexiones y sus implementaciones (MySQL y Txt)
└── servidor/
    └── RemotoServidor.java Publica la fachada por RMI
```

## Configuración

Todo se controla desde [`config/config.properties`](config/config.properties):

| Propiedad | Uso |
|---|---|
| `ipServidor`, `puertoServidor`, `bd`, `usr`, `pwd`, `driver`, `url` | Conexión a MySQL |
| `puertoServidorRMI` | Puerto del registro RMI |
| `tope` | Cantidad máxima de conexiones del pool MySQL |
| `nivelTransaccionalidad` | Nivel de aislamiento de las conexiones MySQL |
| `medioPersistencia` | `mysql` o `txt`: **medio de persistencia a usar** |
| `pool.mysql`, `pool.txt` | Clase concreta del pool para cada medio |

Para cambiar de medio de persistencia basta con modificar `medioPersistencia` y reiniciar el servidor.

## Cómo ejecutarlo

Es un proyecto de Eclipse (Java), con los drivers de MySQL en `config/`.

1. Tener MySQL en ejecución y ajustar usuario/contraseña en `config/config.properties`.
2. Ejecutar `Main` para crear la base de datos `Bedelia` y cargar los alumnos de ejemplo (se puede correr más de una vez).
3. Elegir el medio de persistencia en `medioPersistencia`.
4. Ejecutar `servidor.RemotoServidor` para publicar la fachada.
5. Ejecutar `grafica.Aplicacion` para abrir la interfaz gráfica.

> Los comandos se ejecutan desde la raíz del proyecto, ya que la configuración se lee de la ruta relativa `config/config.properties`.

## Estado actual

Trabajo en curso. Ya están el pool de conexiones MySQL, el pool de archivos de texto, los value objects, la fachada, el servidor RMI y la interfaz gráfica con sus controladores. Quedan por completar las clases de dominio y DAO (`Alumno`, `Registro`, `IDAOAlumnos`, `IDAORegistros`, `DAOAlumnos*`, `DAORegistros*`) y conectar las fábricas, hoy comentadas en `Fachada` y `FabricaAbstracta`.

## Entrega

- Entrega: lunes 16 de noviembre de 2026, 23:59 hs (proyecto Eclipse por e-mail al docente).
- Defensa oral con demo sobre ambos mecanismos de persistencia, obligatoria para todos los integrantes.
