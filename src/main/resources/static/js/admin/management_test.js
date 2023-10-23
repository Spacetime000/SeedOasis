let url = new URL(location.href);
let category = document.getElementById("category");

const pageInput = document.getElementById("page-input");
pageInput.addEventListener("keyup", e => {
    if (e.key === "Enter") {
        let pageNumber = Number(e.target.value);
        let lastPage = Number(e.target.getAttribute('data-last-page'));

        if (pageNumber <= 0 || pageNumber > lastPage) {
            alert(`1페이지부터 ${lastPage}페이지까지 가능합니다.`);
            return;
        }

        url.searchParams.set("page", pageNumber-1);
        location.href = url.href;
    }
});

let prev = document.getElementById("prev");
prev.addEventListener("click", () => {
    if (prev.classList.contains('disable')) {
        alert('첫 페이지입니다.');
        return;
    }

    let prevPage = prev.getAttribute('data-prev');
    url.searchParams.set('page', prevPage);
    location.href = url.href;
});

let next = document.getElementById("next");
next.addEventListener("click", () => {
    if (next.classList.contains('disable')) {
        alert('마지막 페이지입니다.')
        return;
    }
    let nextPage = next.getAttribute("data-next");
    url.searchParams.set("page", nextPage);
    location.href = url.href;
});

//검색
const search = document.getElementById("search-btn");
const input = document.getElementById("search-input");
search.addEventListener("click", () => {
    let searchBy = document.getElementById("searchBy").value;
    let params = new URLSearchParams();
    params.set("category", category.value);
    params.set("query", input.value);
    params.set("searchBy", searchBy);
    href(params);
});

//정렬
document.querySelectorAll(".sort").forEach(e => {
    e.addEventListener("click", t => {
        let currentTarget = t.currentTarget;
        let target = currentTarget.querySelector("span");
        let icon = target.querySelector("ion-icon");
        let params = new URLSearchParams();
        let query = url.searchParams.get("query");
        let searchBy = url.searchParams.get("searchBy");
        params.set("category", category.value);
        

        if (query) {
            params.set("searchBy", searchBy);
            params.set("query", query);
        }

        //1.active가 있으면, 내림차순으로
        //1.1 active가 있으나 이미 내림차순일 경우
        //2.active가 없으면 active가 생기고 오름차순

        if (target.classList.contains("active")) {
            if (icon.getAttribute("name") === 'caret-up-outline') { //1.
                params.set("direction", "DESC");
                params.set("sort", icon.getAttribute("data-id"));
                href(params);
            } else { //1.1
                url.searchParams.set("direction", "ASC");
                location.href = url.href;
            }

        } else { //2.
            params.set("sort", icon.getAttribute("data-id"));
            href(params);
        }
    });
});

//카테고리
category.addEventListener("change", () => {
    url.searchParams.delete("page");
    url.searchParams.set("category", category.value);
    location.href = url.href;
    // let params = new URLSearchParams();
    // params.set("category", category.value);
    // params.set("query", document.getElementById("search-input").value);
    // location.href = url.origin + url.pathname + '?' + params.toString();
});

//삭제
function del() {

    let id = [];

    document.querySelectorAll(".ck").forEach(e => {
        if (e.checked)
            id.push(e.value);
    });

    fetch('/admin/tests', {
        method: "POST",
        headers: {
            "content-Type" : "application/json",
        },
        body: JSON.stringify(id)
    })
    .then(response => {
        if (response.ok)
            location.reload();
    });
}

function href(params) {
    location.href = url.origin + url.pathname + '?' + params.toString();
}