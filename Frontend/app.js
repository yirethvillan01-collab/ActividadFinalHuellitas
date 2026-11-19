/* ============================================================
   CONFIGURACIÓN — cambia aquí la URL base del backend
   ============================================================ */
const CONFIG = {
  API_BASE_URL: "http://localhost:8080",
  ENDPOINTS: {
    listar: "/api/listarMascotas",
    crear: "/api/crearMascotas",
    actualizar: "/api/actualizarMascotas",
    eliminar: "/api/eliminarMascotas", // se usa como /api/eliminarMascotas/{id}
    especies: "/api/listarEspecies",
    sexos: "/api/listarSexos",
  },
};

/* ---------- Utilidades ---------- */
const $ = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => [...root.querySelectorAll(sel)];

const EMOJI = { canino: "🐶", perro: "🐶", felino: "🐱", gato: "🐱", ave: "🐦", conejo: "🐰" };
const emojiDe = (especie) => EMOJI[String(especie ?? "").toLowerCase()] || "🐾";
const norm = (s) => String(s ?? "").normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase().trim();

const pata = () => {
  const s = document.createElementNS("http://www.w3.org/2000/svg", "svg");
  s.setAttribute("class", "paw"); s.setAttribute("aria-hidden", "true");
  s.innerHTML = '<use href="#paw"/>';
  return s;
};

function el(tag, props = {}, ...hijos) {
  const nodo = Object.assign(document.createElement(tag), props);
  hijos.flat().forEach((h) => nodo.append(h));
  return nodo;
}

function toast(mensaje, tipo = "info", detalle = "") {
  const t = el("div", { className: `toast ${tipo}` }, mensaje);
  if (detalle) t.append(el("small", { textContent: detalle }));
  $("#toasts").append(t);
  setTimeout(() => t.remove(), 4500);
}

function cargando(boton, activo) {
  boton.disabled = activo;
  boton.classList.toggle("loading", activo);
}

/* ---------- Cliente de API ---------- */
async function api(ruta, { method = "GET", body } = {}) {
  let res;
  try {
    res = await fetch(CONFIG.API_BASE_URL + ruta, {
      method,
      headers: body ? { "Content-Type": "application/json" } : undefined,
      body: body ? JSON.stringify(body) : undefined,
    });
  } catch {
    throw new Error(
      `No se pudo conectar con el servidor (${CONFIG.API_BASE_URL}). Verifica que el backend esté encendido.`
    );
  }
  let data = null;
  try { data = await res.json(); } catch { /* respuesta sin JSON */ }

  if (!res.ok || !data || data.estado >= 400) {
    const errs = Array.isArray(data?.errores)
      ? data.errores.map((e) => e.mensajeError || e).join(" · ")
      : (typeof data?.errores === "string" ? data.errores : "");
    const err = new Error(data?.mensaje || `Error ${res.status}`);
    err.detalle = errs;
    throw err;
  }
  return data;
}

const mostrarError = (e) => toast(e.message, "error", e.detalle);

const listarTodas = async () => (await api(CONFIG.ENDPOINTS.listar)).datos || [];

async function buscarPorId(id) {
  const m = (await listarTodas()).find((x) => String(x.id) === String(id));
  if (!m) throw new Error("No existe una mascota con ese ID");
  return m;
}

/* ---------- Catálogos (especies y sexos desde el backend) ---------- */
function llenarSelect(sel, items, placeholder) {
  const actual = sel.value;
  sel.replaceChildren(
    el("option", { value: "", textContent: placeholder }),
    ...items.map((i) => el("option", { value: i.id, textContent: i.nombre }))
  );
  sel.value = actual;
}

async function cargarCatalogos() {
  try {
    const [esp, sex] = await Promise.all([api(CONFIG.ENDPOINTS.especies), api(CONFIG.ENDPOINTS.sexos)]);
    $$("select[name=especie]").forEach((s) => llenarSelect(s, esp.datos || [], "Selecciona…"));
    $$("select[name=sexo]").forEach((s) => llenarSelect(s, sex.datos || [], "Selecciona…"));
    llenarSelect($("#f-especie"), esp.datos || [], "Todas");
  } catch (e) { mostrarError(e); }
}

/* ---------- Navegación entre pantallas ---------- */
const VISTAS = ["listar", "crear", "actualizar", "eliminar"];

function irA(vista) {
  if (!VISTAS.includes(vista)) vista = "listar";
  VISTAS.forEach((v) => $(`#view-${v}`).hidden = v !== vista);
  $$(".nav-btn").forEach((b) => {
    if (b.dataset.view === vista) b.setAttribute("aria-current", "page");
    else b.removeAttribute("aria-current");
  });
  if (location.hash !== "#" + vista) history.replaceState(null, "", "#" + vista);
  window.scrollTo({ top: 0 });
  if (vista === "listar") cargarLista();
}

$$(".nav-btn").forEach((b) => b.addEventListener("click", () => irA(b.dataset.view)));

