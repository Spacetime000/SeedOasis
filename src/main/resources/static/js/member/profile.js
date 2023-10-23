document.querySelector(".profile-container").addEventListener("click", e => {
    // console.log(e.currentTarget.nextElementSibling);
    e.currentTarget.nextElementSibling.click();
});

document.querySelector(".input-img").addEventListener("change", e => {
    const imgUrl = URL.createObjectURL(e.target.files[0]);
    const img = document.querySelector(".img");
    img.src = imgUrl;
});

function btn() {
    if (document.querySelector(".input-img").files[0]) {
        document.getElementById("fm").submit();
    }
    else 
        alert("이미지를 다시 업로드해주세요.");
}