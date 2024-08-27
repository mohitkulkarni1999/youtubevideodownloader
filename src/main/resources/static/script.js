document.getElementById('download-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const url = document.getElementById('url').value;
    const quality = document.getElementById('quality').value;

    window.location.href = `/download?url=${encodeURIComponent(url)}&quality=${quality}`;
});
