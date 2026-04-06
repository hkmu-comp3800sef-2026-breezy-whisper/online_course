// Admin users modal functionality
function closeModal(modalId: string): void {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('hidden');
    }
}

// Confirm delete function for user table
function confirmDelete(username: string): boolean {
    return confirm(`Are you sure you want to delete user "${username}"? This action cannot be undone.`);
}


// Open modal when Add button clicked
document.addEventListener('DOMContentLoaded', () => {
    const addUserBtn = document.querySelector('button[onclick*="addUserModal"]') as HTMLButtonElement;
    if (addUserBtn) {
        addUserBtn.onclick = () => {
            const modal = document.getElementById('addUserModal');
            if (modal) modal.classList.remove('hidden');
        };
    }
});

// Close modal on outside click
document.addEventListener('click', (e) => {
    if ((e.target as HTMLElement).id === 'addUserModal') {
        closeModal('addUserModal');
    }
});

