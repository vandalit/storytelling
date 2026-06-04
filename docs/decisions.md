# Decisiones de Diseño y Arquitectura

> Log de decisiones técnicas y de producto. Una entrada por decisión.

---

## D001 — POC-first, no código primero

**Fecha:** 2026-06-03  
**Decisión:** Comenzar con validación de UX (flujos + specs de pantalla) antes de scaffolding Android.  
**Razón:** Evitar construir la arquitectura Compose sobre supuestos no validados.  
**Consecuencia:** El directorio `/src` queda vacío hasta que el POC dé luz verde.

---

## D002 — Cards como unidad atómica sin campos obligatorios

**Fecha:** 2026-06-03  
**Decisión:** Solo el campo `cuerpo` es obligatorio. Título, tipo y proyecto son opcionales en la creación.  
**Razón:** "Captura primero, clasifica después" — reducir fricción de entrada.  
**Consecuencia:** El Inbox puede tener cards sin título. La UI debe manejar placeholders.

---

## D003 — Tags como mecanismo primario de relación

**Fecha:** 2026-06-03  
**Decisión:** Las relaciones entre cards se derivan primero de tags compartidos, no de relaciones explícitas.  
**Razón:** Más natural para el usuario que crear relaciones manuales.  
**Consecuencia:** El sistema debe indexar tags y calcular relaciones bidireccionales automáticamente.

---

## D004 — Franquicias fuera del MVP

**Fecha:** 2026-06-03  
**Decisión:** La estructura de franquicias (cross-project) queda definida en el modelo de datos pero no se implementa en MVP.  
**Razón:** Añade complejidad de sincronización antes de validar el uso básico.  
**Consecuencia:** El modelo de datos debe ser extensible (proyecto_id en cards es nullable).
