document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const loginForm = document.getElementById('loginForm');
    const propertyForm = document.getElementById('propertyForm');
    const propertiesDiv = document.getElementById('properties');
    const paginationDiv = document.getElementById('pagination');

    let currentPage = 1;
    const pageSize = 10;

    function validateForm(form) {
        return [...form.elements].every(input => input.checkValidity());
    }

    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!validateForm(registerForm)) return alert('Please fill all required fields');

        const formData = new FormData(registerForm);
        const data = Object.fromEntries(formData);

        const response = await fetch('/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) alert('Registration successful');
        else alert('Registration failed');
    });

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!validateForm(loginForm)) return alert('Please fill all required fields');

        const formData = new FormData(loginForm);
        const data = Object.fromEntries(formData);

        const response = await fetch('/api/login', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) alert('Login successful');
        else alert('Login failed');
    });

    propertyForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        if (!validateForm(propertyForm)) return alert('Please fill all required fields');

        const formData = new FormData(propertyForm);
        const data = Object.fromEntries(formData);

        const response = await fetch('/api/postProperty', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) alert('Property posted successfully');
        else alert('Failed to post property');
    });

    async function loadProperties(page = 1) {
        const response = await fetch(`/api/properties?page=${page}&size=${pageSize}`);
        const result = await response.json();

        propertiesDiv.innerHTML = '';
        result.content.forEach(property => {
            const propertyDiv = document.createElement('div');
            propertyDiv.className = 'property';
            propertyDiv.innerHTML = `
                <h3>${property.location}</h3>
                <p>Area: ${property.area} sq ft</p>
                <p>Bedrooms: ${property.bedrooms}</p>
                <p>Bathrooms: ${property.bathrooms}</p>
                <p>Nearby: ${property.nearby}</p>
                <button class="like-btn" data-id="${property.id}">Like (${property.likes})</button>
                <button class="interest-btn" data-id="${property.id}">I'm Interested</button>
            `;
            propertiesDiv.appendChild(propertyDiv);
        });

        document.querySelectorAll('.like-btn').forEach(button => {
            button.addEventListener('click', async (e) => {
                const propertyId = e.target.getAttribute('data-id');
                const response = await fetch(`/api/properties/${propertyId}/like`, { method: 'POST' });
                if (response.ok) {
                    const updatedProperty = await response.json();
                    e.target.innerText = `Like (${updatedProperty.likes})`;
                }
            });
        });

        paginationDiv.innerHTML = '';
        for (let i = 1; i <= result.totalPages; i++) {
            const btn = document.createElement('button');
            btn.className = 'pagination-btn';
            btn.innerText = i;
            btn.addEventListener('click', () => loadProperties(i));
            paginationDiv.appendChild(btn);
        }
    }

    loadProperties();
});
