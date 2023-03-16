// INIT
window.addEventListener('load', function() {
    // HEADER
    async function loadHeader() {
        await fetch("/app/header").then(res => res.text()).then(data => {
            document.getElementById("header").innerHTML = data;
            document.getElementById("head-admin").setAttribute("class", "active");
        }).then(() => {
        })
    }

    loadHeader().then(r => r);
});



// EDIT USER
function editUser(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("editUser"));
    const result = document.getElementById("alert");

    fetch("/app/admin/editUser", {
        method: "POST",
        url : "/app/admin/editUser",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.nochange) {
            //
        } else {
            if(data.success === "yes") {
                result.innerText = "Le compte utilisateur a été modifié avec succès.";
                result.style.color = "green";
                setTimeout(function() { result.innerText = ""; }, 5000);
            } else if(data.error === "one") {
                result.innerText = "Un utilisateur est déjà enregistré sous ce nom.";
                result.style.color = "red";
            } else {
                result.innerText = data.msgError;
                result.style.color = "red";
            }
        }
    }).catch(error => {
        console.log('Erreur : ' + error.message);
    })
}
addEventListenerEditUser();



// EVENTS
function addEventListenerEditUser() {
    const form = document.getElementById("editUser");
    form.addEventListener("submit", editUser, false);
}