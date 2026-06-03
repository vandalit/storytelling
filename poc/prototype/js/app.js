// ── State ──────────────────────────────────────────────────────
const state = {
  screen: 'home',       // home | inbox | project | card
  projectId: null,
  cardId: null,
  view: 'mazo',         // mazo | flipbox | timeline
  filters: [],          // active type filters
  flipIndex: 0,
  history: [],
  sheet: null,          // null | 'new-card' | 'new-project' | 'beat'
  sheetData: {},
  offCanvas: false,
  searchQuery: '',
  editingCardId: null,
};

// ── DOM refs ────────────────────────────────────────────────────
const $screen       = () => document.getElementById('screen');
const $appbar       = () => document.getElementById('appbar');
const $fab          = () => document.getElementById('fab');
const $sheet        = () => document.getElementById('sheet');
const $sheetOverlay = () => document.getElementById('sheet-overlay');
const $offcanvas    = () => document.getElementById('offcanvas');
const $canvasOverlay= () => document.getElementById('canvas-overlay');

// ── Navigation ──────────────────────────────────────────────────
function navigate(screen, params = {}) {
  state.history.push({ screen: state.screen, projectId: state.projectId, cardId: state.cardId, view: state.view });
  state.screen = screen;
  state.projectId = params.projectId || state.projectId;
  state.cardId = params.cardId || null;
  state.view = params.view || 'mazo';
  state.filters = [];
  state.flipIndex = 0;
  render();
}

function back() {
  if (state.history.length === 0) return;
  const prev = state.history.pop();
  state.screen = prev.screen;
  state.projectId = prev.projectId;
  state.cardId = prev.cardId;
  state.view = prev.view;
  render();
}

// ── Render ──────────────────────────────────────────────────────
function render() {
  switch (state.screen) {
    case 'home':    renderHome(); break;
    case 'inbox':   renderInbox(); break;
    case 'project': renderProject(); break;
    case 'card':    renderCard(); break;
  }
  updateFab();
}

function renderHome() {
  const projects = Data.projects();
  const inbox = Data.inboxCards();

  setAppbar(`<span class="title">Narrative</span><button class="menu-btn" onclick="openCanvas()">☰</button>`);

  $screen().innerHTML = `
    <div class="home-section" style="padding-top:20px">
      <div class="section-title">Proyectos</div>
      ${projects.map(p => `
        <div class="project-card" onclick="navigate('project',{projectId:'${p.id}'})">
          <div class="project-dot" style="background:${p.color}22">
            <span style="color:${p.color}">◆</span>
          </div>
          <div class="project-info">
            <div class="project-name">${p.nombre}</div>
            <div class="project-meta">${p.tipo} · ${Data.projectCards(p.id).length} cards · ${p.fecha_edicion}</div>
          </div>
          <span class="project-arrow">›</span>
        </div>
      `).join('')}
    </div>

    <div class="home-section" style="margin-top:8px;padding-bottom:100px">
      <div class="section-title">
        Inbox
        <span class="inbox-count">${inbox.length}</span>
      </div>
      ${inbox.slice(0,3).map(c => `
        <div class="inbox-card" onclick="navigate('card',{cardId:'${c.id}'})">
          <div class="row gap-8" style="margin-bottom:4px">
            <span style="font-size:10px;color:${CARD_TYPES[c.tipo]?.color||'#666'}">${CARD_TYPES[c.tipo]?.icon} ${CARD_TYPES[c.tipo]?.label}</span>
            <span class="text-xs text-muted">${c.fecha_creacion}</span>
          </div>
          <div style="font-size:13px;color:#ccc;line-height:1.5;overflow:hidden;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical">${c.cuerpo}</div>
          ${c.tags.length ? `<div class="card-tags">${c.tags.map(t=>`<span class="tag-chip">${t}</span>`).join('')}</div>` : ''}
        </div>
      `).join('')}
      ${inbox.length > 3 ? `<div onclick="navigate('inbox')" style="text-align:center;padding:12px;font-size:13px;color:var(--primary);cursor:pointer">Ver todos (${inbox.length}) ›</div>` : ''}
      ${inbox.length === 0 ? `<div class="inbox-card" style="opacity:0.5"><div style="font-size:13px;color:var(--text-muted)">Sin ideas capturadas. Usa el + para empezar.</div></div>` : ''}
    </div>
  `;
}

function renderInbox() {
  const cards = Data.inboxCards();
  setAppbar(`<button class="back-btn" onclick="back()">←</button><span class="title">Inbox</span>`);

  $screen().innerHTML = `
    <div class="pb-fab">
      ${cards.length === 0 ? emptyState('📥','Inbox vacío','Toca + para capturar una idea sin clasificar.') : ''}
      ${cards.map(c => cardListItem(c)).join('')}
    </div>
  `;
}

function renderProject() {
  const project = Data.project(state.projectId);
  if (!project) { navigate('home'); return; }
  setAppbar(`
    <button class="back-btn" onclick="back()">←</button>
    <span class="title">${project.nombre}</span>
    <div class="view-tabs">
      <span class="view-tab ${state.view==='mazo'?'active':''}" onclick="switchView('mazo')">Mazo</span>
      <span class="view-tab ${state.view==='flipbox'?'active':''}" onclick="switchView('flipbox')">Flip</span>
      <span class="view-tab ${state.view==='timeline'?'active':''}" onclick="switchView('timeline')">Timeline</span>
    </div>
  `);
  switch (state.view) {
    case 'mazo':     renderMazo(project); break;
    case 'flipbox':  renderFlipbox(project); break;
    case 'timeline': renderTimeline(project); break;
  }
}

