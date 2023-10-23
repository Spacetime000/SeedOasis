//리스트 아이콘
let menuIcon = document.querySelector("#menu-icon");
let headerList = document.querySelector(".header-list");

menuIcon.onclick = () => {
    headerList.classList.toggle("open");

    if (menuIcon.innerText.trim() === "menu") {
        menuIcon.innerText = "close";
    } else {
        menuIcon.innerText = "menu";
    }
};

//프로필 메뉴
try {
    let profile = document.querySelector(".profile");
    let profileMenu = document.querySelector(".profile-menu");

    profile.onclick = () => {
        profileMenu.classList.toggle("active");
    };
} catch(error) {
    console.log(error);
}

//메뉴 active
document.addEventListener("DOMContentLoaded", () => {
    const currentMenu = window.location.pathname.split('/')[1];

    switch(currentMenu) {
        case 'notice' :
            document.getElementById("header-notice").classList.add("active");
            break;
        case 'tests' :
            document.getElementById("header-tests").classList.add("active");
            break;
        case 'community' :
            document.getElementById("header-community").classList.add("active");
            break;
        case 'admin' :
            document.getElementById("header-admin").classList.add("active");
            break;
        default :
            break;
    }
});

