const API_BASE = window.API_BASE_URL || "http://localhost:8080/api";

function token() {
  return localStorage.getItem("jwt") || null;
}
function getRole() {
  return localStorage.getItem("role") || null;
}

async function apiRequest(path, method = 'GET', body = null) {
  const headers = { "Content-Type": "application/json" };
  const t = token();
  if (t) headers["Authorization"] = `Bearer ${t}`;

  try {
    const res = await fetch(API_BASE + path, {
      method,
      headers,
      body: body ? JSON.stringify(body) : null,
    });

    if (res.status === 401 || res.status === 403) {
      localStorage.removeItem("jwt");
      localStorage.removeItem("role");
      // Redirect to login page
      window.location.href = "/login.html";
      throw new Error("Unauthorized. Redirecting to login.");
    }

    if (res.status === 204) return null;

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(`Error ${res.status}: ${errorText}`);
    }

    const text = await res.text();
    try {
      return JSON.parse(text);
    } catch {
      return text;
    }
  } catch (err) {
    // Network error or thrown above
    if (err instanceof TypeError) {
      // Usually a network error
      throw new Error("Network error: Unable to reach server.");
    }
    throw err; // Re-throw other errors
  }
}

// Convenience wrappers
async function GET(path) { return apiRequest(path, 'GET'); }
async function POST(path, body) { return apiRequest(path, 'POST', body); }
async function PUT(path, body) { return apiRequest(path, 'PUT', body); }
async function DELETE(path) { return apiRequest(path, 'DELETE'); }
