document.addEventListener('DOMContentLoaded', () => {
  const mobileBtn = document.getElementById('mobile-menu-btn') as HTMLButtonElement;
  const mobileMenu = document.getElementById('mobile-menu') as HTMLDivElement;
  
  if (mobileBtn && mobileMenu) {
    mobileBtn.addEventListener('click', () => {
      mobileMenu.classList.toggle('hidden');
    });
    
    // Click outside to close
    document.addEventListener('click', (e) => {
      if (!mobileBtn.contains(e.target as Node) && !mobileMenu.contains(e.target as Node)) {
        mobileMenu.classList.add('hidden');
      }
    });
    
    window.addEventListener('resize', () => {
      if (window.innerWidth >= 768) {
        mobileMenu.classList.add('hidden');
      }
    });
  }
});