function switchView(v) {
  state.view = v;
  state.filters = [];
  state.flipIndex = 0;
  renderProject();
}

function renderMazo(project) {
  const allCards = Data.projectCards(project.id);
  const types = [...new Set(allCards.map(c => c.tipo))];
  const filtered = state.filters.length > 0
    ? allCards.filter(c => state.filters.includes(c.tipo))
    : allCards;

  $screen().innerHTML = `
    <div class="filter-bar">
      <span class="filter-chip ${state.filters.length===0?'active':''}" onclick="setFilter(null)">Todas</span>
      ${types.map(t => `
        <span class="filter-chip ${state.filters.includes(t)?'active':''}"
              style="${state.filters.includes(t)?`border-color:${CARD_TYPES[t]?.color};color:${CARD_TYPES[t]?.color};background:${CARD_TYPES[t]?.color}18`:''}"
              onclick="toggleFilter('${t}')">
          ${CARD_TYPES[t]?.icon} ${CARD_TYPES[t]?.label}
        </span>
      `).join('')}
    </div>
    <div class="masonry pb-fab">
      ${filtered.map(c => mazoCard(c)).join('')}
    </div>
    ${filtered.length === 0 ? emptyState('🗂️','Sin cards','No hay cards de este tipo todavía.') : ''}
  `;
}

function mazoCard(c) {
  const t = CARD_TYPES[c.tipo] || CARD_TYPES.blanca;
  return `
    <div class="card" onclick="navigate('card',{cardId:'${c.id}'})">
      <div class="row gap-8">
        <span class="card-type-pip" style="background:${t.color}"></span>
        <span class="card-type-label" style="color:${t.color}">${t.label}</span>
        ${c.estado === 'borrador' ? `<span class="estado-badge estado-borrador" style="margin-left:auto">borrador</span>` : ''}
      </div>
      ${c.titulo ? `<div class="card-title">${c.titulo}</div>` : ''}
      <div class="card-body">${c.cuerpo}</div>
      ${c.escena?.beat_etiqueta ? `<div style="margin-top:6px;font-size:10px;color:var(--accent)">● ${c.escena.beat_etiqueta}</div>` : ''}
      ${c.tags.length ? `<div class="card-tags">${c.tags.slice(0,3).map(t=>`<span class="tag-chip">${t}</span>`).join('')}</div>` : ''}
    </div>
  `;
}

function renderFlipbox(project) {
  const allCards = Data.projectCards(project.id);
  const cards = state.filters.length > 0 ? allCards.filter(c => state.filters.includes(c.tipo)) : allCards;
  if (cards.length === 0) { $screen().innerHTML = emptyState('🃏','Sin cards','Agrega cards a este proyecto primero.'); return; }
  const idx = Math.min(state.flipIndex, cards.length - 1);
  const c = cards[idx];
  const t = CARD_TYPES[c.tipo] || CARD_TYPES.blanca;

  $screen().innerHTML = `
    <div style="padding:12px">
      <div class="filter-bar" style="padding:0 0 10px">
        <span class="filter-chip ${state.filters.length===0?'active':''}" onclick="setFilter(null);switchView('flipbox')">Todas</span>
        ${[...new Set(allCards.map(c=>c.tipo))].map(tp => `
          <span class="filter-chip ${state.filters.includes(tp)?'active':''}" onclick="toggleFilter('${tp}');switchView('flipbox')">${CARD_TYPES[tp]?.label}</span>
        `).join('')}
      </div>
    </div>
    <div class="flipbox-wrap">
      <div class="flip-card-outer" id="flip-card" onclick="this.classList.toggle('flipped')">
        <div class="flip-card-inner">
          <div class="flip-face">
            <div>
              <div style="color:${t.color};font-size:28px;margin-bottom:8px">${t.icon}</div>
              <div style="font-size:11px;color:${t.color};text-transform:uppercase;letter-spacing:1px;font-weight:700">${t.label}</div>
              <div style="font-size:22px;font-weight:800;color:var(--text);margin-top:8px">${c.titulo || '—'}</div>
              ${c.escena?.mood ? `<div style="margin-top:8px;font-size:13px;color:var(--text-muted);font-style:italic">${c.escena.mood}</div>` : ''}
              ${c.escena?.beat_etiqueta ? `<div style="margin-top:8px;font-size:12px;color:var(--accent)">● ${c.escena.beat_etiqueta} · ${Math.round((c.escena.beat_posicion||0)*100)}%</div>` : ''}
            </div>
            <div>
              ${c.tags.slice(0,4).map(t=>`<span class="tag-chip" style="margin-right:4px">${t}</span>`).join('')}
            </div>
          </div>
          <div class="flip-back">
            <div style="font-size:13px;color:var(--text-muted);margin-bottom:12px">Cara B — Detalle</div>
            <div style="font-size:14px;color:var(--text);line-height:1.6;margin-bottom:16px">${c.cuerpo}</div>
            ${c.escena ? flipSceneDetail(c.escena) : ''}
            ${c.personaje ? flipPersonajeDetail(c.personaje) : ''}
            <button class="btn btn-primary" style="margin-top:16px" onclick="event.stopPropagation();navigate('card',{cardId:'${c.id}'})">Editar card ›</button>
          </div>
        </div>
      </div>
      <div class="flip-hint">Toca para voltear</div>
      <div class="flipbox-nav">
        <button class="flip-nav-btn" onclick="flipNav(-1)">‹</button>
        <span class="flip-counter">${idx+1} / ${cards.length}</span>
        <button class="flip-nav-btn" onclick="flipNav(1)">›</button>
      </div>
    </div>
  `;
}

