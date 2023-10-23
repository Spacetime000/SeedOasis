let darkmode = document.querySelector("#darkmode");

document.addEventListener("DOMContentLoaded", () => {
    const theme = localStorage.getItem('darkmode');
    if (theme === 'true') {
        localStorage.setItem('darkmode', 'true');
        darkmode.onclick();
    }
});


darkmode.onclick = () => {
    if (darkmode.innerText.trim() === "dark_mode") {
        darkmode.innerText = "light_mode";
        document.body.classList.add("dark");
        localStorage.setItem('darkmode', 'true');
    } else {
        darkmode.innerText = "dark_mode";
        document.body.classList.remove("dark");
        localStorage.setItem('darkmode', 'false');
    }
};