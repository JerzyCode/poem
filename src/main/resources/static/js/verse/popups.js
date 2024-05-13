document.addEventListener('DOMContentLoaded', function () {
    const closeButton = document.getElementById('closeButton');
    const deleteCloseButton = document.getElementById('closeDeletePopupButton');

    if (closeButton) {
        closeButton.addEventListener('click', () => closePopup(document.getElementById('popupForm')));
    }

    if (deleteCloseButton) {
        deleteCloseButton.addEventListener('click', () => closePopup(document.getElementById('deletePopupForm')));
    }
});

function closePopup(popup) {
    popup.style.display = 'none'
}

window.onclick = function (event) {
    const popupForms = document.getElementsByClassName('popup-overlay');
    for (let popup of popupForms)
        if (event.target === popup) {
            popup.style.display = "none";
        }
}
