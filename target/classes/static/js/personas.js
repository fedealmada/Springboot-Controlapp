document.addEventListener("DOMContentLoaded", function () {
  cargarPersonas();

  document.getElementById("formNuevaPersona").addEventListener("submit", function (e) {
    e.preventDefault();
    const persona = {
      nombre: document.getElementById("nombre").value,
      apellido: document.getElementById("apellido").value,
      empresa: document.getElementById("empresa").value,
    };

    fetch("/api/personas", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(persona),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Error al crear");
        return res.json();
      })
      .then(() => {
        mostrarToast("Persona creada correctamente");
        document.getElementById("formNuevaPersona").reset();
        bootstrap.Modal.getInstance(document.getElementById("modalNuevaPersona")).hide();
        cargarPersonas();
      })
      .catch(() => mostrarToast("Error al crear persona", true));
  });
});
function cargarPersonas() {
  fetch("/api/personas")
    .then((res) => res.json())
    .then((data) => {
      const tbody = document.getElementById("tablaPersonas");
      tbody.innerHTML = "";
      data.forEach((p) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${p.id}</td>
          <td>${p.nombre}</td>
          <td>${p.apellido}</td>
          <td>${p.empresa}</td>
          <td>
            <button
              class="btn btn-sm btn-info text-white me-1"
              onclick="verPersona(${p.id})"
              title="Ver"
            >
              <i class="bi bi-eye-fill"></i>
            </button>
            <button
              class="btn btn-sm btn-primary me-1"
              onclick="abrirModalEditar(${p.id})"
              title="Editar"
            >
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button
              class="btn btn-sm btn-danger"
              onclick="eliminarPersona(${p.id})"
              title="Eliminar"
            >
              <i class="bi bi-trash-fill"></i>
            </button>
          </td>
        `;
        tbody.appendChild(tr);
      });
    });
}
function eliminarPersona(id) {
  if (!confirm("¿Estás seguro de eliminar esta persona?")) return;

  fetch("/api/personas/" + id, { method: "DELETE" })
    .then((res) => {
      if (!res.ok) throw new Error();
      mostrarToast("Persona eliminada correctamente");
      cargarPersonas();
    })
    .catch(() => mostrarToast("Error al eliminar persona", true));
}

function mostrarToast(mensaje, esError = false) {
  const toastEl = document.getElementById("toastMensaje");
  const toastTexto = document.getElementById("toastMensajeTexto");

  toastTexto.textContent = mensaje;
  toastEl.classList.remove("bg-success", "bg-danger");
  toastEl.classList.add(esError ? "bg-danger" : "bg-success");

  const toast = new bootstrap.Toast(toastEl);
  toast.show();
}

function verPersona(id) {
  fetch("/api/personas/" + id)
    .then(res => res.json())
    .then(data => {
      document.getElementById("verNombre").textContent = data.nombre;
      document.getElementById("verApellido").textContent = data.apellido;
      document.getElementById("verEmpresa").textContent = data.empresa;
      const modal = new bootstrap.Modal(document.getElementById("modalVerPersona"));
      modal.show();
    })
    .catch(() => mostrarToast("Error al cargar persona", true));
}

function abrirModalEditar(id) {
  fetch("/api/personas/" + id)
    .then(res => res.json())
    .then(data => {
      document.getElementById("editarId").value = data.id;
      document.getElementById("editarNombre").value = data.nombre;
      document.getElementById("editarApellido").value = data.apellido;
      document.getElementById("editarEmpresa").value = data.empresa;
      const modal = new bootstrap.Modal(document.getElementById("modalEditarPersona"));
      modal.show();
    })
    .catch(() => mostrarToast("Error al cargar persona", true));
}