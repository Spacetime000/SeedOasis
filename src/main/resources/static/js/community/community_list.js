//검색
document.getElementById("btn-search").addEventListener("click", () => {
    const query = document.getElementById("query").value;
    const category = document.getElementById("category").value;
    const searchBy = document.getElementById("search").value;

    location.href = "/community?category=" + category + "&searchBy=" + searchBy + "&query=" + query;
});


//페이지
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