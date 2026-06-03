# Narrative App — Arquitectura & Modelo de Datos
> Documento de referencia para Claude Code  
> Estado: MVP Android · Versión 0.1

---

## 1. Concepto

App mobile-first (Android) para construcción narrativa mediante **cards** como unidad atómica. El usuario compone su historia añadiendo ingredientes — personajes, escenas, locaciones, premisas, props, beats — de forma progresiva y sin fricción. No impone metodología: soporta múltiples paradigmas narrativos (Snowflake, beats tipo STC, Hero's Journey, libre).

**Filosofía central:**
- Captura primero, clasifica después
- On-demand: solo se muestran módulos activos, sin secciones vacías
- Relaciones bidireccionales vía sistema de tags (PKM)
- Flexible por nicho: novela, guion, videojuego, D&D, brand storytelling

---

## 2. Taxonomía de Proyecto

```
Franquicia (opcional)
└── Proyecto
    ├── tipo: one-shot | serie | temporadas | videojuego | otro
    ├── división: actos | capítulos | episodios | temporadas | niveles | ninguna
    └── Cards[]
        └── Contenedores[] (agrupadores opcionales dentro del proyecto)
```

### Tipos de proyecto
| Tipo | Descripción |
|------|-------------|
| `one-shot` | Obra única, sin continuación |
| `serie` | Múltiples episodios o capítulos |
| `temporadas` | Serie dividida en temporadas |
| `videojuego` | Incluye cards de mecánica/gameplay |
| `antología` | Historias independientes bajo un paraguas |
| `otro` | Libre (manual de marca, campaña, etc.) |

### Franquicias (cross-project)
- Agrupan múltiples proyectos
- Permiten cards compartidas entre proyectos (personajes, locaciones, lore)
- MVP: estructura definida, implementación post-MVP

---

## 3. Modelo de Datos

### 3.1 Card (unidad atómica)

```
Card {
  id              : UUID
  titulo          : String
  cuerpo          : String (texto libre)
  tipo            : CardType
  proyecto_id     : UUID? (null = Inbox)
  contenedor_id   : UUID? (null = sin agrupador)
  tags            : String[] (sistema PKM, vinculación bidireccional)
  relaciones      : Relacion[] (referencias a otras cards)
  estado          : 'borrador' | 'activa' | 'archivada'
  color_label     : String? (hex)
  imagen          : URL?
  fecha_creacion  : DateTime
  fecha_edicion   : DateTime
  orden           : Int?
}
```

### 3.2 Tipos de Card (CardType)

| Tipo | Descripción | Complejidad |
|------|-------------|-------------|
| `blanca` | Sin clasificar, vive en Inbox | Mínima |
| `premisa` | Tagline, logline, concepto central | Baja |
| `personaje` | Ver modelo extendido §3.4 | Media |
| `escena` | Ver modelo extendido §3.5 | Alta |
| `locacion` | Espacio físico o virtual | Media |
| `worldbuilding` | Lore, reglas del mundo, historia | Media |
| `conflicto` | Tensión central o subplot | Baja |
| `prop` | Objeto narrativo relevante | Baja |
| `beat` | Punto de tensión / inflexión en el arco | Media |
| `mecanica` | Gameplay, regla de juego (videojuegos) | Media |
| `paleta` | Color, tono visual, mood board | Baja |
| `nota` | Pensamiento libre sin tipo definido | Mínima |

### 3.3 Relaciones entre Cards

Las relaciones son **bidireccionales** — si una escena referencia a un personaje, el personaje muestra en qué escenas aparece automáticamente.

```
Relacion {
  card_origen_id  : UUID
  card_destino_id : UUID
  tipo_relacion   : String? (libre: "aparece en", "usa", "ocurre en"...)
}
```

El mecanismo primario de vinculación es el **tag** (`#juan`, `#bosque-oscuro`). Las relaciones explícitas son capa adicional opcional.

### 3.4 Card Personaje (campos extendidos, on-demand)

```
Personaje {
  // Base
  nombre          : String
  rol             : 'protagonista' | 'antagonista' | 'aliado' | 
                    'mentor' | 'foil' | 'secundario' | 'otro'
  descripcion     : String

  // Psicología (on-demand)
  motivacion      : String?
  conflicto_interno : String?
  miedo           : String?
  deseo           : String?
  defecto         : String?

  // Biografía (on-demand)
  backstory       : String?
  habitos         : String?

  // Físico (on-demand)
  apariencia      : String?
  imagen          : URL?

  // Arco (on-demand)
  estado_inicial  : String?
  estado_final    : String?
  cambio_emocional: String?

  // Relaciones con otras cards (auto desde tags)
  escenas_presentes : Card[] (computed)
  relaciones_personajes : Card[] (computed)
}
```

### 3.5 Card Escena (campos extendidos, on-demand)

La escena es la card más multidimensional. Tiene tres capas, todas opcionales salvo el título.

```
Escena {
  // IDENTIDAD (base)
  titulo          : String
  tipo_escena     : 'accion' | 'revelacion' | 'confrontacion' | 
                    'transicion' | 'worldbuilding' | 'otro'
  mood            : String? (tono emocional libre)

  // DRAMATURGIA (on-demand)
  objetivo        : String?
  conflicto       : String?
  stake           : String? (qué se juega)
  turning_point   : String?
  momento_memorable : String?
  descripcion     : String? (desarrollo completo / manuscrito)

  // BEAT — posición en el arco (on-demand)
  beat_posicion   : Float? (0.0 a 1.0, posición en timeline)
  beat_etiqueta   : String? (libre: "catalizador", "clímax", etc.)
  cambio_emocional_antes : String?
  cambio_emocional_despues : String?
  es_setup        : Boolean? (¿siembra algo?)
  es_payoff       : Boolean? (¿resuelve algo?)

  // RELACIONES (computed desde tags + relaciones explícitas)
  pov             : Card? (personaje)
  personajes_presentes : Card[]
  locacion        : Card?
  props_presentes : Card[]
  contenedor      : Contenedor? (capítulo/episodio/nivel)

  // PRODUCCIÓN (on-demand)
  numero_acto     : Int?
  duracion_estimada : String?
  notas_produccion : String?
}
```

### 3.6 Contenedor

Agrupador opcional de escenas dentro de un proyecto.

```
Contenedor {
  id          : UUID
  nombre      : String
  tipo        : 'acto' | 'capitulo' | 'episodio' | 'temporada' | 'nivel' | 'parte'
  proyecto_id : UUID
  orden       : Int
  descripcion : String?
}
```

---

## 4. Arquitectura de Navegación

### 4.1 Estructura general

```
App
├── Inbox
│   └── Cards blancas sin proyecto asignado
│
├── Proyectos
│   ├── Home — proyectos recientes + FAB
│   └── [Proyecto]
│       ├── Vista Mazo (staggered masonry)
│       ├── Vista Flipbox (card por card)
│       ├── Vista Dossier (bento grid)
│       └── Vista Timeline (masonry horizontal + beats)
│
├── Librería Global (post-MVP)
│   └── Personajes y locaciones cross-project
│
└── Menú Off-canvas (Obsidian-style)
    ├── Búsqueda global PKM
    ├── Tags globales
    ├── Templates
    └── Configuración
```

### 4.2 Menú Off-canvas

Inspirado en Obsidian: panel lateral deslizable con navegación PKM completa. Accesible desde cualquier pantalla con swipe derecho o ícono hamburguesa.

---

## 5. Flujo de Captura (mínima fricción)

```
FAB (siempre visible)
  └── Pantalla: Card Blanca
        ├── Campo texto libre (cuerpo)
        ├── [Opcional] Seleccionar tipo de card
        ├── [Opcional] Asignar a proyecto
        └── Guardar → va a Inbox si sin proyecto
                   → va al mazo si tiene proyecto
```

**Regla:** Si el usuario no elige tipo ni proyecto, la card queda como `blanca` en Inbox. Se puede enriquecer después. Cero campos obligatorios salvo el cuerpo.

---

## 6. Vistas del Proyecto

### 6.1 Vista Mazo — Staggered Masonry
- `LazyVerticalStaggeredGrid` de Jetpack Compose
- Todas las cards del proyecto, alturas variables según contenido
- Filtrable por tipo de card (tag chips en la parte superior)
- Drag & drop para reordenar (post-MVP)

### 6.2 Vista Flipbox
- Una card a la vez, navegación horizontal swipe
- Cara A: resumen visual (título, tipo, mood, imagen)
- Cara B: detalle completo con todos los campos
- Filtrable por tipo

### 6.3 Vista Dossier — Bento Grid
- `Grid` de Jetpack Compose con `rowSpan` / `columnSpan`
- Cards de distintos tamaños según relevancia o tipo
- Premisa: card grande (2×2) · Personajes: cards medianas (1×2) · Props: cards pequeñas (1×1)
- Scroll vertical
- Vista infográfica — textos, labels, ilustraciones; no interactiva en edición
- Ideal para compartir o imprimir como dossier del proyecto

### 6.4 Vista Timeline — Masonry Horizontal + Beats
- Scroll horizontal
- Eje X = progresión cronológica (0% → 100% del arco)
- Cards de escena posicionadas según `beat_posicion`
- Carril superior: marcadores de beat con etiqueta libre
- Agrupación visual por acto/contenedor (bandas de color de fondo)
- Cards sin beat_posicion flotan en un carril "sin posicionar" en la parte inferior
- Metodología libre: el usuario define sus propias etiquetas de beat

```
Timeline (scroll →)
─────────────────────────────────────────────────────────
  [ACTO I]          [ACTO II]                  [ACTO III]
  ●catalizador      ●punto medio   ●todo perdido  ●clímax
  ─────────────────────────────────────────────────────
  [escena 1] [escena 2]    [escena 5] [escena 7]  [escena 9]
       [escena 3] [escena 4]    [escena 6]    [escena 8]
─────────────────────────────────────────────────────────
  Sin posicionar:
  [escena sin beat] [escena sin beat]
```

---

## 7. Sistema de Tags (PKM)

- Tags libres tipo hashtag: `#juan`, `#acto-2`, `#conflicto-principal`
- Al tagear una card, el sistema actualiza automáticamente las relaciones bidireccionales
- Búsqueda global por tag desde el off-canvas
- Un tag puede actuar como filtro en cualquier vista

---

## 8. Templates (on-demand, comunidad)

Plantillas que pre-cargan un conjunto de cards vacías con estructura definida. Ejemplos:

| Template | Cards incluidas |
|----------|----------------|
| Novela 3 actos | Premisa + 3 actos + 12 escenas vacías |
| Guion STC | Premisa + 15 beats etiquetados |
| Videojuego RPG | Premisa + mundo + 4 personajes + mecánica core |
| D&D One-Shot | Premisa + mapa + 3 NPCs + 5 encuentros |
| Episodio de serie | Escenas + contenedor episodio + personajes recurrentes |

El usuario puede guardar su propio proyecto como template (post-MVP).

---

## 9. Stack Técnico (Android MVP)

| Capa | Tecnología |
|------|-----------|
| UI | Jetpack Compose |
| Grid/Masonry | `LazyVerticalStaggeredGrid` + `Grid` (Compose) |
| Navegación | Navigation Compose |
| Base de datos local | Room (SQLite) |
| Sync (post-MVP) | Firebase / Supabase |
| Lenguaje | Kotlin |

---

## 10. Roadmap MVP vs Post-MVP

### MVP (Van como usuario primario)
- [x] Card blanca + flujo de captura mínima
- [x] Tipos de card: blanca, personaje, escena, locación, premisa, nota
- [x] Inbox
- [x] Proyectos: one-shot, serie, videojuego
- [x] Vista Mazo (staggered)
- [x] Vista Flipbox
- [x] Sistema de tags bidireccional
- [x] Menú off-canvas + búsqueda
- [x] Card Escena con capas: identidad + dramaturgia + beat
- [x] Vista Timeline básica (posición manual)

### Post-MVP
- [ ] Vista Dossier Bento completa
- [ ] Franquicias y cross-project
- [ ] Templates de comunidad
- [ ] Modo público / social (compartir mazos o secciones)
- [ ] Cards para D&D: ficha de personaje, encuentros
- [ ] Drag & drop en vistas
- [ ] Sync multi-dispositivo
- [ ] Export PDF / dossier
- [ ] Departamento de arte (props, paleta, locaciones como producción)
- [ ] Brand storytelling template

---

## 11. Referencias estudiadas

| App | Metodología | Aporte clave |
|-----|------------|--------------|
| **Fabula** | Snowflake Method | Expansión fractal idea→novela; worldbuilding modular |
| **Story Planner** | Libre / estructura propia | Campos de escena: POV, personajes, locación, acto |
| **Save the Cat!** | 15 beats de Blake Snyder | Cambio emocional por escena; setup/payoff; board visual |
| **Character Story Planner** | Libre | Conflicto, stake, turning point, estrategia por escena |
| **Obsidian** | PKM / Zettelkasten | Tags bidireccionales, off-canvas, búsqueda global |
| **Notion** | Bloques modulares | On-demand, sin secciones vacías, flexible por proyecto |
| **Miro** | Canvas + templates | Comunidad de templates, lienzo espacial |

---

*Documento generado para handoff a Claude Code*  
*Próximo paso: scaffolding del proyecto Android en Jetpack Compose*
