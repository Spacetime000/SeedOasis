let url = new URL(location.href);

//페이지 이동1
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

//페이지 이동 prev, next
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

//삭제
function del() {

    let id = [];

    document.querySelectorAll(".ck").forEach(e => {
        if (e.checked)
            id.push(e.value);
    });

    fetch('/admin/comments', {
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