# Narrative App — Storytelling Assistant

> App mobile-first (Android) para construcción narrativa mediante **cards** como unidad atómica.  
> Estado: **POC de UX** · Versión 0.1

---

## Estructura del Proyecto

```
/docs/                  Arquitectura, modelo de datos, decisiones
  sessions/             Bitácora de sesiones de trabajo
/poc/                   Proof of Concept de UX
  ux-flows/             Flujos de usuario documentados
  screens/              Especificaciones de pantallas
/src/                   Código fuente Android (post-POC)
/legacy/                Contenido previo al proyecto
```

## Docs clave

- [Arquitectura & Modelo de Datos](docs/architecture.md)
- [Sesión 001 — Kickoff & estructura](docs/sessions/session-001.md)
- [POC UX — Flujos principales](poc/ux-flows/flows-overview.md)

## Stack (Android MVP)

| Capa | Tecnología |
|------|-----------|
| UI | Jetpack Compose |
| Navegación | Navigation Compose |
| DB local | Room (SQLite) |
| Lenguaje | Kotlin |

## Filosofía central

- Captura primero, clasifica después
- On-demand: solo se muestran módulos activos
- Relaciones bidireccionales vía tags (PKM)
- Sin metodología impuesta — soporta Snowflake, STC, Hero's Journey, libre
