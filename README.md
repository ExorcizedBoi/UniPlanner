#   UniPlanner

**UniPlanner** es una aplicación móvil diseñada para estudiantes de Ingeniería Civil Informática. Permite visualizar
el progreso curricular, simular escenarios académicos y gestionar evaluaciones con persistencia de datos local.

Desarrollada con **Kotlin Multiplatform** y **Material 3**.

-----------------------------------------------------------------------------------------------------------

##  Características Principales

### 1.  Malla curricular Interactiva
- **Visualización por Semestres:** Scroll horizontal fluido con diferenciación visual de ramos e hitos.
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
- **Método Pomodoro Clásico 25/10**
- **Método Pomodoro 50/10***
- **Active Recall**
- **Spaced Repetition**
- **Rubber Ducking**
- **Método Feynman**

### 5.  Ajustes y personalización
- **Modo Oscuro:** Soporte para tema Claro/Oscuro en toda la aplicación.
- **Gestión de Datos:** Opción de "Reiniciar Carrera" para borrar todo el historial académico y comenzar desde cero.

## 6. Horario interactivo
- **Visualización semanal:** Un calendario intuitivo de lunes a viernes.
- **Personalización total:** Crea bloques de clases definiendo hora de inicio y final.
- **Gestión:** Agrega y elimina clases fácilmente para ajustar tu semestre.

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

