document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('changeLangEng').addEventListener('click', function (event) {
        console.log("presseed")
        event.preventDefault();
        changeLanguage('en');
    });

    document.getElementById('changeLangPl').addEventListener('click', function (event) {
        event.preventDefault();
        changeLanguage('pl');
    });

    function changeLanguage(lang) {
        var currentUrl = new URL(window.location);
        currentUrl.searchParams.set('lang', lang);
        window.location.replace(currentUrl.toString());
    }
});