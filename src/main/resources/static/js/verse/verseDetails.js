document.addEventListener('DOMContentLoaded', function () {
    const deleteButton = document.getElementById('delete-verse-button');
    if (deleteButton) {
        deleteButton.addEventListener('click', function () {
            document.getElementById('deletePopupForm').style.display = 'block';

            document.getElementById('confirmDeleteButton').addEventListener("click", function () {
                const verseId = this.getAttribute('data-verse-id');
                const userId = this.getAttribute('data-user-id');
                deleteVerse(verseId, userId)
            })
        });
    }

});

document.getElementById('edit-verse-button').addEventListener('click', function () {
    document.getElementById('popupForm').style.display = 'block';
    document.getElementById('versePopupTitle').textContent = 'Edit Verse'
});


function deleteVerse(verseId, userId) {
    document.getElementById('deletePopupForm').style.display = 'none';

    fetch(`/rest/api/verse?verseId=${verseId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                console.log('Verse deleted successfully');
                location.href = '/verses/' + userId;
            } else {
                console.error('Failed to delete verse');
            }
        })
        .catch(error => console.error('Error:', error));
}