/* ---------- Tarjeta de mascota ---------- */
function tarjeta(m, { acciones = true, solo = false } = {}) {
  const esp = m.especie?.nombre, sexo = m.sexo?.nombre;
  const fila = (k, v) => [el("dt", { textContent: k }), el("dd", { textContent: v || "—" })];
  const card = el("article", {
    className: `pet ${norm(sexo) === "hembra" ? "hembra" : ""} ${solo ? "solo" : ""}`,
  },
    el("div", { className: "pet-head" },
      el("span", { className: "avatar", textContent: emojiDe(esp), ariaHidden: "true" }),
      el("div", {}, el("h2", { textContent: m.nombre }), el("small", { textContent: `ID ${m.id}` }))
    ),
    el("dl", {},
      fila("Especie", esp), fila("Raza", m.raza),
      fila("Edad", m.edad != null ? `${m.edad} ${m.edad === 1 ? "año" : "años"}` : ""),
      fila("Sexo", sexo), fila("Responsable", m.responsable),
      fila("Notas", m.observaciones)
    )
  );
  if (acciones) {
    card.append(el("div", { className: "pet-actions" },
      el("button", { className: "btn btn-pink", onclick: () => editar(m) }, pata(), "Editar"),
      el("button", { className: "btn btn-ghost", onclick: () => prepararEliminar(m) }, pata(), "Eliminar")
    ));
  }
  return card;
}

/* ---------- LISTAR + FILTROS ---------- */
let mascotas = [];
let cargaActual = 0; // evita que una respuesta vieja pise una más reciente

async function cargarLista() {
  const n = ++cargaActual;
  const cont = $("#lista");
  cont.replaceChildren(...[1, 2, 3].map(() => el("div", { className: "skeleton" })));
  $("#conteo").textContent = "";
  try {
    const datos = await listarTodas();
    if (n !== cargaActual) return;
    mascotas = datos;
    aplicarFiltros();
  } catch (e) {
    if (n !== cargaActual) return;
    mascotas = []; // no dejar datos viejos (por ejemplo, mascotas ya eliminadas)
    $("#chips").replaceChildren();
    cont.replaceChildren(el("div", { className: "state" },
      el("strong", { textContent: "No pudimos cargar la lista" }), e.message));
  }
}

function aplicarFiltros() {
  const campos = [
    { clave: "ID", input: $("#f-id") },
    { clave: "Nombre", input: $("#f-nombre") },
    { clave: "Responsable", input: $("#f-responsable") },
    { clave: "Especie", input: $("#f-especie") },
  ];
  const [fId, fNombre, fResp, fEsp] = campos.map((c) => c.input.value.trim());

  const res = mascotas.filter((m) =>
    (!fId || String(m.id) === fId) &&
    (!fNombre || norm(m.nombre).includes(norm(fNombre))) &&
    (!fResp || norm(m.responsable).includes(norm(fResp))) &&
    (!fEsp || String(m.especie?.id) === fEsp)
  );

  // Etiquetas de filtros activos (cada una se puede quitar)
  $("#chips").replaceChildren(...campos.filter((c) => c.input.value.trim()).map((c) => {
    const valor = c.input.tagName === "SELECT" ? c.input.selectedOptions[0].textContent : c.input.value.trim();
    return el("button", {
      type: "button", className: "chip", textContent: `${c.clave}: ${valor}  ✕`,
      ariaLabel: `Quitar filtro ${c.clave}`,
      onclick: () => { c.input.value = ""; aplicarFiltros(); },
    });
  }));

  $("#conteo").textContent = `${res.length} ${res.length === 1 ? "resultado" : "resultados"}`;
  const cont = $("#lista");
  if (!res.length) {
    cont.replaceChildren(el("div", { className: "state" },
      el("strong", { textContent: mascotas.length ? "No hay mascotas con esos filtros" : "Aún no hay mascotas" }),
      mascotas.length ? "Prueba cambiando o quitando algún filtro." : "Registra la primera desde la pestaña Registrar."));
    return;
  }
  cont.replaceChildren(...res.map((m) => tarjeta(m)));
}

// Al filtrar se vuelve a consultar el backend, para no usar datos desactualizados
$("#form-filtros").addEventListener("submit", (e) => { e.preventDefault(); cargarLista(); });
$("#form-filtros").addEventListener("reset", () => setTimeout(aplicarFiltros, 0));
$("#f-especie").addEventListener("change", aplicarFiltros);

/* ---------- Formularios compartidos (Crear / Actualizar) ---------- */
function montarCampos(form) {
  $(".fields", form).append($("#tpl-campos").content.cloneNode(true));
}

function validar(form) {
  let ok = true;
  $$(".error-msg", form).forEach((n) => n.remove());
  $$("[required]", form).forEach((c) => {
    const vacio = !c.value.trim();
    c.classList.toggle("invalid", vacio);
    c.setAttribute("aria-invalid", vacio);
    if (vacio) {
      ok = false;
      c.parentElement.append(el("div", { className: "error-msg", textContent: "Este campo es obligatorio" }));
    }
  });
  if (!ok) $(".invalid", form).focus();
  return ok;
}

