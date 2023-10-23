const id = document.getElementById("id").value;
const btnComment = document.querySelector(".btn-comment");
const section = document.querySelector("section");

section.addEventListener("keyup", keyupHandler);

//댓글 입력 칸 조절
function keyupHandler(e) {
    const target = e.target;
    const textarea = target.closest(".new-comment");

    if (textarea) {
        textarea.style.height = "auto";
        textarea.style.height = `${textarea.scrollHeight}px`;
    }
}

btnComment.addEventListener("click", async () => {
    const comment = document.getElementById("new-comment");
    const res = await fetch('/community/comment', {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify({
            id : id,
            comment : comment.value,
        }),
    })

    if (res.ok) {
        // comment.value = '';
        // let temp = await res.json();
        location.replace(`/community/${id}`);
    } else if(res.status == 400) {
        let txt = await res.text();
        alert(txt);
        location.replace("/community");
    }
});

//댓글 입력 크기 조절
// const newComment = document.getElementById("new-comment");
// newComment.addEventListener("keyup", () => {
//     newComment.style.height = "auto";
//     newComment.style.height = `${newComment.scrollHeight}px`;
// });

//글 삭제
const communityDel = document.getElementById("community-del");
if (communityDel) {
    communityDel.addEventListener("click", () => {
        if (confirm("삭제하시겠습니까?")) {
            fetch(`/community/${id}`, {
                method : "DELETE",
            })
            .then(() => location.replace("/community"));
        }
    });
}

//댓글 삭제
function delComment(e) {
    if (confirm("삭제하시겠습니까?")) {
        fetch(`/community/comment/${e.getAttribute('data-id')}`, {
            method : "DELETE",
        })
        .then(() => location.replace(`/community/${id}`));
    }
    
}

//댓글 수정 전환
function modifyComment(e) {
    const commentInfo = e.closest(".comment-info");
    const dtl = commentInfo.querySelector(".comment-detail");
    const newNode = document.createElement("textarea");
    const btnModify = document.createElement("button");
    btnModify.setAttribute("type", "button");
    btnModify.setAttribute("onclick", "commentModify(this)");
    btnModify.classList.add("btn-basic", "btn-modify");
    btnModify.innerText = "변경";
    dtl.insertAdjacentElement("afterend", btnModify);
    newNode.classList.add("new-comment");
    newNode.innerHTML = dtl.innerHTML;
    dtl.replaceWith(newNode);
}

function commentModify(e) {
    const coin = e.closest(".comment-info");
    const commentId = coin.querySelector("input[type='hidden']").value;
    const comment = coin.querySelector("textarea");

    fetch('/community/comment', {
        method : 'PATCH',
        headers : {
            'Content-Type' : 'application/json'
        },
        body : JSON.stringify({
            id : commentId,
            comment : comment.value,
        }),
    })
    .then((res) => {
        if (res.ok) {
            const newNode = document.createElement("pre")
            newNode.classList.add("comment-detail");
            newNode.innerHTML = comment.value;
            comment.replaceWith(newNode);
            e.remove();
            alert("변경완료");
        } else {
            alert("변경 실패");
            location.replace(window.location.href);
        }
    });

}