function flipSceneDetail(e) {
  return `
    ${e.objetivo ? `<div style="margin-bottom:8px"><span style="font-size:10px;color:var(--text-dim);text-transform:uppercase;font-weight:700">Objetivo</span><div style="font-size:13px;color:var(--text);margin-top:2px">${e.objetivo}</div></div>` : ''}
    ${e.conflicto ? `<div style="margin-bottom:8px"><span style="font-size:10px;color:var(--text-dim);text-transform:uppercase;font-weight:700">Conflicto</span><div style="font-size:13px;color:var(--text);margin-top:2px">${e.conflicto}</div></div>` : ''}
    ${e.turning_point ? `<div style="margin-bottom:8px"><span style="font-size:10px;color:var(--text-dim);text-transform:uppercase;font-weight:700">Turning Point</span><div style="font-size:13px;color:var(--text);margin-top:2px">${e.turning_point}</div></div>` : ''}
  `;
}

function flipPersonajeDetail(p) {
  return `
    ${p.rol ? `<div style="margin-bottom:8px"><span style="font-size:10px;color:var(--text-dim);text-transform:uppercase;font-weight:700">Rol</span><div style="font-size:13px;color:var(--text);margin-top:2px">${p.rol}</div></div>` : ''}
    ${p.motivacion ? `<div style="margin-bottom:8px"><span style="font-size:10px;color:var(--text-dim);text-transform:uppercase;font-weight:700">Motivación</span><div style="font-size:13px;color:var(--text);margin-top:2px">${p.motivacion}</div></div>` : ''}
    ${p.conflicto_interno ? `<div style="margin-bottom:8px"><span style="font-size:10px;color:var(--text-dim);text-transform:uppercase;font-weight:700">Conflicto interno</span><div style="font-size:13px;color:var(--text);margin-top:2px">${p.conflicto_interno}</div></div>` : ''}
  `;
}

function flipNav(dir) {
  const project = Data.project(state.projectId);
  const cards = state.filters.length > 0
    ? Data.projectCards(project.id).filter(c => state.filters.includes(c.tipo))
    : Data.projectCards(project.id);
  state.flipIndex = Math.max(0, Math.min(cards.length - 1, state.flipIndex + dir));
  renderProject();
}

function renderTimeline(project) {
  const scenes = Data.scenesForTimeline(project.id);
  const containers = Data.containers(project.id);
  const positioned = scenes.filter(s => s.escena?.beat_posicion !== null && s.escena?.beat_posicion !== undefined);
  const unpositioned = scenes.filter(s => !s.escena?.beat_posicion && s.escena?.beat_posicion !== 0);

  const positioned_sorted = [...positioned].sort((a, b) => a.escena.beat_posicion - b.escena.beat_posicion);

  $screen().innerHTML = `
    <div id="timeline-container" style="height:calc(100% - 0px)">
      <div class="timeline-track" style="min-width:${Math.max(900, positioned_sorted.length * 160 + 100)}px">

        <!-- Act bands -->
        <div class="act-bands">
          ${containers.map(c => {
            const width = c.tipo === 'acto' ? (100 / containers.length) : 100;
            return `<div class="act-band" style="flex:${containers.length>0?1:0};width:${width}%;background:${c.color}44">
              <span>${c.nombre}</span>
            </div>`;
          }).join('')}
        </div>

        <!-- Progress line -->
        <div class="timeline-progress"></div>

        <!-- Beat markers -->
        <div class="beat-markers" style="position:relative;width:100%;min-height:50px">
          ${positioned_sorted.filter(s=>s.escena.beat_etiqueta).map(s => `
            <div class="beat-marker" style="left:${s.escena.beat_posicion*100}%">
              <div class="beat-dot"></div>
              <div class="beat-label">${s.escena.beat_etiqueta}</div>
            </div>
          `).join('')}
        </div>

        <!-- Scene rail -->
        <div class="scene-rail" style="position:relative;height:200px;width:100%">
          ${positioned_sorted.map((s, i) => {
            const pct = s.escena.beat_posicion * 100;
            const t = CARD_TYPES[s.tipo] || CARD_TYPES.escena;
            return `
              <div class="scene-card-tl ${i%2===1?'odd':''}"
                   style="left:${pct}%;--type-color:${t.color}"
                   onclick="openBeatSheet('${s.id}')">
                <div class="sc-title">${s.titulo}</div>
                ${s.escena.mood ? `<div class="sc-mood">${s.escena.mood}</div>` : ''}
                <div class="sc-beat">${Math.round(pct)}% ${s.escena.beat_etiqueta ? '· '+s.escena.beat_etiqueta : ''}</div>
              </div>
            `;
          }).join('')}
        </div>

        <!-- Unpositioned -->
        ${unpositioned.length > 0 ? `
          <div class="unpositioned-rail">
            <div class="unpositioned-label">Sin posicionar (${unpositioned.length})</div>
            <div class="unpositioned-cards">
              ${unpositioned.map(s => `
                <div class="scene-card-tl" style="position:relative;transform:none;left:auto;top:auto;--type-color:${CARD_TYPES.escena.color}" onclick="openBeatSheet('${s.id}')">
                  <div class="sc-title">${s.titulo}</div>
                  <div class="sc-beat" style="color:var(--text-dim)">sin posición · toca para asignar</div>
                </div>
              `).join('')}
            </div>
          </div>
        ` : ''}

      </div>
    </div>
  `;
}

