// utils.js
function requireAuth(allowedRoles = []) {
  const t = localStorage.getItem('jwt');
  const role = localStorage.getItem('role');
  if (!t) { window.location.href = '/login.html'; return false; }
  if (allowedRoles.length && !allowedRoles.includes(role)) {
    alert('Access denied');
    window.location.href = '/login.html';
    return false;
  }
  return true;
}

function logout() {
  localStorage.removeItem('jwt');
  localStorage.removeItem('role');
  window.location.href = '/login.html';
}

function el(id){ return document.getElementById(id); }
function fmtDate(dstr){
  if(!dstr) return '';
  const d = new Date(dstr);
  return d.toLocaleDateString();
}
