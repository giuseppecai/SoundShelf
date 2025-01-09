document.addEventListener("DOMContentLoaded", function() {
    // Funzione per validare email
    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // Funzione per validare URL immagine
    function validateImageURL(url) {
        const urlRegex = /\.(jpeg|jpg|gif|png|bmp)$/i;
        return urlRegex.test(url);
    }

    // Funzione per validare liste separate da virgole
    function validateCommaSeparated(input) {
        return input.split(',').every(item => item.trim() !== "");
    }

    // Validazione form di login
    const loginForm = document.querySelector(".login-form");
    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();

            if (!email || !validateEmail(email)) {
                alert("Inserisci un'email valida.");
                event.preventDefault();
                return;
            }

            if (!password || password.length < 6) {
                alert("La password deve avere almeno 6 caratteri.");
                event.preventDefault();
            }
        });
    }

    // Validazione form di registrazione
    const registerForm = document.querySelector(".register-form");
    if (registerForm) {
        registerForm.addEventListener("submit", function(event) {
            const email = document.getElementById("email").value.trim();
            const nome = document.getElementById("nome").value.trim();
            const cognome = document.getElementById("cognome").value.trim();
            const password = document.getElementById("password").value.trim();
            const telefono = document.getElementById("telefono").value.trim();

            if (!email || !validateEmail(email)) {
                alert("Inserisci un'email valida.");
                event.preventDefault();
                return;
            }

            if (!nome || nome.length < 2) {
                alert("Il nome deve contenere almeno 2 caratteri.");
                event.preventDefault();
                return;
            }

            if (!cognome || cognome.length < 2) {
                alert("Il cognome deve contenere almeno 2 caratteri.");
                event.preventDefault();
                return;
            }

            if (!password || password.length < 6) {
                alert("La password deve avere almeno 6 caratteri.");
                event.preventDefault();
                return;
            }

            if (!telefono || !/^\d+$/.test(telefono)) {
                alert("Inserisci un numero di telefono valido.");
                event.preventDefault();
            }
        });
    }

    // Validazione form di aggiunta/modifica prodotto
    function validateProductForm(event) {
        const name = document.getElementById("name").value.trim();
        const description = document.getElementById("description").value.trim();
        const salePrice = document.getElementById("salePrice").value.trim();
        const originalPrice = document.getElementById("originalPrice").value.trim();
        const availability = document.getElementById("availability").value.trim();
        const releaseDate = document.getElementById("releaseDate").value.trim();
        const image = document.getElementById("image").value.trim();
        const supportedDevice = document.getElementById("supportedDevice").value.trim();
        const artists = document.getElementById("artists").value.trim();
        const genres = document.getElementById("genres").value.trim();

        if (!name) {
            alert("Il nome del prodotto è obbligatorio.");
            event.preventDefault();
            return;
        }
        if (!description) {
            alert("La descrizione è obbligatoria.");
            event.preventDefault();
            return;
        }
        if (!salePrice || isNaN(salePrice) || salePrice <= 0) {
            alert("Inserisci un prezzo di vendita valido maggiore di 0.");
            event.preventDefault();
            return;
        }
        if (!originalPrice || isNaN(originalPrice) || originalPrice <= 0) {
            alert("Inserisci un prezzo originale valido maggiore di 0.");
            event.preventDefault();
            return;
        }
        if (!availability || isNaN(availability) || availability < 0) {
            alert("La disponibilità deve essere un numero valido maggiore o uguale a 0.");
            event.preventDefault();
            return;
        }
        if (!releaseDate) {
            alert("La data di rilascio è obbligatoria.");
            event.preventDefault();
            return;
        }
        if (!validateImageURL(image)) {
            alert("Inserisci un URL immagine valido.");
            event.preventDefault();
            return;
        }
        if (!supportedDevice) {
            alert("Il formato supportato è obbligatorio.");
            event.preventDefault();
            return;
        }
        if (!validateCommaSeparated(artists)) {
            alert("Inserisci almeno un artista, separato da virgole se sono più di uno.");
            event.preventDefault();
            return;
        }
        if (!validateCommaSeparated(genres)) {
            alert("Inserisci almeno un genere, separato da virgole se sono più di uno.");
            event.preventDefault();
        }
    }

    const addProductForm = document.querySelector("form[action$='InserisciNuovoProdottoControl']");
    if (addProductForm) {
        addProductForm.addEventListener("submit", validateProductForm);
    }

    const editProductForm = document.querySelector("form[action*='ModificaProdottoControl']");
    if (editProductForm) {
        editProductForm.addEventListener("submit", validateProductForm);
    }

    // Validazione form di checkout
    const checkoutForm = document.querySelector(".checkout-form");
    if (checkoutForm) {
        checkoutForm.addEventListener("submit", function(event) {
            const shippingAddress = document.querySelector("textarea[name='shippingAddress']").value.trim();

            if (!shippingAddress || shippingAddress.length < 10) {
                alert("L'indirizzo di spedizione deve contenere almeno 10 caratteri.");
                event.preventDefault();
            }
        });
    }
});
