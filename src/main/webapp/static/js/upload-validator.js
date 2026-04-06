// Client-side file size validation (10MB = 10 * 1024 * 1024 bytes)
document.addEventListener('DOMContentLoaded', function() {
    const fileInput = document.getElementById('file');
    const submitBtn = document.querySelector('button[type="submit"]');
    
    if (fileInput) {
        fileInput.addEventListener('change', function() {
            const maxSize = 10 * 1024 * 1024; // 10MB
            if (this.files.length > 0 && this.files[0].size > maxSize) {
                alert('File too large! Maximum size is 10MB.');
                this.value = '';
                submitBtn.disabled = true;
            } else {
                submitBtn.disabled = false;
            }
        });
        
        // Disable submit initially if no file
        if (!fileInput.files || fileInput.files.length === 0) {
            submitBtn.disabled = true;
        }
    }
});
