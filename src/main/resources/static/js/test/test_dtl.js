const tests = document.querySelector("section");

//정답 입력 크기 조절
function keyupHandler(e) {
    const target = e.target;
    const textarea = target.closest(".answers-textarea");

    if (textarea) {
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`;
    }
}

//이벤트
tests.addEventListener("keyup", keyupHandler);

//해답보기
function seeAnswer(e) {
    document.querySelectorAll(".solution").forEach(e => {
        e.style.display = "initial";
    });

    document.querySelector(".retry").style.display = "initial";
    e.style.display = "none";

    document.querySelectorAll("button[name='incorrect-btn']").forEach(e => {
        e.classList.remove("hidden");
    });

    document.querySelector(".score").classList.remove("hidden");
}

//오답 처리
function incorrect(e) {
    const test = e.parentNode;
    const question = test.querySelector(".test-head");
    const incorrectCheck = test.querySelector("input[name='incorrectCheck']");

    if (question.style.color === "red") {
        question.style.color = "var(--text-color)";
        incorrectCheck.checked = false;
    } else {
        question.style.color = "red";
        incorrectCheck.checked = true;
    }
}

//점수 계산
function score() {
    let test = document.querySelectorAll(".test").length; //문제 갯수
    //문제 한개당 점수
    let score = 100 / test;
    //틀린 갯수
    let incorrect = 0;

    document.querySelectorAll("input[name='incorrectCheck']").forEach(e => {
        if (e.checked)
        incorrect += 1;
    });

    let result = 100 - (score * incorrect);

    document.querySelector(".scoreboard").innerText = Math.floor(result);
    document.querySelector(".scoreboard").innerText = `${Math.floor(result)}점(${test - incorrect}/${test})`
}

//좋아요 기능
const likeBtn = document.querySelector(".like-btn");
likeBtn.addEventListener("click", () => {
    let likeNumb = likeBtn.querySelector(".like-numb");
    let id = document.querySelector("#id").value;

    if (!likeBtn.classList.contains("heart-active")) { //좋아요 +
        fetch(`/tests/${id}/like`)
            .then((res) => res.json())
            .then((json) => {
                if (json.success == "true") {
                    likeBtn.classList.add("heart-active");
                    likeNumb.innerText = Number(likeNumb.innerText) + 1;
                    likeNumb.classList.add("heart-active");
                    likeBtn.querySelector(".heart").classList.add("heart-active");
                } else {
                    alert("로그인 후 이용가능합니다.");
                }
            });
    } else { //좋아요 -
        fetch(`/tests/${id}/like`, {method : "DELETE",})
            .then((res) => res.json())
            .then((json) => {
                if (json.success == "true") {
                    likeBtn.classList.remove("heart-active");
                    likeNumb.innerText = Number(likeNumb.innerText) - 1;
                    likeNumb.classList.remove("heart-active");
                    likeBtn.querySelector(".heart").classList.remove("heart-active");
                } else {
                    alert("로그인 후 이용가능합니다.");
                }
            });
    }


    // likeBtn.classList.toggle("heart-active");

    // if (likeBtn.classList.contains("heart-active")) {
    //     likeNumb.innerText = Number(likeNumb.innerText) + 1;
    // } else {
    //     likeNumb.innerText = Number(likeNumb.innerText) - 1;
    // }
    // likeNumb.classList.toggle("heart-active");
    // likeBtn.querySelector(".heart").classList.toggle("heart-active");
});

//다시 풀기
function retry(e) {
    document.querySelectorAll(".test").forEach(test => {
        if (test.querySelector("input[name='incorrectCheck'").checked == false) {
            test.remove();
        } else {
            test.querySelector(".test-head").style.color = "var(--text-color)";
            test.querySelector("input[name='incorrectCheck']").checked = false;
        }
    });

    document.querySelectorAll(".solution").forEach(e => {
        e.style.display = "none";
    });

    document.querySelector(".retry").style.display = "none";
    e.style.display = "none";
    document.querySelector(".see-answer").style.display = "initial"
    document.querySelectorAll("button[name='incorrect-btn']").forEach(e => {
        e.classList.add("hidden");
    });

    document.querySelector(".score").classList.add("hidden");
}

const deleteBtn = document.getElementById("delete-btn");
if (deleteBtn) {
    deleteBtn.addEventListener("click", () => {
        let id = document.querySelector("#id").value;
        if (!confirm("삭제하시겠습니까?"))
            return;
        
        fetch(`/tests/${id}`, {method : "DELETE",})
            .then((res) => {
                if (res.ok) {
                    alert("삭제되었습니다.");
                    location.replace("/tests");
                } else {
                    alert("삭제권한이 없습니다.");
                }
            })

    });
}