// Mock database — simula Firestore / Room
// Estructura plana con relaciones vía proyecto_id, contenedor_id, tags

const DB = {
  projects: [
    { id: 'p1', nombre: 'El Umbral', tipo: 'one-shot', division: 'actos', color: '#7B5EA7', fecha_creacion: '2026-05-15', fecha_edicion: '2026-06-01' },
    { id: 'p2', nombre: 'Ciclo Rojo', tipo: 'serie', division: 'episodios', color: '#A75E5E', fecha_creacion: '2026-06-01', fecha_edicion: '2026-06-03' },
  ],

  containers: [
    { id: 'c1', nombre: 'Acto I',     tipo: 'acto',     proyecto_id: 'p1', orden: 1, color: '#3A2D4F' },
    { id: 'c2', nombre: 'Acto II',    tipo: 'acto',     proyecto_id: 'p1', orden: 2, color: '#2D3A4F' },
    { id: 'c3', nombre: 'Acto III',   tipo: 'acto',     proyecto_id: 'p1', orden: 3, color: '#4F2D3A' },
    { id: 'c4', nombre: 'Episodio 1', tipo: 'episodio', proyecto_id: 'p2', orden: 1, color: '#2D4F4A' },
  ],

  cards: [
    // ── El Umbral ───────────────────────────────────────────────
    {
      id: 'k1', titulo: 'Premisa', tipo: 'premisa', proyecto_id: 'p1', contenedor_id: null, estado: 'activa',
      cuerpo: 'En una ciudad que borra sus muertos, una detective descubre que ella misma ha muerto tres veces.',
      tags: ['#el-umbral', '#premisa', '#ana-ruiz'],
      fecha_creacion: '2026-05-15', fecha_edicion: '2026-06-01',
    },
    {
      id: 'k2', titulo: 'Ana Ruiz', tipo: 'personaje', proyecto_id: 'p1', contenedor_id: null, estado: 'activa',
      cuerpo: 'Detective privada, 34 años. Especialista en casos de personas desaparecidas.',
      tags: ['#ana-ruiz', '#protagonista', '#detective'],
      personaje: {
        rol: 'protagonista',
        motivacion: 'Encontrar la verdad sobre su propia muerte',
        conflicto_interno: 'No sabe si puede confiar en sus propios recuerdos',
        miedo: 'Descubrir que ella misma es la responsable',
        deseo: 'Recordar quién era antes del primer borrado',
        defecto: 'Obsesiva, incapaz de soltar un caso',
        backstory: 'Llegó a la ciudad huyendo de algo que ya no recuerda.',
        apariencia: 'Cabello negro corto, cicatriz en el cuello izquierdo.',
        estado_inicial: 'Cínica, desconfiada, solitaria',
        estado_final: 'Aceptación del ciclo, paz incómoda',
        cambio_emocional: 'De la negación a la aceptación',
      },
      fecha_creacion: '2026-05-16', fecha_edicion: '2026-06-01',
    },
    {
      id: 'k3', titulo: 'El Custodio', tipo: 'personaje', proyecto_id: 'p1', contenedor_id: null, estado: 'activa',
      cuerpo: 'El guardián del sistema de borrado. No es malvado — es el sistema encarnado.',
      tags: ['#el-custodio', '#antagonista'],
      personaje: {
        rol: 'antagonista',
        motivacion: 'Mantener el orden del ciclo de olvido',
        conflicto_interno: 'Empezó a dudar del sistema que defiende',
        miedo: 'Que el caos del recuerdo destruya la ciudad',
        deseo: 'Un sucesor que entienda la carga',
        defecto: 'Incapacidad para ver a las personas como individuos',
      },
      fecha_creacion: '2026-05-17', fecha_edicion: '2026-05-28',
    },
    {
      id: 'k4', titulo: 'La Ciudad Sin Nombre', tipo: 'locacion', proyecto_id: 'p1', contenedor_id: null, estado: 'activa',
      cuerpo: 'Metrópolis que opera con el Sistema de Borrado — al morir, todos los registros de una persona desaparecen en 48h.',
      tags: ['#ciudad', '#worldbuilding', '#el-umbral'],
      fecha_creacion: '2026-05-18', fecha_edicion: '2026-05-25',
    },
    {
      id: 'k5', titulo: 'Despertar', tipo: 'escena', proyecto_id: 'p1', contenedor_id: 'c1', estado: 'activa',
      cuerpo: 'Ana despierta sin saber dónde está. Su ID no existe en el sistema. El portero la mira como si fuera un fantasma.',
      tags: ['#ana-ruiz', '#ciudad', '#apertura'],
      escena: {
        tipo_escena: 'revelacion', mood: 'confuso, inquietante',
        objetivo: 'Establecer el misterio central', conflicto: 'Ana no existe para el sistema',
        stake: 'Su identidad y su vida entera',
        turning_point: 'Encuentra su propio expediente marcado como "borrado"',
        beat_posicion: 0.05, beat_etiqueta: 'catalizador',
        cambio_emocional_antes: 'Confundida', cambio_emocional_despues: 'Aterrada con determinación',
        es_setup: true, es_payoff: false,
      },
      fecha_creacion: '2026-05-20', fecha_edicion: '2026-06-01',
    },
    {
      id: 'k6', titulo: 'El Archivo Prohibido', tipo: 'escena', proyecto_id: 'p1', contenedor_id: 'c2', estado: 'activa',
      cuerpo: 'Ana descubre la sala donde se guardan los registros antes del borrado. El Custodio la atrapa.',
      tags: ['#ana-ruiz', '#el-custodio', '#archivo'],
      escena: {
        tipo_escena: 'confrontacion', mood: 'tenso, claustrofóbico',
        objetivo: 'Ana obtiene información sobre sus muertes anteriores',
        conflicto: 'El Custodio la atrapa en el archivo',
        stake: 'Su tercera muerte — la definitiva',
        turning_point: 'El Custodio le ofrece un trato en lugar de borrarla',
        beat_posicion: 0.35, beat_etiqueta: 'punto de giro 1',
        cambio_emocional_antes: 'Investigadora en control', cambio_emocional_despues: 'Vulnerable, necesita un aliado',
        es_setup: true, es_payoff: false,
      },
      fecha_creacion: '2026-05-21', fecha_edicion: '2026-06-02',
    },
    {
      id: 'k7', titulo: 'La Trampa', tipo: 'escena', proyecto_id: 'p1', contenedor_id: 'c2', estado: 'activa',
      cuerpo: 'El trato del Custodio era mentira. Ana queda atrapada y descubre que fue usada.',
      tags: ['#ana-ruiz', '#el-custodio', '#traicion'],
      escena: {
        tipo_escena: 'confrontacion', mood: 'traición, desesperación',
        objetivo: 'Romper la alianza falsa',
        conflicto: 'Ana descubre que el Custodio la usó para identificar "recordadores"',
        stake: 'Todos los que recuerdan serán borrados',
        turning_point: 'Ana decide sabotear el sistema desde adentro',
        beat_posicion: 0.6, beat_etiqueta: 'punto medio',
        cambio_emocional_antes: 'Esperanza frágil', cambio_emocional_despues: 'Furia controlada',
        es_setup: false, es_payoff: true,
      },
      fecha_creacion: '2026-05-22', fecha_edicion: '2026-06-02',
    },
    {
      id: 'k8', titulo: 'Todo Perdido', tipo: 'escena', proyecto_id: 'p1', contenedor_id: 'c3', estado: 'activa',
      cuerpo: 'El sistema inicia el borrado masivo. Ana falla en su primer intento de detenerlo.',
      tags: ['#ana-ruiz', '#ciudad', '#crisis'],
      escena: {
        tipo_escena: 'accion', mood: 'oscuro, desesperado',
        objetivo: 'Máxima tensión antes del clímax',
        conflicto: 'El sistema activa el protocolo de borrado masivo',
        stake: 'La existencia de todos los recordadores',
        turning_point: 'Ana encuentra el núcleo físico del sistema',
        beat_posicion: 0.8, beat_etiqueta: 'todo perdido',
        cambio_emocional_antes: 'Determinada', cambio_emocional_despues: 'Al borde del colapso',
        es_setup: false, es_payoff: false,
      },
      fecha_creacion: '2026-05-23', fecha_edicion: '2026-06-03',
    },
    {
      id: 'k9', titulo: 'El Umbral', tipo: 'escena', proyecto_id: 'p1', contenedor_id: 'c3', estado: 'activa',
      cuerpo: 'Ana ingresa al núcleo. Tiene que elegir: destruir el sistema o convertirse en la nueva Custodio.',
      tags: ['#ana-ruiz', '#el-custodio', '#climax', '#el-umbral'],
      escena: {
        tipo_escena: 'confrontacion', mood: 'épico, íntimo, circular',
        objetivo: 'Resolución del arco de Ana',
        conflicto: 'El precio de la libertad es convertirse en lo que odias',
        stake: 'El alma de Ana y la ciudad entera',
        turning_point: 'Ana activa el borrado del Custodio... y del sistema',
        momento_memorable: '"Sabía que lo harías" — el Custodio sonríe mientras desaparece.',
        beat_posicion: 0.95, beat_etiqueta: 'clímax',
        cambio_emocional_antes: 'Quebrada pero de pie', cambio_emocional_despues: 'Paz extraña, nueva',
        es_setup: false, es_payoff: true,
      },
      fecha_creacion: '2026-05-24', fecha_edicion: '2026-06-03',
    },
    // ── Ciclo Rojo ──────────────────────────────────────────────
    {
      id: 'k10', titulo: 'Premisa — Ciclo Rojo', tipo: 'premisa', proyecto_id: 'p2', contenedor_id: null, estado: 'activa',
      cuerpo: 'En la primera colonia marciana, los muertos no son enterrados — son reciclados como combustible para mantener vivos a los demás.',
      tags: ['#ciclo-rojo', '#marte', '#premisa'],
      fecha_creacion: '2026-06-01', fecha_edicion: '2026-06-03',
    },
    {
      id: 'k11', titulo: 'Kira Voss', tipo: 'personaje', proyecto_id: 'p2', contenedor_id: null, estado: 'activa',
      cuerpo: 'Ingeniera de sistemas de reciclaje. Descubre que la muerte de su madre no fue un accidente.',
      tags: ['#kira', '#protagonista', '#ciclo-rojo'],
      personaje: {
        rol: 'protagonista',
        motivacion: 'Justicia por la muerte de su madre',
        conflicto_interno: 'Su trabajo mantiene viva a la colonia, pero también perpetúa el sistema que la mató',
        miedo: 'Descubrir que su madre eligió morir',
      },
      fecha_creacion: '2026-06-01', fecha_edicion: '2026-06-03',
    },
    {
      id: 'k12', titulo: 'Estación Roja', tipo: 'locacion', proyecto_id: 'p2', contenedor_id: null, estado: 'activa',
      cuerpo: 'La colonia marciana. Todo está teñido de rojo por el polvo. El olor a combustible es constante.',
      tags: ['#marte', '#ciclo-rojo', '#locacion'],
      fecha_creacion: '2026-06-01', fecha_edicion: '2026-06-02',
    },
    {
      id: 'k13', titulo: 'Primer Contacto', tipo: 'escena', proyecto_id: 'p2', contenedor_id: 'c4', estado: 'activa',
      cuerpo: 'Kira recibe el código de reciclaje de su madre — pero el timestamp no cuadra con el reporte oficial.',
      tags: ['#kira', '#marte', '#misterio'],
      escena: {
        tipo_escena: 'revelacion', mood: 'frío, técnico, amenazante',
        objetivo: 'Establecer la pregunta central',
        beat_posicion: 0.08, beat_etiqueta: 'gancho',
        es_setup: true, es_payoff: false,
      },
      fecha_creacion: '2026-06-02', fecha_edicion: '2026-06-03',
    },
    {
      id: 'k14', titulo: 'El Archivo de Kira', tipo: 'escena', proyecto_id: 'p2', contenedor_id: 'c4', estado: 'borrador',
      cuerpo: 'Kira accede al archivo de reciclaje de su madre. Alguien ya estuvo ahí antes.',
      tags: ['#kira', '#archivo', '#ciclo-rojo'],
      escena: { tipo_escena: 'revelacion', mood: 'paranoico', beat_posicion: null, beat_etiqueta: null },
      fecha_creacion: '2026-06-03', fecha_edicion: '2026-06-03',
    },
    // ── Inbox ───────────────────────────────────────────────────
    {
      id: 'k15', titulo: null, tipo: 'blanca', proyecto_id: null, contenedor_id: null, estado: 'activa',
      cuerpo: '¿Y si el antagonista no sabe que es el villano? Cree que es el héroe de su propia historia.',
      tags: [],
      fecha_creacion: '2026-06-02', fecha_edicion: '2026-06-02',
    },
    {
      id: 'k16', titulo: null, tipo: 'blanca', proyecto_id: null, contenedor_id: null, estado: 'activa',
      cuerpo: 'Escena de lluvia en el archivo — mood melancólico. El agua en un lugar donde no debería haber agua.',
      tags: ['#lluvia', '#mood'],
      fecha_creacion: '2026-06-03', fecha_edicion: '2026-06-03',
    },
    {
      id: 'k17', titulo: 'Mecánica del olvido', tipo: 'nota', proyecto_id: null, contenedor_id: null, estado: 'activa',
      cuerpo: '¿Quién decide qué se borra? ¿Hay apelación? ¿Hay errores intencionales? ¿Puede alguien pagar para no ser borrado?',
      tags: ['#worldbuilding', '#sistema'],
      fecha_creacion: '2026-06-03', fecha_edicion: '2026-06-03',
    },
  ],
};

