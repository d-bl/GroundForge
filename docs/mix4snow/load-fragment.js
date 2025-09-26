fetch('fragment.html')
    .then(response => response.text())
    .then(html => {
        document.getElementById('targetDiv').innerHTML = html;
    })
    .catch(err => console.error('Failed to load fragment:', err));
