# POC UX — Flujos Principales

> Objetivo: validar que los flujos core son intuitivos antes de escribir código Android.  
> Metodología: especificaciones de pantalla + recorrido narrativo del usuario.

---

## Flujo 1 — Captura Mínima (Critical Path)

El flujo más importante. Debe ser cero fricción.

```
[Cualquier pantalla]
    └── FAB (siempre visible, esquina inferior derecha)
            └── [Pantalla] Nueva Card
                    ├── Campo texto libre (autofocus)
                    ├── [Opcional] Chip: tipo de card
                    ├── [Opcional] Chip: proyecto
                    └── Botón Guardar
                            ├── Sin proyecto → Inbox (card blanca)
                            └── Con proyecto → Mazo del proyecto
```

**Validar:** ¿Cuántos taps desde cualquier pantalla hasta guardar una idea? Target: 2 taps.

---

## Flujo 2 — Crear Proyecto

```
[Proyectos Home]
    └── FAB / Botón "Nuevo proyecto"
            └── [Modal/Sheet] Nuevo Proyecto
                    ├── Nombre del proyecto (required)
                    ├── Tipo: one-shot | serie | videojuego | otro
                    └── Crear
                            └── [Pantalla] Vista Mazo (vacía)
                                    └── Onboarding tip: "Agrega tu primera card"
```

**Validar:** ¿El selector de tipo es necesario en la creación o se puede elegir después?

---

## Flujo 3 — Enriquecer una Card

```
[Vista Mazo o Inbox]
    └── Tap en Card
            └── [Pantalla] Detalle de Card
                    ├── Título + cuerpo (editables inline)
                    ├── Tipo actual (chip editable)
                    ├── Tags (input tipo PKM con #)
                    └── [Si tipo = personaje] Secciones on-demand:
                            ├── "Psicología +" → despliega campos motivación, miedo, deseo...
                            ├── "Biografía +" → backstory, hábitos
                            ├── "Físico +" → apariencia, imagen
                            └── "Arco +" → estado inicial/final, cambio emocional
```

**Validar:** ¿El patrón "sección colapsable on-demand" es claro o confunde?

---

## Flujo 4 — Vista Timeline

```
[Vista Proyecto]
    └── Tab / Selector de vista → Timeline
            └── [Pantalla] Timeline (scroll horizontal)
                    ├── Carril superior: beat markers (●catalizador, ●clímax...)
                    ├── Carril principal: escenas posicionadas (0.0 → 1.0)
                    ├── Carril inferior: escenas sin posicionar
                    └── Tap en escena → Sheet detalle escena
                            └── Slider: posición en arco (0% → 100%)
                                Etiqueta beat (libre)
```

**Validar:** ¿El concepto 0.0–1.0 es intuitivo? ¿Slider o drag-and-drop?

---

## Flujo 5 — Búsqueda PKM (Off-canvas)

```
[Cualquier pantalla]
    └── Swipe derecho / Hamburger icon
            └── [Panel] Off-canvas
                    ├── Búsqueda global (query libre o #tag)
                    ├── Tags globales (nube de tags)
                    ├── Templates
                    └── Configuración
```

**Validar:** ¿El off-canvas es discoverable? ¿Swipe compite con otros gestos?

---

## Métricas de éxito del POC

| Flujo | Métrica |
|-------|---------|
| Captura | ≤ 2 taps para guardar |
| Proyecto | ≤ 3 taps para crear |
| Enriquecimiento | Secciones on-demand: 0 confusión |
| Timeline | Posicionamiento: intuitivo sin tutorial |
| Off-canvas | 100% discoverable sin instrucción |
