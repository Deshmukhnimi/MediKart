// auth.js - used on login.html and register.html
document.addEventListener('submit', async function(e){
  if (e.target && e.target.id === 'loginForm') {
    e.preventDefault();
    const fm = new FormData(e.target);
    const payload = { email: fm.get('email'), password: fm.get('password') };
    try {
      const res = await POST('/auth/login', payload);
      // res => { token, role }
      localStorage.setItem('jwt', res.token);
      // role may be "ADMIN" or "USER" (backend returns user.getCategory().name())
      // store with ROLE_ prefix to ease checks in pages
      if (res.role && (res.role.startsWith('ROLE_') === false)) localStorage.setItem('role', 'ROLE_' + res.role);
      else localStorage.setItem('role', res.role);
      // redirect
      const role = localStorage.getItem('role');
      if (role === 'ROLE_ADMIN') window.location.href = '/admin/users.html';
      else window.location.href = '/user/medicines.html';
    } catch (err) {
      const el = document.getElementById('loginError');
      el.style.display = 'block';
      el.textContent = err.message || 'Login failed';
    }
  }

  if (e.target && e.target.id === 'registerForm') {
    e.preventDefault();
    const fm = new FormData(e.target);
    const payload = {
      name: fm.get('name'),
      email: fm.get('email'),
      password: fm.get('password'),
      address: fm.get('address') || null,
      contactNo: fm.get('contactNo') || null,
      // by default frontend won't send category; backend decides first admin existence
    };
    try {
      await POST('/auth/register', payload);
      alert('Registration successful. Please login.');
      window.location.href = '/login.html';
    } catch (err) {
      const el = document.getElementById('regError');
      el.style.display = 'block';
      el.textContent = err.message || 'Registration failed';
    }
  }
});
