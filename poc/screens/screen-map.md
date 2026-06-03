# Mapa de Pantallas — POC UX

> Inventario de todas las pantallas del MVP con su propósito y componentes clave.

---

## Pantallas Core

### S01 — Proyectos Home
**Ruta:** `app/home`  
**Propósito:** Landing de la app. Lista de proyectos recientes + acceso al Inbox.

| Elemento | Comportamiento |
|----------|---------------|
| Lista de proyectos | Cards con nombre, tipo, fecha última edición |
| Chip "Inbox" | Acceso directo a cards blancas sin proyecto |
| FAB | Dos opciones: Nueva Card (blanca) / Nuevo Proyecto |
| Hamburger | Abre off-canvas |

---

### S02 — Inbox
**Ruta:** `app/inbox`  
**Propósito:** Bandeja de entrada. Cards sin clasificar ni asignar.

| Elemento | Comportamiento |
|----------|---------------|
| Lista de cards blancas | Orden cronológico inverso |
| Chip filtros | Por tipo (si ya fueron tipificadas) |
| FAB | Nueva card blanca (autofocus en cuerpo) |
| Long press card | Menú: Asignar a proyecto / Archivar / Eliminar |

---

### S03 — Vista Mazo (Proyecto)
**Ruta:** `app/project/{id}/mazo`  
**Propósito:** Vista principal de un proyecto. Todas las cards en staggered masonry.

| Elemento | Comportamiento |
|----------|---------------|
| Header | Nombre del proyecto + tipo |
| Chips de filtro | Por CardType |
| Grid masonry | `LazyVerticalStaggeredGrid`, alturas variables |
| FAB | Nueva card para este proyecto |
| Selector de vista | Tabs: Mazo / Flipbox / Timeline |

---

### S04 — Vista Flipbox (Proyecto)
**Ruta:** `app/project/{id}/flipbox`  
**Propósito:** Una card a la vez, navegación por swipe.

| Elemento | Comportamiento |
|----------|---------------|
| Cara A | Título, tipo, mood, imagen (si tiene) |
| Cara B (flip) | Detalle completo |
| Swipe izquierda/derecha | Card anterior / siguiente |
| Filtro activo | Solo muestra las cards del tipo seleccionado |

---

### S05 — Detalle de Card
**Ruta:** `app/card/{id}`  
**Propósito:** Edición y visualización completa de una card.

| Elemento | Comportamiento |
|----------|---------------|
| Título | Editable inline |
| Cuerpo | TextArea sin límite |
| Chip tipo | Tap → selector de tipo |
| Tags | Input inline con `#` autocomplete |
| Secciones on-demand | Solo visibles si el tipo las tiene (personaje, escena) |
| Relaciones | Cards vinculadas (computed desde tags) |

---

### S06 — Detalle Personaje (extensión de S05)
Campos adicionales organizados en secciones colapsables:
- **Psicología** — motivación, conflicto interno, miedo, deseo, defecto
- **Biografía** — backstory, hábitos
- **Físico** — apariencia, imagen
- **Arco** — estado inicial/final, cambio emocional
- **Aparece en** — escenas relacionadas (computed)

---

### S07 — Detalle Escena (extensión de S05)
Campos adicionales en tres capas colapsables:
- **Dramaturgia** — objetivo, conflicto, stake, turning point, momento memorable
- **Beat** — posición en arco (slider 0–100%), etiqueta, setup/payoff
- **Producción** — acto, duración estimada, notas

---

### S08 — Vista Timeline (Proyecto)
**Ruta:** `app/project/{id}/timeline`  
**Propósito:** Mapa visual del arco narrativo con escenas posicionadas.

| Elemento | Comportamiento |
|----------|---------------|
| Scroll horizontal | Representa el arco 0% → 100% |
| Carril beats | Markers editables con etiqueta libre |
| Carril escenas | Posicionadas según `beat_posicion` |
| Carril "sin posicionar" | Escenas sin beat asignado |
| Tap escena | Sheet: slider de posición + etiqueta |
| Bandas de color | Agrupación visual por contenedor/acto |

---

### S09 — Nuevo Proyecto
**Tipo:** Bottom Sheet  
**Campos:** Nombre (required), Tipo (picker), División opcional

---

### S10 — Nueva Card
**Tipo:** Bottom Sheet / Screen full (depende del flujo)  
**Campos:** Cuerpo (autofocus, required), Tipo (chip opcional), Proyecto (chip opcional)

---

### S11 — Off-canvas (Panel PKM)
**Tipo:** Drawer lateral  
**Contenido:** Búsqueda global, tags globales, proyectos, templates, configuración

---

## Estado del inventario

| Pantalla | Spec | Flujo validado | Prototipo |
|----------|------|----------------|-----------|
| S01 Home | ✅ | pendiente | — |
| S02 Inbox | ✅ | pendiente | — |
| S03 Mazo | ✅ | pendiente | — |
| S04 Flipbox | ✅ | pendiente | — |
| S05 Card detalle | ✅ | pendiente | — |
| S06 Personaje | ✅ | pendiente | — |
| S07 Escena | ✅ | pendiente | — |
| S08 Timeline | ✅ | pendiente | — |
| S09 Nuevo proyecto | ✅ | pendiente | — |
| S10 Nueva card | ✅ | pendiente | — |
| S11 Off-canvas | ✅ | pendiente | — |