function renderCard() {
  const card = Data.card(state.cardId);
  if (!card) { back(); return; }
  const project = card.proyecto_id ? Data.project(card.proyecto_id) : null;
  const t = CARD_TYPES[card.tipo] || CARD_TYPES.blanca;
  const related = Data.relatedCards(card.id);

  setAppbar(`
    <button class="back-btn" onclick="back()">←</button>
    <span class="title">${card.titulo || 'Card'}</span>
    <button class="action-btn" onclick="saveCard('${card.id}')">Guardar</button>
  `);

  $screen().innerHTML = `
    <!-- Identidad -->
    <div class="detail-section">
      <div class="detail-label">Título</div>
      <textarea class="field-editable detail-field title-field" id="edit-titulo" rows="1"
        style="font-size:20px;font-weight:700" oninput="autoResize(this)" placeholder="Sin título">${card.titulo || ''}</textarea>
    </div>

    <div class="detail-section">
      <div class="detail-label">Contenido</div>
      <textarea class="field-editable" id="edit-cuerpo" rows="4" oninput="autoResize(this)">${card.cuerpo}</textarea>
    </div>

    <!-- Tipo -->
    <div class="detail-section">
      <div class="detail-label">Tipo de card</div>
      <div class="type-selector" id="type-selector">
        ${Object.entries(CARD_TYPES).map(([key, val]) => `
          <div class="type-option ${card.tipo===key?'selected':''}"
               style="${card.tipo===key?`--type-color:${val.color};border-color:${val.color};background:${val.color}18`:''}"
               onclick="selectType('${key}')">
            <span style="color:${val.color}">${val.icon}</span>
            <span style="font-size:12px">${val.label}</span>
          </div>
        `).join('')}
      </div>
    </div>

    <!-- Tags -->
    <div class="detail-section">
      <div class="detail-label">Tags</div>
      <div class="tags-input-area" id="tags-area">
        ${card.tags.map(tag => `
          <span class="tag-pill">${tag}<span class="remove" onclick="removeTag('${card.id}','${tag}')">×</span></span>
        `).join('')}
        <input class="tag-input-field" placeholder="+ #tag" id="tag-input"
               onkeydown="handleTagInput(event,'${card.id}')">
      </div>
    </div>

    <!-- Estado -->
    <div class="detail-section">
      <div class="detail-label">Estado</div>
      <div class="chip-row">
        ${['activa','borrador','archivada'].map(e => `
          <span class="chip ${card.estado===e?'selected':''}" onclick="setEstado('${card.id}','${e}')">${e}</span>
        `).join('')}
      </div>
    </div>

    <!-- Proyecto -->
    <div class="detail-section">
      <div class="detail-label">Proyecto</div>
      <select class="select-field" id="edit-proyecto" onchange="setProject('${card.id}',this.value)">
        <option value="">Inbox (sin proyecto)</option>
        ${Data.projects().map(p => `<option value="${p.id}" ${card.proyecto_id===p.id?'selected':''}>${p.nombre}</option>`).join('')}
      </select>
    </div>

    <!-- Expanded: Personaje -->
    ${card.tipo === 'personaje' ? personajeSection(card) : ''}

    <!-- Expanded: Escena -->
    ${card.tipo === 'escena' ? escenaSection(card) : ''}

    <!-- Relacionadas -->
    ${related.length > 0 ? `
      <div style="padding:12px 16px 4px">
        <div class="detail-label">Cards relacionadas por tag</div>
      </div>
      <div class="related-list">
        ${related.slice(0,5).map(r => {
          const rt = CARD_TYPES[r.tipo] || CARD_TYPES.blanca;
          return `
            <div class="related-card" onclick="navigate('card',{cardId:'${r.id}'})">
              <span style="color:${rt.color};font-size:18px">${rt.icon}</span>
              <div class="related-info">
                <div class="related-title">${r.titulo || r.cuerpo.slice(0,40)+'…'}</div>
                <div class="related-meta">${rt.label} · ${r.tags.filter(t=>card.tags.includes(t)).join(', ')}</div>
              </div>
              <span style="color:var(--text-dim)">›</span>
            </div>
          `;
        }).join('')}
      </div>
    ` : ''}

    <!-- Meta -->
    <div style="padding:16px;color:var(--text-dim);font-size:11px;border-top:1px solid var(--border);margin-top:8px;padding-bottom:80px">
      Creada ${card.fecha_creacion} · Editada ${card.fecha_edicion}
      ${project ? ` · <span style="color:var(--primary)">${project.nombre}</span>` : ' · Inbox'}
    </div>
  `;
}

