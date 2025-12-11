#   UniPlanner 

**UniPlanner** es una aplicación móvil diseñada para estudiantes de Ingeniería Civil Informática. Permite visualizar 
el progreso curricular, simular escenarios académicos y gestionar evaluaciones con persistencia de datos local.

Desarrollada con **Kotlin Multiplatform** y **Material 3**.

-----------------------------------------------------------------------------------------------------------

##  Características Principales

### 1.  Malla curricular Interactiva
- **Visualización por Semestres:** Scroll horizontal fluido con diferenciación visual de ramos y hitos.
- **Sistema de Estados:** Colores dinámicos para ramos Aprobados (Verde), Disponibles (Azul), Bloqueados (Gris) y Reprobados (Rojo).
- **Modo Simulación:** Permite al estudiante marcar un ramo como "Reprobado" y el sistema calcula en tiempo real el "efecto dominó" hacia el futuro, marcando en rojo los ramos que se volverían imposibles de tomar.

### 2.  Gestor de evaluaciones
- **Agenda Personalizada:** Creacion, lectura y eliminacion de pruebas/entregas.
- **Alertas Visuales:** Los recordatorios cambian de color según la urgencia (rojo si faltan menos de 3 días y naranja con menos de 7 días).
- **Persistencia de Datos:** Las evaluaciones se guardan localmente y sobreviven al cierre de la aplicación.
- **Formato Local:** Horario adaptado al formato de fecha chileno (dd/mm/yyyy).

### 3.  Calculadora de notas
- Cálculo de promedios ponderados.
- Estimación de nota necesaria en examen para aprobar.

## 4. Metodos de estudio
- **Método Pomodoro Clásico:** Trabaja 25 min, descansa 5. El estándar de oro para evitar la fatiga mental.
- **Método Pomodoro 50/10:*** Trabaja 50 min, descansa 10. Ideal para tareas complejas o programación profunda, dando mayor inmersión con menos interrupciones.
- **Active Recall:** No solo leas. Cierra el libro e intenta recordar y explicar lo aprendido. Es la forma más potente de memorizar.
- **Spaced Repetition:** Repasa el contenido en un intervalo de dias (1 día, 3 días, 1 semana). Para asi combatir la curva del olvido.
- **Rubber Ducking:** Explícale tu materia a un patito de goma (o cualquier objeto inanimado). Al expresar, encuentras los errores
- **Método Feynman:** Intenta explicar lo que haz estudiado en términos tan simples que hasta un niño de 5 años lo entienda

### 5.  Ajustes y personalización
- **Modo Oscuro:** Soporte para tema Claro/Oscuro en toda la aplicación.
- **Gestión de Datos:** Opción de "Reiniciar Carrera" para borrar todo el historial académico y comenzar desde cero.

-----------------------------------------------------------------------------------------------------------

##  Tecnologías utilizadas

* **Lenguaje:** Kotlin
* **UI:** Material Design 3
* **Arquitectura:** Contiene una separación de Vistas, Modelos y Persistencia
* **Navegación:** Navigation Drawer (menú lateral estilo cajón)

-----------------------------------------------------------------------------------------------------------
## Donaciones y contribuciones
* **MercadoPago**
* **CuentaRut**

