# POC — Task Flows de Evaluación

Abre `index.html` en un browser. La barra superior tiene accesos directos a cada flujo.

## Flow 1 — Captura mínima
**Objetivo:** Medir fricción de captura. Target: ≤ 2 taps para guardar.
1. Toca **+** (FAB)
2. Selecciona "Nueva card"
3. Escribe la idea
4. (Opcional) asigna tipo y proyecto
5. Toca **Guardar card**
6. Aparece en Inbox o en el Mazo del proyecto

**Preguntas a validar:**
- ¿El bottom sheet aparece rápido y con autofocus?
- ¿El chip de tipo es claro o confuso?
- ¿La card aparece en el lugar correcto?

---

## Flow 2 — Crear proyecto
1. Toca **+** → "Nuevo proyecto"
2. Escribe el nombre
3. Elige tipo (one-shot, serie...)
4. Toca **Crear proyecto**
5. Aterriza en el Mazo vacío

**Preguntas:**
- ¿Necesitas elegir la división desde el inicio o puede ser después?
- ¿El Mazo vacío da suficientes pistas de qué hacer?

---

## Flow 3 — Enriquecer card (Personaje)
1. Abre proyecto **El Umbral**
2. Tap card **Ana Ruiz**
3. Cambia el tipo si no es `personaje`
4. Expande sección **Psicología** → rellena campos
5. Expande sección **Arco narrativo**
6. Agrega un tag: escribe `#ana` en el campo de tags + Enter
7. Toca **Guardar**

**Preguntas:**
- ¿El patrón de sección colapsable es intuitivo?
- ¿Cuántos campos es "demasiados"?
- ¿Las cards relacionadas (por tag) tienen sentido?

---

## Flow 4 — Timeline + posicionamiento de beat
1. Abre proyecto **El Umbral**
2. Toca tab **Timeline**
3. Observa las escenas posicionadas
4. Toca escena **"El Archivo de Kira"** en "Sin posicionar"  
   (está en Ciclo Rojo → abre ese proyecto en timeline)
5. Mueve el slider a 35%
6. Escribe etiqueta: "primer giro"
7. Toca **Posicionar**
8. La escena aparece en el timeline

**Preguntas:**
- ¿El scroll horizontal es natural?
- ¿El slider 0–100% es claro o preferiría labels de acto?
- ¿Las bandas de acto/episodio ayudan?

---

## Flow 5 — Búsqueda PKM (Off-canvas)
1. Toca **☰** (hamburger) en cualquier pantalla
2. Escribe `ana` en el buscador
3. Observa resultados en tiempo real
4. Toca un resultado → va a la card
5. Vuelve, toca tag **#el-umbral** en la nube
6. Ve todas las cards con ese tag

**Preguntas:**
- ¿El off-canvas es discoverable? ¿O necesita tutorial?
- ¿Swipe desde el borde sería mejor que el ícono?
- ¿La nube de tags tiene sentido visual?

---

## Observaciones del modelo de datos

Anota aquí lo que el prototipo revela sobre el modelo:

| Observación | Implicación para BD |
|-------------|---------------------|
| Tags como arrays en Card | Firestore: `arrayContains` query · Room: tabla de join |
| Relaciones computed desde tags | No necesita tabla de relaciones en MVP |
| beat_posicion 0.0–1.0 Float | Firestore: ok · Room: REAL column |
| Secciones on-demand (personaje/escena) | Firestore: nested object · Room: columnas nullable |
| Cards de Inbox (proyecto_id = null) | Firestore: query where proyecto_id == null |
| Búsqueda full-text | Firestore: limitada (prefix only) · Room: FTS5 · → evaluar Algolia |

## Decisión Firebase vs Room

**Usar Room (local-first) si:**
- MVP es offline-first sin colaboración
- No hay sync multi-dispositivo en v1
- Full-text search es crítico

**Usar Firestore si:**
- Hay colaboración o multi-dispositivo desde v1
- Se necesita sync en tiempo real
- Se acepta búsqueda limitada o Algolia como capa de search

**Recomendación provisional:** Room para MVP + Firestore sync post-MVP (arquitectura Repository pattern — el switch es transparente).
