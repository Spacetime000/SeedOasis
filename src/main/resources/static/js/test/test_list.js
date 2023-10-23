//검색
document.getElementById("btn-search").addEventListener("click", () => {
    const query = document.getElementById("input-search").value.trim();
    const category = document.getElementById("search-category").value;
    const option = document.getElementById("search-option").value;

    location.href = "/tests?category=" + category + "&option=" + option + "&query=" + query;
});

//모달
const overlay = document.getElementById('overlay')
const modal = document.getElementById("modal");

document.querySelector(".close-button").addEventListener("click", () => {
    closeModal(modal);
});

overlay.addEventListener("click", () => {
    closeModal(modal);
});

if (document.querySelector(".btn-modal")) {
    document.querySelector(".btn-modal").addEventListener("click", () => {
        openModal(modal);
    });
}

function openModal(m) {
    if (modal == null) return;
    m.classList.add("active");
    overlay.classList.add("active");
}

function closeModal(m) {
    if (modal == null) return;
    m.classList.remove("active");
    overlay.classList.remove("active");
}

//페이지 이동
const currentUrl = new URL(location.href);
document.querySelectorAll(".link").forEach((e) => {
    e.addEventListener("click", (t) => {
        currentUrl.searchParams.set("page", t.target.getAttribute('data-page') -1);
        location.href=currentUrl.toString();
    })
});

document.getElementById("prev").addEventListener("click", (e) => {
    const prev = e.target.getAttribute('data-prev');

    if (prev != -1) {
        currentUrl.searchParams.set("page", prev);
        location.href=currentUrl.toString();
    }

});

document.getElementById("next").addEventListener("click", (e) => {
    const next = e.target.getAttribute('data-next');
    const maxNext = e.target.getAttribute('data-max');

    if (next != maxNext) {
        currentUrl.searchParams.set("page", next);
        location.href=currentUrl.toString();
    }
});
