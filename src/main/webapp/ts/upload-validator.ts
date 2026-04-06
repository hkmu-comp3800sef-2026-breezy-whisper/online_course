// Client-side file size validation with UI feedback (10MB)
document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('file') as HTMLInputElement;
    const fileError = document.getElementById('fileError') as HTMLDivElement;
    const submitBtn = document.querySelector('button[type="submit"]') as HTMLButtonElement;
    
    if (fileInput && fileError && submitBtn) {
        fileInput.addEventListener('change', function () {
            const maxSize: number = 10 * 1024 * 1024; // 10MB
            const files = this.files;
            if (files && files.length > 0) {
                const fileSize: number = files[0].size;
                const isTooLarge = fileSize > maxSize;
                
                if (isTooLarge) {
                    fileError.classList.remove('hidden');
                    fileError.textContent = 'File too large! Maximum size is 10MB.';
                    fileInput.value = '';
                    submitBtn.disabled = true;
                    alert('File too large! Maximum size is 10MB.');
                } else {
                    fileError.classList.add('hidden');
                    submitBtn.disabled = false;
                }
            } else {
                fileError.classList.add('hidden');
                submitBtn.disabled = true;
            }
        });
        
        // Disable submit initially if no file
        submitBtn.disabled = true;
    }
});

