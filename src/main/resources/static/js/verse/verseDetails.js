document.getElementById('delete-verse-button').addEventListener('click', function () {
    document.getElementById('popupForm').style.display = 'block';

    document.getElementById('confirmDeleteButton').addEventListener("click", function () {
        const verseId = this.getAttribute('data-verse-id');
        const userId = this.getAttribute('data-user-id');
        deleteVerse(verseId, userId)
    })
});

function deleteVerse(verseId, userId) {
    document.getElementById('popupForm').style.display = 'none';

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