# Sesión 001 — Kickoff & Estructura del Proyecto

**Fecha:** 2026-06-03  
**Objetivo:** Arrancar el repositorio, definir estructura, iniciar POC de UX  
**Estado:** ✅ Completada

---

## Contexto

Primera sesión de trabajo. Se recibió el documento de arquitectura `narrativeapparchitecture1.md` con el modelo de datos, navegación y stack técnico completos. El objetivo inmediato no es construir la app Android, sino **validar los flujos de UX** antes de invertir en código.

## Decisiones tomadas

1. **Estructura de repo creada** — `/docs`, `/poc`, `/src`, `/legacy`
2. **Contenido previo** movido a `/legacy` para no perder historial
3. **Documento de arquitectura** copiado a `/docs/architecture.md` como fuente de verdad
4. **POC-first** — Antes de scaffolding Android, se validan flujos con especificaciones de pantalla

## Artefactos generados esta sesión

| Archivo | Descripción |
|---------|-------------|
| `docs/architecture.md` | Arquitectura completa v0.1 |
| `poc/ux-flows/flows-overview.md` | Flujos principales del MVP |
| `poc/screens/` | Specs por pantalla (ver archivos) |
| `docs/sessions/session-001.md` | Este documento |

## Flujos en scope para el POC

1. **Flujo de captura mínima** — FAB → Card Blanca → Inbox
2. **Flujo de creación de proyecto** — Nuevo proyecto → tipo → vista mazo
3. **Flujo de enriquecimiento de card** — Blanca → asignar tipo → campos on-demand
4. **Flujo de vista timeline** — Escenas + beats + posicionamiento manual
5. **Navegación off-canvas** — Búsqueda global + tags

## Próxima sesión

- Revisar specs de pantallas del POC
- Decidir herramienta de prototipado (Figma, HTML estático, o Compose Preview)
- Validar modelo de datos contra flujos reales

---

*Sesión conducida con Claude Code (claude-sonnet-4-6)*