function personajeSection(card) {
  const p = card.personaje || {};
  return `
    <div class="expand-section">
      <div class="expand-header" onclick="toggleSection(this)">
        <span class="expand-label">◉ Psicología</span>
        <span class="expand-icon">▾</span>
      </div>
      <div class="expand-body">
        ${fieldGroup('Rol', 'p-rol', p.rol || '')}
        ${fieldGroup('Motivación', 'p-motivacion', p.motivacion || '')}
        ${fieldGroup('Conflicto interno', 'p-conflicto', p.conflicto_interno || '')}
        ${fieldGroup('Miedo', 'p-miedo', p.miedo || '')}
        ${fieldGroup('Deseo', 'p-deseo', p.deseo || '')}
        ${fieldGroup('Defecto', 'p-defecto', p.defecto || '')}
      </div>
    </div>
    <div class="expand-section">
      <div class="expand-header" onclick="toggleSection(this)">
        <span class="expand-label">◉ Biografía</span>
        <span class="expand-icon">▾</span>
      </div>
      <div class="expand-body">
        ${fieldGroup('Backstory', 'p-backstory', p.backstory || '')}
        ${fieldGroup('Hábitos', 'p-habitos', p.habitos || '')}
        ${fieldGroup('Apariencia', 'p-apariencia', p.apariencia || '')}
      </div>
    </div>
    <div class="expand-section">
      <div class="expand-header" onclick="toggleSection(this)">
        <span class="expand-label">◉ Arco narrativo</span>
        <span class="expand-icon">▾</span>
      </div>
      <div class="expand-body">
        ${fieldGroup('Estado inicial', 'p-estado-ini', p.estado_inicial || '')}
        ${fieldGroup('Estado final', 'p-estado-fin', p.estado_final || '')}
        ${fieldGroup('Cambio emocional', 'p-cambio', p.cambio_emocional || '')}
      </div>
    </div>
  `;
}

function escenaSection(card) {
  const e = card.escena || {};
  return `
    <div class="expand-section">
      <div class="expand-header" onclick="toggleSection(this)">
        <span class="expand-label">▶ Identidad de escena</span>
        <span class="expand-icon">▾</span>
      </div>
      <div class="expand-body">
        <div class="field-group">
          <label>Tipo de escena</label>
          <select class="select-field" style="font-size:13px">
            ${['accion','revelacion','confrontacion','transicion','worldbuilding','otro'].map(t=>`<option ${e.tipo_escena===t?'selected':''}>${t}</option>`).join('')}
          </select>
        </div>
        ${fieldGroup('Mood / tono emocional', 'e-mood', e.mood || '')}
      </div>
    </div>
    <div class="expand-section">
      <div class="expand-header" onclick="toggleSection(this)">
        <span class="expand-label">▶ Dramaturgia</span>
        <span class="expand-icon">▾</span>
      </div>
      <div class="expand-body">
        ${fieldGroup('Objetivo de la escena', 'e-objetivo', e.objetivo || '')}
        ${fieldGroup('Conflicto', 'e-conflicto', e.conflicto || '')}
        ${fieldGroup('Stake (qué se juega)', 'e-stake', e.stake || '')}
        ${fieldGroup('Turning point', 'e-turning', e.turning_point || '')}
        ${fieldGroup('Momento memorable', 'e-momento', e.momento_memorable || '')}
      </div>
    </div>
    <div class="expand-section">
      <div class="expand-header" onclick="toggleSection(this)">
        <span class="expand-label">▶ Beat en el arco</span>
        <span class="expand-icon">▾</span>
      </div>
      <div class="expand-body">
        <div class="beat-slider-wrap">
          <div class="beat-value" id="beat-display">${e.beat_posicion !== null && e.beat_posicion !== undefined ? Math.round(e.beat_posicion*100)+'%' : '—'}</div>
          <input type="range" class="beat-slider" min="0" max="100" value="${e.beat_posicion!==null&&e.beat_posicion!==undefined?Math.round(e.beat_posicion*100):50}"
                 oninput="document.getElementById('beat-display').textContent=this.value+'%'">
          <div class="beat-value-label">Posición en el arco narrativo</div>
        </div>
        ${fieldGroup('Etiqueta de beat', 'e-beat-label', e.beat_etiqueta || '')}
        ${fieldGroup('Estado emocional antes', 'e-antes', e.cambio_emocional_antes || '')}
        ${fieldGroup('Estado emocional después', 'e-despues', e.cambio_emocional_despues || '')}
        <div class="row gap-8" style="margin-top:4px">
          <label style="display:flex;align-items:center;gap:6px;font-size:13px;color:var(--text-muted);cursor:pointer">
            <input type="checkbox" ${e.es_setup?'checked':''} style="accent-color:var(--primary)"> Es setup (siembra)
          </label>
          <label style="display:flex;align-items:center;gap:6px;font-size:13px;color:var(--text-muted);cursor:pointer">
            <input type="checkbox" ${e.es_payoff?'checked':''} style="accent-color:var(--primary)"> Es payoff (resuelve)
          </label>
        </div>
      </div>
    </div>
  `;
}

function fieldGroup(label, id, value) {
  return `
    <div class="field-group">
      <label>${label}</label>
      <textarea class="field-editable" id="${id}" rows="2" oninput="autoResize(this)">${value}</textarea>
    </div>
  `;
}

