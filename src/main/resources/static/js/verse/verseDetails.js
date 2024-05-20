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

    const likeButton = document.getElementById('like-button');
    if (likeButton) {
        const verseId = document.querySelector('[verse-id]').getAttribute('verse-id');
        let isLikedAtt = document.querySelector('[data-is-liked]').getAttribute('data-is-liked')
        switchLikeButton(isLikedAtt)

        likeButton.addEventListener("click", () => likeOrUnlikeVerse(verseId))
    }

});

document.getElementById('edit-verse-button').addEventListener('click', function () {
    const userLang = navigator.language.split('-')[0];
    let text = 'Edit Verse'
    if (userLang === 'pl') {
        text = 'Edytuj Wiersz'
    }
    document.getElementById('popupForm').style.display = 'block';
    document.getElementById('versePopupTitle').textContent = text;
});


function deleteVerse(verseId, userId) {
    document.getElementById('deletePopupForm').style.display = 'none';
    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');

    fetch(`/rest/api/verse?verseId=${verseId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
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

function likeOrUnlikeVerse(verseId) {
    const isLikedByUser = document.querySelector('[data-is-liked]').getAttribute('data-is-liked');
    const csrfToken = document.querySelector('meta[name="csrf-token"]').getAttribute('content');

    fetch(`/rest/api/like?verseId=${verseId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        }
    }).then(response => {
        if (response.ok) {
            switchLikeButton(isLikedByUser)
            switchLikesCounter(isLikedByUser)
        }
    }).catch(error => console.error('Error:', error));
}

function switchLikeButton(isCurrentLiked) {
    let button = document.getElementById('like-button');
    let isLikedAtt = document.querySelector('[data-is-liked]')

    if (isCurrentLiked === 'true' || isCurrentLiked === true) {
        button.style.background = '#666666'
        button.textContent = 'Unlike'
        isLikedAtt.setAttribute('data-is-liked', 'false');

    } else {
        button.style.background = '#363062'
        button.textContent = 'Like'
        isLikedAtt.setAttribute('data-is-liked', 'true');
    }
}

function switchLikesCounter(isCurrentLiked) {
    let likesParagraph = document.getElementById('likes-paragraph');

    if (isCurrentLiked === 'true' || isCurrentLiked === true) {
        likesParagraph.textContent = `${likesParagraph.textContent.split(' ')[0]} ${parseInt(likesParagraph.textContent.split(' ')[1]) + 1}`;
    } else {
        likesParagraph.textContent = `${likesParagraph.textContent.split(' ')[0]} ${parseInt(likesParagraph.textContent.split(' ')[1]) - 1}`;
    }
}