// El backend recibe un objeto (no un arreglo), con especie y sexo como ids
function leerForm(form) {
  const f = Object.fromEntries(new FormData(form));
  return {
    nombre: f.nombre.trim(),
    especie: Number(f.especie),
    raza: f.raza.trim(),
    edad: Number(f.edad),
    responsable: f.responsable.trim(),
    observaciones: f.observaciones.trim(),
    sexo: Number(f.sexo),
  };
}

const formCrear = $("#form-crear");
const formAct = $("#form-actualizar");
[formCrear, formAct].forEach(montarCampos);
const catalogosListos = cargarCatalogos();

/* ---------- CREAR ---------- */
formCrear.addEventListener("submit", async (e) => {
  e.preventDefault();
  if (!validar(formCrear)) return;
  const btn = $("[type=submit]", formCrear);
  cargando(btn, true);
  try {
    const { mensaje, datos } = await api(CONFIG.ENDPOINTS.crear, { method: "POST", body: leerForm(formCrear) });
    toast(mensaje, "ok", datos?.nombre ? `${datos.nombre} quedó con el ID ${datos.id}` : "");
    formCrear.reset();
    irA("listar"); // vuelve al listado y lo refresca
  } catch (err) { mostrarError(err); }
  cargando(btn, false);
});
formCrear.addEventListener("reset", () => $$(".invalid, .error-msg", formCrear).forEach((n) =>
  n.classList.contains("error-msg") ? n.remove() : n.classList.remove("invalid")));

/* ---------- ACTUALIZAR ---------- */
let idEnEdicion = null;

async function llenarEdicion(m) {
  await catalogosListos;
  idEnEdicion = m.id;
  const f = formAct.elements;
  f.nombre.value = m.nombre ?? "";
  f.especie.value = m.especie?.id ?? "";
  f.raza.value = m.raza ?? "";
  f.edad.value = m.edad ?? "";
  f.sexo.value = m.sexo?.id ?? "";
  f.responsable.value = m.responsable ?? "";
  f.observaciones.value = m.observaciones ?? "";
  $("#actualizar-badge").textContent = `Editando a ${m.nombre} · ID ${m.id}`;
  formAct.hidden = false;
}

function editar(m) {
  irA("actualizar");
  $("#actualizar-id").value = m.id;
  llenarEdicion(m);
}

$("#form-buscar-actualizar").addEventListener("submit", async (e) => {
  e.preventDefault();
  const btn = $("[type=submit]", e.target);
  cargando(btn, true);
  try {
    await llenarEdicion(await buscarPorId($("#actualizar-id").value.trim()));
  } catch (err) { formAct.hidden = true; mostrarError(err); }
  cargando(btn, false);
});

formAct.addEventListener("submit", async (e) => {
  e.preventDefault();
  if (!validar(formAct)) return;
  const btn = $("[type=submit]", formAct);
  cargando(btn, true);
  try {
    const { mensaje } = await api(CONFIG.ENDPOINTS.actualizar, {
      method: "PUT",
      body: { id: idEnEdicion, ...leerForm(formAct) },
    });
    toast(mensaje, "ok");
    formAct.hidden = true;
    $("#actualizar-id").value = "";
    irA("listar"); // vuelve al listado y lo refresca
  } catch (err) { mostrarError(err); }
  cargando(btn, false);
});
$("#cancelar-actualizar").addEventListener("click", () => { formAct.hidden = true; });

/* ---------- ELIMINAR ---------- */
const preview = $("#eliminar-preview");
const dialogo = $("#confirmar");

function prepararEliminar(m) {
  irA("eliminar");
  $("#eliminar-id").value = m.id;
  mostrarPreview(m);
}

function mostrarPreview(m) {
  const btn = el("button", { className: "btn btn-danger", textContent: "Eliminar mascota" });
  btn.addEventListener("click", () => {
    $("#confirmar-texto").textContent = `Se eliminará a ${m.nombre} (ID ${m.id}). Esta acción no se puede deshacer.`;
    dialogo.returnValue = "";
    dialogo.showModal();
    dialogo.onclose = async () => {
      if (dialogo.returnValue !== "ok") return;
      cargando(btn, true);
      try {
        const { mensaje } = await api(`${CONFIG.ENDPOINTS.eliminar}/${encodeURIComponent(m.id)}`, { method: "DELETE" });
        toast(mensaje, "ok");
        preview.replaceChildren();
        $("#eliminar-id").value = "";
        irA("listar"); // vuelve al listado y lo refresca
      } catch (err) { mostrarError(err); cargando(btn, false); }
    };
  });
  preview.replaceChildren(tarjeta(m, { acciones: false, solo: true }), el("div", { className: "actions" }, btn));
}

$("#form-buscar-eliminar").addEventListener("submit", async (e) => {
  e.preventDefault();
  const btn = $("[type=submit]", e.target);
  cargando(btn, true);
  try {
    mostrarPreview(await buscarPorId($("#eliminar-id").value.trim()));
  } catch (err) { preview.replaceChildren(); mostrarError(err); }
  cargando(btn, false);
});

/* ---------- Inicio ---------- */
irA(location.hash.slice(1));
