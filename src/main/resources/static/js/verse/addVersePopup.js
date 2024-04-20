document.addEventListener('DOMContentLoaded', function () {
    var closeButton = document.getElementById('closeButton');
    if (closeButton) {
        closeButton.addEventListener('click', closePopup);
    }

    var submitButton = document.getElementById('submitButton');
    if (submitButton) {
        submitButton.addEventListener('click', function (event) {
            event.preventDefault();
            submitForm();
        });
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

function submitForm() {
    const form = document.getElementById('verseForm');
    const formData = {
        title: form.title.value,
        shortDescription: form.shortDescription.value,
        text: form.text.value,
        imageUrl: form.imageUrl.value
    };
    console.log(formData)

    fetch('/rest/api/verse', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    }).then(() => {
        location.reload()
        form.reset();
    })
        .catch((error) => {
            console.error('Error:', error);
            form.reset();
        });

    closePopup();
}