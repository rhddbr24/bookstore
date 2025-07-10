document.addEventListener('DOMContentLoaded', function() {
    const navLinks = document.querySelectorAll('nav a');
    navLinks.forEach(function(link) {
        link.addEventListener('click', function() {
            console.log('메뉴 클릭:', this.textContent);
        });
    });
}); 