// ── Sheets ──────────────────────────────────────────────────────
function openNewCardSheet() {
  state.sheetData = { tipo: 'blanca', proyecto_id: state.projectId || '' };
  $sheet().innerHTML = `
    <div class="sheet-handle"></div>
    <div class="sheet-title">Nueva card</div>
    <div class="sheet-body">
      <textarea class="capture-textarea" id="new-card-body" placeholder="¿Qué tienes en mente?" autofocus></textarea>
      <div class="divider" style="margin:8px 0"></div>
      <div style="margin-bottom:12px">
        <div class="input-label">Tipo</div>
        <div class="chip-row" id="new-card-types">
          ${Object.entries(CARD_TYPES).map(([k,v])=>`
            <span class="chip ${k==='blanca'?'selected':''}" onclick="selectNewType('${k}')"
                  style="${k==='blanca'?'border-color:var(--primary);color:var(--primary)':''}"
                  data-type="${k}">${v.icon} ${v.label}</span>
          `).join('')}
        </div>
      </div>
      <div>
        <div class="input-label">Proyecto</div>
        <select class="select-field" id="new-card-project">
          <option value="">Inbox (sin proyecto)</option>
          ${Data.projects().map(p=>`<option value="${p.id}" ${state.projectId===p.id?'selected':''}>${p.nombre}</option>`).join('')}
        </select>
      </div>
    </div>
    <div class="sheet-actions">
      <button class="btn btn-secondary" onclick="closeSheet()">Cancelar</button>
      <button class="btn btn-primary" onclick="saveNewCard()">Guardar card</button>
    </div>
  `;
  openSheet();
  setTimeout(() => document.getElementById('new-card-body')?.focus(), 300);
}

function saveNewCard() {
  const body = document.getElementById('new-card-body')?.value.trim();
  if (!body) { document.getElementById('new-card-body').style.borderBottom = '1px solid var(--danger)'; return; }
  const tipo = state.sheetData.tipo || 'blanca';
  const proyecto_id = document.getElementById('new-card-project')?.value || null;
  const card = Data.addCard({ titulo: null, cuerpo: body, tipo, proyecto_id });
  closeSheet();
  // navigate to card detail
  navigate('card', { cardId: card.id });
}

function selectNewType(key) {
  state.sheetData.tipo = key;
  document.querySelectorAll('#new-card-types .chip').forEach(el => {
    const isSelected = el.dataset.type === key;
    el.classList.toggle('selected', isSelected);
    el.style.borderColor = isSelected ? 'var(--primary)' : '';
    el.style.color = isSelected ? 'var(--primary)' : '';
  });
}

function openNewProjectSheet() {
  $sheet().innerHTML = `
    <div class="sheet-handle"></div>
    <div class="sheet-title">Nuevo proyecto</div>
    <div class="sheet-body">
      <div style="display:flex;flex-direction:column;gap:16px">
        <div>
          <div class="input-label">Nombre del proyecto *</div>
          <input class="input-field" id="np-nombre" placeholder="Ej: El umbral..." autofocus>
        </div>
        <div>
          <div class="input-label">Tipo</div>
          <select class="select-field" id="np-tipo">
            ${PROJECT_TYPES.map(t=>`<option>${t}</option>`).join('')}
          </select>
        </div>
        <div>
          <div class="input-label">División</div>
          <select class="select-field" id="np-division">
            <option value="ninguna">ninguna</option>
            <option value="actos">actos</option>
            <option value="capitulos">capítulos</option>
            <option value="episodios">episodios</option>
            <option value="temporadas">temporadas</option>
            <option value="niveles">niveles</option>
          </select>
        </div>
      </div>
    </div>
    <div class="sheet-actions">
      <button class="btn btn-secondary" onclick="closeSheet()">Cancelar</button>
      <button class="btn btn-primary" onclick="saveNewProject()">Crear proyecto</button>
    </div>
  `;
  openSheet();
  setTimeout(() => document.getElementById('np-nombre')?.focus(), 300);
}

function saveNewProject() {
  const nombre = document.getElementById('np-nombre')?.value.trim();
  if (!nombre) { document.getElementById('np-nombre').style.borderColor = 'var(--danger)'; return; }
  const tipo = document.getElementById('np-tipo')?.value;
  const division = document.getElementById('np-division')?.value;
  const project = Data.addProject({ nombre, tipo, division });
  closeSheet();
  navigate('project', { projectId: project.id });
}

function openBeatSheet(cardId) {
  const card = Data.card(cardId);
  if (!card || !card.escena) return;
  const e = card.escena;
  const pos = e.beat_posicion !== null && e.beat_posicion !== undefined ? Math.round(e.beat_posicion * 100) : 50;
  $sheet().innerHTML = `
    <div class="sheet-handle"></div>
    <div class="sheet-title">${card.titulo}</div>
    <div class="sheet-body">
      <div style="font-size:13px;color:var(--text-muted);margin-bottom:16px">${card.cuerpo.slice(0,100)}…</div>
      <div class="beat-slider-wrap">
        <div class="beat-value" id="bs-display">${e.beat_posicion !== null && e.beat_posicion !== undefined ? pos+'%' : '—'}</div>
        <input type="range" class="beat-slider" min="0" max="100" value="${pos}" id="bs-slider"
               oninput="document.getElementById('bs-display').textContent=this.value+'%'">
        <div class="beat-value-label">Posición en el arco (0% inicio → 100% final)</div>
      </div>
      <div style="margin-top:16px">
        <div class="input-label">Etiqueta de beat</div>
        <input class="input-field" id="bs-label" placeholder="Ej: catalizador, clímax, punto medio..."
               value="${e.beat_etiqueta || ''}">
      </div>
      <div class="chip-row" style="margin-top:8px">
        ${['catalizador','punto de giro','punto medio','todo perdido','clímax','resolución'].map(l=>`
          <span class="chip" onclick="document.getElementById('bs-label').value='${l}'">${l}</span>
        `).join('')}
      </div>
    </div>
    <div class="sheet-actions">
      <button class="btn btn-secondary" onclick="closeSheet()">Cancelar</button>
      <button class="btn btn-primary" onclick="saveBeat('${cardId}')">Posicionar</button>
    </div>
  `;
  openSheet();
}