// ── Query helpers (simula capa de datos / Firestore queries) ──

const Data = {
  projects: () => DB.projects,
  project: (id) => DB.projects.find(p => p.id === id),

  cards: () => DB.cards,
  card: (id) => DB.cards.find(c => c.id === id),

  projectCards: (projectId) => DB.cards.filter(c => c.proyecto_id === projectId),
  inboxCards: () => DB.cards.filter(c => !c.proyecto_id),

  containers: (projectId) => DB.containers.filter(c => c.proyecto_id === projectId),

  cardsByType: (projectId, tipo) =>
    DB.cards.filter(c => c.proyecto_id === projectId && c.tipo === tipo),

  cardsByTag: (tag) => DB.cards.filter(c => c.tags.includes(tag)),

  relatedCards: (cardId) => {
    const card = Data.card(cardId);
    if (!card || !card.tags.length) return [];
    return DB.cards.filter(c => c.id !== cardId && c.tags.some(t => card.tags.includes(t)));
  },

  scenesForTimeline: (projectId) =>
    DB.cards.filter(c => c.proyecto_id === projectId && c.tipo === 'escena'),

  allTags: () => {
    const tags = new Set();
    DB.cards.forEach(c => c.tags.forEach(t => tags.add(t)));
    return [...tags].sort();
  },

  search: (query) => {
    const q = query.toLowerCase();
    return DB.cards.filter(c =>
      (c.titulo && c.titulo.toLowerCase().includes(q)) ||
      c.cuerpo.toLowerCase().includes(q) ||
      c.tags.some(t => t.includes(q))
    );
  },

  // Mutations
  addCard: (card) => {
    card.id = 'k' + Date.now();
    card.fecha_creacion = new Date().toISOString().split('T')[0];
    card.fecha_edicion = card.fecha_creacion;
    card.tags = card.tags || [];
    card.estado = 'activa';
    DB.cards.unshift(card);
    return card;
  },

  updateCard: (id, updates) => {
    const idx = DB.cards.findIndex(c => c.id === id);
    if (idx !== -1) {
      DB.cards[idx] = { ...DB.cards[idx], ...updates, fecha_edicion: new Date().toISOString().split('T')[0] };
    }
    return DB.cards[idx];
  },

  addProject: (project) => {
    project.id = 'p' + Date.now();
    project.fecha_creacion = new Date().toISOString().split('T')[0];
    project.fecha_edicion = project.fecha_creacion;
    project.color = ['#7B5EA7','#5EA77B','#5EA7A7','#A77B5E','#A75E5E'][Math.floor(Math.random()*5)];
    DB.projects.push(project);
    return project;
  },
};

// Card type metadata
const CARD_TYPES = {
  blanca:       { label: 'Sin tipo',      color: '#666',    icon: '○' },
  premisa:      { label: 'Premisa',       color: '#7B5EA7', icon: '◆' },
  personaje:    { label: 'Personaje',     color: '#5EA77B', icon: '◉' },
  escena:       { label: 'Escena',        color: '#5E9EA7', icon: '▶' },
  locacion:     { label: 'Locación',      color: '#A7875E', icon: '⬡' },
  worldbuilding:{ label: 'Worldbuilding', color: '#8EA75E', icon: '⬢' },
  conflicto:    { label: 'Conflicto',     color: '#A75E5E', icon: '⚡' },
  prop:         { label: 'Prop',          color: '#7E5EA7', icon: '◇' },
  beat:         { label: 'Beat',          color: '#A75E8E', icon: '◈' },
  nota:         { label: 'Nota',          color: '#777',    icon: '✎' },
};

const PROJECT_TYPES = ['one-shot','serie','temporadas','videojuego','antología','otro'];
