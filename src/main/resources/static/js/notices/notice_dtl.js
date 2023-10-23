try {
    document.getElementById("btn-del").addEventListener("click", (e) => {
        console.log(e.target.getAttribute("data-id"));
        
        if (confirm("삭제하시겠습니까?")) {
            fetch("/notice/" + e.target.getAttribute("data-id"), {
                method : "DELETE",
            })
            .then((response) => response.json())
            .then((data) => {
                alert(data.message)
                location.replace("/notice");
            })
        }
    });
} catch(error) {
    console.log(error);
}

document.getElementById("btn-list").addEventListener("click", () => {

    if (document.referrer === '')
        location.href = "/notice";
        
    const backUrl = new URL(document.referrer);
    const currentUrl = new URL(location.href);
    const urlPage = backUrl.searchParams.get("page");
    const searchType = backUrl.searchParams.get("search_type");
    const searchQuery = backUrl.searchParams.get("search_query");
    const listPage = new URL(currentUrl.origin + "/notice");
        
    //이전 페이지 주소는 다르지만 page가 있다면?
    //상세페이지의 hostname과 이전 페이지 hostname 비교

    if (currentUrl.hostname === backUrl.hostname) {
        if (urlPage != null) {
            listPage.searchParams.set("page", urlPage);
        }
        
        if (searchType != null && searchQuery !=null) {
            listPage.searchParams.set("search_type", searchType);
            listPage.searchParams.set("search_query", searchQuery.trim());
        }

        location.href = listPage.href;
    } else {
        location.href = "/notice";
    }


    // 
    // if (currentUrl.hostname === backUrl.hostname) {
    //     if (urlPage === null) {
            
    //         result += "?page=0";
    //     } else {
    //         result = result.concat("?page=" + urlPage);
    //     }
    // } else {
    //     location.href = "/notice";
    // }
    // let result = "/notice";

    

    // if (search_type != null && search_query !=null) {
    //     result = result.concat("&search_type=" + search_type + "&search_query=" + search_query.trim());
    // }

    // location.href = result;
});