function saveBeat(cardId) {
  const pos = parseInt(document.getElementById('bs-slider')?.value || '50') / 100;
  const label = document.getElementById('bs-label')?.value.trim();
  const card = Data.card(cardId);
  if (card) {
    card.escena.beat_posicion = pos;
    card.escena.beat_etiqueta = label || null;
  }
  closeSheet();
  if (state.screen === 'project') renderProject();
}

function openSheet() {
  $sheet().classList.add('open');
  $sheetOverlay().classList.add('open');
}
function closeSheet() {
  $sheet().classList.remove('open');
  $sheetOverlay().classList.remove('open');
}

// ── Off-canvas ──────────────────────────────────────────────────
function openCanvas() {
  renderCanvas();
  $offcanvas().classList.add('open');
  $canvasOverlay().classList.add('open');
}
function closeCanvas() {
  $offcanvas().classList.remove('open');
  $canvasOverlay().classList.remove('open');
}

function renderCanvas() {
  const tags = Data.allTags();
  $offcanvas().innerHTML = `
    <div class="canvas-header">
      <div class="canvas-logo">narr<span>a</span>tive</div>
    </div>
    <div class="canvas-body">
      <div class="search-bar" style="margin-top:12px">
        <span class="search-icon">🔍</span>
        <input placeholder="Buscar cards, #tags..." id="canvas-search" oninput="canvasSearch(this.value)">
      </div>

      <div id="canvas-search-results"></div>

      <div class="canvas-section">Proyectos</div>
      ${Data.projects().map(p => `
        <div class="canvas-item" onclick="closeCanvas();navigate('project',{projectId:'${p.id}'})">
          <span class="ci-icon" style="color:${p.color}">◆</span>
          ${p.nombre}
          <span style="margin-left:auto;font-size:11px;color:var(--text-dim)">${Data.projectCards(p.id).length}</span>
        </div>
      `).join('')}
      <div class="canvas-item" onclick="closeCanvas();navigate('inbox')">
        <span class="ci-icon">📥</span> Inbox
        <span style="margin-left:auto;font-size:11px;color:var(--text-dim)">${Data.inboxCards().length}</span>
      </div>

      <div class="canvas-section">Tags globales</div>
      <div class="tag-cloud">
        ${tags.map(t => `<span class="tag-chip" onclick="searchByTag('${t}')">${t}</span>`).join('')}
      </div>

      <div class="canvas-section">Acciones</div>
      <div class="canvas-item" onclick="closeCanvas();openNewProjectSheet()">
        <span class="ci-icon">＋</span> Nuevo proyecto
      </div>
      <div class="canvas-item" onclick="closeCanvas();openNewCardSheet()">
        <span class="ci-icon">✎</span> Nueva card
      </div>
    </div>
  `;
}

function canvasSearch(q) {
  const results = q.length > 1 ? Data.search(q) : [];
  const container = document.getElementById('canvas-search-results');
  if (!container) return;
  if (results.length === 0 && q.length > 1) {
    container.innerHTML = `<div style="padding:8px 16px;font-size:13px;color:var(--text-dim)">Sin resultados para "${q}"</div>`;
    return;
  }
  container.innerHTML = results.slice(0,5).map(c => {
    const t = CARD_TYPES[c.tipo] || CARD_TYPES.blanca;
    return `
      <div class="canvas-item" onclick="closeCanvas();navigate('card',{cardId:'${c.id}'})">
        <span class="ci-icon" style="color:${t.color}">${t.icon}</span>
        <div>
          <div style="font-size:13px">${c.titulo || c.cuerpo.slice(0,40)+'…'}</div>
          <div style="font-size:11px;color:var(--text-dim)">${t.label}</div>
        </div>
      </div>
    `;
  }).join('');
}

function searchByTag(tag) {
  closeCanvas();
  const results = Data.cardsByTag(tag);
  setAppbar(`<button class="back-btn" onclick="back()">←</button><span class="title">${tag}</span>`);
  $screen().innerHTML = `
    <div style="padding:16px 0 80px">
      <div style="padding:0 16px 12px;font-size:12px;color:var(--text-muted)">${results.length} cards con ${tag}</div>
      ${results.map(c => cardListItem(c)).join('')}
    </div>
  `;
  state.history.push({ screen: state.screen });
  state.screen = 'search';
  updateFab();
}

// ── Card interactions ───────────────────────────────────────────
function selectType(tipo) {
  const card = Data.card(state.cardId);
  if (!card) return;
  Data.updateCard(state.cardId, { tipo });
  // re-render type selector
  document.querySelectorAll('.type-option').forEach(el => {
    const isSelected = el.getAttribute('onclick').includes(`'${tipo}'`);
    el.classList.toggle('selected', isSelected);
    const t = CARD_TYPES[tipo];
    if (isSelected) {
      el.style.borderColor = t.color;
      el.style.background = t.color + '18';
    } else {
      el.style.borderColor = '';
      el.style.background = '';
    }
  });
}

