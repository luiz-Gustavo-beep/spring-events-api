const loginSection = document.getElementById('login-section');
const registerSection = document.getElementById('register-section');
const userDashboard = document.getElementById('user-dashboard');
const adminDashboard = document.getElementById('admin-dashboard');
const loginForm = document.getElementById('login-form');
const usuarioForm = document.getElementById('usuario-form');
const eventoForm = document.getElementById('evento-form');
const adminUserForm = document.getElementById('admin-user-form');
const inscricaoEventoSelect = document.getElementById('inscricao-evento');
const inscricoesList = document.getElementById('inscricoes-list');
const mensagens = document.getElementById('mensagens');
const userNameDisplay = document.getElementById('user-name-display');
const logoutButton = document.getElementById('logout-button');
const logoutButtonAdmin = document.getElementById('logout-button-admin');

let usuarioAtual = null;

async function requestJson(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            'Content-Type': 'application/json'
        },
        ...options
    });

    const body = await response.json().catch(() => null);
    if (!response.ok) {
        const error = body?.message || body?.error || response.statusText;
        throw new Error(error || 'Falha na requisição');
    }
    return body;
}

function showMessage(text, type = 'success') {
    mensagens.innerHTML = `<div class="${type}">${text}</div>`;
}

function resetView() {
    loginSection.classList.remove('hidden');
    registerSection.classList.remove('hidden');
    userDashboard.classList.add('hidden');
    adminDashboard.classList.add('hidden');
    usuarioAtual = null;
    userNameDisplay.textContent = '';
    clearEventSelect();
    clearInscricoesList();
}

function clearEventSelect() {
    inscricaoEventoSelect.innerHTML = '';
}

function clearInscricoesList() {
    inscricoesList.innerHTML = '';
}

async function carregarEventosDisponiveis(usuarioId) {
    const eventos = await requestJson(`/api/eventos/disponiveis?usuarioId=${usuarioId}`);
    inscricaoEventoSelect.innerHTML = '';

    eventos.forEach(evento => {
        const option = document.createElement('option');
        option.value = evento.id;
        option.textContent = `${evento.titulo} - ${evento.local}`;
        inscricaoEventoSelect.append(option);
    });
}

async function carregarInscricoes(usuarioId) {
    const inscricoes = await requestJson(`/api/inscricoes/usuario/${usuarioId}`);
    inscricoesList.innerHTML = '';

    inscricoes.forEach(inscricao => {
        const evento = inscricao.evento;
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${evento.id}</td>
            <td>${evento.titulo}</td>
            <td>${evento.local}</td>
            <td>${evento.dataInicio ? evento.dataInicio.replace('T', ' ') : ''}</td>
            <td>${evento.dataFim ? evento.dataFim.replace('T', ' ') : ''}</td>
        `;
        inscricoesList.append(row);
    });
}

async function abrirDashboard(usuario) {
    usuarioAtual = usuario;
    loginSection.classList.add('hidden');
    registerSection.classList.add('hidden');
    if (usuario.perfil === 'ADMIN') {
        adminDashboard.classList.remove('hidden');
        userDashboard.classList.add('hidden');
    } else {
        userDashboard.classList.remove('hidden');
        adminDashboard.classList.add('hidden');
        await carregarEventosDisponiveis(usuario.id);
        await carregarInscricoes(usuario.id);
    }
    userNameDisplay.textContent = usuario.nome;
}

loginForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const email = document.getElementById('login-email').value.trim();
    const senha = document.getElementById('login-senha').value.trim();

    try {
        const usuario = await requestJson('/api/usuarios/login', {
            method: 'POST',
            body: JSON.stringify({ email, senha })
        });
        showMessage('Login realizado com sucesso.', 'success');
        await abrirDashboard(usuario);
        loginForm.reset();
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

usuarioForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const usuario = {
        nome: document.getElementById('usuario-nome').value.trim(),
        email: document.getElementById('usuario-email').value.trim(),
        senha: document.getElementById('usuario-senha').value.trim(),
        dataNascimento: document.getElementById('usuario-data-nascimento').value || null
    };

    try {
        await requestJson('/api/usuarios', {
            method: 'POST',
            body: JSON.stringify(usuario)
        });
        showMessage('Usuário cadastrado com sucesso. Faça login para continuar.', 'success');
        usuarioForm.reset();
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

eventoForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const evento = {
        titulo: document.getElementById('evento-titulo').value.trim(),
        descricao: document.getElementById('evento-descricao').value.trim(),
        local: document.getElementById('evento-local').value.trim(),
        dataInicio: document.getElementById('evento-data-inicio').value,
        dataFim: document.getElementById('evento-data-fim').value,
        vagas: Number(document.getElementById('evento-vagas').value)
    };

    try {
        await requestJson('/api/eventos', {
            method: 'POST',
            body: JSON.stringify(evento)
        });
        showMessage('Evento cadastrado com sucesso.', 'success');
        eventoForm.reset();
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

adminUserForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const usuario = {
        nome: document.getElementById('admin-usuario-nome').value.trim(),
        email: document.getElementById('admin-usuario-email').value.trim(),
        senha: document.getElementById('admin-usuario-senha').value.trim(),
        perfil: document.getElementById('admin-usuario-perfil').value
    };

    try {
        await requestJson('/api/usuarios/admin', {
            method: 'POST',
            body: JSON.stringify(usuario)
        });
        showMessage('Usuário criado com sucesso.', 'success');
        adminUserForm.reset();
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

document.getElementById('inscrever-button').addEventListener('click', async () => {
    if (!usuarioAtual) {
        showMessage('Faça login para se inscrever em eventos.', 'error');
        return;
    }

    const eventoId = inscricaoEventoSelect.value;
    if (!eventoId) {
        showMessage('Selecione um evento para se inscrever.', 'error');
        return;
    }

    try {
        await requestJson('/api/inscricoes', {
            method: 'POST',
            body: JSON.stringify({ usuarioId: usuarioAtual.id, eventoId: Number(eventoId) })
        });
        showMessage('Inscrição realizada com sucesso.', 'success');
        await carregarEventosDisponiveis(usuarioAtual.id);
        await carregarInscricoes(usuarioAtual.id);
    } catch (error) {
        showMessage(error.message, 'error');
    }
});

logoutButton.addEventListener('click', () => resetView());
logoutButtonAdmin.addEventListener('click', () => resetView());

window.addEventListener('load', () => {
    resetView();
});
