# PetProject
## Sistema de Adopción Inteligente con Neo4j

**Pets** es una plataforma social  optimizada para la gestión eficiente de adopciones de mascotas, refugios y eventos. Este proyecto académico busca demostrar la aplicación práctica de estructuras de datos avanzadas y algoritmos complejos en casos reales, como el de la adopción de mascotas.


## 🎓 Contexto Académico

Este proyecto fue desarrollado como un trabajo académico enfocado en la materia **Diseño y Análisis de Algoritmos**. Su objetivo principal no es solo construir una aplicación web funcional, sino demostrar cómo la elección correcta de estructuras de datos (grafos) y algoritmos específicos puede resolver problemas reales de optimización, búsqueda y emparejamiento (matching).

## 💡 Caso de Uso

El problema central que resuelve **Pets** es la desconexión entre los animales que necesitan hogar, los refugios que los albergan y los posibles adoptantes. Hemos identificado que actualmente la adopción de mascotas es un proceso lento, donde las personas no encuentran fácilmente un animal que cumpla con sus preferencias, o incluso que los refugios no son reconocidos por la comunidad, lo que usualmente desemboca en la finalización de la actividad de muchos refugios. 

Por esto, creamos Pets. 

El sistema funciona como una **Red Social** donde:
*   Los usuarios pueden conectarse como "amigos", creando una red de confianza que amplifica la difusión de casos de adopción.
*   Se maximiza la visibilidad de los refugios.
*   Se utilizan algoritmos inteligentes para recomendar la mascota ideal a cada usuario, basándose en compatibilidad de preferencias.

## 🛠️ Tecnologías Utilizadas

*   **Java 21**: Lenguaje anfitrión.
*   **Spring Boot 3.5.7**: Framework para el desarrollo ágil de la API REST.
*   **Neo4j**: Base de datos orientada a grafos. Se eligió por su capacidad nativa para modelar y consultar relaciones complejas (amigos, dueños, refugios, interacciones).
*   **Spring Data Neo4j**: Abstracción para interactuar con la base de datos de grafos.
*   **Lombok**: Para reducir el código boilerplate.
*   **MapStruct**: Para el mapeo eficiente entre Entidades y DTOs.


## 🏗️ Arquitectura y Lógica del Sistema

### Por qué Neo4j (Grafos)

En un sistema de adopción y social, las **relaciones** son tan importantes como los datos en sí. Un modelo relacional tradicional (SQL) requeriría múltiples tablas de unión y *joins* costosos para responder preguntas como *"¿Qué amigo de mis amigos adoptó un perro de este refugio?"* o *"¿Cuál es el camino más corto para llevar un animal a su nuevo hogar?"*.

Con Neo4j, modelamos el dominio como un grafo:
*   **Nodos**: `User`, `Animal`, `Shelter`, `Location`, `Event`, `Specie`, `Trait`.
*   **Relaciones**: `:ADOPTS`, `:FOSTERS`, `:TAKES_CARE_OF` `:FRIENDS_WITH`, `:HOUSES`, `:HAS_TRAIT`, `:IS_OF_SPECIES`, `:CONNECTS`, `:LOCATED_IN`, `:TAKES_PLACE_IN`, `:PREFERS`, `:LOOKS_FOR`, `:WORKS_IN`

### Algoritmos Implementados

| Algoritmo | Propósito en "Pets" | Servicio |
| :--- | :--- | :--- |
| **Dijkstra** | **Logística**: Encuentra la ruta más corta (menor distancia) entre dos ubicaciones geográficas. Ideal para coordinar transportes de animales. | `DijkstraService` |
| **Greedy (Voraz)** | **Matching Inteligente**: Asigna mascotas a usuarios priorizando a los usuarios con menos opciones (según su compatibilidad con los animales disponibles) y luego buscando la mejor compatibilidad local. Esto puede ser utilizado para analizar la probabilidad de adopción o apadrinamiento de todos los animales en cualquier refugio. | `GreedyMatchingService` |
| **BFS (Breadth-First Search)** | **Red Social**: Calcula los grados de separación entre un usuario y un animal. Ayuda a mostrar qué tan conectado está un usuario con un animal. Se traduce a casos donde los usuarios quieren adoptar o apadrinar un animal, pero prefieren tener referencias de aquel animal. | `BFSService` |
| **DFS (Depth-First Search)** | **Exploración**: Busca caminos exploratorios entre ubicaciones sin necesariamente buscar el óptimo, útil para descubrir rutas alternativas. | `DFSService` |
| **Prim (MST)** | **Infraestructura**: Calcula el Árbol de Expansión Mínima para conectar un conjunto de ubicaciones con el menor costo posible (ej. para un posible recorrido de veterinarios, voluntarios o incluso transporte de alimento). | `PrimService` |
| **QuickSort** | **Organización**: Ordena eficientemente los refugios basándose en su capacidad, permitiendo redistribuir los animales entre los refugios de manera eficiente para optimizar el espacio total disponible. | `QuickSortService` |
| **Dynamic Programming** | **Optimización de Eventos**: Maximiza la inscripción a eventos de adopción dado un presupuesto o capacidad limitada, resolviendo un problema tipo "Knapsack". | `DynamicProgrammingService` |
| **Backtracking** | **Exploración Exhaustiva**: Genera todas las combinaciones posibles de adopción para un usuario según sus preferencias, explorando el espacio de soluciones completo para decisiones complejas. | `BacktrackingService` |
| **Branch & Bound** | **Rutas de Visita**: Encuentra la ruta óptima para que un usuario visite los animales que apadrina, optimizando tiempo y distancia. | `BranchAndBoundService` |

## 🚀 Funcionalidades Principales (API)

La API REST expone los algoritmos a través del controlador `AlgoritmosController`.

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/algoritmos/bfs` | Busca conexión social y grados de separación entre usuario y animal. |
| `GET` | `/api/algoritmos/dfs/{startId}/{endId}` | Ruta DFS entre dos ubicaciones. |
| `GET` | `/api/algoritmos/dijkstra/{startId}/{endId}` | Camino más corto (Dijkstra) entre dos ubicaciones. |
| `GET` | `/api/algoritmos/mst` | Obtiene el MST de las ubicaciones (Prim). |
| `GET` | `/api/algoritmos/greedy/match-pets` | Ejecuta el algoritmo de matching masivo (Pet-User). |
| `GET` | `/api/algoritmos/sort/shelters` | Lista de refugios ordenados por capacidad (QuickSort). |
| `GET` | `/api/algoritmos/dynamic/optimize` | Optimiza asistencia a eventos (Dynamic Prog). |
| `GET` | `/api/algoritmos/backtrack/adoption-options/{userId}` | Opciones de adopción exhaustivas. |
| `GET` | `/api/algoritmos/branch-bound/route/{startId}/{userEmail}` | Ruta óptima de visita de refugios. |

## 📦 Instalación y Ejecución

### Requisitos Previos
*   **Java 21** instalado.
*   **Maven** instalado.
*   **Neo4j Database**: Puedes usar Neo4j Desktop o una imagen de Docker.

### Configuración
Asegúrate de configurar las credenciales de tu base de datos en el archivo *application.properties*.