function setEstado(cardId, estado) {
  Data.updateCard(cardId, { estado });
  document.querySelectorAll('.chip').forEach(el => {
    if (['activa','borrador','archivada'].includes(el.textContent.trim())) {
      el.classList.toggle('selected', el.textContent.trim() === estado);
    }
  });
}

function setProject(cardId, proyecto_id) {
  Data.updateCard(cardId, { proyecto_id: proyecto_id || null });
}

function saveCard(cardId) {
  const titulo = document.getElementById('edit-titulo')?.value.trim() || null;
  const cuerpo = document.getElementById('edit-cuerpo')?.value || '';
  Data.updateCard(cardId, { titulo, cuerpo });
  // Flash confirm
  const btn = document.querySelector('.action-btn');
  if (btn) { btn.textContent = '✓'; setTimeout(() => btn.textContent = 'Guardar', 1200); }
}

function removeTag(cardId, tag) {
  const card = Data.card(cardId);
  if (!card) return;
  card.tags = card.tags.filter(t => t !== tag);
  renderCard();
}

function handleTagInput(e, cardId) {
  if (e.key === 'Enter' || e.key === ' ') {
    e.preventDefault();
    const val = e.target.value.trim();
    if (!val) return;
    const tag = val.startsWith('#') ? val : '#' + val;
    const card = Data.card(cardId);
    if (card && !card.tags.includes(tag)) {
      card.tags.push(tag);
      renderCard();
    }
  }
}

function toggleSection(header) {
  header.classList.toggle('open');
  const body = header.nextElementSibling;
  if (body) body.classList.toggle('open');
}

function toggleFilter(tipo) {
  const idx = state.filters.indexOf(tipo);
  if (idx === -1) state.filters.push(tipo);
  else state.filters.splice(idx, 1);
  renderProject();
}

function setFilter(tipo) {
  state.filters = tipo ? [tipo] : [];
  renderProject();
}

// ── Helpers ─────────────────────────────────────────────────────
function setAppbar(html) {
  $appbar().innerHTML = html;
}

function updateFab() {
  const fab = $fab();
  if (!fab) return;
  const showScreens = ['home','inbox','project'];
  if (showScreens.includes(state.screen)) {
    fab.classList.remove('hidden');
    fab.onclick = state.screen === 'home' ? showFabMenu : openNewCardSheet;
  } else {
    fab.classList.add('hidden');
  }
}

function showFabMenu() {
  $sheet().innerHTML = `
    <div class="sheet-handle"></div>
    <div class="sheet-title">Crear</div>
    <div class="sheet-body">
      <div class="canvas-item" onclick="closeSheet();openNewCardSheet()" style="padding:14px 0;border-bottom:1px solid var(--border)">
        <span class="ci-icon" style="font-size:20px">✎</span>
        <div>
          <div style="font-size:14px;font-weight:600;color:var(--text)">Nueva card</div>
          <div style="font-size:12px;color:var(--text-muted)">Captura una idea al instante</div>
        </div>
      </div>
      <div class="canvas-item" onclick="closeSheet();openNewProjectSheet()" style="padding:14px 0">
        <span class="ci-icon" style="font-size:20px">◆</span>
        <div>
          <div style="font-size:14px;font-weight:600;color:var(--text)">Nuevo proyecto</div>
          <div style="font-size:12px;color:var(--text-muted)">One-shot, serie, videojuego…</div>
        </div>
      </div>
    </div>
  `;
  openSheet();
}

function cardListItem(c) {
  const t = CARD_TYPES[c.tipo] || CARD_TYPES.blanca;
  return `
    <div style="margin:0 12px 8px;background:var(--card);border-radius:var(--radius);padding:14px;cursor:pointer;border:1px solid var(--border);border-left:3px solid ${t.color}"
         onclick="navigate('card',{cardId:'${c.id}'})">
      <div class="row gap-8" style="margin-bottom:4px">
        <span style="font-size:11px;color:${t.color};font-weight:600">${t.icon} ${t.label}</span>
        ${c.estado === 'borrador' ? `<span class="estado-badge estado-borrador">borrador</span>` : ''}
        <span style="margin-left:auto;font-size:11px;color:var(--text-dim)">${c.fecha_edicion}</span>
      </div>
      ${c.titulo ? `<div style="font-size:14px;font-weight:600;color:var(--text);margin-bottom:4px">${c.titulo}</div>` : ''}
      <div style="font-size:13px;color:var(--text-muted);line-height:1.5;overflow:hidden;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical">${c.cuerpo}</div>
      ${c.tags.length ? `<div class="card-tags" style="margin-top:6px">${c.tags.slice(0,4).map(t=>`<span class="tag-chip">${t}</span>`).join('')}</div>` : ''}
    </div>
  `;
}

function emptyState(icon, title, sub) {
  return `<div class="empty-state"><div class="empty-icon">${icon}</div><div class="empty-title">${title}</div><div class="empty-sub">${sub}</div></div>`;
}

function autoResize(el) {
  el.style.height = 'auto';
  el.style.height = el.scrollHeight + 'px';
}

// ── Init ────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {
  render();
});
