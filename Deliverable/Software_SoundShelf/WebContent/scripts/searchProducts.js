document.addEventListener('DOMContentLoaded', function () {
    const searchForm = document.getElementById('searchForm');
    searchForm.addEventListener('submit', searchProducts);
});

function searchProducts(event) {
    console.log('Funzione searchProducts chiamata');
    event.preventDefault();

    const name = document.getElementById('productName').value.trim();
    const artist = document.getElementById('productArtist').value.trim();
    const genre = document.getElementById('productGenre').value.trim();

    let url = '';

    if (name) url += `name=${encodeURIComponent(name)}&`;
    if (artist) url += `artist=${encodeURIComponent(artist)}&`;
    if (genre) url += `genre=${encodeURIComponent(genre)}&`;

    if (url.endsWith('&')) {
        url = url.slice(0, -1);
    }

    console.log('URL richiesto:', url);

    const xhr = new XMLHttpRequest();
    xhr.open('GET', "/SoundShelf/searchProducts?" + url, true);
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');

    xhr.onload = function () {
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText);
            displayResults(response);
        } else {
            document.getElementById('errorMessage').textContent = 'Errore nella ricerca dei prodotti.';
            document.getElementById('errorMessage').style.display = 'block';
        }
    };

    xhr.onerror = function () {
        document.getElementById('errorMessage').textContent = 'Errore nella ricerca dei prodotti.';
        document.getElementById('errorMessage').style.display = 'block';
    };

    xhr.send();
}

function displayResults(products) {
    const resultsContainer = document.getElementById('results');
    resultsContainer.innerHTML = '';  // Pulisce il contenitore dei risultati prima di aggiungere i nuovi

    if (products.length === 0) {
        resultsContainer.innerHTML = '<p>Nessun prodotto trovato.</p>';
    } else {
        products.forEach(product => {
            const productElement = document.createElement('div');
            productElement.className = 'grid-item product';

            productElement.innerHTML = `
                <div class="product-details">
                    <h3>${product.name}</h3>
                    <p><strong>Artista:</strong> ${Array.isArray(product.artists) ? product.artists.map(artist => artist.stageName || artist).join(', ') : product.artists}</p>
                    <p><strong>Genere:</strong> ${Array.isArray(product.genres) ? product.genres.map(genre => genre.name || genre).join(', ') : product.genres}</p>
                    <p><strong>Prezzo:</strong> €${product.salePrice}</p>
                    <p><strong>Prezzo Originale:</strong> €${product.originalPrice}</p>
                    <p><strong>Descrizione:</strong> ${product.description}</p>
                    <p><strong>Data di Pubblicazione:</strong> ${product.releaseDate}</p>
                    <p><strong>Disponibilità:</strong> ${product.availability}</p>
                    <form action="/SoundShelf/prodottoControl" method="get">
                        <input type="hidden" name="productId" value="${product.productCode}">
                        <button type="submit" class="button">Visualizza Prodotto</button>
                    </form>
                    <form action="/SoundShelf/carrelloControl" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.productCode}">
                        <button type="submit" class="button">Aggiungi al Carrello</button>
                    </form>
                </div>
            `;

            resultsContainer.appendChild(productElement);
        });
    }
}


