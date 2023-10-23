const testForm = document.querySelector(".test-form");
let noElements = document.querySelectorAll(".test-no");
// let no = parseInt(noElements[noElements.length - 1].innerText);
let no = 0; //로드되었을 때 현재 값 넣기

document.addEventListener("DOMContentLoaded", () => {
    no = document.querySelectorAll(".test-no").length;
});

//버튼
// document.querySelector(".test-save").addEventListener("click", () => {
//     let formData = new FormData();
//     document.querySelectorAll(".test").forEach((e) => {
//         formData.append("question", e.querySelector.value);
//     });

//     console.log(formData);
// });

//버튼 2
document.getElementById("save").addEventListener("click", () => {
    testForm.submit();
});

//버튼 3
/*
document.getElementById("save2").addEventListener("click", () => {
    const tests = [];
    const formData = new FormData();
    const testElements = document.querySelectorAll(".test");
    for (let i = 0; i < testElements.length; i++) {
        const test = {
            question : testElements[i].querySelector(".test-question").value,
            answers : testElements[i].querySelector(".answers-textarea").value,
        };
        tests.push(test);
        formData.append("img", testElements[i].querySelector(".input-img").files[0]);
    }
    
    formData.append("testForm", JSON.stringify(tests));

    fetch("/tests/tt", {
        method: "POST",
        body: formData,
    })
});
*/

//클릭
function clickHandler(e) {
    const target = e.target;
    const testImg = target.closest(".test-img");
    const testRemove = target.closest(".test-remove");

    // 이미지 추가
    if (testImg) {
        const inputImg = testImg.nextElementSibling;
        inputImg.click();
    }

    // 제거
    if (testRemove) {
        testRemove.parentNode.remove();
        no -= 1;
        noElements = document.querySelectorAll(".test-no");
        for (let i = 0; i < noElements.length; i++) {
            noElements[i].innerText = `${i + 1}.`;
        }
    }
}

//변경
function changeHandler(e) {
    const target = e.target;
    const inputImg = target.closest(".input-img");
    const testImg = inputImg.previousElementSibling;
    if (inputImg.files.length !== 0) {
        // const testImg = inputImg.previousElementSibling;
        const imgUrl = URL.createObjectURL(inputImg.files[0]);
        const img = new Image();
        img.src = imgUrl;
        // img.style.maxWidth = "100%";
        // img.style.maxHeight = "100%";
        img.classList.add("preview");
        // testImg.innerHTML = "";
        testImg.querySelector("div").remove();
        testImg.appendChild(img);
    } else {
        testImg.querySelector("img").remove();
        let ce = document.createElement("div");
        ce.innerText = "이미지 추가 클릭";
        testImg.appendChild(ce);

        // testImg.innerHTML = `
        //     <input type="hidden" name="originImg">
        //     <div>이미지 추가 클릭</div>
        // `;


        // const testImg = inputImg.previousElementSibling;
        // if (inputImg.getAttribute("data-img")) {
        //     console.log("존재");
        // } else {
        //     console.log("미존재");
        // }
    }
}

//정답 입력 크기 조절
function keyupHandler(e) {
    const target = e.target;
    const textarea = target.closest(".answers-textarea");

    if (textarea) {
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`;
    }
}

testForm.addEventListener('click', clickHandler);
testForm.addEventListener('change', changeHandler);
testForm.addEventListener("keyup", keyupHandler);

//태그 추가
document.querySelector(".test-add").addEventListener("click", () => {
    let ce = document.createElement("div");
    ce.setAttribute("class", "test");
    no += 1;
    ce.innerHTML = `
        <div class="test-head">
            <div class="test-no">${no}.</div>
            <input class="test-question" name="question" type="text" placeholder="문제를 입력하세요." required>
        </div>
        <div class="test-img">
            <input type="hidden" name="originImg">
            <div>이미지 추가 클릭</div>
        </div>
        <input class="input-img" type="file" name="img" accept="image/*">
        <div class="img-btns">
            <button type="button" class="img-btn-remove">이미지 제거</button>
        </div>
        <div class="answers">
            <h2>정답</h2>
            <textarea class="answers-textarea" name="answers" placeholder="정답을 입력해주세요." required></textarea>
        </div>
        <div class="test-remove">
            <ion-icon name="trash-outline"></ion-icon>
        </div>
        `;

    testForm.appendChild(ce);
    noElements = document.querySelectorAll(".test-no");
});

//이미지 첨부 제거
function imgRemove(e) {
    const targetFile = e.parentNode.previousElementSibling;
    targetFile.value = ''; //input file 선택 취소
    const testImg = targetFile.previousElementSibling;
    // testImg.innerHTML = `<div>이미지 추가 클릭</div>`;
    testImg.querySelector("input[name='originImg']").value = '';
    testImg.querySelector("img").remove();
    let ce = document.createElement("div");
    ce.innerText = "이미지 추가 클릭";
    testImg.appendChild(ce);
}

//이미지 복구
function imgReset(e) {
    const targetFile = e.parentNode.previousElementSibling;
    targetFile.value = ''; //input file 선택 취소
    const testImg = targetFile.previousElementSibling;
    testImg.querySelector("div").remove();
    const originImg = testImg.querySelector("input[name='originImg']");

    if (originImg.value.length === 0) {
        originImg.value = originImg.getAttribute("data-img");
        let ce = document.createElement("img");
        ce.classList.add("preview");
        ce.src = originImg.getAttribute("data-img");
        testImg.appendChild(ce);
    }
}