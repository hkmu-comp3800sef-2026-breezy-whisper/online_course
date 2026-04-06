"use strict";
// Client-side file size validation with UI feedback (10MB)
document.addEventListener('DOMContentLoaded', function () {
    const fileInput = document.getElementById('file');
    const fileError = document.getElementById('fileError');
    const submitBtn = document.querySelector('button[type="submit"]');
    if (fileInput && fileError && submitBtn) {
        fileInput.addEventListener('change', function () {
            const maxSize = 10 * 1024 * 1024; // 10MB
            const files = this.files;
            if (files && files.length > 0) {
                const fileSize = files[0].size;
                const isTooLarge = fileSize > maxSize;
                if (isTooLarge) {
                    fileError.classList.remove('hidden');
                    fileError.textContent = 'File too large! Maximum size is 10MB.';
                    fileInput.value = '';
                    submitBtn.disabled = true;
                    alert('File too large! Maximum size is 10MB.');
                }
                else {
                    fileError.classList.add('hidden');
                    submitBtn.disabled = false;
                }
            }
            else {
                fileError.classList.add('hidden');
                submitBtn.disabled = true;
            }
        });
        // Disable submit initially if no file
        submitBtn.disabled = true;
    }
});
//# sourceMappingURL=upload-validator.js.map