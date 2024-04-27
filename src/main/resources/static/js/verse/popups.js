document.addEventListener('DOMContentLoaded', function () {
    var closeButton = document.getElementById('closeButton');
    if (closeButton) {
        closeButton.addEventListener('click', closePopup);
    }
});

function closePopup() {
    document.getElementById('popupForm').style.display = 'none';
}

window.onclick = function (event) {
    const popupForm = document.getElementById('popupForm');
    if (event.target === popupForm) {
        popupForm.style.display = "none";
    }
}
