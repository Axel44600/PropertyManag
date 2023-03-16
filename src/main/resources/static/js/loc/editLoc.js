// INIT
window.addEventListener('load', function() {
    // HEADER
    async function loadHeader() {
        await fetch("/app/header").then(res => res.text()).then(data => {
            document.getElementById("header").innerHTML = data;
            document.getElementById("head-loc").setAttribute("class", "active");
        }).then(() => {
        })}

    loadHeader().then(r => r);
});



// EDIT PROFIL LOCATAIRE
function editLoc(e) {
    e.preventDefault();
    const values = new FormData(document.getElementById("editLoc"));
    const result = document.getElementById("alert");

    fetch("/app/editLocataire", {
        method: "POST",
        url : "/app/editLocataire",
        body: values
    }).then((response) => {
        return response.json();
    }).then((data) => {
        if(data.nochange === "yes") {
            //
        } else {
            if(data.success === "yes") {
                result.innerText = "Le profil du locataire a été modifier avec succès.";
                result.style.color = "green";
                setTimeout(function() { result.innerText = ""; }, 5000);
            } else if(data.error === "one") {
                result.innerText = "Un locataire possède déjà cette adresse email.";
                result.style.color = "red";
            } else if(data.error === "two") {
                result.innerText = "Un locataire possède déjà ce numéro de téléphone";
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
addEventListenerEditLoc();



// EVENTS
function addEventListenerEditLoc() {
    const form = document.getElementById("editLoc");
    form.addEventListener("submit", editLoc, false);
}